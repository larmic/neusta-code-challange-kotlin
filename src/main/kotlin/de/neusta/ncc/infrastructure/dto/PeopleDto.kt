package de.neusta.ncc.infrastructure.dto

import io.swagger.v3.oas.annotations.media.Schema
import java.io.Serializable

data class PeopleDto(
    @Schema(title = "first name", required = true, example = "Alexander James") var firstName: String? = null,
    @Schema(title = "Last name", required = true, example = "Cole") var lastName: String? = null,
    @Schema(title = "Title", example = "Dr.") var title: String? = null,
    @Schema(title = "Name addition", example = "von|van|de") var addition: String? = null,
    @Schema(title = "Unique ldap name", required = true, example = "acole") var ldapUser: String? = null
) : Serializable