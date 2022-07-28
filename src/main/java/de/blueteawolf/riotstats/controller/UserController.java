package de.blueteawolf.riotstats.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author BlueTeaWolf
 */
@RestController
public class UserController {

    @GetMapping(value = "/api/user")
    public String user(){
        return "welcome";
    }

}
