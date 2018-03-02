package de.neusta.ncc.infrastructure.dto

import io.swagger.annotations.ApiModelProperty
import java.io.Serializable

data class RoomDto constructor(
        @ApiModelProperty(value = "List of all persons working in corresponding room", required = true) var people: List<PeopleDto>? = emptyList(),
        @ApiModelProperty(value = "Unique room number", required = true, example = "1000") var room: String ? = null) : Serializable