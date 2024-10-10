package Backend.ChessApp.ForgotPassword;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import org.springframework.validation.annotation.Validated;

public class PasswordHandler {

    UserRepository userRepo;
    public void changeUserPassword(User user, String userPassword){
        //user.setUserPassword(passwordEncoder.encode(userPassword)); Add encoding for users in the future
        user.setUserPassword((userPassword));
        userRepo.save(user);
    }

    public static class PasswordDto{
        private String oldPassword;
        private String token;
        private String newPassword;
    }

}
