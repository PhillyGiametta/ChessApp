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
//    public void updateResetPasswordToken(String token, String userEmail)  {
//        User user = userRepo.findByUserEmail(userEmail);
//        if (user != null) {
//            user.setResetPasswordToken(token);
//            userRepo.save(user);
//        } else {
//            throw new CustomerNotFoundException("Could not find any user with the email " + email);
//        }
//    }
//
//    public Customer getByResetPasswordToken(String token) {
//        return userRepo.findByResetPasswordToken(token);
//    }
//
//    public void updatePassword(User user, String newPassword) {
//        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//        String encodedPassword = passwordEncoder.encode(newPassword);
//        user.setPassword(encodedPassword);
//
//        user.setResetPasswordToken(null);
//        userRepo.save(user);
//    }

}
