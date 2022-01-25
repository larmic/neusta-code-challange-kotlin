package de.neusta.ncc.infrastructure

import de.neusta.ncc.application.RoomImportService
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import de.neusta.ncc.infrastructure.dto.ErrorMessageDto
import de.neusta.ncc.infrastructure.dto.ImportResultDto
import de.neusta.ncc.infrastructure.mapper.CsvImportMapper
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException
import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(description = "Import csv data into app", name = "UploadRestController")
class UploadRestController(
    private var csvImportMapper: CsvImportMapper,
    private var roomImportService: RoomImportService
) {

    @Operation(
        summary = "Imports a csv file to internal data storage.",
        description =
        """ Csv file should have the following syntax:  
      Room number, person1, person2, person3, ...
      where person should be like:
      title firstname extraname addition lastname (ldapUserName) """
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Success"),
            ApiResponse(responseCode = "400", description = "Import failure"),
            ApiResponse(responseCode = "405", description = "Wrong method type"),
            ApiResponse(responseCode = "500", description = "Internal server error")]
    )
    @RequestMapping(
        value = ["/api/import"],
        method = [(RequestMethod.POST)],
        consumes = ["multipart/form-data"],
        produces = ["application/json"]
    )
    fun uploadCsv(@RequestParam file: MultipartFile): ResponseEntity<*> {
        try {
            val rooms = csvImportMapper.map(file)
            roomImportService.importRooms(rooms)

            return ResponseEntity.ok(ImportResultDto(file.originalFilename))
        } catch (e: FileImportException) {
            return buildError(0, e.message)
        } catch (e: EmptyFileImportException) {
            return buildError(0, e.message)
        } catch (e: RoomIsNotUniqueException) {
            return buildError(2, e.message)
        } catch (e: LdapUserIsNotUniqueException) {
            return buildError(3, e.message)
        } catch (e: CsvPersonNotValidException) {
            return buildError(4, e.message)
        } catch (e: AssertionError) {
            if (isRoomNumberInvalidError(e)) {
                return buildError(6, e.message)
            }
            return buildError(0, e.message)
        }

    }

    private fun isRoomNumberInvalidError(error: AssertionError): Boolean {
        val message = error.message
        return message != null && message.startsWith("Room with number") && message.endsWith("must have 4 arbitrary characters.")
    }

    private fun buildError(errorCode: Int, errorMessage: String?): ResponseEntity<ErrorMessageDto> {
        val dto = ErrorMessageDto(errorCode, errorMessage ?: "Unknown error")
        return ResponseEntity(dto, HttpStatus.BAD_REQUEST)
    }

}