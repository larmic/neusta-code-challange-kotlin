package de.neusta.ncc.domain

class Person(firstName: String,
             secondFirstName: String? = null,
             lastName: String,
             val title: PersonTitle? = null,
             val addition: PersonAddition? = null,
             ldapUser: String) {
    val firstName: String = if (secondFirstName.isNullOrBlank()) firstName.trim { it <= ' ' } else "${firstName.trim { it <= ' ' }} ${secondFirstName.trim { it <= ' ' }}"
    val lastName: String = lastName.trim { it <= ' ' }
    val ldapUser: String = ldapUser.trim { it <= ' ' }

    // TODO verify on init block required fields are not empty

    override fun toString(): String {
        val titleAsString = title?.label ?: ""
        val additionAsString = addition?.label ?: ""

        return "$titleAsString $firstName $additionAsString $lastName ($ldapUser)"
                .removeDuplicatedWhiteSpaces()
                .trim()
    }

    private fun String.removeDuplicatedWhiteSpaces() = this.replace("\\s+".toRegex(), " ")
}