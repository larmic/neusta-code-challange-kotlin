package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import org.springframework.stereotype.Component

/**
 * Validate each room is unique in given list of strings (with ignoring case sensitive).
 * Acceptance criteria: A room exists only once in an import file
 */
@Component
class RoomUniqueValidator {

    @Throws(RoomIsNotUniqueException::class)
    fun validate(rooms: List<String>) {
        if (!StringListUniqueChecker().itemsUnique(rooms)) {
            throw RoomIsNotUniqueException()
        }
    }

}