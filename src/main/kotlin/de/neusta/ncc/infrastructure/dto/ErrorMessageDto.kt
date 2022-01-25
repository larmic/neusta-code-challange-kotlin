package de.neusta.ncc.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

data class ErrorMessageDto(
    @Schema(title = "specific error code", required = true, example = "6") val code: Int,
    @Schema(title = "specific error message", required = true, example = "Room number must have 4 arbitrary characters.") val message: String
) : Serializable
