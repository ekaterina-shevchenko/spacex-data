package de.siemens.controllers;

import de.siemens.api.data.Rocket;
import de.siemens.services.RocketsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import static org.mockito.Mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class RocketsControllerTest {
    @Autowired
    RocketsController rocketsController;
    @MockBean
    RocketsService rocketsService;
    @MockBean
    HttpServletRequest request;

    List<Rocket> rockets;

    @BeforeEach
    public void initializeMocks() {
        Rocket rocket1 = new Rocket("1", "Rock", true, 1500000, 100, new Date());
        Rocket rocket2 = new Rocket("2", "Firefly", false, 1000000, 0, new Date());
        Rocket rocket3 = new Rocket("3", "Vibe", true, 2000000, 50, new Date());
        rockets = Arrays.asList(rocket1, rocket2, rocket3);
        when(rocketsService.getAllRockets()).thenReturn(rockets);
        when(rocketsService.getActiveRockets(rockets)).thenReturn(Arrays.asList(rocket1, rocket3));
        when(rocketsService.getLaunchCosts(rockets))
                .thenReturn(rockets.stream().map(Rocket::getLaunchCost).collect(Collectors.toList()));
        when(rocketsService.getSuccessRates(rockets))
                .thenReturn(rockets.stream().map(Rocket::getSuccessRate).collect(Collectors.toList()));
        when(request.getRemoteAddr()).thenReturn("localhost");
    }

    @Test
    public void testGetRockets() {
        ModelAndView expectedMV = getExpectedModelAndView();
        ModelAndView actualMV = rocketsController.getRockets(request);
        // ModelAndView does not override equals()/hashcode(), thus we compare toString() results
        assertEquals(expectedMV.toString(), actualMV.toString());
        verify(rocketsService, times(1)).getAllRockets();
        verify(rocketsService, times(1)).getActiveRockets(any());
        verify(rocketsService, times(1)).getLaunchCosts(any());
        verify(rocketsService, times(1)).getSuccessRates(any());
    }

    private ModelAndView getExpectedModelAndView() {
        ModelAndView mv = new ModelAndView("rockets");
        mv.addObject("total", rockets.size());
        mv.addObject("active", rockets.stream().filter(Rocket::getActive).count());
        Integer overallLaunchCost = rockets.stream().map(Rocket::getLaunchCost).reduce(0, Integer::sum);
        mv.addObject("cost", overallLaunchCost / rockets.size());
        Integer overallSuccessRate = rockets.stream().map(Rocket::getSuccessRate).reduce(0, Integer::sum);
        mv.addObject("rate", overallSuccessRate / rockets.size());
        return mv;
    }
}
