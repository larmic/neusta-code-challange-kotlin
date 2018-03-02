package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.tuple
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths

class CsvImportMapperTest {

    private lateinit var mapper: CsvImportMapper

    @Before
    fun setUp() {
        mapper = CsvImportMapper(CsvPersonToPersonMapper())
    }

    @Test(expected = EmptyFileImportException::class)
    fun testMapWithEmptyFile() {
        mapper.map(MockMultipartFile("empty.csv", null as ByteArray?))
    }

    @Test(expected = EmptyFileImportException::class)
    fun testMapNewWithEmptyFile() {
        mapper.map(MockMultipartFile("empty.csv", null as ByteArray?))
    }

    @Test
    @Throws(Exception::class)
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
    @Throws(Exception::class)
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
    @Test(expected = FileImportException::class)
    @Throws(Exception::class)
    fun testMapWithGetBytesThrowsIOException() {
        val multipartFileMock = mock<MultipartFile>(MultipartFile::class.java)
        `when`(multipartFileMock.getBytes()).thenThrow(IOException())
        `when`(multipartFileMock.isEmpty()).thenReturn(false)

        mapper.map(multipartFileMock)
        val dataModel = mapper.map(MockMultipartFile("empty_room.csv", Files.readAllBytes(Paths.get("src/test/resources/upload/empty_room.csv"))))

        assertThat<Room>(dataModel).hasSize(3)
        assertThat(dataModel[0].roomNumber).isEqualTo("1102")
        assertThat<Person>(dataModel[0].persons).isEmpty()
        assertThat(dataModel[1].roomNumber).isEqualTo("1103")
        assertThat<Person>(dataModel[1].persons).isEmpty()
        assertThat(dataModel[2].roomNumber).isEqualTo("1104")
        assertThat<Person>(dataModel[2].persons).isEmpty()
    }
}