package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.PersonAddition
import de.neusta.ncc.domain.PersonTitle
import de.neusta.ncc.domain.Room
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Test

class RoomMapperTest {

    private var roomMapper = RoomMapper()

    @Test
    fun mapToDto() {
        val person1 = Person(firstName = "Leif", secondFirstName = "Arne", lastName = "Genzmer", ldapUser = "lgenzmer", title = PersonTitle.DR)
        val person2 = Person(firstName = "Samin", lastName = "Ölker", ldapUser = "soelker", addition = PersonAddition.DE)

        val room = Room.RoomBuilder("1103")
                .persons(listOf(person1, person2))
                .build()

        val (people, room1) = roomMapper.mapToDto(room)

        assertThat(room1).isEqualTo("1103")
        assertThat(people)
                .extracting("firstName", "lastName", "title", "addition", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple("Leif Arne", "Genzmer", "Dr.", null, "lgenzmer"),
                        tuple("Samin", "Ölker", null, "de", "soelker")
                )
    }

    @Test
    fun mapToDtoWithRoomIsEmpty() {
        val room = Room.RoomBuilder("1104").build()

        val (people, room1) = roomMapper.mapToDto(room)

        assertThat(room1).isEqualTo("1104")
        assertThat(people).isEmpty()
    }
}