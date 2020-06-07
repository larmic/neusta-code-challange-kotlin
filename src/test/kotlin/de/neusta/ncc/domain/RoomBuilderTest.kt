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
        val susanne = Person.PersonBuilder("Susanne", "Moog", "smoog").build()
        val uwe = Person.PersonBuilder("Uwe", "Svensson", "usvens").title(PersonTitle.DR).build()
        val alex = Person.PersonBuilder("Alexander", "Cole", "acole").secondFirstName("James").build()
        val florenz = Person.PersonBuilder("Florenz", "Buhrke", "fbuhrke").addition(PersonAddition.VON).build()

        val room = Room.RoomBuilder("4444")
                .persons(Arrays.asList(susanne, uwe, alex, florenz))
                .build()

        assertThat(room.roomNumber).isEqualTo("4444")
        assertThat(room.persons).containsExactlyInAnyOrder(susanne, uwe, alex, florenz)
    }

}