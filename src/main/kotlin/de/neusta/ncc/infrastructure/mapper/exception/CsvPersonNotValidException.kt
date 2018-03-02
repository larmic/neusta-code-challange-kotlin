package de.neusta.ncc.infrastructure.mapper.exception

class CsvPersonNotValidException constructor(var person: String) : RuntimeException("Could not map csv person '$person'")