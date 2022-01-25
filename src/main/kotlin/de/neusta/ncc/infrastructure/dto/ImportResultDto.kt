package de.neusta.ncc.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

data class ImportResultDto(
    @Schema(title = "File name of the uploaded file", required = true, example = "sitzplan.csv") val uploadedFileName: String? = null
) : Serializable