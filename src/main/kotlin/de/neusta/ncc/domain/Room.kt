package de.neusta.ncc.domain

class Room(val roomNumber: String, val persons: List<Person> = emptyList()) {

    init {
        assert(roomNumber.length == 4 && roomNumber.isNotBlank()) { "Room with number $roomNumber must have 4 arbitrary characters." }
    }

}