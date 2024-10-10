package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;
import jakarta.servlet.http.HttpServletRequest;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.UUID;

@RestController
public class forgotPasswordController { //TODO

    @Autowired
    UserRepository userRepo;
    @Autowired
    ResetTokenRepo resetTokenRepo;
//    @Autowired
//    JpaRepository jpaRepository;
   // @Autowired
  //  private JavaMailSender mailSender;
 //   @Autowired
   // private Environment environment;
  //  private MessageSource messages;


    @PostMapping(path = "/users/resetPassword")
    public String resetPassword(@RequestParam("userEmail") String userEmail){
        User user = userRepo.findByUserEmail(userEmail);
        if(user == null)
            return "User does not exist";
        //ResetToken token = new ResetToken(user, )
        return "not available";
    }






//    @PostMapping("/users/resetPassword")
//    public EmailUtil.GenericResponse resetPassword(HttpServletRequest request,
//                                                   @RequestParam("userEmail") String userEmail) {
//        User user = userRepo.findByUserEmail(userEmail);
//        if(user == null)
//            return new EmailUtil.GenericResponse("User does not exist", true);
//        String token = UUID.randomUUID().toString();
//        userRepo.createPasswordResetTokenForUser(user, token);
//        mailSender.send(constructResetTokenEmail(getAppUrl(request),
//                request.getLocale(), token, user));
//        return new EmailUtil.GenericResponse("PasswordToken created", false);
//
//    }
//    @PostMapping("/users/savePassword")
//    public EmailUtil.GenericResponse savePassword(final Locale locale,PasswordHandler.PasswordDto passwordDto){
//        String result = securityUserService.validatePasswordResetToken(passwordDto.getToken());
//
//        if(result != null){
//            return new EmailUtil.GenericResponse("auth.message: " + result, true);
//        }
//
//    }
//
//    private SimpleMailMessage constructResendVerificationTokenEmail(final String contextPath, final Locale locale, final ResetToken newToken, final User user) {
//        final String confirmationUrl = contextPath + "/registrationConfirm.html?token=" + newToken.getToken();
//        final String message = messages.getMessage("message.resendToken", null, locale);
//        return constructEmail("Resend Registration Token", message + " \r\n" + confirmationUrl, user);
//    }
//
//    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final Locale locale, final String token, final User user) {
//        final String url = contextPath + "/user/changePassword?token=" + token;
//        final String message = messages.getMessage("message.resetPassword", null, locale);
//        return constructEmail("Reset Password", message + " \r\n" + url, user);
//    }
//
//    private SimpleMailMessage constructEmail(String subject, String body, User user) {
//        final SimpleMailMessage email = new SimpleMailMessage();
//        email.setSubject(subject);
//        email.setText(body);
//        email.setTo(user.getUserEmail());
//        email.setFrom(environment.getProperty("support.email"));
//        return email;
//    }
//
//    private String getAppUrl(HttpServletRequest request) {
//        return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
//    }
//
//    private String getClientIP(HttpServletRequest request) {
//        final String xfHeader = request.getHeader("X-Forwarded-For");
//        if (xfHeader == null || xfHeader.isEmpty() || !xfHeader.contains(request.getRemoteAddr())) {
//            return request.getRemoteAddr();
//        }
//        return xfHeader.split(",")[0];
//    }





}
