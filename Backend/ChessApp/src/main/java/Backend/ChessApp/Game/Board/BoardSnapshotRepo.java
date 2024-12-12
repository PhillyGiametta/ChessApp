package Backend.ChessApp.Game.Board;

import jakarta.transaction.Transactional;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardSnapshotRepo extends JpaRepository<BoardSnapshot, Integer> {
    @Override
    <S extends BoardSnapshot> @NotNull List<S> saveAll(Iterable<S> entities);
}
