package de.neusta.ncc.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

data class DefaultSpringErrorDto(
    @Schema(title = "Timestamp in milles when the error occurred", required = true, example = "14443434344") val timestamp: String? = null,
    @Schema(title = "Http status", required = true, example = "405") val status: String? = null,
    @Schema(title = "Short error message", required = true, example = "Method Not Allowed") val error: String? = null,
    @Schema(title = "returning exception", required = true, example = "org.springframework.web.HttpRequestMethodNotSupportedException") val exception: String? = null,
    @Schema(title = "specific error message", required = true, example = "Request method 'DELETE' not supported") val message: String? = null,
    @Schema(title = "requested url", required = true, example = "/api/room/kitchen-1") val path: String? = null
) : Serializable