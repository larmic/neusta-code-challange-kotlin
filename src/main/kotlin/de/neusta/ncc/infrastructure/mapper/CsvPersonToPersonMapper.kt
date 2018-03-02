package de.neusta.ncc.infrastructure.mapper

import de.neusta.ncc.domain.Person
import de.neusta.ncc.domain.PersonAddition
import de.neusta.ncc.domain.PersonTitle
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException
import org.springframework.stereotype.Component

@Component
class CsvPersonToPersonMapper {

    private val pattern = ("^"
            + "\\s*"
            + "(?<title>(Dr\\.))?"
            + "\\s*"
            + "(?<firstName>\\S+)"
            + "\\s*"
            + "(?<extraName>\\S+)?(?<!von|van|de)"
            + "\\s*"
            + "(?<addition>von|van|de)?"
            + "\\s+"
            + "(?<lastName>\\S+)"
            + "\\s+"
            + "\\((?<ldap>\\S+)\\)"
            + "\\s*"
            + "$").toRegex()

    @Throws(CsvPersonNotValidException::class)
    fun map(csvImportPerson: String): Person {
        val groups = pattern.matchEntire(csvImportPerson)?.groups
        if (pattern.matches(csvImportPerson)) {
            return Person.PersonBuilder(groups!![3]!!.value, groups[6]!!.value, groups[7]!!.value)
                    .title(PersonTitle.valueOfByLabel(groups[2]?.value))
                    .secondFirstName(groups[4]?.value)
                    .addition(PersonAddition.valueOfByLabel(groups[5]?.value))
                    .build()
        }

        throw CsvPersonNotValidException(csvImportPerson)
    }

}