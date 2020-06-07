package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class RoomBuilderTest {

    @Test
    fun testRoomNumber() {
        val room = Room.RoomBuilder("1234").build()

        assertThat(room.roomNumber).isEqualTo("1234")
        assertThat(room.persons).isEmpty()
        assertThat(room.persons).isNotNull
    }

    @Test
    fun testPersons() {
        val susanne = Person(firstName = "Susanne", lastName = "Moog", ldapUser = "smoog")
        val uwe = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", title = PersonTitle.DR)
        val alex = Person(firstName = "Alexander", secondFirstName = "James", lastName = "Cole", ldapUser = "acole")
        val florenz = Person(firstName = "Florenz", lastName = "Buhrke", ldapUser = "fbuhrke", addition = PersonAddition.VON)

        val room = Room.RoomBuilder("4444")
                .persons(listOf(susanne, uwe, alex, florenz))
                .build()

        assertThat(room.roomNumber).isEqualTo("4444")
        assertThat(room.persons).containsExactlyInAnyOrder(susanne, uwe, alex, florenz)
    }

}