package de.neusta.ncc.infrastructure

import de.neusta.ncc.application.RoomImportService
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException
import de.neusta.ncc.infrastructure.dto.DefaultSpringErrorDto
import de.neusta.ncc.infrastructure.mapper.CsvImportMapper
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.anyList
import org.mockito.Mockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.lang.AssertionError

/**
 * Mapping and validation tests of {@link UploadController}.
 * <p>
 * Mocks inner mapper and services to be loosely coupled from core logic.
 */
@ExtendWith(SpringExtension::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadControllerTest {

    @Autowired
    private lateinit var uploadRequestSender: UploadRequestSender

    @MockBean
    private lateinit var roomImportServiceMock: RoomImportService

    @MockBean
    private lateinit var csvImportMapperMock: CsvImportMapper

    @Test
    fun testUploadWithSuccess() {
        val exchange = uploadRequestSender.sendUploadRequest("simple.csv", String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body).isEqualTo("{\"uploadedFileName\":\"simple.csv\"}")
    }

    @Test
    fun testUploadWithFileIsNull() {
        val exchange = uploadRequestSender.sendUploadRequest(null, String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchange.body).contains("\"message\":\"Required request part 'file' is not present\"")
    }

    @Test
    fun testUploadWithCsvImportPersonIsNotValid() {
        doThrow(CsvPersonNotValidException("test")).`when`(roomImportServiceMock).importRooms(anyList())

        val exchange = uploadRequestSender.sendUploadRequest("simple.csv", String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchange.body).isEqualTo("{\"code\":4,\"message\":\"Could not map csv person 'test'\"}")
    }

    @Test
    @Disabled("Mocking error")
    fun testUploadWithWrongRoomNumberLength() {
        doThrow(AssertionError("Room with number 111 must have 4 arbitrary characters.")).`when`(csvImportMapperMock).map(any())

        val exchange = uploadRequestSender.sendUploadRequest("room_number_not_valid.csv", String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchange.body).isEqualTo("{\"code\":6,\"message\":\"Room with number 100 must have 4 arbitrary characters.\"}")
    }

    @Test
    fun testUploadWithRoomNumberIsNotUnique() {
        doThrow(RoomIsNotUniqueException()).`when`(roomImportServiceMock).importRooms(anyList())

        val exchange = uploadRequestSender.sendUploadRequest("simple.csv", String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchange.body).isEqualTo("{\"code\":2,\"message\":\"Room numbers should only appear once.\"}")
    }

    @Test
    fun testUploadWithPersonIsNotUnique() {
        doThrow(LdapUserIsNotUniqueException()).`when`(roomImportServiceMock).importRooms(anyList())

        val exchange = uploadRequestSender.sendUploadRequest("simple.csv", String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchange.body).isEqualTo("{\"code\":3,\"message\":\"LDAP users should only appear once.\"}")
    }

    @Test
    fun testUploadWithWrongMethodType() {
        assertMethodNotAllowedForUpload(HttpMethod.DELETE)
        assertMethodNotAllowedForUpload(HttpMethod.GET)
        assertMethodNotAllowedForUpload(HttpMethod.PUT)
    }

    private fun assertMethodNotAllowedForUpload(httpMethod: HttpMethod) {
        val exchange = uploadRequestSender.sendUploadRequest("simple.csv", httpMethod, DefaultSpringErrorDto::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
        assertThat(exchange.body?.timestamp).isNotEmpty()
        assertThat(exchange.body?.status).isEqualTo("405")
        assertThat(exchange.body?.error).isEqualTo("Method Not Allowed")
        assertThat(exchange.body?.message).isEqualTo("Request method '" + httpMethod.name + "' not supported")
        assertThat(exchange.body?.path).isEqualTo("/api/import")
    }

}