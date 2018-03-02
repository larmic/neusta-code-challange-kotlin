package de.neusta.ncc.infrastructure

import de.neusta.ncc.domain.Room
import de.neusta.ncc.domain.RoomRepository
import org.springframework.stereotype.Repository

/**
 * Simple embedded rooms cache. Could be replaced by a database. Up to now this is a very simple version of a storage.
 */
@Repository
class CacheRoomRepository : RoomRepository {

    private var rooms = emptyList<Room>()

    override fun replaceRooms(rooms: List<Room>) {
        this.rooms = rooms.toList()
    }

    override fun findByRoomNumber(roomNumber: String): Room? {
        return rooms.firstOrNull { r -> r.roomNumber == roomNumber }
    }

    override fun findByLikeLdapUser(ldapUser: String): List<Room> {
        return if (ldapUser.isEmpty()) {
            emptyList()
        } else rooms.filter { r -> roomContainsLikeLdapUserIgnoreCase(ldapUser, r) }

    }

    override fun getRooms(): List<Room> {
        return rooms
    }

    private fun roomContainsLikeLdapUserIgnoreCase(ldapUser: String, r: Room): Boolean {
        return r.persons.stream().anyMatch { (_, _, _, _, ldapUser1) -> ldapUser1.toLowerCase().contains(ldapUser.toLowerCase()) }
    }

}