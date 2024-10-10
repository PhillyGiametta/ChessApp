package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class ResetToken {
    private static final int EXPIRATION = 30; //minutes

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int userId;

    private String token;

    @OneToOne(targetEntity=User.class, fetch =FetchType.EAGER)
    @JoinColumn(nullable = false, name = "userId")
    private User user;

    private Date expiryDate;

    public ResetToken(User user, String token){

    }


    public void createPasswordResetTokenForUser(User user, String token){
        ResetToken myToken = new ResetToken(user, token);

    }
}
