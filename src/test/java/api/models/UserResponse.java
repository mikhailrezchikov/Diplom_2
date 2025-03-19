package api.models;

import lombok.Data;

@Data
public class UserResponse {

    private String accessToken;
    private String refreshToken;
    private User user;
    private boolean success;

    @Data
    public static class User {
        private String email;
        private String name;
    }
}
