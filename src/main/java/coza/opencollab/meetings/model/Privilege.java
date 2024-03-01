package coza.opencollab.meetings.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Privilege implements Serializable {


    private Role role;
    private Function function;
}
