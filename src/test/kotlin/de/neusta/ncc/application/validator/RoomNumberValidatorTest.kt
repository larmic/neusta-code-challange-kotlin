package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.fail
import org.junit.Test

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

    @Test(expected = RoomNumberNotValidException::class)
    fun validateWithEmptyValue() {
        validator.validate("")
    }

    @Test(expected = RoomNumberNotValidException::class)
    fun validateWithNullValue() {
        validator.validate(null)
    }

    private fun assertException(room: String) {
        try {
            validator.validate(room)
            fail("Should throw exception")
        } catch (e: RoomNumberNotValidException) {
            assertThat(e.message).isEqualTo("Room with number $room must have 4 arbitrary characters.")
        }

    }
}