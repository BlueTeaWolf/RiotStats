package de.blueteawolf.riotstats.summoner;

import de.blueteawolf.riotstats.api.Region;
import de.blueteawolf.riotstats.api.Summoner;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;

/**
 * @author BlueTeaWolf
 */
@Controller
@AllArgsConstructor
public class SummonerController {

    private SummonerRepository repository;
    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/summoner", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute SummonerInput summonerInput) throws IOException {
        System.out.println("User from UI= " + summonerInput);

//        Tests if Summoner is in the database
        if(repository.findBySummonerName(summonerInput.getSummonerName()).isPresent()){
            System.out.println("Works I guess");
            System.out.println(repository.findBySummonerName(summonerInput.getSummonerName()));
        } else {
            Summoner summoner = new Summoner(summonerInput.getSummonerName(), Region.EUW1);
            summoner.getProfile();
            repository.save(summoner);
        }

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("summonerInformation");
        modelAndView.addObject("summoner", summonerInput);
        return modelAndView;
    }

}
