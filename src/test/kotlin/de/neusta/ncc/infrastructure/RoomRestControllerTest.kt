package de.neusta.ncc.infrastructure

import com.ninjasquad.springmockk.MockkBean
import de.neusta.ncc.domain.Room
import de.neusta.ncc.domain.RoomRepository
import de.neusta.ncc.infrastructure.dto.DefaultSpringErrorDto
import de.neusta.ncc.infrastructure.dto.RoomDto
import de.neusta.ncc.infrastructure.mapper.CsvPersonToPersonMapper
import io.mockk.every
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.groups.Tuple
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RoomRestControllerTest {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    @Autowired
    private lateinit var csvPersonToPersonMapper: CsvPersonToPersonMapper

    @MockkBean
    private lateinit var roomRepositoryMock: RoomRepository

    @BeforeEach
    fun setUp() {
        every { (roomRepositoryMock.getRooms()) } returns listOf(
            createRoom("1000", "Susanne Moog (smoog)"),
            createRoom("1001", "Alexander James Cole (acole)", "Dr. Samin van Ölker (soelker)")
        )
        every { roomRepositoryMock.findByRoomNumber("1000") } returns createRoom("1000", "Susanne Moog (smoog)")
        every { roomRepositoryMock.findByRoomNumber("1001") } returns createRoom(
            roomNumber = "1001",
            "Alexander James Cole (acole)",
            "Dr. Samin van Ölker (soelker)"
        )
    }

    @Test
    fun `find all rooms`() {
        val exchange = testRestTemplate.exchange(
            "/api/room",
            HttpMethod.GET,
            HttpEntity.EMPTY,
            object : ParameterizedTypeReference<List<RoomDto>>() {

            })

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body).extracting("room").containsExactlyInAnyOrder("1000", "1001")
        assertThat(getRoomFrom(exchange.body!!, "1000")!!.people)
            .extracting("title", "firstName", "addition", "lastName", "ldapUser")
            .containsExactlyInAnyOrder(
                Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
            )
        assertThat(getRoomFrom(exchange.body!!, "1001")!!.people)
            .extracting("title", "firstName", "addition", "lastName", "ldapUser")
            .containsExactlyInAnyOrder(
                Tuple.tuple(null, "Alexander James", null, "Cole", "acole"),
                Tuple.tuple("Dr.", "Samin", "van", "Ölker", "soelker")
            )
    }

    @Test
    fun `find all rooms and filter by empty ldap user`() {
        val exchange = testRestTemplate.exchange(
            "/api/room?ldapUser=",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<RoomDto>>() {

            })

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body).extracting("room").containsExactlyInAnyOrder("1000", "1001")
        assertThat(getRoomFrom(exchange.body!!, "1000")!!.people)
            .extracting("title", "firstName", "addition", "lastName", "ldapUser")
            .containsExactlyInAnyOrder(
                Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
            )
        assertThat(getRoomFrom(exchange.body!!, "1001")!!.people)
            .extracting("title", "firstName", "addition", "lastName", "ldapUser")
            .containsExactlyInAnyOrder(
                Tuple.tuple(null, "Alexander James", null, "Cole", "acole"),
                Tuple.tuple("Dr.", "Samin", "van", "Ölker", "soelker")
            )
    }

    @Test
    fun testGetAllRoomsWithNoRoomsExists() {
        every { (roomRepositoryMock.getRooms()) } returns emptyList()

        val exchange = testRestTemplate.exchange(
            "/api/room",
            HttpMethod.GET,
            null,
            object : ParameterizedTypeReference<List<RoomDto>>() {

            })

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body).isEmpty()
        assertThat(exchange.body).isNotNull()
    }

    @Test
    fun testGetRoom() {
        val room1000 = testRestTemplate.exchange("/api/room/1000", HttpMethod.GET, null, RoomDto::class.java)
        assertThat(room1000.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(room1000.body!!.room).isEqualTo("1000")
        assertThat(room1000.body!!.people)
            .extracting("title", "firstName", "addition", "lastName", "ldapUser")
            .containsExactlyInAnyOrder(
                Tuple.tuple(null, "Susanne", null, "Moog", "smoog")
            )

        val room1001 = testRestTemplate.exchange("/api/room/1001", HttpMethod.GET, null, RoomDto::class.java)
        assertThat(room1001.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(room1001.body!!.room).isEqualTo("1001")
        assertThat(room1001.body!!.people)
            .extracting("title", "firstName", "addition", "lastName", "ldapUser")
            .containsExactlyInAnyOrder(
                Tuple.tuple(null, "Alexander James", null, "Cole", "acole"),
                Tuple.tuple("Dr.", "Samin", "van", "Ölker", "soelker")
            )
    }

    @Test
    fun testGetRoomWithWrongLength() {
        val exchangeLength1 = testRestTemplate.exchange("/api/room/1", HttpMethod.GET, null, String::class.java)
        assertThat(exchangeLength1.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchangeLength1.body).isEqualTo("{\"code\":6,\"message\":\"Room with number 1 must have 4 arbitrary characters.\"}")

        val exchangeLength2 = testRestTemplate.exchange("/api/room/10", HttpMethod.GET, null, String::class.java)
        assertThat(exchangeLength2.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchangeLength2.body).isEqualTo("{\"code\":6,\"message\":\"Room with number 10 must have 4 arbitrary characters.\"}")

        val exchangeLength3 = testRestTemplate.exchange("/api/room/101", HttpMethod.GET, null, String::class.java)
        assertThat(exchangeLength3.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchangeLength3.body).isEqualTo("{\"code\":6,\"message\":\"Room with number 101 must have 4 arbitrary characters.\"}")

        val exchangeLength5 = testRestTemplate.exchange("/api/room/10151", HttpMethod.GET, null, String::class.java)
        assertThat(exchangeLength5.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchangeLength5.body).isEqualTo("{\"code\":6,\"message\":\"Room with number 10151 must have 4 arbitrary characters.\"}")

        val exchangeLength6 = testRestTemplate.exchange("/api/room/101512", HttpMethod.GET, null, String::class.java)
        assertThat(exchangeLength6.statusCode).isEqualTo(HttpStatus.BAD_REQUEST)
        assertThat(exchangeLength6.body).isEqualTo("{\"code\":6,\"message\":\"Room with number 101512 must have 4 arbitrary characters.\"}")
    }

    @Test
    fun testGetRoomWithNotFound() {
        every { (roomRepositoryMock.findByRoomNumber("1015")) } returns null

        val exchange = testRestTemplate.exchange("/api/room/1015", HttpMethod.GET, null, String::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.NOT_FOUND)
        assertThat(exchange.body).isEqualTo("{\"code\":5,\"message\":\"Room not found.\"}")
    }

    @Test
    fun testGetRoomWithWrongMethodType() {
        assertMethodNotAllowedForUpload(HttpMethod.DELETE, "/api/room/1000")
        assertMethodNotAllowedForUpload(HttpMethod.POST, "/api/room/1000")
        assertMethodNotAllowedForUpload(HttpMethod.PUT, "/api/room/1000")
    }

    @Test
    fun testGetAllRoomsWithWrongMethodType() {
        assertMethodNotAllowedForUpload(HttpMethod.DELETE, "/api/room")
        assertMethodNotAllowedForUpload(HttpMethod.POST, "/api/room")
        assertMethodNotAllowedForUpload(HttpMethod.PUT, "/api/room")
    }

    private fun createRoom(roomNumber: String, vararg persons: String): Room {
        val per = persons.map { csvPersonToPersonMapper.map(it) }
        return Room(roomNumber = roomNumber, persons = per)
    }

    private fun getRoomFrom(rooms: List<RoomDto>, roomNumber: String): RoomDto? {
        return rooms.stream()
            .filter { (_, room) -> roomNumber == room }
            .findAny().orElse(null)
    }


    private fun assertMethodNotAllowedForUpload(httpMethod: HttpMethod, url: String) {
        val exchange = testRestTemplate.exchange(url, httpMethod, HttpEntity.EMPTY, DefaultSpringErrorDto::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.METHOD_NOT_ALLOWED)
        assertThat(exchange.body!!.timestamp).isNotEmpty()
        assertThat(exchange.body!!.status).isEqualTo("405")
        assertThat(exchange.body!!.error).isEqualTo("Method Not Allowed")
        assertThat(exchange.body!!.path).isEqualTo(url)
    }
}