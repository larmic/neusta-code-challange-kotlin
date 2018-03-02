package de.neusta.ncc.infrastructure

import de.neusta.ncc.domain.Room
import de.neusta.ncc.domain.RoomRepository
import de.neusta.ncc.infrastructure.dto.ImportResultDto
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

/**
 * Complete (happy path) integration test. For validation and mapping tests see {@link UploadControllerTest}.
 */
@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UploadControllerIntegrationTest {

    @Autowired
    private lateinit var uploadRequestSender: UploadRequestSender

    @Autowired
    private lateinit var roomRepository: RoomRepository

    @Test
    fun testUploadWithSuccess() {
        val exchange = uploadRequestSender.sendUploadRequest("simple.csv", ImportResultDto::class.java)

        assertThat(exchange.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(exchange.body.uploadedFileName).isEqualTo("simple.csv")

        assertThat<Room>(roomRepository.getRooms()).hasSize(2)

        val room1111 = roomRepository.findByRoomNumber("1111")
        assertThat(room1111).isNotNull()
        assertThat(room1111!!.roomNumber).isEqualTo("1111")
        assertThat(room1111.persons)
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple(null, "Dennis", null, "Fischer", "dfischer"),
                        tuple(null, "Frank", null, "Supper", "fsupper"),
                        tuple(null, "Susanne", null, "Moog", "smoog")
                )

        val room1110 = roomRepository.findByRoomNumber("1110")
        assertThat(room1110).isNotNull()
        assertThat(room1110!!.roomNumber).isEqualTo("1110")
        assertThat(room1110.persons)
                .extracting("title", "firstName", "addition", "lastName", "ldapUser")
                .containsExactlyInAnyOrder(
                        tuple(null, "Christina", null, "Hülsemann", "chuelsemann"),
                        tuple(null, "Iftikar Ahmad", null, "Khan", "ikhan"),
                        tuple(null, "Mabelle", null, "Tengue", "mtengue"),
                        tuple(null, "Ralf", null, "Schmidt", "rschmidt")
                )

    }

}