package de.neusta.ncc.domain

interface RoomRepository {

    /**
     * Replaced already existing rooms by new collection of rooms
     */
    fun replaceRooms(rooms: List<Room>)

    /**
     * @return [Room] matching exactly given room number.
     */
    fun findByRoomNumber(roomNumber: String): Room?

    fun getRooms(): List<Room>

}