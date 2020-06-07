package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.PersonAddition
import de.neusta.ncc.domain.PersonTitle
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.dto.PeopleDto
import de.neusta.ncc.infrastructure.dto.RoomDto
import org.springframework.stereotype.Component

@Component
class RoomMapper {

    fun mapToDto(room: Room) = RoomDto(room = room.roomNumber, people = room.persons.map { it.mapToDto() })

    private fun Person.mapToDto() = PeopleDto(this.firstName, this.lastName, this.title.mapToDto(), this.addition.mapToDto(), this.ldapUser)
    private fun PersonTitle?.mapToDto() = this?.label
    private fun PersonAddition?.mapToDto() = this?.label

}