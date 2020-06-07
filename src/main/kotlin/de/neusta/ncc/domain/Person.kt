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

    // TODO verify on init block required fields are not empty

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