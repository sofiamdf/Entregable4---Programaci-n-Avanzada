package com.sofia.playlist;

import com.sofia.playlist.model.Video;
import com.sofia.playlist.service.PlaylistService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class HomeController {

    private final PlaylistService playlistService;

    public HomeController(PlaylistService playlistService) {
        this.playlistService = playlistService;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Video> videos = playlistService.findAll();
        model.addAttribute("videos", videos);
        return "index";
    }

    @PostMapping("/add")
    public String add(@RequestParam String name, @RequestParam String url) {
        playlistService.add(name, url);
        return "redirect:/";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable long id) {
        playlistService.deleteById(id);
        return "redirect:/";
    }

    @PostMapping("/like/{id}")
    public String like(@PathVariable long id) {
        playlistService.like(id);
        return "redirect:/";
    }

    @PostMapping("/favorite/{id}")
    public String favorite(@PathVariable long id) {
        playlistService.toggleFavorite(id);
        return "redirect:/";
    }

    @PostMapping("/move-up/{id}")
    public String moveUp(@PathVariable long id) {
        playlistService.moveUp(id);
        return "redirect:/";
    }

    @PostMapping("/move-down/{id}")
    public String moveDown(@PathVariable long id) {
        playlistService.moveDown(id);
        return "redirect:/";
    }
}
