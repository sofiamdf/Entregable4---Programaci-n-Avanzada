package com.sofia.playlist;

import com.sofia.playlist.model.Video;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VideoTest {

    @Test
    void gettersAndSettersWork() {
        Video v = new Video();
        v.setId(7);
        v.setName("Name");
        v.setUrl("http://example.com");
        v.setEmbedUrl("http://embed");
        v.setLikes(3);
        v.setFavorite(true);

        assertEquals(7, v.getId());
        assertEquals("Name", v.getName());
        assertEquals("http://example.com", v.getUrl());
        assertEquals("http://embed", v.getEmbedUrl());
        assertEquals(3, v.getLikes());
        assertTrue(v.isFavorite());
    }
}
