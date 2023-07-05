package de.blueteawolf.riotstats.controller;

import de.blueteawolf.riotstats.api.Region;
import de.blueteawolf.riotstats.api.Summoner;
import de.blueteawolf.riotstats.Repos.SummonerRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * @author BlueTeaWolf
 */
@Controller
@AllArgsConstructor
public class SummonerController {

    private SummonerRepository summonerRepo;
    private MatchAnalyzeController matchAnalyzeController;

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/summoner", method = RequestMethod.POST)
    public ModelAndView save(@ModelAttribute SummonerInput summonerInput, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        System.out.println("User from IP= " + request.getLocalAddr());
        //TODO use com.maxmind.geoip2 to get continent of user for Region. Is now automatically EUW1
//        System.out.println("User from UI= " + summonerInput);

        if (summonerRepo.findBySummonerName(summonerInput.getSummonerName()).isPresent()) {
            System.out.println(summonerRepo.findBySummonerName(summonerInput.getSummonerName()));
        } else {
            Summoner summoner = new Summoner(summonerInput.getSummonerName(), Region.EUW1);
            summonerRepo.save(summoner);
        }

        String url = "/summonerName/" + summonerInput.getSummonerName();
        response.setStatus(HttpServletResponse.SC_TEMPORARY_REDIRECT);
        response.setHeader("Location", url);
        return null;
    }

    @RequestMapping("/summonerName/{summonerName}")
    public ModelAndView summonerInformation(@PathVariable String summonerName) {
        String decodedSummonerName = new String(summonerName.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        Optional<Summoner> optionalSummoner = summonerRepo.findBySummonerName(summonerName);
        Summoner summoner = optionalSummoner.orElseThrow(() -> new IllegalArgumentException("Invalid summoner name: " + summonerName));

        matchAnalyzeController.analyzeMatches(summoner);

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("summonerInformation");
        modelAndView.addObject("summoner", summoner);
        return modelAndView;
    }

    @PatchMapping("/updateSummoner")
    public ResponseEntity.BodyBuilder updateSummoner(@RequestParam String summonerName, String server) {

        Optional<Summoner> check = summonerRepo.findBySummonerName(summonerName);

        if(check.isPresent()) {
            if(check.get().lastUpdate()) {
                Summoner summoner = new Summoner(summonerName, Region.valueOf(server));
                summonerRepo.save(summoner);
                return ResponseEntity.status(HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS);
            }
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST);
        }
    }
//    @Async
//    @RequestMapping("/summonerName/{summonerName}")
//    public CompletableFuture<ModelAndView> summonerInformation(@PathVariable String summonerName) {
//        Optional<Summoner> optionalSummoner = summonerRepo.findBySummonerName(summonerName);
//        Summoner summoner = optionalSummoner.orElseThrow(() -> new IllegalArgumentException("Invalid summoner name: " + summonerName));
//
//        CompletableFuture<Boolean> analysisFuture = matchAnalyzeController.analyzeMatches(summoner);
////        CompletableFuture.runAsync(() -> matchAnalyzeController.analyzeMatches(summoner));
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("summonerInformation");
//        modelAndView.addObject("summoner", summoner);
//
//        // Wait for the analysis to complete
////        analysisFuture.join();
//
//        // Pass a flag to the view indicating that the analysis is complete
//        modelAndView.addObject("analysisComplete", true);
//        return CompletableFuture.completedFuture(modelAndView);
////        CompletableFuture.runAsync(() -> matchAnalyzeController.analyzeMatches(summoner));
////
////        ModelAndView modelAndView = new ModelAndView();
////        modelAndView.setViewName("summonerInformation");
////        modelAndView.addObject("summoner", summoner);
////        return CompletableFuture.completedFuture(modelAndView);
//    }
}
