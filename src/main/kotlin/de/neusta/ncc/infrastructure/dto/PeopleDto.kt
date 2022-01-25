package de.neusta.ncc.infrastructure.dto

data class PeopleDto(
    var firstName: String,
    var lastName: String,
    var title: String? = null,
    var addition: String? = null,
    var ldapUser: String,
)