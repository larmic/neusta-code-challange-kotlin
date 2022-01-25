package de.neusta.ncc

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean

@SpringBootApplication
open class NccApplication {

    @Bean
    open fun api() = OpenAPI().info(
        Info().title("Neusta code challenge REST services")
            .version("0.0.1")
            .contact(Contact().name("Lars Michaelis").email("l.michaelis@neusta.de"))
    )

}

fun main(args: Array<String>) {
    runApplication<NccApplication>(*args)
}