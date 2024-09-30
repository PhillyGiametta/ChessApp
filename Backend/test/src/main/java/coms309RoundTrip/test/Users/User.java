package coms309RoundTrip.test.Users;

import jakarta.persistence.*;

import java.util.Date;

import static jakarta.persistence.GenerationType.IDENTITY;


@Entity
@Table(name = "user")
public class User {

    //CLASS VARIABLES-------------------------------------------
    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id")
    private int id;
    private String userEmail;
    private String userName;
    private Date userMadeDate;
    private Date userLastLoginDate;
    private int userRanking;
    private int userWins;
    private int userLosses;
    private double userWLRatio;
    private UserActivity activity;

    //CONSTRUCTORS----------------------------------------
    public User(String userName, String userEmail){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRanking=0; userWins=0; userLosses=0; userWLRatio = 0;
        this.userMadeDate = new Date();
        this.userLastLoginDate = new Date();
        this.activity = UserActivity.ONLINE; //Set User Online since they are freshly made
    }

    public User(String userName, String userEmail, int userRanking, int userWins, int userLosses){
        this.userName = userName;
        this.userEmail = userEmail;
        this.userRanking = userRanking;
        this.userWins = userWins;
        this.userLosses = userLosses;
        this.userMadeDate = new Date();
        this.userLastLoginDate = new Date();
        this.userWLRatio = userWins / (double)userLosses;
        this.activity = UserActivity.ONLINE; //needs some way of going offline or idle
    } //Likely not used, may be used in case of copying a user, or for testing.


    //GETTERS AND SETTERS---------------------------------------------------------------
    public int getUserId() {return this.id; }
    public void setUserId(int uId) {this.id = uId;}
    public String getUserName() {return this.userName;}
    public void setUserName(String uName){this.userName = uName;}
    public String getUserEmail(){return this.userEmail;}
    public void setUserEmail(String uEmail){this.userEmail = uEmail;}
    public Date getUserMadeDate(){return this.userMadeDate;}
    public void setUserMadeDate(Date uMadeDate){this.userMadeDate = uMadeDate;}
    public Date getUserLastLoginDate(){return this.userLastLoginDate;}
    public void setUserLastLoginDate(Date uLastLogin) {this.userLastLoginDate = uLastLogin;}
    public int getUserRanking(){return this.userRanking;}
    public void setUserRanking(int userRanking) {
        this.userRanking = userRanking;
    }
    public int getUserWins(){return this.userWins;}
    public void setUserWins(int uWins){this.userWins = uWins;}
    public int getUserLosses(){return this.userLosses;}
    public void setUserLosses(int uLoss){this.userLosses = uLoss;}
    public double getUserWLRatio(){return this.userWLRatio;}
    public void setUserWLRatio(double uWLRatio){this.userWLRatio = uWLRatio;}
    public UserActivity getActivity(){return activity;}
    public void setActivity(UserActivity uActive){this.activity = uActive;}
}
