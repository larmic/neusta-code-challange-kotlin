package de.neusta.ncc

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod

/**
 * Redirect to swagger-ui.html.html.
 */
@Controller
class RedirectToIndexController {

    @RequestMapping(value = ["/"], method = [(RequestMethod.GET)])
    fun redirect(): String {
        return "redirect:swagger-ui.html"
    }

}