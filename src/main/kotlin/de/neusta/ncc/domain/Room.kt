package de.neusta.ncc.domain

data class Room constructor(val roomNumber: String, val persons: List<Person>) {

    class RoomBuilder(private val roomNumber: String) {
        private var persons = emptyList<Person>()

        fun persons(persons: List<Person>): RoomBuilder {
            this.persons = persons.toList()
            return this
        }

        fun build(): Room {
            return Room(roomNumber, persons)
        }
    }

}