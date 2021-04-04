package de.siemens.controllers;

import de.siemens.api.data.Failure;
import de.siemens.api.data.Launch;
import de.siemens.services.LaunchesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.servlet.ModelAndView;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class LaunchesControllerTest {
    @Autowired
    LaunchesController launchesController;
    @MockBean
    LaunchesService launchesService;
    @MockBean
    HttpServletRequest request;

    List<Launch> launches;
    List<Failure> failures;
    Map<String, Integer> failuresStatistics;

    @BeforeEach
    public void initializeMocks() {
        Failure failure1 = new Failure(10000, "We got out of coffee");
        Failure failure2 = new Failure(100, "Something went wrong");
        failures = Arrays.asList(failure1, failure2);
        Launch launch1 = new Launch("1", "Flight to the Moon", true,
                Collections.singletonList(failure1));
        Launch launch2 = new Launch("2", "Trip to Mars", false,
                Collections.singletonList(failure2));
        Launch launch3 = new Launch("3", "Round trip around the office", true,
                Collections.singletonList(null));
        launches = Arrays.asList(launch1, launch2, launch3);
        when(launchesService.getAllLaunches()).thenReturn(launches);
        when(launchesService.getFailures(launches)).thenReturn(failures);
        when(launchesService.filterSuccessfulLaunches(launches)).thenReturn(Arrays.asList(launch1, launch3));
        when(launchesService.getTimeToFailure(launches))
                .thenReturn(Arrays.asList(failure1.getTime(), failure2.getTime()));
        failuresStatistics = new HashMap<>();
        failuresStatistics.put(failure1.getReason(), 1);
        failuresStatistics.put(failure2.getReason(), 1);
        when(launchesService.getFailuresStatistics(launches)).thenReturn(failuresStatistics);
        when(request.getRemoteAddr()).thenReturn("localhost");
    }

    @Test
    public void testGetLaunches() {
        ModelAndView expectedMV = getExpectedModelAndView();
        ModelAndView actualMV = launchesController.getLaunches(request);
        // ModelAndView does not override equals()/hashcode(), thus we compare toString() results
        assertEquals(expectedMV.toString(), actualMV.toString());
        verify(launchesService, times(1)).getAllLaunches();
        verify(launchesService, times(1)).getTimeToFailure(any());
        verify(launchesService, times(1)).filterSuccessfulLaunches(any());
        verify(launchesService, times(1)).getFailuresStatistics(any());
    }

    private ModelAndView getExpectedModelAndView() {
        ModelAndView mv = new ModelAndView("launches");
        mv.addObject("total", launches.size());
        mv.addObject("successful", launches.stream().filter(Launch::getSuccess).count());
        Integer overallTimeToFailure = failures.stream().map(Failure::getTime).reduce(0, Integer::sum);
        mv.addObject("time",
                overallTimeToFailure / failures.size());
        Map<String, Map<String, Integer>> failuresView = new HashMap<>();
        failuresView.put("failures", failuresStatistics);
        mv.addAllObjects(failuresView);
        return mv;
    }
}
