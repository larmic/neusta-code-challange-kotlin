package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test

class RoomUniqueValidatorTest {

    private val validator = RoomUniqueValidator()

    @Test
    fun validate() {
        validator.validate(emptyList())
        validator.validate(listOf("1234"))
        validator.validate(listOf("1234", "1235"))
        validator.validate(listOf("1234", "1236"))
        validator.validate(listOf("1234", "1235", "1236"))
    }

    @Test
    fun validateWithDuplicates() {
        assertException(listOf("1234", "1234"))
        assertException(listOf("1234", "1235", "1234"))
        assertException(listOf("1234", "1235", "1235", "1236"))
    }

    private fun assertException(rooms: List<String>) {
        try {
            validator.validate(rooms)
            fail("Should throw exception")
        } catch (e: RoomIsNotUniqueException) {
            assertThat(e.message).isEqualTo("Room numbers should only appear once.")
        }

    }
}