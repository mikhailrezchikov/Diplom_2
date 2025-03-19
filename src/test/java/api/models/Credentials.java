package api.models;

import lombok.Data;

@Data
public class Credentials {

    private final String email;
    private final String password;

    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public static Credentials fromUser(User user) {
        return new Credentials(user.getEmail(), user.getPassword());
    }
}
