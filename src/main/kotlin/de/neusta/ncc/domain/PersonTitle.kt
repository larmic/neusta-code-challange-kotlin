package de.neusta.ncc.domain

import java.util.*

enum class PersonTitle(val label: String) {

    DR("Dr.");

    companion object {
        fun valueOfByLabel(label: String?): PersonTitle? {
            return if (label.isNullOrBlank()) {
                null
            } else {
                label.mapToAddition() ?: throw IllegalArgumentException("Person title $label is not supported")
            }
        }

        private fun String.mapToAddition() = values().firstOrNull { it.label.lowercase() == this.trimAndLowercase() }
        private fun String.trimAndLowercase() = this.trim { it <= ' ' }.lowercase()
    }
}