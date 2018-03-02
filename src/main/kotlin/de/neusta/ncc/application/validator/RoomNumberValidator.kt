package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException
import org.springframework.stereotype.Component

/**
 * Validate length of a room number.
 * Acceptance criteria: 4 arbitrary characters
 */
@Component
class RoomNumberValidator {

    @Throws(RoomNumberNotValidException::class)
    fun validate(room: String?) {
        if (!(room != null && room.length == 4)) {
            throw RoomNumberNotValidException(room)
        }
    }

}