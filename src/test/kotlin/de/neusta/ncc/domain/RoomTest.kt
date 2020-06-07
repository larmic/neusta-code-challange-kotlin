package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@DisplayName("Create room with room")
class RoomTest {

    @ParameterizedTest
    @ValueSource(strings = ["1234", "ABCF", "/[]{", "ÄÜÖ?"])
    fun `contains no people`(number: String) {
        val room = Room(roomNumber = number)

        assertThat(room.roomNumber).isEqualTo(number)
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
    @ValueSource(strings = ["", " ", "   ", "", "1", "12", "123", "AB", "/"])
    fun `number has less than 4 arbitrary characters`(number: String) {
        val exception = assertThrows<AssertionError> { Room(roomNumber = number) }
        assertThat(exception.message).isEqualTo("Room with number $number must have 4 arbitrary characters.")
    }

    @Test
    internal fun `number has 4 empty characters`() {
        val exception = assertThrows<AssertionError> { Room(roomNumber = "    ") }
        assertThat(exception.message).isEqualTo("Room with number      must have 4 arbitrary characters.")
    }

    @ParameterizedTest
    @ValueSource(strings = ["1234Z", "ABCFZZ", "/[]{ZZZ", "ÄÜÖ?ZZZZ"])
    fun `number has more than 4 arbitrary characters`(number: String) {
        val exception = assertThrows<AssertionError> { Room(roomNumber = number) }
        assertThat(exception.message).isEqualTo("Room with number $number must have 4 arbitrary characters.")
    }
}