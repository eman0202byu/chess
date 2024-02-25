package model;

public record AuthData(String authToken, String username) {
    public AuthData changeAuthToken(String newAuthToken) {
        return new AuthData(newAuthToken, username);
    }

    public AuthData changeUsername(String newUsername) {
        return new AuthData(authToken, newUsername);
    }
}
