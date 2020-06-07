package de.neusta.ncc.domain

class Room(val roomNumber: String, val persons: List<Person> = emptyList()) {

    init {
        assert(roomNumber.isNotBlank()) { "Room number must not be empty" }
    }

}