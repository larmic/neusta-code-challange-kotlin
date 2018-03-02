package de.neusta.ncc.infrastructure.mapper.exception

class EmptyFileImportException : RuntimeException("Required request part 'file' is empty")