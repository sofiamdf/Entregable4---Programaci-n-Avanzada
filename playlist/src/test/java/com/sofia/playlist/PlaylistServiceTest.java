package com.sofia.playlist;

import com.sofia.playlist.model.Video;
import com.sofia.playlist.repository.PlaylistRepository;
import com.sofia.playlist.service.PlaylistService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PlaylistServiceTest {

    @Mock
    private PlaylistRepository repository;

    @InjectMocks
    private PlaylistService service;

    @Test
    void testFindAll() {
        List<Video> mockList = Arrays.asList(new Video(1, "A", "u", "e", 0, false));
        when(repository.findAll()).thenReturn(mockList);

        List<Video> result = service.findAll();

        assertThat(result).isSameAs(mockList);
        verify(repository).findAll();
    }

    @Test
    void testAddVideo() {
        when(repository.add(any(Video.class))).thenAnswer(invocation -> {
            Video v = invocation.getArgument(0);
            v.setId(42);
            return v;
        });

        String name = "My Video";
        String url = "https://www.youtube.com/watch?v=ABC123";

        Video added = service.add(name, url);

        ArgumentCaptor<Video> captor = ArgumentCaptor.forClass(Video.class);
        verify(repository).add(captor.capture());

        Video passed = captor.getValue();
        assertEquals(name, passed.getName());
        assertEquals(url, passed.getUrl());
        assertNotNull(passed.getEmbedUrl());
        assertTrue(passed.getEmbedUrl().contains("/embed/ABC123"));

        assertEquals(42, added.getId());
    }

    @Test
    void testDeleteVideo() {
        when(repository.deleteById(1L)).thenReturn(true);
        when(repository.deleteById(2L)).thenReturn(false);

        assertTrue(service.deleteById(1L));
        assertFalse(service.deleteById(2L));

        verify(repository, times(2)).deleteById(anyLong());
    }

    @Test
    void testLikeVideo() {
        when(repository.like(5L)).thenReturn(true);
        when(repository.like(6L)).thenReturn(false);

        assertTrue(service.like(5L));
        assertFalse(service.like(6L));

        verify(repository, times(2)).like(anyLong());
    }

    @Test
    void testToggleFavorite() {
        when(repository.toggleFavorite(3L)).thenReturn(true);
        when(repository.toggleFavorite(4L)).thenReturn(false);

        assertTrue(service.toggleFavorite(3L));
        assertFalse(service.toggleFavorite(4L));

        verify(repository, times(2)).toggleFavorite(anyLong());
    }

    @Test
    void testConversionDeYouTubeUrls() {
        assertEquals("https://www.youtube.com/embed/ABC123", PlaylistService.toEmbedUrl("https://www.youtube.com/watch?v=ABC123"));
        assertEquals("https://www.youtube.com/embed/ABC123", PlaylistService.toEmbedUrl("https://youtu.be/ABC123"));
        assertEquals("https://www.youtube.com/embed/ABC123", PlaylistService.toEmbedUrl("https://www.youtube.com/embed/ABC123"));
    }
}
