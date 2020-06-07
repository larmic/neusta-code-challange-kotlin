package de.neusta.ncc.infrastructure.mapper

import com.fasterxml.jackson.databind.MappingIterator
import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import de.neusta.ncc.domain.Room
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException
import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.IOException
import java.lang.AssertionError
import java.util.*

/**
 * Maps a given {@link MultipartFile} to list of {@link Room}. Does not validate any content of given file.
 */
@Component
class CsvImportMapper(private val csvPersonToPersonMapper: CsvPersonToPersonMapper) {

    private val log = LoggerFactory.getLogger(this.javaClass)

    @Throws(FileImportException::class, EmptyFileImportException::class, AssertionError::class)
    fun map(file: MultipartFile): List<Room> {
        try {
            return file
                    .loadBytes()
                    .mapToSimpleImportModel()
                    .convertCsvImportRooms()
        } catch (e: IOException) {
            log.error("Could not import {}", file.originalFilename, e)
            throw FileImportException(e)
        }

    }

    @Throws(IOException::class)
    private fun MultipartFile.loadBytes(): ByteArray {
        if (this.isEmpty) {
            log.error("File %s is empty", this.originalFilename)
            throw EmptyFileImportException()
        }

        return this.bytes
    }

    @Throws(IOException::class)
    private fun ByteArray.mapToSimpleImportModel(): List<SimpleImportRoom> {
        val dataModel = ArrayList<SimpleImportRoom>()

        val it = readCsvValues(this)
        while (it.hasNext()) {
            val csvRow = it.next()
            val roomNumber = csvRow[0]
            val persons = listOf(*csvRow).subList(1, csvRow.size)

            dataModel.add(SimpleImportRoom(roomNumber, persons.filter { it.isNotEmpty() }))
        }

        return dataModel
    }

    @Throws(IOException::class)
    private fun readCsvValues(csvData: ByteArray): MappingIterator<Array<String>> {
        val mapper = CsvMapper()
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY)
        return mapper.readerFor(Array<String>::class.java).readValues(csvData)
    }

    @Throws(CsvPersonNotValidException::class, AssertionError::class)
    private fun List<SimpleImportRoom>.convertCsvImportRooms(): List<Room> {
        return this.map { it.convertToRoom() }
    }

    private inner class SimpleImportRoom internal constructor(private val room: String, private val persons: List<String>) {

        @Throws(CsvPersonNotValidException::class, AssertionError::class)
        internal fun convertToRoom(): Room {
            val people = persons.map { csvPersonToPersonMapper.map(it) }
            return Room(roomNumber = room, persons = people)
        }
    }
}