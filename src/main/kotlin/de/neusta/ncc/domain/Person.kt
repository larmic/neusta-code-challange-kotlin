package de.neusta.ncc.domain

class Person(firstName: String,
             secondFirstName: String? = null,
             lastName: String,
             val title: PersonTitle? = null,
             val addition: PersonAddition? = null,
             ldapUser: String) {
    val firstName: String = mergeFirstAndSecondName(secondFirstName, firstName)
    val lastName: String = lastName.normalizeName()
    val ldapUser: String = ldapUser.normalizeName()

    init {
        assert(firstName.isNotBlank()) { "First name must not be empty" }
        assert(lastName.isNotBlank()) { "Last name must not be empty" }
        assert(ldapUser.isNotBlank()) { "LDAP user must not be empty" }
    }

    override fun toString(): String {
        val titleAsString = title?.label ?: ""
        val additionAsString = addition?.label ?: ""

        return "$titleAsString $firstName $additionAsString $lastName ($ldapUser)"
                .removeDuplicatedWhiteSpaces()
                .trim()
    }

    private fun String.removeDuplicatedWhiteSpaces() = this.replace("\\s+".toRegex(), " ")
    private fun String.normalizeName() = trim { it <= ' ' }
    private fun mergeFirstAndSecondName(secondFirstName: String?, firstName: String) =
            if (secondFirstName.isNullOrBlank()) firstName.normalizeName() else "${firstName.normalizeName()} ${secondFirstName.normalizeName()}"
}