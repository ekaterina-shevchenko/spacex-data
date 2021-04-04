package de.siemens.api.data;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Rocket {
    private String id;
    private String name;
    private Boolean active;
    @JsonProperty("cost_per_launch")
    private Integer launchCost;
    @JsonProperty("success_rate_pct")
    private Integer successRate;
    @JsonProperty("first_flight")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date firstFlight;
}
