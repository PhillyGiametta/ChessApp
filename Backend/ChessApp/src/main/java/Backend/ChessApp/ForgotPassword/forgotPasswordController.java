package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import net.bytebuddy.utility.RandomString;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.UUID;


@Controller
@Tag(name = "forgot password", description = "Handles password tokens given HTTP, non-functional")
public class forgotPasswordController {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordHandler passHand;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepo rtr;

    @Autowired
    private UserService userService;

    @Operation(summary = "Returns a reset token from the user")
    @GetMapping("/forgotPassword/{resetToken}")
    User forgotPassword(@PathVariable String resetToken){
        return passHand.getByResetPasswordToken(resetToken);
    }
    @Operation(summary = "creates a passwordToken for a user given the id.")
    @PostMapping("/forgotPassword/{userId}")
    void tokenCreator(@PathVariable int userId){
        String rt = RandomString.make(30);
        User user = userRepository.findById(userId);
        ResetToken resetToken = new ResetToken(user, rt);
        rtr.save(resetToken);

    }




}
