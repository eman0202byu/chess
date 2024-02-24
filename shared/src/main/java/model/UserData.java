package model;

public record UserData(String username, String password, String email) {
    UserData changeUsername(String newUsername) {
        return new UserData(newUsername, password, email);
    }

    UserData changePassword(String newPassword) {
        return new UserData(username, newPassword, email);
    }

    UserData changeEmail(String newEmail) {
        return new UserData(username, password, newEmail);
    }
}
