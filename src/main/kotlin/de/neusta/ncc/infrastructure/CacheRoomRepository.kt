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

    override fun findByRoomNumber(roomNumber: String) = rooms.firstOrNull { r -> r.roomNumber == roomNumber }
    override fun getRooms() = rooms
}