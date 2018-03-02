package de.neusta.ncc.infrastructure.dto

import io.swagger.annotations.ApiModelProperty
import java.io.Serializable

data class PeopleDto constructor(
        @ApiModelProperty(value = "first name", required = true, example = "Alexander James") var firstName: String? = null,
        @ApiModelProperty(value = "Last name", required = true, example = "Cole") var lastName: String? = null,
        @ApiModelProperty(value = "Title", example = "Dr.") var title: String? = null,
        @ApiModelProperty(value = "Name addition", example = "von|van|de") var addition: String? = null,
        @ApiModelProperty(value = "Unique ldap name", required = true, example = "acole") var ldapUser: String? = null) : Serializable