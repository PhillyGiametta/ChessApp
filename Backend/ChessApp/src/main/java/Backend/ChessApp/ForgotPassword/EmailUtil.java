package Backend.ChessApp.ForgotPassword;

import Backend.ChessApp.Users.User;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;

public class EmailUtil {
    public static class GenericResponse{
        private String message;
        private boolean error;
        public GenericResponse(){
        }
        public GenericResponse(String message, boolean error){
            super();
            this.message = message;
            this.error = error;
        }
    }
    public static class MailSender{
        private String subject;
        private String body;
        private User supportUser;

        private MailSender constructResetTokenEmail(String path, Locale locale, User user, String token){
            String url = path + "/user/changePassword?token=" + token;
            String message = "idk";
            return constructEmail("Reset Password", message + " \r\n" + url, user);
        }
        private MailSender constructEmail(String subject, String body, User user)
        {
            MailSender email = new MailSender();
            email.setSubject(subject);
            email.setText(body);
            email.setTo(user.getUserEmail());
            email.setFrom(env.getProperty("support.email"));
            return email;
        }
        private void setSubject(String subject) {this.subject = subject;}
        private void setText(String body) {this.body = body;}
        private void setTo(String userEmail) {this.supportUser.setUserEmail(userEmail);}
        private void setFrom(String )
    }
    @Bean
    public JavaMailSender getJavaMailSender(){

    }

}
