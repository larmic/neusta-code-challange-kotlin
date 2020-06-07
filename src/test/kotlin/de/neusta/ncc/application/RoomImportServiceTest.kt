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
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class RoomImportServiceTest {

    private val roomRepository = CacheRoomRepository()
    private var roomImportService = RoomImportService(
            RoomNumberValidator(),
            RoomUniqueValidator(),
            LdapUserUniqueValidator(),
            roomRepository)

    private var susanne = Person.PersonBuilder("Susanne", "Moog", "smoog").build()
    private var uwe = Person.PersonBuilder("Uwe", "Svensson", "usvens").title(PersonTitle.DR).build()
    private var alex = Person.PersonBuilder("Alexander", "Cole", "acole").secondFirstName("James").build()
    private var samin = Person.PersonBuilder("Samin", "Ölker", "soelker").build()

    @Test
    fun testImport() {
        val room1 = Room.RoomBuilder("1000").persons(Arrays.asList(susanne, uwe)).build()
        val room2 = Room.RoomBuilder("1001").persons(Arrays.asList(alex, samin)).build()

        roomImportService.importRooms(Arrays.asList(room1, room2))

        assertThat(roomRepository.getRooms()).hasSize(2)
        assertThat(roomRepository.findByRoomNumber("1000")!!.roomNumber).isEqualTo("1000")
        assertThat(roomRepository.findByRoomNumber("1000")!!.persons).extracting("firstName", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Susanne", "Moog", "smoog"),
                        tuple("Uwe", "Svensson", "usvens")
                )
        assertThat(roomRepository.findByRoomNumber("1001")!!.roomNumber).isEqualTo("1001")
        assertThat(roomRepository.findByRoomNumber("1001")!!.persons).extracting("firstName", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Alexander James", "Cole", "acole"),
                        tuple("Samin", "Ölker", "soelker")
                )
    }

    @Test
    fun testImportWithWrongRoomNumberLength() {
        val room = Room.RoomBuilder("100").persons(Arrays.asList<Person>(susanne, uwe)).build()

        val exception = assertThrows<RoomNumberNotValidException> { roomImportService!!.importRooms(listOf(room)) }

        assertThat(exception.message).isEqualTo("Room with number 100 must have 4 arbitrary characters.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithRoomNumberIsNotUnique() {
        val room1 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(susanne, uwe)).build()
        val room2 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(alex, samin)).build()

        val exception = assertThrows<RoomIsNotUniqueException> { roomImportService!!.importRooms(Arrays.asList(room1, room2)) }
        assertThat(exception.message).isEqualTo("Room numbers should only appear once.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithRoomNumberIsNotUniqueAndRoomsAreEmpty() {
        val room1 = Room.RoomBuilder("1001").build()
        val room2 = Room.RoomBuilder("1001").build()

        val exception = assertThrows<RoomIsNotUniqueException> { roomImportService!!.importRooms(Arrays.asList(room1, room2)) }
        assertThat(exception.message).isEqualTo("Room numbers should only appear once.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithPersonIsNotUnique() {
        val saminWithSusannesLdap = Person.PersonBuilder("Samin", "Ölker", "smoog").build()

        val room1 = Room.RoomBuilder("1000").persons(Arrays.asList<Person>(susanne, uwe)).build()
        val room2 = Room.RoomBuilder("1001").persons(Arrays.asList<Person>(alex, saminWithSusannesLdap)).build()

        val exception = assertThrows<LdapUserIsNotUniqueException> { roomImportService!!.importRooms(Arrays.asList(room1, room2)) }
        assertThat(exception.message).isEqualTo("LDAP users should only appear once.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }
}