package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.PersonAddition
import de.neusta.ncc.domain.PersonTitle
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class CsvPersonToPersonMapperTest {

    private val mapper = CsvPersonToPersonMapper()

    @Test
    fun `map person with minimal attributes`() {
        val person = mapper.map("Alexander Cole (acole)")

        assertThat(person.title).isNull()
        assertThat(person.firstName).isEqualTo("Alexander")
        assertThat(person.addition).isNull()
        assertThat(person.lastName).isEqualTo("Cole")
        assertThat(person.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with all attributes`() {
        val person = mapper.map("Dr. Alexander James von Cole (acole)")

        assertThat(person.title).isEqualTo(PersonTitle.DR)
        assertThat(person.firstName).isEqualTo("Alexander James")
        assertThat(person.addition).isEqualTo(PersonAddition.VON)
        assertThat(person.lastName).isEqualTo("Cole")
        assertThat(person.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with multiple white spaces`() {
        val person = mapper.map("  Dr.  Alexander   James    von     Cole      (acole)   ")

        assertThat(person.title).isEqualTo(PersonTitle.DR)
        assertThat(person.firstName).isEqualTo("Alexander James")
        assertThat(person.addition).isEqualTo(PersonAddition.VON)
        assertThat(person.lastName).isEqualTo("Cole")
        assertThat(person.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with special signs`() {
        val person = mapper.map("Dr. ÄÖÜäöüéèß ÄÖÜäöüéèß von ÄÖÜäöüéèß (ÄÖÜäöüéèß)")

        assertThat(person.title).isEqualTo(PersonTitle.DR)
        assertThat(person.firstName).isEqualTo("ÄÖÜäöüéèß ÄÖÜäöüéèß")
        assertThat(person.addition).isEqualTo(PersonAddition.VON)
        assertThat(person.lastName).isEqualTo("ÄÖÜäöüéèß")
        assertThat(person.ldapUser).isEqualTo("ÄÖÜäöüéèß")
    }

    @Test
    fun `map person with second firstname`() {
        val person = mapper.map("Alexander James Cole (acole)")

        assertThat(person.title).isNull()
        assertThat(person.firstName).isEqualTo("Alexander James")
        assertThat(person.addition).isNull()
        assertThat(person.lastName).isEqualTo("Cole")
        assertThat(person.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with title`() {
        val person = mapper.map("Dr. Uwe Svensson (usvens)")
        assertThat(person.title).isEqualTo(PersonTitle.DR)
        assertThat(person.addition).isNull()
        assertThat(person.firstName).isEqualTo("Uwe")
        assertThat(person.lastName).isEqualTo("Svensson")
        assertThat(person.ldapUser).isEqualTo("usvens")
    }

    @Test
    fun `map person with addition`() {
        val person = mapper.map("Alexander von Cole (acole)")
        assertThat(person.title).isNull()
        assertThat(person.firstName).isEqualTo("Alexander")
        assertThat(person.addition).isEqualTo(PersonAddition.VON)
        assertThat(person.lastName).isEqualTo("Cole")
        assertThat(person.ldapUser).isEqualTo("acole")

        val person1 = mapper.map("Alexander van Cole (acole)")
        assertThat(person1.title).isNull()
        assertThat(person1.firstName).isEqualTo("Alexander")
        assertThat(person1.addition).isEqualTo(PersonAddition.VAN)
        assertThat(person1.lastName).isEqualTo("Cole")
        assertThat(person1.ldapUser).isEqualTo("acole")

        val person2 = mapper.map("Alexander de Cole (acole)")
        assertThat(person2.title).isNull()
        assertThat(person2.firstName).isEqualTo("Alexander")
        assertThat(person2.addition).isEqualTo(PersonAddition.DE)
        assertThat(person2.lastName).isEqualTo("Cole")
        assertThat(person2.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person without lastname but addition`() {
        val person = mapper.map("Alexander von (acole)")
        assertThat(person.title).isNull()
        assertThat(person.firstName).isEqualTo("Alexander")
        assertThat(person.addition).isNull()
        assertThat(person.lastName).isEqualTo("von")
        assertThat(person.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with lastname is equal to addition`() {
        val person = mapper.map("Alexander von Van (acole)")
        assertThat(person.title).isNull()
        assertThat(person.firstName).isEqualTo("Alexander")
        assertThat(person.addition).isEqualTo(PersonAddition.VON)
        assertThat(person.lastName).isEqualTo("Van")
        assertThat(person.ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with three names`() {
        assertThrows<CsvPersonNotValidException> { mapper.map("Alexander James Cole Pinnhammer (acole)") }
    }

    @Test
    fun `map person without firstname`() {
        assertThrows<CsvPersonNotValidException> { mapper.map("Pinnhammer (jpinnhammer)") }
    }

    @Test
    fun `map person without lastname`() {
        assertThrows<CsvPersonNotValidException> { mapper.map("Janina (jpinnhammer)") }
    }

    @Test
    fun `map person without ldap`() {
        assertThrows<CsvPersonNotValidException> { mapper.map("Janina von Pinnhammer") }
    }

    @Test
    fun `map empty person`() {
        assertThrows<CsvPersonNotValidException> { mapper.map("") }
    }

}