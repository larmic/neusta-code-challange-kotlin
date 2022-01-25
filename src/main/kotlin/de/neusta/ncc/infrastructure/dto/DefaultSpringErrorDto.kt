package de.neusta.ncc.infrastructure.dto

data class DefaultSpringErrorDto(
    val timestamp: String? = null,
    val status: String? = null,
    val error: String? = null,
    val exception: String? = null,
    val message: String? = null,
    val path: String? = null
)