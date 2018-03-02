package de.neusta.ncc.application

import de.neusta.ncc.application.validator.LdapUserUniqueValidator
import de.neusta.ncc.application.validator.RoomNumberValidator
import de.neusta.ncc.application.validator.RoomUniqueValidator
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException
import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.PersonTitle
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.CacheRoomRepository
import org.assertj.core.api.Assertions.*
import org.junit.Before
import org.junit.Test
import java.util.*

class RoomImportServiceTest {

    private val roomRepository = CacheRoomRepository()
    private var roomImportService: RoomImportService? = null

    private var susanne: Person? = null
    private var uwe: Person? = null
    private var alex: Person? = null
    private var samin: Person? = null

    @Before
    fun setUp() {
        roomImportService = RoomImportService(
                RoomNumberValidator(),
                RoomUniqueValidator(),
                LdapUserUniqueValidator(),
                roomRepository)

        susanne = Person.PersonBuilder("Susanne", "Moog", "smoog").build()
        uwe = Person.PersonBuilder("Uwe", "Svensson", "usvens").title(PersonTitle.DR).build()
        alex = Person.PersonBuilder("Alexander", "Cole", "acole").secondFirstName("James").build()
        samin = Person.PersonBuilder("Samin", "Ölker", "soelker").build()
    }

    @Test
    fun testImport() {
        val room1 = Room.RoomBuilder("1000").persons(Arrays.asList<Person>(susanne, uwe)).build()
        val room2 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(alex, samin)).build()

        roomImportService!!.importRooms(Arrays.asList(room1, room2))

        assertThat(roomRepository.getRooms()).hasSize(2)
        assertThat(roomRepository.findByRoomNumber("1000")!!.roomNumber).isEqualTo("1000")
        assertThat(roomRepository.findByRoomNumber("1000")!!.persons).extracting("firstName", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Susanne", "Moog", "smoog"),
                        tuple("Uwe", "Svensson", "usvens")
                )
        assertThat(roomRepository.findByRoomNumber("1001")!!.roomNumber).isEqualTo("1001")
        assertThat<Person>(roomRepository.findByRoomNumber("1001")!!.persons).extracting("firstName", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Alexander James", "Cole", "acole"),
                        tuple("Samin", "Ölker", "soelker")
                )
    }

    @Test
    fun testImportWithWrongRoomNumberLength() {
        val room = Room.RoomBuilder("100").persons(Arrays.asList<Person>(susanne, uwe)).build()

        try {
            roomImportService!!.importRooms(listOf(room))
            fail("Should throw exception")
        } catch (e: RoomNumberNotValidException) {
            assertThat(e.message).isEqualTo("Room with number 100 must have 4 arbitrary characters.")
        }

        assertThat<Room>(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithRoomNumberIsNotUnique() {
        val room1 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(susanne, uwe)).build()
        val room2 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(alex, samin)).build()

        try {
            roomImportService!!.importRooms(Arrays.asList(room1, room2))
            fail("Should throw exception")
        } catch (e: RoomIsNotUniqueException) {
            assertThat(e.message).isEqualTo("Room numbers should only appear once.")
        }

        assertThat<Room>(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithRoomNumberIsNotUniqueAndRoomsAreEmpty() {
        val room1 = Room.RoomBuilder("1001").build()
        val room2 = Room.RoomBuilder("1001").build()

        try {
            roomImportService!!.importRooms(Arrays.asList(room1, room2))
            fail("Should throw exception")
        } catch (e: RoomIsNotUniqueException) {
            assertThat(e.message).isEqualTo("Room numbers should only appear once.")
        }

        assertThat<Room>(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithPersonIsNotUnique() {
        val saminWithSusannesLdap = Person.PersonBuilder("Samin", "Ölker", "smoog").build()

        val room1 = Room.RoomBuilder("1000").persons(Arrays.asList<Person>(susanne, uwe)).build()
        val room2 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(alex, saminWithSusannesLdap)).build()

        try {
            roomImportService!!.importRooms(Arrays.asList(room1, room2))
            fail("Should throw exception")
        } catch (e: LdapUserIsNotUniqueException) {
            assertThat(e.message).isEqualTo("LDAP users should only appear once.")
        }

        assertThat<Room>(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }
}