package ru.kantser.game.model.state;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import ru.kantser.game.model.state.game.GameState;

import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;

public class FileUserStateRepository implements UserStateRepository {
    private final Path baseDir;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public FileUserStateRepository(Path baseDir) throws IOException {
        this.baseDir = baseDir;
        if (!Files.exists(baseDir)) {
            Files.createDirectories(baseDir);
        }
    }

    private Path pathFor(String userId) {
        // простая схема: {baseDir}/{userId}.json
        // sanitize userId если нужно — сейчас предполагаем корректные id
        return baseDir.resolve(userId + ".json");
    }

    @Override
    public synchronized Optional<GameState> load(String userId) throws IOException {
        Path p = pathFor(userId);
        if (!Files.exists(p)) return Optional.empty();
        byte[] bytes = Files.readAllBytes(p);
        GameState state = mapper.readValue(bytes, GameState.class);
        return Optional.ofNullable(state);
    }

    @Override
    public void save(String userId, GameState state) throws IOException {
        Path p = pathFor(userId);
        // атомарно сохраняем: сначала tmp, затем move/replace
        Path tmp = Files.createTempFile(baseDir, userId + ".", ".tmp");
        mapper.writeValue(tmp.toFile(), state);
        Files.move(tmp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }


    @Override
    public synchronized boolean exists(String userId) {
        return Files.exists(pathFor(userId));
    }
}
