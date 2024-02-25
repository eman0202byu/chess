package model;

public record UserData(String username, String password, String email) {
    public UserData changeUsername(String newUsername) {
        return new UserData(newUsername, password, email);
    }

    public UserData changePassword(String newPassword) {
        return new UserData(username, newPassword, email);
    }

    public UserData changeEmail(String newEmail) {
        return new UserData(username, password, newEmail);
    }
}
