package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PersonBuilderTest {

    @Test
    fun `build person with minimal parameters`() {
        val person = Person.PersonBuilder("Uwe", "Svensson", "usvens").build()
        assertThat(person.firstName).isEqualTo("Uwe")
        assertThat(person.lastName).isEqualTo("Svensson")
        assertThat(person.ldapUser).isEqualTo("usvens")
    }

    @Test
    fun `build person with addition`() {
        val person1 = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .addition(PersonAddition.VAN)
                .build()
        assertThat(person1.addition).isEqualTo(PersonAddition.VAN)

        val person2 = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .addition(PersonAddition.DE)
                .build()
        assertThat(person2.addition).isEqualTo(PersonAddition.DE)

        val person3 = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .addition(PersonAddition.VON)
                .build()
        assertThat(person3.addition).isEqualTo(PersonAddition.VON)
    }

    @Test
    fun `build person with title`() {
        val person = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .title(PersonTitle.DR)
                .build()
        assertThat(person.title).isEqualTo(PersonTitle.DR)
    }

    @Test
    fun `build person with title is null`() {
        val person = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .title(null)
                .build()
        assertThat(person.title).isNull()
    }

    @Test
    fun `build person with second first name`() {
        val (firstName) = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName("Tom")
                .build()
        assertThat(firstName).isEqualTo("Uwe Tom")
    }

    @Test
    fun `build person with second first name is null`() {
        val (firstName) = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName(null)
                .build()
        assertThat(firstName).isEqualTo("Uwe")
    }

    @Test
    fun `build person with second first name is empty`() {
        val (firstName) = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName("")
                .build()
        assertThat(firstName).isEqualTo("Uwe")
    }

    @Test
    fun `build person with second first name contains a space`() {
        val (firstName) = Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .secondFirstName(" ")
                .build()
        assertThat(firstName).isEqualTo("Uwe")
    }

}