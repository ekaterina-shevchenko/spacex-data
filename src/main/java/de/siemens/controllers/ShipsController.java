package de.siemens.controllers;

import de.siemens.api.data.Ship;
import de.siemens.services.ShipsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@Controller
public class ShipsController {

    @Autowired
    ShipsService shipsService;

    @GetMapping("/ships")
    public ModelAndView getShips(HttpServletRequest request) {
        log.info("Received request from IP address {} to the url {}", request.getRemoteAddr(), "/ships");
        ModelAndView mv = new ModelAndView("ships");
        List<Ship> ships = shipsService.getAllShips();
        mv.addObject("total", ships.size());
        mv.addObject("active", shipsService.getActiveShips(ships).size());
        mv.addObject("ports", shipsService.getPortsStatistics(ships));
        log.info("Returning the view to the user with IP address {}", request.getRemoteAddr());
        return mv;
    }
}
