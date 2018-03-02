package de.neusta.ncc.domain

data class Person constructor(
        val firstName: String,
        val lastName: String,
        val title: PersonTitle?,
        val addition: PersonAddition?,
        val ldapUser: String) {

    override fun toString(): String {
        val stringBuilder = StringBuilder()

        if (title != null) {
            stringBuilder.append(title.label).append(" ")
        }

        stringBuilder.append(firstName).append(" ")

        if (addition != null) {
            stringBuilder.append(addition.label).append(" ")
        }

        stringBuilder.append(lastName).append(" (").append(ldapUser).append(")")

        return stringBuilder.toString()
    }

    class PersonBuilder(private val firstName: String, private val lastName: String, private val ldapUser: String) {
        private var secondFirstName: String? = null
        private var title: PersonTitle? = null
        private var addition: PersonAddition? = null

        fun secondFirstName(secondFirstName: String?): PersonBuilder {
            this.secondFirstName = secondFirstName
            return this
        }

        fun title(personTitle: PersonTitle?): PersonBuilder {
            this.title = personTitle
            return this
        }

        fun addition(personAddition: PersonAddition?): PersonBuilder {
            this.addition = personAddition
            return this
        }

        fun build(): Person {
            return if (secondFirstName == null || secondFirstName!!.trim { it <= ' ' }.isEmpty()) {
                Person(firstName.trim { it <= ' ' }, lastName.trim { it <= ' ' }, title, addition, ldapUser.trim { it <= ' ' })
            } else Person(firstName.trim { it <= ' ' } + " " + secondFirstName!!.trim { it <= ' ' }, lastName.trim { it <= ' ' }, title, addition, ldapUser.trim { it <= ' ' })
        }
    }
}