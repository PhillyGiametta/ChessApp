package Backend.ChessApp.Game;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ChessGameRepository extends JpaRepository<ChessGame, Integer> {
    ChessGame findById(int id);
}
