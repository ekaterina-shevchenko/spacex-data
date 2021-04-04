package de.siemens.api.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Launch {
    private String id;
    private String name;
    private Boolean success;
    private List<Failure> failures;
}
