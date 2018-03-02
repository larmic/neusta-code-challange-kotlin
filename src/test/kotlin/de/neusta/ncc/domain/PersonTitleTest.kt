package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

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

    @Test(expected = IllegalArgumentException::class)
    fun `get value by label when label is unknown`() {
        PersonTitle.valueOfByLabel("not-known")
    }

}