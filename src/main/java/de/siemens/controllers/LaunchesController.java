package de.siemens.controllers;

import de.siemens.api.data.Launch;
import de.siemens.services.LaunchesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Controller
public class LaunchesController {

    @Autowired
    LaunchesService launchesService;

    @GetMapping("/launches")
    public ModelAndView getLaunches(HttpServletRequest request) {
        log.info("Received request from IP address {} to the url {}", request.getRemoteAddr(), "/launches");
        ModelAndView mv = new ModelAndView("launches");
        List<Launch> launches = launchesService.getAllLaunches();
        mv.addObject("total", launches.size());
        mv.addObject("successful", launchesService.filterSuccessfulLaunches(launches).size());
        List<Integer> timeToFailure = launchesService.getTimeToFailure(launches);
        mv.addObject("time",
                timeToFailure.stream().reduce(0, Integer::sum) / timeToFailure.size());
        Map<String, Map<String, Integer>> failuresView = new HashMap<>();
        failuresView.put("failures", launchesService.getFailuresStatistics(launches));
        mv.addAllObjects(failuresView);
        log.info("Returning the view to the user with IP address {}", request.getRemoteAddr());
        return mv;
    }
}
