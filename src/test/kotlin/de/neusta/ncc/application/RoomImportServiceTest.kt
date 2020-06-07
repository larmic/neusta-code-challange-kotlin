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

    private var susanne = Person(firstName = "Susanne", lastName = "Moog", ldapUser = "smoog")
    private var uwe = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", title = PersonTitle.DR)
    private var alex = Person(firstName = "Alexander", secondFirstName = "James", lastName = "Cole", ldapUser = "acole")
    private var samin = Person(firstName = "Samin", lastName = "Ölker", ldapUser = "soelker")

    @Test
    fun testImport() {
        val room1 = Room(roomNumber = "1000", persons = listOf(susanne, uwe))
        val room2 = Room(roomNumber = "1001", persons = listOf(alex, samin))

        roomImportService.importRooms(listOf(room1, room2))

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
        val room = Room(roomNumber = "100", persons = listOf(susanne, uwe))

        val exception = assertThrows<RoomNumberNotValidException> { roomImportService.importRooms(listOf(room)) }

        assertThat(exception.message).isEqualTo("Room with number 100 must have 4 arbitrary characters.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithRoomNumberIsNotUnique() {
        val room1 = Room(roomNumber = "1001", persons = listOf(susanne, uwe))
        val room2 = Room(roomNumber = "1001", persons = listOf(alex, samin))

        val exception = assertThrows<RoomIsNotUniqueException> { roomImportService.importRooms(listOf(room1, room2)) }
        assertThat(exception.message).isEqualTo("Room numbers should only appear once.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithRoomNumberIsNotUniqueAndRoomsAreEmpty() {
        val room1 = Room(roomNumber = "1001")
        val room2 = Room(roomNumber = "1001")

        val exception = assertThrows<RoomIsNotUniqueException> { roomImportService.importRooms(listOf(room1, room2)) }
        assertThat(exception.message).isEqualTo("Room numbers should only appear once.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }

    @Test
    fun testImportWithPersonIsNotUnique() {
        val saminWithSusannesLdap = Person(firstName = "Samin", lastName = "Ölker", ldapUser = "smoog")

        val room1 = Room(roomNumber = "1000", persons = listOf(susanne, uwe))
        val room2 = Room(roomNumber = "1001", persons = listOf(alex, saminWithSusannesLdap))

        val exception = assertThrows<LdapUserIsNotUniqueException> { roomImportService.importRooms(listOf(room1, room2)) }
        assertThat(exception.message).isEqualTo("LDAP users should only appear once.")
        assertThat(roomRepository.getRooms()).hasSize(0) // assert that nothing has been imported
    }
}