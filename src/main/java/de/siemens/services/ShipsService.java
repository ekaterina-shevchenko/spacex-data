package de.siemens.services;

import de.siemens.api.SpaceXClient;
import de.siemens.api.data.Port;
import de.siemens.api.data.Ship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ShipsService {

    @Autowired
    SpaceXClient spaceXClient;

    public List<Ship> getAllShips() {
        return spaceXClient.getShips();
    }

    public List<Ship> getActiveShips(List<Ship> ships) {
        return ships
                .stream()
                .filter(Ship::getActive)
                .collect(Collectors.toList());
    }

    public List<Port> getPortsStatistics(List<Ship> ships) {
        List<String> distinctPorts = ships
                .stream()
                .map(Ship::getHomePort)
                .distinct()
                .collect(Collectors.toList());
        Map<String, Integer> ports = new HashMap<>();
        for (String port : distinctPorts) {
            ports.put(port, 0);
        }
        for (Ship ship : ships) {
            String port = ship.getHomePort();
            ports.put(port, ports.get(port) + 1);
        }
        List<Port> homePorts = new ArrayList<>();
        for (String key : ports.keySet()) {
            homePorts.add(new Port(key, ports.get(key)));
        }
        return homePorts;
    }
}
