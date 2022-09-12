package project.www.database.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDetails {
    private UserCredentials userCredentials;
    private String email;
}
