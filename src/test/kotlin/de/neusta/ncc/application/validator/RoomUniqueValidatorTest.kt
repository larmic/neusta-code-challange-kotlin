package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

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
        val exception = assertThrows<RoomIsNotUniqueException> { validator.validate(rooms) }
        assertThat(exception.message).isEqualTo("Room numbers should only appear once.")
    }
}