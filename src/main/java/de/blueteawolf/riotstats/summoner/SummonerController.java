package de.blueteawolf.riotstats.summoner;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author BlueTeaWolf
 */
@Controller
public class SummonerController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/summoner", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute SummonerInput summonerInput) {
        System.out.println("User from UI= " + summonerInput);

        //Write code to save in database
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("summonerInformation");
        modelAndView.addObject("summoner", summonerInput);
        return modelAndView;
    }

}
