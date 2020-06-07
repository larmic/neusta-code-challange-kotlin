package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class PersonTest {

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