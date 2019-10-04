package com.nicopaez.passwordapi;

import org.apache.commons.text.RandomStringGenerator;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@RestController
public class PasswordController {

    @RequestMapping(value="/", method=RequestMethod.GET)
    @ApiIgnore
    public RedirectView index() {
        return new RedirectView("/swagger-ui.html", true);
    }

    @RequestMapping(value="/hash", method= RequestMethod.GET)
    public Map<String, Object> hash(String password) {
        String hashed = BCrypt.hashpw(password, BCrypt.gensalt(12));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("plain_pass", password);
        map.put("hash", hashed);
        return map;
    }

    @RequestMapping(value="/check-match", method= RequestMethod.GET)
    public Map<String, Object> checkMatch(String password, String hash) {
        boolean matched = false;
        try {
             matched = BCrypt.checkpw(password, hash);
        } catch (Exception ex) {

        }
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("matched", matched);

        return map;
    }

    @RequestMapping(value="/password", method= RequestMethod.GET)
    public Map<String, Object> password() {
        RandomStringGenerator generator = new RandomStringGenerator.Builder()
                .withinRange('a', 'z').build();
        String password = generator.generate(20);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("password", password);

        return map;
    }

    @RequestMapping(value="/valid", method= RequestMethod.GET)
    public Map<String, Object> valid(String password) {
        String pattern = "(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#$%^&+=])(?=\\S+$).{8,}";
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("isValid", password.matches(pattern));
        return map;
    }

}
