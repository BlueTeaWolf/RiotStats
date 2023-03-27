package de.blueteawolf.riotstats.summoner;

import de.blueteawolf.riotstats.api.Region;
import de.blueteawolf.riotstats.api.Summoner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

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
    public ModelAndView save(@ModelAttribute SummonerInput summonerInput, HttpServletResponse response, HttpServletRequest request) throws IOException {
        System.out.println("User from IP= " + request.getLocalName());
        //TODO use com.maxmind.geoip2 to get continent of user for Region. Is now automatically EUW1
        System.out.println("User from UI= " + summonerInput);
        // Check if Summoner is in the database
        if (repository.findBySummonerName(summonerInput.getSummonerName()).isPresent()) {
            System.out.println("Summoner in database");
            System.out.println(repository.findBySummonerName(summonerInput.getSummonerName()));
        } else {
            Summoner summoner = new Summoner(summonerInput.getSummonerName(), Region.EUW1);
            summoner.getProfile();
            repository.save(summoner);
        }

        // Redirect to the second site with the desired URL structure
        String url = "/summonerName/" + summonerInput.getSummonerName();
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", url);

        return null;
    }

    @RequestMapping("/summonerName/{summonerName}")
    public ModelAndView summonerInformation(@PathVariable String summonerName) {
        Optional<Summoner> optionalSummoner = repository.findBySummonerName(summonerName);
        Summoner summoner = optionalSummoner.orElseThrow(() -> new IllegalArgumentException("Invalid summoner name: " + summonerName));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("summonerInformation");
        modelAndView.addObject("summoner", summoner);
        return modelAndView;
    }

}
