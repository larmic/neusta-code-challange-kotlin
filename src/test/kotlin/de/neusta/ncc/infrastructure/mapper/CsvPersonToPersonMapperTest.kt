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
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Alexander Cole (acole)")

        assertThat(title).isNull()
        assertThat(firstName).isEqualTo("Alexander")
        assertThat(addition).isNull()
        assertThat(lastName).isEqualTo("Cole")
        assertThat(ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with all attributes`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Dr. Alexander James von Cole (acole)")

        assertThat(title).isEqualTo(PersonTitle.DR)
        assertThat(firstName).isEqualTo("Alexander James")
        assertThat(addition).isEqualTo(PersonAddition.VON)
        assertThat(lastName).isEqualTo("Cole")
        assertThat(ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with multiple white spaces`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("  Dr.  Alexander   James    von     Cole      (acole)   ")

        assertThat(title).isEqualTo(PersonTitle.DR)
        assertThat(firstName).isEqualTo("Alexander James")
        assertThat(addition).isEqualTo(PersonAddition.VON)
        assertThat(lastName).isEqualTo("Cole")
        assertThat(ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with special signs`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Dr. ÄÖÜäöüéèß ÄÖÜäöüéèß von ÄÖÜäöüéèß (ÄÖÜäöüéèß)")

        assertThat(title).isEqualTo(PersonTitle.DR)
        assertThat(firstName).isEqualTo("ÄÖÜäöüéèß ÄÖÜäöüéèß")
        assertThat(addition).isEqualTo(PersonAddition.VON)
        assertThat(lastName).isEqualTo("ÄÖÜäöüéèß")
        assertThat(ldapUser).isEqualTo("ÄÖÜäöüéèß")
    }

    @Test
    fun `map person with second firstname`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Alexander James Cole (acole)")

        assertThat(title).isNull()
        assertThat(firstName).isEqualTo("Alexander James")
        assertThat(addition).isNull()
        assertThat(lastName).isEqualTo("Cole")
        assertThat(ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with title`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Dr. Uwe Svensson (usvens)")
        assertThat(title).isEqualTo(PersonTitle.DR)
        assertThat(addition).isNull()
        assertThat(firstName).isEqualTo("Uwe")
        assertThat(lastName).isEqualTo("Svensson")
        assertThat(ldapUser).isEqualTo("usvens")
    }

    @Test
    fun `map person with addition`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Alexander von Cole (acole)")
        assertThat(title).isNull()
        assertThat(firstName).isEqualTo("Alexander")
        assertThat(addition).isEqualTo(PersonAddition.VON)
        assertThat(lastName).isEqualTo("Cole")
        assertThat(ldapUser).isEqualTo("acole")

        val (firstName1, lastName1, title1, addition1, ldapUser1) = mapper.map("Alexander van Cole (acole)")
        assertThat(title1).isNull()
        assertThat(firstName1).isEqualTo("Alexander")
        assertThat(addition1).isEqualTo(PersonAddition.VAN)
        assertThat(lastName1).isEqualTo("Cole")
        assertThat(ldapUser1).isEqualTo("acole")

        val (firstName2, lastName2, title2, addition2, ldapUser2) = mapper.map("Alexander de Cole (acole)")
        assertThat(title2).isNull()
        assertThat(firstName2).isEqualTo("Alexander")
        assertThat(addition2).isEqualTo(PersonAddition.DE)
        assertThat(lastName2).isEqualTo("Cole")
        assertThat(ldapUser2).isEqualTo("acole")
    }

    @Test
    fun `map person without lastname but addition`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Alexander von (acole)")
        assertThat(title).isNull()
        assertThat(firstName).isEqualTo("Alexander")
        assertThat(addition).isNull()
        assertThat(lastName).isEqualTo("von")
        assertThat(ldapUser).isEqualTo("acole")
    }

    @Test
    fun `map person with lastname is equal to addition`() {
        val (firstName, lastName, title, addition, ldapUser) = mapper.map("Alexander von Van (acole)")
        assertThat(title).isNull()
        assertThat(firstName).isEqualTo("Alexander")
        assertThat(addition).isEqualTo(PersonAddition.VON)
        assertThat(lastName).isEqualTo("Van")
        assertThat(ldapUser).isEqualTo("acole")
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