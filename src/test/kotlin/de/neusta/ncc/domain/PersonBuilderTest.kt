package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import org.junit.jupiter.params.provider.ValueSource

class PersonBuilderTest {

    @Test
    fun `build person with minimal parameters`() {
        val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens")
        assertThat(person.firstName).isEqualTo("Uwe")
        assertThat(person.lastName).isEqualTo("Svensson")
        assertThat(person.ldapUser).isEqualTo("usvens")
    }

    @ParameterizedTest
    @EnumSource(PersonAddition::class)
    fun `build person with addition`(addition: PersonAddition) {
        val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", addition = addition)
        assertThat(person.addition).isEqualTo(addition)
    }

    @Test
    fun `build person with title`() {
        val person = Person(firstName = "Uwe", lastName = "Svensson", ldapUser = "usvens", title = PersonTitle.DR)
        assertThat(person.title).isEqualTo(PersonTitle.DR)
    }

    @Test
    fun `build person with second first name`() {
        val person = Person(firstName = "Uwe", secondFirstName = "Tom", lastName = "Svensson", ldapUser = "usvens")
        assertThat(person.firstName).isEqualTo("Uwe Tom")
    }

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "   "])
    fun `build person with second first name is empty`(secondFirstName: String) {
        val person = Person(firstName = "Uwe", secondFirstName = secondFirstName, lastName = "Svensson", ldapUser = "usvens")
        assertThat(person.firstName).isEqualTo("Uwe")
    }
}