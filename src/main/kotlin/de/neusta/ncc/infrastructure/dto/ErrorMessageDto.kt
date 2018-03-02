package de.neusta.ncc.infrastructure.dto;

import io.swagger.annotations.ApiModel
import io.swagger.annotations.ApiModelProperty

@ApiModel(value = "ErrorMessageDto", description = "Contains defined error message")
data class ErrorMessageDto constructor(
        @ApiModelProperty(value = "specific error code", required = true, example = "6") val code: Int,
        @ApiModelProperty(value = "specific error message", required = true, example = "Room number must have 4 arbitrary characters.") val message: String)
