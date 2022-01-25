package de.neusta.ncc.infrastructure

import de.neusta.ncc.domain.RoomRepository
import de.neusta.ncc.infrastructure.dto.ErrorMessageDto
import de.neusta.ncc.infrastructure.dto.RoomDto
import de.neusta.ncc.infrastructure.mapper.RoomMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(description = "Restservice to load all existings rooms", name = "RoomRestController")
class RoomRestController(private var roomRepository: RoomRepository, private var roomMapper: RoomMapper) {

    @Operation(
        summary = "Loads all existing rooms.",
        description = "If a ldap user name query parameter is used all rooms with persons contains parts of this name will be returned."
    )
    @ApiResponses(
        value = [
            (ApiResponse(responseCode = "200", description = "Success")),
            (ApiResponse(responseCode = "405", description = "Wrong method type")),
            (ApiResponse(responseCode = "500", description = "Internal server error"))]
    )
    @RequestMapping(value = ["/api/room"], method = [(RequestMethod.GET)], produces = ["application/json"])
    fun getAllRooms(): ResponseEntity<List<RoomDto>> =
        ResponseEntity.ok(roomRepository.getRooms().map { roomMapper.mapToDto(it) })

    @Operation(summary = "Loads a specific room for a given number.")
    @ApiResponses(
        value = [
            (ApiResponse(responseCode = "200", description = "Success")),
            (ApiResponse(responseCode = "404", description = "Room for number not found")),
            (ApiResponse(responseCode = "405", description = "Wrong method type")),
            (ApiResponse(responseCode = "500", description = "Internal server error"))]
    )
    @RequestMapping(value = ["/api/room/{number}"], method = [(RequestMethod.GET)], produces = ["application/json"])
    fun getRoom(@PathVariable number: String): ResponseEntity<*> {
        if (isRoomNumberInvalid(number)) {
            return ResponseEntity(
                ErrorMessageDto(6, "Room with number $number must have 4 arbitrary characters."),
                HttpStatus.BAD_REQUEST
            )
        }

        val room = roomRepository.findByRoomNumber(number)
            ?: return ResponseEntity(ErrorMessageDto(5, "Room not found."), HttpStatus.NOT_FOUND)

        return ResponseEntity.ok(roomMapper.mapToDto(room))
    }

    private fun isRoomNumberInvalid(number: String) = number.length != 4
}