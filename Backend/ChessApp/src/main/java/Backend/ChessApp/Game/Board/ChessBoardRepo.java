package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.ChessGame;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChessBoardRepo extends JpaRepository<Board, Integer> {
    @Override
    @NotNull
    List<Board> findAll();

    Board findById(int id);

}
