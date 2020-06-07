package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class RoomNumberValidatorTest {

    private val validator = RoomNumberValidator()

    @Test
    fun validate() {
        validator.validate("1234")
        validator.validate("ABCF")
        validator.validate("/[]{")
        validator.validate("ÄÜÖ?")
    }

    @Test
    fun validateWithTooShort() {
        assertException("123")
        assertException("AB")
        assertException("/")
    }

    @Test
    fun validateWithTooLong() {
        assertException("1234Z")
        assertException("ABCFZZ")
        assertException("/[]{ZZZ")
        assertException("ÄÜÖ?ZZZZ")
    }

    @Test
    fun validateWithEmptyValue() {
        assertThrows<RoomNumberNotValidException> { validator.validate("") }
    }

    @Test
    fun validateWithNullValue() {
        assertThrows<RoomNumberNotValidException> { validator.validate(null) }
    }

    private fun assertException(room: String) {
        val exception = assertThrows<RoomNumberNotValidException> { validator.validate(room) }
        assertThat(exception.message).isEqualTo("Room with number $room must have 4 arbitrary characters.")
    }
}