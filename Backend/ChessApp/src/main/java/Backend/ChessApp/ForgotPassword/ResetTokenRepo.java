package Backend.ChessApp.ForgotPassword;
import Backend.ChessApp.Users.*;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.stream.Stream;

public interface ResetTokenRepo extends JpaRepository<ResetToken, Long> {
    ResetToken findByToken(String token);

    ResetToken findByUser(User user);

    Stream<ResetToken> findAllByExpiryDateLessThan(Date now);

    void deleteByExpiryDateLessThan(Date now);

    @Modifying
    @Query("delete from ResetToken t where t.expiryDate <= ?1")
    void deleteAllExpiredSince(Date now);
}
