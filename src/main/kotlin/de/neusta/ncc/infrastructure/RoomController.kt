package de.neusta.ncc.infrastructure

import de.neusta.ncc.domain.Room
import de.neusta.ncc.domain.RoomRepository
import de.neusta.ncc.infrastructure.dto.DefaultSpringErrorDto
import de.neusta.ncc.infrastructure.dto.ErrorMessageDto
import de.neusta.ncc.infrastructure.dto.RoomDto
import de.neusta.ncc.infrastructure.mapper.RoomMapper
import io.swagger.annotations.*
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class RoomController(private var roomRepository: RoomRepository,
                     private var roomMapper: RoomMapper) {

    @ApiOperation(value = "Loads all existing rooms.", notes = "If a ldap user name query parameter is used all rooms with persons contains parts of this name will be returned.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success")),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/room"], method = [(RequestMethod.GET)], produces = ["application/json"])
    fun getAllRooms(): ResponseEntity<List<RoomDto>> {
        return ResponseEntity.ok(roomRepository.getRooms().map { roomMapper.mapToDto(it) })
    }

    @ApiOperation(value = "Loads a specific room for a given number.")
    @ApiResponses(value = [
        (ApiResponse(code = 200, message = "Success", response = RoomDto::class)),
        (ApiResponse(code = 404, message = "Room for number not found", response = ErrorMessageDto::class)),
        (ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto::class)),
        (ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto::class))])
    @RequestMapping(value = ["/api/room/{number}"], method = [(RequestMethod.GET)], produces = ["application/json"])
    fun getRoom(@PathVariable number: String): ResponseEntity<*> {
        if (!isRoomNumberValid(number)) {
            return ResponseEntity(ErrorMessageDto(6, "Room with number $number must have 4 arbitrary characters."), HttpStatus.BAD_REQUEST)
        }

        val room = roomRepository.findByRoomNumber(number)
                ?: return ResponseEntity(ErrorMessageDto(5, "Room not found."), HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(roomMapper.mapToDto(room))
    }

    private fun isRoomNumberValid(number: String) = number.length == 4
}