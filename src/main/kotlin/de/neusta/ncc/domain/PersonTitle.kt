package de.neusta.ncc.domain

import java.util.*

enum class PersonTitle(val label: String) {

    DR("Dr.");

    companion object {
        fun valueOfByLabel(label: String?): PersonTitle? {
            return if (isLabelEmpty(label)) {
                null
            } else findAddition(label!!)
                    .orElseThrow { IllegalArgumentException(String.format("Person addition %label is not supported", label)) }

        }

        private fun findAddition(label: String): Optional<PersonTitle> {
            for (addition in PersonTitle.values()) {
                if (addition.label.toLowerCase() == label.trim { it <= ' ' }.toLowerCase()) {
                    return Optional.of(addition)
                }
            }
            return Optional.empty()
        }

        private fun isLabelEmpty(label: String?): Boolean {
            return label == null || label.isEmpty() || label.trim { it <= ' ' }.isEmpty()
        }
    }
}