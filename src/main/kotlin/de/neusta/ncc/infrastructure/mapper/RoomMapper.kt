package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.dto.PeopleDto
import de.neusta.ncc.infrastructure.dto.RoomDto
import org.springframework.stereotype.Component

@Component
class RoomMapper {

    fun mapToDto(room: Room): RoomDto {
        val people = room.persons.map { p -> mapPersonToDto(p) }
        return RoomDto(people, room.roomNumber)
    }

    private fun mapPersonToDto(p: Person): PeopleDto {
        val title = if (p.title != null) p.title.label else null
        val addition = if (p.addition != null) p.addition.label else null

        return PeopleDto(p.firstName, p.lastName, title, addition, p.ldapUser)
    }

}