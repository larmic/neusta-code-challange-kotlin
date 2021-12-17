package de.neusta.ncc.domain

import java.util.*

enum class PersonAddition(val label: String) {

    VON("von"),
    VAN("van"),
    DE("de");

    companion object {
        fun valueOfByLabel(label: String?): PersonAddition? {
            return if (label.isNullOrBlank()) {
                null
            } else {
                label.mapToAddition() ?: throw IllegalArgumentException("Person addition $label is not supported")
            }
        }

        private fun String.mapToAddition() = values().firstOrNull { it.label == this.trimAndLowercase() }
        private fun String.trimAndLowercase() = this.trim { it <= ' ' }.lowercase()
    }
}