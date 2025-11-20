package com.sofia.playlist.repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.CollectionType;
import com.sofia.playlist.model.Video;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class PlaylistRepository {

    private final File file;
    private final ObjectMapper mapper = new ObjectMapper();
    private final List<Video> videos = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public PlaylistRepository() {
        this.file = new File("playlist.json");
        load();
    }

    private synchronized void load() {
        if (!file.exists()) {
            return;
        }

        try {
            CollectionType type = mapper.getTypeFactory().constructCollectionType(List.class, Video.class);
            List<Video> loaded = mapper.readValue(file, type);
            videos.clear();
            if (loaded != null) {
                videos.addAll(loaded);
            }
            long maxId = videos.stream().map(Video::getId).max(Comparator.naturalOrder()).orElse(0L);
            idGenerator.set(maxId + 1);
        } catch (IOException e) {
            // If load fails, keep empty list
            e.printStackTrace();
        }
    }

    private synchronized void save() {
        try {
            File tmp = new File(file.getAbsolutePath() + ".tmp");
            mapper.writerWithDefaultPrettyPrinter().writeValue(tmp, videos);
            Files.move(tmp.toPath(), file.toPath(), StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized List<Video> findAll() {
        return new ArrayList<>(videos);
    }

    public synchronized Video add(Video video) {
        long id = idGenerator.getAndIncrement();
        video.setId(id);
        videos.add(video);
        save();
        return video;
    }

    public synchronized boolean deleteById(long id) {
        Optional<Video> found = videos.stream().filter(v -> v.getId() == id).findFirst();
        if (found.isPresent()) {
            videos.remove(found.get());
            save();
            return true;
        }
        return false;
    }

    public synchronized boolean like(long id) {
        for (Video v : videos) {
            if (v.getId() == id) {
                v.setLikes(v.getLikes() + 1);
                save();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean toggleFavorite(long id) {
        for (Video v : videos) {
            if (v.getId() == id) {
                v.setFavorite(!v.isFavorite());
                save();
                return true;
            }
        }
        return false;
    }

    public synchronized boolean moveUp(long id) {
        int idx = -1;
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).getId() == id) {
                idx = i;
                break;
            }
        }
        if (idx > 0) {
            Video curr = videos.get(idx);
            Video prev = videos.get(idx - 1);
            videos.set(idx - 1, curr);
            videos.set(idx, prev);
            save();
            return true;
        }
        return false;
    }

    public synchronized boolean moveDown(long id) {
        int idx = -1;
        for (int i = 0; i < videos.size(); i++) {
            if (videos.get(i).getId() == id) {
                idx = i;
                break;
            }
        }
        if (idx >= 0 && idx < videos.size() - 1) {
            Video curr = videos.get(idx);
            Video next = videos.get(idx + 1);
            videos.set(idx + 1, curr);
            videos.set(idx, next);
            save();
            return true;
        }
        return false;
    }
}
