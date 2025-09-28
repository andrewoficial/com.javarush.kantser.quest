package ru.kantser.game.service.game;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.kantser.game.constatnt.AppConstant;
import ru.kantser.game.model.state.GameState;
import ru.kantser.game.model.state.StateRepository;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

public class SaveGameManager implements StateRepository {
    private static final Logger log = LoggerFactory.getLogger(SaveGameManager.class);
    private final String baseSavePath;
    private final ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);

    public SaveGameManager(String baseSavePath) {
        log.info("init с папкой {}", baseSavePath);
        this.baseSavePath = baseSavePath;
        createDirectories();
    }

    private void createDirectories() {
        log.info("createDirectories");
        try {
            Files.createDirectories(Paths.get(baseSavePath));
        } catch (IOException e) {
            throw new RuntimeException("Could not create save directory", e);
        }
    }

    private Path getUserSavePath(String userId, String saveId) {
        log.info("getUserSavePath");
        return Paths.get(baseSavePath, userId, saveId + AppConstant.FileExtensions.FILE_EXTENSION_DOT_JSON);
    }

    private Path getUserDirectory(String userId) {
        final var o = Paths.get(baseSavePath, userId);
        log.info("getUserDirectory return {}", o);
        return o;
    }

    @Override
    public synchronized GameState load(String userId, String saveId) throws IOException {
        log.info("load");
        Path savePath = getUserSavePath(userId, saveId);
        if (!Files.exists(savePath)) {
            throw new FileNotFoundException("Save game not found: " + savePath);
        }

        String json = Files.readString(savePath);
        GameState state = mapper.readValue(json, GameState.class);
        if (state == null) {
            throw new IOException("Failed to deserialize state from " + savePath);
        }
        return state;
    }

    @Override
    public synchronized void save(GameState state, String userId, String saveId) throws IOException {
        log.info("save");
        Path userDir = getUserDirectory(userId);
        Files.createDirectories(userDir);

        Path savePath = getUserSavePath(userId, saveId);

        Path tmp = Files.createTempFile(userDir, saveId + ".", ".tmp");
        mapper.writeValue(tmp.toFile(), state);
        Files.move(tmp, savePath, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
    }

    @Override
    public synchronized boolean exists(String userId, String saveId) {
        log.info("exists");
        return Files.exists(getUserSavePath(userId, saveId));
    }

    public synchronized List<String> listSaves(String userId) {
        log.info("listSaves");
        Path userDir = getUserDirectory(userId);
        if (!Files.exists(userDir)) {
            log.info("return empty list");
            return List.of();
        }

        try {
            log.info("return not empty list");
            return Files.list(userDir)
                    .filter(path -> path.toString().endsWith(AppConstant.FileExtensions.FILE_EXTENSION_DOT_JSON))
                    .map(path -> path.getFileName().toString().replace(AppConstant.FileExtensions.FILE_EXTENSION_DOT_JSON, ""))
                    .sorted()
                    .collect(Collectors.toList());
        } catch (IOException e) {
            log.info("return empty list");
            return List.of();
        }
    }

    public synchronized void delete(String userId, String saveId) throws IOException {
        log.info("delete");
        Path savePath = getUserSavePath(userId, saveId);
        if (!Files.exists(savePath)) {
            throw new FileNotFoundException("Save game not found: " + savePath);
        }
        Files.delete(savePath);
    }
}