package de.neusta.ncc.infrastructure.dto

import io.swagger.annotations.ApiModelProperty
import java.io.Serializable

data class ImportResultDto constructor(
        @ApiModelProperty(value = "File name of the uploaded file", required = true, example = "sitzplan.csv")
        val uploadedFileName: String? = null) : Serializable