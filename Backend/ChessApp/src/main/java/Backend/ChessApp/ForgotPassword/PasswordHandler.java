package Backend.ChessApp.ForgotPassword;

import Backend.ChessApp.Users.User;
import Backend.ChessApp.Users.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Transactional
public class PasswordHandler {

    UserRepository userRepo;
    public void changeUserPassword(User user, String userPassword){
        //user.setUserPassword(passwordEncoder.encode(userPassword)); Add encoding for users in the future
        user.setUserPassword((userPassword));
        userRepo.save(user);
    }
    public void updateResetPasswordToken(String token, String userEmail)  {
        User user = userRepo.findByUserEmail(userEmail);
        if (user == null) {
            return;
        }
        user.setPasswordResetToken(token);
        userRepo.save(user);
    }

    public User getByResetPasswordToken(String token) {
        return userRepo.findByPasswordResetToken(token);
    }

    public void updatePassword(User user, String newPassword) {
        user.setUserPassword(newPassword);
        user.setPasswordResetToken(null);
        userRepo.save(user);
    }

}
