package com.sofia.playlist;

import com.sofia.playlist.model.Video;
import com.sofia.playlist.repository.PlaylistRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlaylistRepositoryTest {

    private Path tempDir;
    private File tempFile;
    private PlaylistRepository repository;

    @BeforeEach
    void setUp() throws IOException {
        tempDir = Files.createTempDirectory("playlist-test-");
        tempFile = Files.createTempFile(tempDir, "playlist-test", ".json").toFile();
        // ensure the temp file contains an empty JSON array so load() finds valid content
        Files.write(tempFile.toPath(), "[]".getBytes(StandardCharsets.UTF_8));
        repository = new PlaylistRepository(tempFile);
    }

    @AfterEach
    void tearDown() throws IOException {
        if (tempFile != null && tempFile.exists()) Files.deleteIfExists(tempFile.toPath());
        if (tempDir != null) Files.deleteIfExists(tempDir);
    }

    @Test
    void testAddAndFindAll() {
        Video v1 = new Video(0, "One", "u1", "e1", 0, false);
        Video v2 = new Video(0, "Two", "u2", "e2", 0, false);

        Video added1 = repository.add(v1);
        Video added2 = repository.add(v2);

        List<Video> all = repository.findAll();
        assertEquals(2, all.size());
        assertTrue(all.stream().anyMatch(v -> v.getName().equals("One")));
        assertTrue(all.stream().anyMatch(v -> v.getName().equals("Two")));

        assertTrue(added1.getId() > 0);
        assertTrue(added2.getId() > 0);
    }

    @Test
    void testDeleteById() {
        Video v = new Video(0, "X", "u", "e", 0, false);
        Video added = repository.add(v);

        boolean deleted = repository.deleteById(added.getId());
        assertTrue(deleted);

        boolean deletedAgain = repository.deleteById(9999L);
        assertFalse(deletedAgain);
    }

    @Test
    void testLikeAndToggleFavorite() {
        Video v = new Video(0, "L", "u", "e", 0, false);
        Video added = repository.add(v);

        boolean liked = repository.like(added.getId());
        assertTrue(liked);

        // liking non-existing
        assertFalse(repository.like(9999L));

        boolean toggled = repository.toggleFavorite(added.getId());
        assertTrue(toggled);

        // second toggle returns true as well (it toggles back)
        boolean toggled2 = repository.toggleFavorite(added.getId());
        assertTrue(toggled2);

        assertFalse(repository.toggleFavorite(9999L));
    }

    @Test
    void testSaveAndReload() {
        Video v = new Video(0, "Persist", "u", "e", 0, true);
        Video added = repository.add(v);

        // create new repository instance which should read the file
        PlaylistRepository repo2 = new PlaylistRepository(tempFile);
        List<Video> all = repo2.findAll();
        assertTrue(all.stream().anyMatch(x -> x.getName().equals("Persist")));
    }
}
