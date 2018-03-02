package de.neusta.ncc.application.validator.exception

class LdapUserIsNotUniqueException : RuntimeException("LDAP users should only appear once.")