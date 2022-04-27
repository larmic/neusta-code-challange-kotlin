package de.neusta.ncc.application.validator

import java.util.*

/**
 * Validate each string is unique in given list of strings (with ignoring case sensitive).
 */
class StringListUniqueChecker {

    fun itemsUnique(values: List<String>): Boolean {
        val lowerCaseRooms = values.map { it.lowercase() }
        val roomFilteredByDuplicates = HashSet(lowerCaseRooms)

        return roomFilteredByDuplicates.size == values.size
    }

}