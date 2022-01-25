package de.neusta.ncc.infrastructure.dto

data class RoomDto(var people: List<PeopleDto>? = emptyList(), var room: String)