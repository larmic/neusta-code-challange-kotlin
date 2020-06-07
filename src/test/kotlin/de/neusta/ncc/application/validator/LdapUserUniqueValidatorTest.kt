package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.*

class LdapUserUniqueValidatorTest {

    private val validator = LdapUserUniqueValidator()

    @Test
    fun validate() {
        validator.validate(emptyList())
        validator.validate(listOf("dreuschling"))
        validator.validate(listOf("dreuschling", "rsheho"))
        validator.validate(listOf("dreuschling", "ahaeusler"))
        validator.validate(listOf("dreuschling", "rsheho", "ahaeusler"))
    }

    @Test
    fun validateWithDuplicates() {
        assertException(listOf("dreuschling", "dreuschling"))
        assertException(listOf("dreuschling", "rsheho", "dreuschling"))
        assertException(listOf("dreuschling", "rsheho", "rsheho", "ahaeusler"))
    }

    private fun assertException(rooms: List<String>) {
        val exception = assertThrows<LdapUserIsNotUniqueException> { validator.validate(rooms) }
        assertThat(exception.message).isEqualTo("LDAP users should only appear once.")
    }

}