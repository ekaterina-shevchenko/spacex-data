package de.siemens.controllers;

import de.siemens.api.data.Rocket;
import de.siemens.services.RocketsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class RocketsController {

    @Autowired
    RocketsService rocketsService;

    @GetMapping("/rockets")
    public ModelAndView getRockets(HttpServletRequest request) {
        log.info("Received request from IP address {} to the url {}", request.getRemoteAddr(), "/rockets");
        ModelAndView mv = new ModelAndView("rockets");
        List<Rocket> rockets = rocketsService.getAllRockets();
        mv.addObject("total", rockets.size());
        mv.addObject("active", rocketsService.getActiveRockets(rockets).size());
        List<Integer> launchCosts = rocketsService.getLaunchCosts(rockets);
        mv.addObject("cost",
                launchCosts.stream().reduce(0, Integer::sum) / launchCosts.size());
        List<Integer> successRates = rocketsService.getSuccessRates(rockets);
        mv.addObject("rate",
                successRates.stream().reduce(0, Integer::sum) / successRates.size());
        log.info("Returning the view to the user with IP address {}", request.getRemoteAddr());
        return mv;
    }
}
