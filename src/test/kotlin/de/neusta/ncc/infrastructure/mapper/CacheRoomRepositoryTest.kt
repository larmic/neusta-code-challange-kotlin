package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.CacheRoomRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test

class CacheRoomRepositoryTest {

    private lateinit var roomRepository: CacheRoomRepository

    private lateinit var room1: Room
    private lateinit var room2: Room

    @Before
    fun setUp() {
        roomRepository = CacheRoomRepository()

        val alexander = Person.PersonBuilder("Alexander", "Cole", "acole")
                .secondFirstName("James")
                .build()
        val alexandersBrother = Person.PersonBuilder("Alexander", "Cole", "abcole")
                .secondFirstName("Brother")
                .build()

        room1 = Room.RoomBuilder("1110")
                .persons(listOf(alexander))
                .build()
        room2 = Room.RoomBuilder("1111")
                .persons(listOf(alexandersBrother))
                .build()
    }

    @Test
    fun `replace rooms`() {
        roomRepository.replaceRooms(listOf(room1, room2))

        assertThat<Room>(roomRepository.getRooms()).containsExactlyInAnyOrder(room1, room2)

        // verify list of rooms will be replaced on each replaceRooms
        roomRepository.replaceRooms(listOf(room1))

        assertThat<Room>(roomRepository.getRooms()).containsExactly(room1)
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

    @Test
    fun `find room by ldap user name`() {
        roomRepository.replaceRooms(listOf(room1, room2))

        assertThat(roomRepository.findByLikeLdapUser("acole")).containsExactly(room1)
        assertThat(roomRepository.findByLikeLdapUser("aCole")).containsExactly(room1)
        assertThat(roomRepository.findByLikeLdapUser("abcole")).containsExactly(room2)
        assertThat(roomRepository.findByLikeLdapUser("cole")).containsExactlyInAnyOrder(room1, room2)
        assertThat(roomRepository.findByLikeLdapUser("unkown")).isEmpty()
    }

    @Test
    fun `find rooms by empty ldap user name`() {
        roomRepository.replaceRooms(listOf(room1, room2))

        assertThat(roomRepository.findByLikeLdapUser("")).isEmpty()
    }

}