package de.neusta.ncc.application.validator

import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException
import org.springframework.stereotype.Component

/**
 * Validate each ldap user is unique in given list of strings (with ignoring case sensitive).
 * Acceptance criteria: A person exists only once in an import file
 */
@Component
class LdapUserUniqueValidator {

    @Throws(LdapUserIsNotUniqueException::class)
    fun validate(ldapUsers: List<String>) {
        if (!StringListUniqueChecker().itemsUnique(ldapUsers)) {
            throw LdapUserIsNotUniqueException()
        }
    }

}