package de.neusta.ncc.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class PersonTest {

    @Test
    fun testToString() {
        assertThat(Person.PersonBuilder("Susanne", "Moog", "smoog")
                .build()
                .toString())
                .isEqualTo("Susanne Moog (smoog)")

        assertThat(Person.PersonBuilder("Uwe", "Svensson", "usvens")
                .title(PersonTitle.DR)
                .build()
                .toString())
                .isEqualTo("Dr. Uwe Svensson (usvens)")

        assertThat(Person.PersonBuilder("Alexander", "Cole", "acole")
                .addition(PersonAddition.VAN)
                .build()
                .toString())
                .isEqualTo("Alexander van Cole (acole)")

        assertThat(Person.PersonBuilder("Alexander", "Cole", "acole")
                .addition(PersonAddition.VON)
                .build()
                .toString())
                .isEqualTo("Alexander von Cole (acole)")

        assertThat(Person.PersonBuilder("Alexander", "Cole", "acole")
                .addition(PersonAddition.DE)
                .build()
                .toString())
                .isEqualTo("Alexander de Cole (acole)")

        assertThat(Person.PersonBuilder("Alexander", "Cole", "acole")
                .title(PersonTitle.DR)
                .secondFirstName("James")
                .build()
                .toString())
                .isEqualTo("Dr. Alexander James Cole (acole)")

        assertThat(Person.PersonBuilder("Alexander", "Cole", "acole")
                .title(PersonTitle.DR)
                .secondFirstName("James")
                .addition(PersonAddition.VON)
                .build()
                .toString())
                .isEqualTo("Dr. Alexander James von Cole (acole)")
    }

}