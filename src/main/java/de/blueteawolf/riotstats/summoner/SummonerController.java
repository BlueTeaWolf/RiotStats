package de.blueteawolf.riotstats.summoner;

import de.blueteawolf.riotstats.api.Match;
import de.blueteawolf.riotstats.api.MatchAnalyzer;
import de.blueteawolf.riotstats.api.Region;
import de.blueteawolf.riotstats.api.Summoner;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.json.JSONException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Optional;

/**
 * @author BlueTeaWolf
 */
@Controller
@AllArgsConstructor
public class SummonerController {

    private SummonerRepository summonerRepo;
    private MatchRepository matchRepository;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/summoner", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute SummonerInput summonerInput, HttpServletResponse response, HttpServletRequest request) {
        System.out.println("User from IP= " + request.getLocalAddr());
        //TODO use com.maxmind.geoip2 to get continent of user for Region. Is now automatically EUW1
        System.out.println("User from UI= " + summonerInput);
        // Check if Summoner is in the database
        if (summonerRepo.findBySummonerName(summonerInput.getSummonerName()).isPresent()) {
            System.out.println("Summoner in database");
            System.out.println(summonerRepo.findBySummonerName(summonerInput.getSummonerName()));
        } else {
                Summoner summoner = new Summoner(summonerInput.getSummonerName(), Region.EUW1);
                summoner.getProfile();
                summonerRepo.save(summoner);
        }

        // Redirect to the second site with the desired URL structure
        String url = "/summonerName/" + summonerInput.getSummonerName();
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", url);
        return null;
    }

    @RequestMapping("/summonerName/{summonerName}")
    public ModelAndView summonerInformation(@PathVariable String summonerName) throws IOException {
        Optional<Summoner> optionalSummoner = summonerRepo.findBySummonerName(summonerName);
        Summoner summoner = optionalSummoner.orElseThrow(() -> new IllegalArgumentException("Invalid summoner name: " + summonerName));
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("summonerInformation");
        modelAndView.addObject("summoner", summoner);

        try {
            Match match = new Match(Region.EUROPE, summoner.getPuuID());
            match.getMatches(10);
            MatchAnalyzer matchAnalyzer = new MatchAnalyzer();
            matchAnalyzer.analyzeMatch(match.getMatchDetails(0), summoner.getPuuID());
            matchRepository.save(matchAnalyzer);

//            not needed. Can be uncomment to save performance
            for (String puuID : matchAnalyzer.getPlayersByPuuID().keySet()) {
                if (!(puuID.equals(summoner.getPuuID()))) {
                    MatchAnalyzer matchAnalysis = new MatchAnalyzer();
                    matchAnalysis.analyzeMatch(match.getMatchDetails(0), puuID);
                    matchRepository.save(matchAnalysis);
                }
            }


        } catch (JSONException jsonException) {
            System.out.println("Not more than 10 matches");
        }

        return modelAndView;
    }
}
