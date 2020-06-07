package de.neusta.ncc.application

import de.neusta.ncc.application.validator.LdapUserUniqueValidator
import de.neusta.ncc.application.validator.RoomUniqueValidator
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import de.neusta.ncc.domain.Room
import de.neusta.ncc.domain.RoomRepository
import org.springframework.stereotype.Service

/**
 * Imports given list of {@link Room} to repository. Existing rooms will be cleared.
 * If importing rooms are not valid (i.e. uniqueness of rooms or person) nothing will be imported.
 */
@Service
class RoomImportService(private var roomUniqueValidator: RoomUniqueValidator,
                        private var ldapUserUniqueValidator: LdapUserUniqueValidator,
                        private var roomRepository: RoomRepository) {

    @Throws(RoomIsNotUniqueException::class, LdapUserIsNotUniqueException::class)
    fun importRooms(rooms: List<Room>) {
        validateRoomNumbersAreUnique(rooms)
        validateLdapUsersAreUnique(rooms)

        roomRepository.replaceRooms(rooms)
    }

    private fun validateRoomNumbersAreUnique(rooms: List<Room>) {
        val roomNumbers = rooms.map { it.roomNumber }

        roomUniqueValidator.validate(roomNumbers)
    }

    private fun validateLdapUsersAreUnique(rooms: List<Room>) {
        val ldapUsers = rooms
                .flatMap { it.persons }
                .map { it.ldapUser }

        ldapUserUniqueValidator.validate(ldapUsers)
    }

}