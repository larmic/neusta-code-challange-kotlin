package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Create room with room")
class RoomTest {

    @Test
    fun `contains no people`() {
        val room = Room(roomNumber = "1234")

        assertThat(room.roomNumber).isEqualTo("1234")
        assertThat(room.persons).isEmpty()
        assertThat(room.persons).isNotNull
    }

    @Test
    fun `contains people`() {
        val susanne = Person(firstName = "Susanne", lastName = "Moog", ldapUser = "smoog")
        val uwe = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", title = PersonTitle.DR)
        val alex = Person(firstName = "Alexander", secondFirstName = "James", lastName = "Cole", ldapUser = "acole")
        val florenz = Person(firstName = "Florenz", lastName = "Buhrke", ldapUser = "fbuhrke", addition = PersonAddition.VON)

        val room = Room(roomNumber = "4444", persons = listOf(susanne, uwe, alex, florenz))

        assertThat(room.roomNumber).isEqualTo("4444")
        assertThat(room.persons).containsExactlyInAnyOrder(susanne, uwe, alex, florenz)
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "   "])
    fun `has empty room number`(number: String) {
        val exception = assertThrows<AssertionError> { Room(roomNumber = number) }
        assertThat(exception.message).isEqualTo("Room number must not be empty")
    }
}