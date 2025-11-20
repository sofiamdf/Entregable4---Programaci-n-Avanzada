package com.sofia.playlist;

import com.sofia.playlist.HomeController;
import com.sofia.playlist.model.Video;
import com.sofia.playlist.service.PlaylistService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PlaylistService playlistService;

    @Test
    void getIndexReturnsVideosModel() throws Exception {
        when(playlistService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("videos"));

        verify(playlistService).findAll();
    }

    @Test
    void postAddRedirects() throws Exception {
        when(playlistService.add(anyString(), anyString())).thenReturn(new Video());

        mockMvc.perform(post("/add").param("name", "X").param("url", "u"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(playlistService).add("X", "u");
    }

    @Test
    void postDeleteRedirects() throws Exception {
        when(playlistService.deleteById(1L)).thenReturn(true);

        mockMvc.perform(post("/delete/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(playlistService).deleteById(1L);
    }

    @Test
    void postLikeRedirects() throws Exception {
        when(playlistService.like(2L)).thenReturn(true);

        mockMvc.perform(post("/like/2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(playlistService).like(2L);
    }

    @Test
    void postFavoriteRedirects() throws Exception {
        when(playlistService.toggleFavorite(3L)).thenReturn(true);

        mockMvc.perform(post("/favorite/3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/"));

        verify(playlistService).toggleFavorite(3L);
    }
}
