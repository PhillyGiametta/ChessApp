package Backend.ChessApp.Game.Board;

import Backend.ChessApp.Game.ChessGame;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChessBoardRepo extends JpaRepository<ChessBoard, Integer> {
    @Override
    @NotNull
    List<ChessBoard> findAll();

    ChessBoard findById(int id);

}
