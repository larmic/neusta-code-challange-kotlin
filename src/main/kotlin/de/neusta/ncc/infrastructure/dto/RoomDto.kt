package de.neusta.ncc.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

data class RoomDto(
    @Schema(title = "List of all persons working in corresponding room", required = true) var people: List<PeopleDto>? = emptyList(),
    @Schema(title = "Unique room number", required = true, example = "1000") var room: String? = null
) : Serializable