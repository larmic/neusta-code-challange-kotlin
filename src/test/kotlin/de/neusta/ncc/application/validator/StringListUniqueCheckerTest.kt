package de.neusta.ncc.application.validator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.util.*

class StringListUniqueCheckerTest {

    private val validator = StringListUniqueChecker()

    @Test
    fun testValidate() {
        assertThat(validator.itemsUnique(listOf("1243", "ÄÖÜ?", "1222"))).isTrue()
    }

    @Test
    fun testValidateWithSimpleDuplicates() {
        assertThat(validator.itemsUnique(listOf("1243", "ÄÖÜ?", "1222", "1222"))).isFalse()
        assertThat(validator.itemsUnique(listOf("1243", "ÄÖÜ?", "1222", "1222"))).isFalse()
        assertThat(validator.itemsUnique(listOf("1243", "1243", "ÄÖÜ?", "1222"))).isFalse()
    }

    @Test
    fun testValidateWithFailedCaseInsensitive() {
        assertThat(validator.itemsUnique(listOf("äÖÜ?", "ÄÖÜ?"))).isFalse()
        assertThat(validator.itemsUnique(listOf("äÖÜ?", "ÄÖÜ?"))).isFalse()
        assertThat(validator.itemsUnique(listOf("ZZZZ", "zzZz"))).isFalse()
    }

    @Test
    fun testValidateWithNumbersAreEmpty() {
        assertThat(validator.itemsUnique(emptyList())).isTrue()
    }
}