package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test
import java.util.*

class LdapUserUniqueValidatorTest {

    private val validator = LdapUserUniqueValidator()

    @Test
    fun validate() {
        validator.validate(emptyList())
        validator.validate(listOf("dreuschling"))
        validator.validate(Arrays.asList("dreuschling", "rsheho"))
        validator.validate(Arrays.asList("dreuschling", "ahaeusler"))
        validator.validate(Arrays.asList("dreuschling", "rsheho", "ahaeusler"))
    }

    @Test
    fun validateWithDuplicates() {
        assertException(Arrays.asList("dreuschling", "dreuschling"))
        assertException(Arrays.asList("dreuschling", "rsheho", "dreuschling"))
        assertException(Arrays.asList("dreuschling", "rsheho", "rsheho", "ahaeusler"))
    }

    private fun assertException(rooms: List<String>) {
        try {
            validator.validate(rooms)
            fail("Should throw exception")
        } catch (e: LdapUserIsNotUniqueException) {
            assertThat(e.message).isEqualTo("LDAP users should only appear once.")
        }

    }

}