package de.siemens.api;

import de.siemens.api.data.Launch;
import de.siemens.api.data.Rocket;
import de.siemens.api.data.Ship;
import de.siemens.config.SpaceXClientConfiguration;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "spacex", url = "https://api.spacexdata.com/v4",
        configuration = SpaceXClientConfiguration.class)
public interface SpaceXClient {

    @RequestMapping(method = RequestMethod.GET, value = "/rockets")
    public List<Rocket> getRockets();

    @RequestMapping(method = RequestMethod.GET, value = "/ships")
    public List<Ship> getShips();

    @RequestMapping(method = RequestMethod.GET, value = "/launches")
    public List<Launch> getLaunches();
}
