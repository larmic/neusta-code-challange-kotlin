package de.neusta.ncc.domain

enum class PersonAddition(val label: String) {

    VON("von"),
    VAN("van"),
    DE("de");

    companion object {
        fun valueOfByLabel(label: String?): PersonAddition? {
            return if (label.isNullOrBlank()) {
                null
            } else {
                label.findAddition() ?: throw IllegalArgumentException("Person addition $label is not supported")
            }
        }

        private fun String.findAddition() = values().firstOrNull { addition -> addition.label == this.trimAndLowercase() }
        private fun String.trimAndLowercase() = this.trim { it <= ' ' }.toLowerCase()
    }
}