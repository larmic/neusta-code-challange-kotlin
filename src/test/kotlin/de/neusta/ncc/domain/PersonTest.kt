package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

class PersonTest {

    @Nested
    @DisplayName("Create person with person")
    inner class CreatePerson {
        @Test
        fun `has minimal attributes`() {
            val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens")
            assertThat(person.firstName).isEqualTo("Uwe")
            assertThat(person.lastName).isEqualTo("Svensson")
            assertThat(person.ldapUser).isEqualTo("usvens")
        }

        @Test
        fun `has minimal not trimmed attributes`() {
            val person = Person(firstName = " Uwe ", lastName = " Svensson ", ldapUser = " usvens ")
            assertThat(person.firstName).isEqualTo("Uwe")
            assertThat(person.lastName).isEqualTo("Svensson")
            assertThat(person.ldapUser).isEqualTo("usvens")
        }

        @ParameterizedTest
        @EnumSource(PersonAddition::class)
        fun `has addition`(addition: PersonAddition) {
            val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", addition = addition)
            assertThat(person.addition).isEqualTo(addition)
        }

        @Test
        fun `has title`() {
            val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", title = PersonTitle.DR)
            assertThat(person.title).isEqualTo(PersonTitle.DR)
        }

        @Test
        fun `has second first name`() {
            val person = Person(firstName = "Uwe", secondFirstName = "Tom", lastName = "Svensson", ldapUser = "usvens")
            assertThat(person.firstName).isEqualTo("Uwe Tom")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   "])
        fun `has empty second first name`(secondFirstName: String) {
            val person = Person(firstName = "Uwe", secondFirstName = secondFirstName, lastName = "Svensson", ldapUser = "usvens")
            assertThat(person.firstName).isEqualTo("Uwe")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   "])
        fun `has empty first name`(firstName: String) {
            val exception = assertThrows<AssertionError> { Person(firstName = firstName, lastName = "Michaelis", ldapUser = "lamichae") }
            assertThat(exception.message).isEqualTo("First name must not be empty")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   "])
        fun `has empty last name`(lastName: String) {
            val exception = assertThrows<AssertionError> { Person(firstName = "Lars", lastName = lastName, ldapUser = "lamichae") }
            assertThat(exception.message).isEqualTo("Last name must not be empty")
        }

        @ParameterizedTest
        @ValueSource(strings = ["", " ", "   "])
        fun `has empty ldap user name`(ldapUser: String) {
            val exception = assertThrows<AssertionError> { Person(firstName = "Lars", lastName = "Michaelis", ldapUser = ldapUser) }
            assertThat(exception.message).isEqualTo("LDAP user must not be empty")
        }
    }

    @Nested
    @DisplayName("Test toString() with")
    inner class ToString {
        @Test
        internal fun `person has no title or addition`() {
            val person = Person(firstName = "Susanne", lastName = "Moog", ldapUser = "smoog")
            assertThat(person.toString()).isEqualTo("Susanne Moog (smoog)")
        }

        @Test
        internal fun `person has title but no addition`() {
            val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", title = PersonTitle.DR)
            assertThat(person.toString()).isEqualTo("Dr. Uwe Svensson (usvens)")
        }

        @ParameterizedTest
        @EnumSource(PersonAddition::class)
        internal fun `person has addition but no title`(addition: PersonAddition) {
            val person = Person(firstName = "Alexander", lastName = "Cole", ldapUser = "acole", addition = addition)
            assertThat(person.toString()).isEqualTo("Alexander ${addition.label} Cole (acole)")
        }

        @Test
        internal fun `person has second first name`() {
            val person = Person(firstName = "Alexander", lastName = "Cole", ldapUser = "acole", secondFirstName = "James")
            assertThat(person.toString()).isEqualTo("Alexander James Cole (acole)")
        }
    }
}