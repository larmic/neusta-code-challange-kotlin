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

    /**
     * @return a [List] of [Room]s that contains persons with given is like ldap user names.
     */
    fun findByLikeLdapUser(ldapUser: String): List<Room>

    fun getRooms(): List<Room>

}