package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.UUID;


@Controller
public class forgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordHandler passHand;

    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "";
    }

    @PostMapping("/forgot_password")
    public String processForgotPassword() {
        return "";
    }

    public void sendEmail(){

    }


    @GetMapping("/reset_password")
    public String showResetPasswordForm() {
        return "";
    }

    @PostMapping("/reset_password")
    public String processResetPassword() {
        return "";
    }
}
