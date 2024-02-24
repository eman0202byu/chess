package model;

public record AuthData(String authToken, String username) {
    AuthData changeAuthToken(String newAuthToken) {
        return new AuthData(newAuthToken, username);
    }

    AuthData changeUsername(String newUsername) {
        return new AuthData(authToken, newUsername);
    }
}
