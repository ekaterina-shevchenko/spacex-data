package de.siemens.api.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Ship {
    private String id;
    private String name;
    private Boolean active;
    private String type;
    @JsonProperty("year_built")
    private Integer yearBuilt;
    @JsonProperty("home_port")
    private String homePort;
}
