package Backend.ChessApp.Users;

import Backend.ChessApp.AdminControl.Admin;
import Backend.ChessApp.Game.ChessGame;
import Backend.ChessApp.Group.Group;
import Backend.ChessApp.Settings.SettingsUserStates;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static jakarta.persistence.GenerationType.IDENTITY;

@Entity
@Table(name = "user", schema = "DBChessApp")
public class User {

    //CLASS VARIABLES-------------------------------------------
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "user_id", nullable = false)
    private int id;

    private String userEmail;
    private String userName;
    private String userPassword;
    private Date userMadeDate;
    private Date userLastLoginDate;
    private UserActivity activity;
    private String passwordResetToken;
    private LocalDateTime passwordResetTokenCreationDate;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private SettingsUserStates settings;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Admin admin;

    @ManyToOne
    @JoinColumn(name = "chess_game_id")
    private ChessGame chessGame;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonBackReference
    private Group group;

    //Tracks which team user is on "white" or "black"
    private String team;

    //CONSTRUCTORS----------------------------------------

    public User(){
        //NULL User
    }
    public User(String userName, String userEmail, String userPassword){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userMadeDate = new Date();
        this.userLastLoginDate = new Date();
        this.activity = UserActivity.ONLINE; //Set User Online since they are freshly made
    }

    public User(String userName, String userEmail,String userPassword, int userRanking, int userWins, int userLosses){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userMadeDate = new Date();
        this.userLastLoginDate = new Date();
        this.activity = UserActivity.ONLINE; //needs some way of going offline or idle
    } //Likely not used, may be used in case of copying a user, or for testing.


    //GETTERS AND SETTERS---------------------------------------------------------------
    public int getUserId() {return this.id; }

    public void setUserId(int uId) {this.id = uId;}

    public String getUserName() {return this.userName;}

    public void setUserName(String uName){this.userName = uName;}

    public String getUserEmail(){return this.userEmail;}

    public void setUserEmail(String uEmail){this.userEmail = uEmail;}

    public String getUserPassword() {return this.userPassword;}

    public void setUserPassword(String uPassword){this.userPassword = uPassword;}

    public Date getUserMadeDate(){return this.userMadeDate;}

    public void setUserMadeDate(Date uMadeDate){this.userMadeDate = uMadeDate;}

    public Date getUserLastLoginDate(){return this.userLastLoginDate;}

    public void setUserLastLoginDate(Date uLastLogin) {this.userLastLoginDate = uLastLogin;}

    public UserActivity getActivity(){return activity;}

    public void setActivity(UserActivity uActive){this.activity = uActive;}

    public String getPasswordResetToken(){return passwordResetToken;}

    public void setPasswordResetToken(String token){this.passwordResetToken = token;}

    public LocalDateTime getPasswordResetTokenCreationDate(){return this.passwordResetTokenCreationDate;}

    public void setPasswordResetTokenCreationDate(LocalDateTime tokenCreationDate){this.passwordResetTokenCreationDate = tokenCreationDate;}

    public Group getGroup(){return group;}

    public void setGroup(Group group){this.group = group;}

    public SettingsUserStates getSettings(){return this.settings;}

    public void setSettings(SettingsUserStates settings) {
        this.settings = settings;
    }

    public void setAdmin(Admin admin) {
        this.admin = admin;
    }

    public void setChessGame(ChessGame chessGame) {
        this.chessGame = chessGame;
    }

    public void setTeam(String team) {
        this.team = team;
    }
}
