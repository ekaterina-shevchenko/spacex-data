package de.siemens.services;

import de.siemens.api.SpaceXClient;
import de.siemens.api.data.Failure;
import de.siemens.api.data.Launch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class LaunchesService {

    @Autowired
    SpaceXClient spaceXClient;

    public List<Launch> getAllLaunches() {
        return spaceXClient.getLaunches();
    }

    public List<Launch> filterSuccessfulLaunches(List<Launch> launches) {
        return launches
                .stream()
                .filter(l -> l.getSuccess() != null && l.getSuccess())
                .collect(Collectors.toList());
    }

    public List<Integer> getTimeToFailure(List<Launch> launches) {
        List<Failure> failures = getFailures(launches);
        return failures
                .stream()
                .map(Failure::getTime)
                .filter(t -> t.compareTo(0) >= 0)
                .collect(Collectors.toList());
    }

    public List<Failure> getFailures(List<Launch> launches) {
        return launches
                .stream()
                .flatMap(item -> item.getFailures().stream())
                .collect(Collectors.toList());
    }

    public Map<String, Integer> getFailuresStatistics(List<Launch> launches) {
        List<Failure> failures = getFailures(launches);
        Map<String, Integer> failuresTemp = failures
                .stream()
                .collect(Collectors.toMap(Failure::getReason, f -> 0));
        for (Failure failure: failures) {
            String reason = failure.getReason();
            failuresTemp.put(reason, failuresTemp.get(reason) + 1);
        }
        return failuresTemp;
    }
}
