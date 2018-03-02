package de.neusta.ncc.infrastructure.mapper.exception

class FileImportException(error: Throwable? = null) : RuntimeException("Access error when get bytes of file.", error)