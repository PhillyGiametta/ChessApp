package Backend.ChessApp.Users;

import Backend.ChessApp.Leaderboard.LeaderboardEntry;
import Backend.ChessApp.Leaderboard.LeaderboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Date;

@RestController
@RequestMapping("/profile")
public class UserProfileController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LeaderboardRepository leaderboardRepository;

    @PostMapping("/{userName}/uploadProfilePic")
    public ResponseEntity<?> uploadProfilePic(@PathVariable String userName, @RequestParam("file") MultipartFile file) {
        try{
            User user = userRepository.findByUserName(userName);

            user.setProfilePicture(file.getBytes());
            userRepository.save(user);

            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{userName}/profilePic")
    public ResponseEntity<?> getProfilePic(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);

        byte[] pic = user.getProfilePicture();
        if(pic == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok().header(HttpHeaders.CONTENT_TYPE, "image/jpeg").body(pic);
    }

    @GetMapping("/{userName}/getUserName")
    public String getUserName(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);
        return user.getUserName();
    }

    @GetMapping("/{userName}/getRating")
    public int getRating(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);
        LeaderboardEntry le = leaderboardRepository.findByUserId(user.getUserId());
        return le.getRating();
    }

    @GetMapping("/{userName}/getCreationDate")
    public Date getCreationDate(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);
        return user.getUserMadeDate();
    }

    @GetMapping("/{userName}/getLastOnline")
    public Date getLastOnline(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);
        return user.getUserLastLoginDate();
    }

    @GetMapping("/{userName}/Status")
    public UserActivity getStatus(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);
        return user.getActivity();
    }
}
