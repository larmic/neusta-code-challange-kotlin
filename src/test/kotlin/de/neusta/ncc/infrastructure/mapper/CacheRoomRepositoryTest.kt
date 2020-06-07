package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.CacheRoomRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class CacheRoomRepositoryTest {

    private var roomRepository = CacheRoomRepository()

    private lateinit var room1: Room
    private lateinit var room2: Room

    @BeforeEach
    fun setUp() {
        val alexander = Person(firstName = "Alexander", secondFirstName = "James", lastName = "Cole", ldapUser = "acole")
        val alexandersBrother = Person(firstName = "Alexander", secondFirstName = "Brother", lastName = "Cole", ldapUser = "abcole")

        room1 = Room(roomNumber = "1110", persons = listOf(alexander))
        room2 = Room(roomNumber = "1111", persons = listOf(alexandersBrother))
    }

    @Test
    fun `replace rooms`() {
        roomRepository.replaceRooms(listOf(room1, room2))

        assertThat(roomRepository.getRooms()).containsExactlyInAnyOrder(room1, room2)

        // verify list of rooms will be replaced on each replaceRooms
        roomRepository.replaceRooms(listOf(room1))

        assertThat(roomRepository.getRooms()).containsExactly(room1)
    }

    @Test
    fun `find room by number`() {
        roomRepository.replaceRooms(listOf(room1, room2))

        assertThat(roomRepository.findByRoomNumber("1110")).isEqualTo(room1)
        assertThat(roomRepository.findByRoomNumber("1111")).isEqualTo(room2)
    }

    @Test
    fun `find room by number but room does not exists`() {
        assertThat(roomRepository.findByRoomNumber("0000")).isNull()
    }
}