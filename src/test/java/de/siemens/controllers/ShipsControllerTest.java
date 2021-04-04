package de.siemens.controllers;

import de.siemens.api.data.Port;
import de.siemens.api.data.Ship;
import de.siemens.services.ShipsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ShipsControllerTest {
    @Autowired
    ShipsController shipsController;
    @MockBean
    HttpServletRequest request;
    @MockBean
    ShipsService shipsService;

    List<Ship> ships;
    List<Port> ports;

    @BeforeEach
    public void initializeMocks() {
        Ship ship1 = new Ship("1", "Victory", true, "battle", 1610, "Barcelona");
        Ship ship2 = new Ship("2", "Bismarck", false, "battle", 1959, "Hamburg");
        Ship ship3 = new Ship("3", "Arizona", true, "battle", 1980, "Hamburg");
        ships = Arrays.asList(ship1, ship2, ship3);
        when(shipsService.getAllShips()).thenReturn(ships);
        when(shipsService.getActiveShips(any())).thenReturn(Arrays.asList(ship1, ship3));

        Port port1 = new Port("Hamburg", 2);
        Port port2 = new Port("Barcelona", 1);
        ports = Arrays.asList(port1, port2);
        when(shipsService.getPortsStatistics(any())).thenReturn(ports);

        when(request.getRemoteAddr()).thenReturn("localhost");
    }

    @Test
    public void testGetShips() {
        ModelAndView expectedMV = getExpectedModelAndView();
        ModelAndView actualMV = shipsController.getShips(request);
        // ModelAndView does not override equals()/hashcode(), thus we compare toString() results
        assertEquals(expectedMV.toString(), actualMV.toString());
        verify(shipsService, times(1)).getAllShips();
        verify(shipsService, times(1)).getActiveShips(any());
        verify(shipsService, times(1)).getPortsStatistics(any());
    }

    private ModelAndView getExpectedModelAndView() {
        ModelAndView mv = new ModelAndView("ships");
        mv.addObject("total", ships.size());
        mv.addObject("active", (int) ships.stream().filter(Ship::getActive).count());
        mv.addObject("ports", ports);
        return mv;
    }
}
