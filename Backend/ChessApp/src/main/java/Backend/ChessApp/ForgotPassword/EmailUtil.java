package Backend.ChessApp.ForgotPassword;

import Backend.ChessApp.Users.User;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Locale;

public class EmailUtil {
    public static String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
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
