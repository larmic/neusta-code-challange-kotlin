package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class PersonAdditionTest {

    @Test
    fun `get value by label`() {
        assertThat(PersonAddition.valueOfByLabel("von")).isEqualTo(PersonAddition.VON)
        assertThat(PersonAddition.valueOfByLabel(" von ")).isEqualTo(PersonAddition.VON)
        assertThat(PersonAddition.valueOfByLabel("van")).isEqualTo(PersonAddition.VAN)
        assertThat(PersonAddition.valueOfByLabel(" van ")).isEqualTo(PersonAddition.VAN)
        assertThat(PersonAddition.valueOfByLabel("de")).isEqualTo(PersonAddition.DE)
        assertThat(PersonAddition.valueOfByLabel(" de ")).isEqualTo(PersonAddition.DE)
    }

    @Test
    fun `get value by label when label is empty`() {
        assertThat(PersonAddition.valueOfByLabel("")).isNull()
        assertThat(PersonAddition.valueOfByLabel(" ")).isNull()
    }

    @Test(expected = IllegalArgumentException::class)
    fun `get value by label when label is unknown`() {
        PersonAddition.valueOfByLabel("not-known")
    }

}