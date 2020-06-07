package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class CsvImportMapperTest {

    private var mapper = CsvImportMapper(CsvPersonToPersonMapper())

    @Test
    fun testMapWithEmptyFile() {
        assertThrows<EmptyFileImportException> { mapper.map(MockMultipartFile("empty.csv", null as ByteArray?)) }
    }

    @Test
    fun testMapNewWithEmptyFile() {
        assertThrows<EmptyFileImportException> { mapper.map(MockMultipartFile("empty.csv", null as ByteArray?)) }
    }

    @Test
    fun testMapWithEmptyRoom() {
        val dataModel = mapper.map(MockMultipartFile("empty_room.csv", Files.readAllBytes(Paths.get("src/test/resources/upload/empty_room.csv"))))

        assertThat(dataModel).hasSize(3)
        assertThat(dataModel[0].roomNumber).isEqualTo("1102")
        assertThat(dataModel[0].persons).isEmpty()
        assertThat(dataModel[1].roomNumber).isEqualTo("1103")
        assertThat(dataModel[1].persons).isEmpty()
        assertThat(dataModel[2].roomNumber).isEqualTo("1104")
        assertThat(dataModel[2].persons).isEmpty()
    }

    @Test
    fun testMapSimple() {
        val dataModel = mapper.map(MockMultipartFile("simple.csv", Files.readAllBytes(Paths.get("src/test/resources/upload/simple.csv"))))

        assertThat(dataModel).hasSize(2)
        assertThat(dataModel[0].roomNumber).isEqualTo("1111")
        assertThat(dataModel[0].persons)
                .extracting("firstName", "lastName", "ldapUser")
                .containsOnlyOnce(
                        tuple("Dennis", "Fischer", "dfischer"),
                        tuple("Frank", "Supper", "fsupper"),
                        tuple("Susanne", "Moog", "smoog")
                )
        assertThat(dataModel[1].roomNumber).isEqualTo("1110")
        assertThat(dataModel[1].persons)
                .extracting("firstName", "lastName", "ldapUser")
                .containsOnlyOnce(
                        tuple("Christina", "Hülsemann", "chuelsemann"),
                        tuple("Iftikar Ahmad", "Khan", "ikhan"),
                        tuple("Mabelle", "Tengue", "mtengue"),
                        tuple("Ralf", "Schmidt", "rschmidt")
                )
    }

    /**
     * No blackbox test. [MultipartFile.getBytes] throws checked [IOException] so this test ensures
     * exception will be handled correctly.
     */
    @Test
    @Disabled
    fun testMapWithGetBytesThrowsIOException() {
        val multipartFileMock = mock(MultipartFile::class.java)
        `when`(multipartFileMock.bytes).thenThrow(IOException())
        `when`(multipartFileMock.isEmpty).thenReturn(false)

        assertThrows<FileImportException> { mapper.map(multipartFileMock) }
    }
}