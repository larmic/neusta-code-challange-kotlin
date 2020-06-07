package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

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

    @ParameterizedTest
    @ValueSource(strings = ["", " ", "   "])
    fun `get value by label when label is empty`(label: String) {
        assertThat(PersonAddition.valueOfByLabel(label)).isNull()
    }

    @Test
    fun `get value by label when label is unknown`() {
        assertThrows<IllegalArgumentException> { PersonAddition.valueOfByLabel("not-known") }
    }

}