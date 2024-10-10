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

//    @Bean
//    public JavaMailSender getJavaMailSender(){
//
//    }

}
