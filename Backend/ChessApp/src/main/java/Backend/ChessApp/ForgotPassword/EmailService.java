package Backend.ChessApp.ForgotPassword;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import static org.hibernate.sql.ast.SqlTreeCreationLogger.LOGGER;

@Component
public class EmailService {    private final static String EMAIL_CONFIRMATION_SUBJECT = "Confirm your udeesa account";

    @Autowired
    private JavaMailSender javaMailSender;

    public void sendConfirmationEmail(String token, String email) {
        // build email
        // send message
        String message = "Welcome to Udeesa, test token" + token;
        String from = "no-reply@udeesa.org";
        send(email, from, message);
    }

    @Async
    private void send(String to, String from, String email) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(EMAIL_CONFIRMATION_SUBJECT);
            helper.setText(email);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new IllegalStateException("failed to send email");
        }
    }
}