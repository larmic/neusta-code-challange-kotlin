package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class PersonTitleTest {

    @Test
    fun `get value by label`() {
        assertThat(PersonTitle.valueOfByLabel("Dr.")).isEqualTo(PersonTitle.DR)
        assertThat(PersonTitle.valueOfByLabel(" Dr. ")).isEqualTo(PersonTitle.DR)
    }

    @Test
    fun `get value by label when label is empty`() {
        assertThat(PersonTitle.valueOfByLabel("")).isNull()
        assertThat(PersonTitle.valueOfByLabel(" ")).isNull()
    }

    @Test
    fun `get value by label when label is null`() {
        assertThat(PersonTitle.valueOfByLabel(null)).isNull()
    }

    @Test
    fun `get value by label when label is unknown`() {
        assertThrows<IllegalArgumentException> { PersonTitle.valueOfByLabel("not-known") }
    }
}