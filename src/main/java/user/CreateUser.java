package user;

import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class CreateUser {
    private String email;
    private String password;
    private String name;
}
