package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;

import jakarta.persistence.*;
import net.bytebuddy.utility.RandomString;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

@Entity
public class ResetToken {
    private static final int EXPIRATION = 30; //minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int user_id;

    private String token;

    @OneToOne(targetEntity=User.class, fetch =FetchType.EAGER)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    private Date expiryDate;

    public ResetToken(User user, String token){
        this.user = user;
        this.token = token; this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    @Contract("_ -> new")
    private @NotNull Date calculateExpiryDate(final int expiryTimeInMinutes) {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(new Date().getTime());
        cal.add(Calendar.MINUTE, expiryTimeInMinutes);
        return new Date(cal.getTime().getTime());
    }
    public void updateToken(final String token) {
        this.token = token;
        this.expiryDate = calculateExpiryDate(EXPIRATION);
    }

    public String createToken(){
          return RandomString.make(6);
    }



    public int getId() {
        return user_id;
    }
    public String getToken() {
        return token;
    }
    public void setToken(final String token) {
        this.token = token;
    }
    public User getUser() {
        return user;
    }
    public void setUser(final User user) {
        this.user = user;
    }
    public Date getExpiryDate() {
        return expiryDate;
    }
    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }
}
