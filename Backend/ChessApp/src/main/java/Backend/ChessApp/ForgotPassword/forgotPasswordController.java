package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.UUID;

@RestController
public class forgotPasswordController {

    @Autowired
    UserRepository userRepo;
    @Autowired
    ResetTokenRepo resetTokenRepo;
    @Autowired
    JpaRepository jpaRepository;

    @PostMapping("/users/resetPassword")
    public EmailUtil.GenericResponse resetPassword(HttpServletRequest request,
                                                   @RequestParam("userEmail") String userEmail) {
        User user = userRepo.findByUserEmail(userEmail);
        if(user == null)
            return new EmailUtil.GenericResponse("User does not exist", true);
        String token = UUID.randomUUID().toString();
        userRepo.createPasswordResetTokenForUser(user, token);
        EmailUtil.MailSender.send(constructResetTokenEmail(getAppUrl(request),
                request.getLocale(), user, token));
    }
    @PostMapping("/users/savePassword")
    public EmailUtil.GenericResponse savePassword(final Locale locale, @Valid PasswordHandler.PasswordDto passwordDto){
        String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());

        if(result != null){
            reutnr new EmailUtil.GenericResponse("auth.message" + result, )
        }
    }





}
