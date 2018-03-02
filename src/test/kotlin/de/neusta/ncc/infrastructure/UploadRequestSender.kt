package de.neusta.ncc.infrastructure

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.core.io.FileSystemResource
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap

@Component
class UploadRequestSender {

    @Autowired
    private lateinit var testRestTemplate: TestRestTemplate

    fun <T> sendUploadRequest(file: String?, responseType: Class<T>): ResponseEntity<T> {
        return sendUploadRequest(file, HttpMethod.POST, responseType)
    }

    fun <T> sendUploadRequest(file: String?, httpMethod: HttpMethod, responseType: Class<T>): ResponseEntity<T> {
        val fileSystemResource = buildFileResource(file)
        val requestEntity = buildHttpEntity(fileSystemResource)

        return testRestTemplate.exchange("/api/import", httpMethod, requestEntity, responseType)
    }

    private fun buildFileResource(file: String?): FileSystemResource? {
        return if (file != null) FileSystemResource("src/test/resources/upload/" + file) else null
    }

    private fun buildHttpEntity(fileSystemResource: FileSystemResource?): HttpEntity<*> {
        val parameters = LinkedMultiValueMap<String, Any>()
        parameters.add("file", fileSystemResource)

        val headers = HttpHeaders()
        headers.contentType = MediaType.MULTIPART_FORM_DATA

        return HttpEntity<MultiValueMap<String, Any>>(parameters, headers)
    }

}