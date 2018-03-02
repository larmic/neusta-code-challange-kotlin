package de.neusta.ncc.domain

enum class PersonAddition(val label: String) {

    VON("von"),
    VAN("van"),
    DE("de");

    companion object {
        fun valueOfByLabel(label: String?): PersonAddition? {
            return if (isLabelEmpty(label)) {
                null
            } else {
                findAddition(label!!) ?: throw IllegalArgumentException(String.format("Person addition %label is not supported", label))
            }
        }

        private fun findAddition(label: String): PersonAddition? {
            return PersonAddition.values().firstOrNull { addition -> addition.label.toLowerCase() == label.trim { it <= ' ' }.toLowerCase() }
        }

        private fun isLabelEmpty(label: String?): Boolean {
            return label == null || label.isEmpty() || label.trim { it <= ' ' }.isEmpty()
        }
    }
}