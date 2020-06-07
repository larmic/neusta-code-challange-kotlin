package de.neusta.ncc.domain

class Room(val roomNumber: String, val persons: List<Person> = emptyList()) {

    init {
        assert(roomNumber.validateRoomNumber()) { "Room with number $roomNumber must have 4 arbitrary characters." }
    }

    private fun String.validateRoomNumber() = this.length == 4 && this.isNotBlank()
}