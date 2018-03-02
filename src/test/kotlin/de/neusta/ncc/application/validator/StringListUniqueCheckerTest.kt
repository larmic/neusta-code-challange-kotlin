package de.neusta.ncc.application.validator

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import java.util.*

class StringListUniqueCheckerTest {

    private val validator = StringListUniqueChecker()

    @Test
    fun testValidate() {
        assertThat(validator.itemsUnique(Arrays.asList("1243", "ÄÖÜ?", "1222"))).isTrue()
    }

    @Test
    fun testValidateWithSimpleDuplicates() {
        assertThat(validator.itemsUnique(Arrays.asList("1243", "ÄÖÜ?", "1222", "1222"))).isFalse()
        assertThat(validator.itemsUnique(Arrays.asList("1243", "ÄÖÜ?", "1222", "1222"))).isFalse()
        assertThat(validator.itemsUnique(Arrays.asList("1243", "1243", "ÄÖÜ?", "1222"))).isFalse()
    }

    @Test
    fun testValidateWithFailedCaseInsensitive() {
        assertThat(validator.itemsUnique(Arrays.asList("äÖÜ?", "ÄÖÜ?"))).isFalse()
        assertThat(validator.itemsUnique(Arrays.asList("äÖÜ?", "ÄÖÜ?"))).isFalse()
        assertThat(validator.itemsUnique(Arrays.asList("ZZZZ", "zzZz"))).isFalse()
    }

    @Test
    fun testValidateWithNumbersAreEmpty() {
        assertThat(validator.itemsUnique(emptyList())).isTrue()
    }
}