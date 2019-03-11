package de.neusta.ncc.application

import de.neusta.ncc.application.validator.LdapUserUniqueValidator
import de.neusta.ncc.application.validator.RoomNumberValidator
import de.neusta.ncc.application.validator.RoomUniqueValidator
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException
import de.neusta.ncc.domain.Room
import de.neusta.ncc.domain.RoomRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

/**
 * Imports given list of {@link Room} to repository. Existing rooms will be cleared.
 * If importing rooms are not valid (i.e. uniqueness of rooms or person) nothing will be imported.
 */
@Service
class RoomImportService @Autowired constructor(
        private var roomNumberValidator: RoomNumberValidator,
        private var roomUniqueValidator: RoomUniqueValidator,
        private var ldapUserUniqueValidator: LdapUserUniqueValidator,
        private var roomRepository: RoomRepository) {

    @Throws(RoomNumberNotValidException::class, RoomIsNotUniqueException::class, LdapUserIsNotUniqueException::class)
    fun importRooms(rooms: List<Room>) {
        validateRoomNumber(rooms)
        validateRoomNumbersAreUnique(rooms)
        validateLdapUsersAreUnique(rooms)

        roomRepository.replaceRooms(rooms)
    }

    private fun validateRoomNumber(rooms: List<Room>) {
        rooms.forEach { (roomNumber) -> roomNumberValidator.validate(roomNumber) }
    }

    private fun validateRoomNumbersAreUnique(rooms: List<Room>) {
        val roomNumbers = rooms
                .map { r -> r.roomNumber }

        roomUniqueValidator.validate(roomNumbers)
    }

    private fun validateLdapUsersAreUnique(rooms: List<Room>) {
        val ldapUsers = rooms
                .flatMap { r -> r.persons }
                .map { p -> p.ldapUser }

        ldapUserUniqueValidator.validate(ldapUsers)
    }

}