package de.siemens.services;

import de.siemens.api.SpaceXClient;
import de.siemens.api.data.Rocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class RocketsService {

    @Autowired
    SpaceXClient spaceXClient;

    public List<Rocket> getAllRockets() {
        return spaceXClient.getRockets();
    }

    public List<Rocket> getActiveRockets(List<Rocket> rockets) {
        return rockets
                .stream()
                .filter(Rocket::getActive)
                .collect(Collectors.toList());
    }

    public List<Integer> getLaunchCosts(List<Rocket> rockets) {
        return rockets
                .stream()
                .map(Rocket::getLaunchCost)
                .collect(Collectors.toList());
    }

    public List<Integer> getSuccessRates(List<Rocket> rockets) {
        return rockets
                .stream()
                .map(Rocket::getSuccessRate)
                .collect(Collectors.toList());
    }
}
