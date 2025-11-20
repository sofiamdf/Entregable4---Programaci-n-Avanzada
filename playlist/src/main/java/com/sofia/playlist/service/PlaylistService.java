package com.sofia.playlist.service;

import com.sofia.playlist.model.Video;
import com.sofia.playlist.repository.PlaylistRepository;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.List;

@Service
public class PlaylistService {

	private final PlaylistRepository repository;

	public PlaylistService(PlaylistRepository repository) {
		this.repository = repository;
	}

	public List<Video> findAll() {
		return repository.findAll();
	}

	public Video add(String name, String url) {
		String embed = toEmbedUrl(url);
		Video v = new Video();
		v.setName(name);
		v.setUrl(url);
		v.setEmbedUrl(embed);
		v.setLikes(0);
		v.setFavorite(false);
		return repository.add(v);
	}

	public boolean deleteById(long id) {
		return repository.deleteById(id);
	}

	public boolean like(long id) {
		return repository.like(id);
	}

	public boolean toggleFavorite(long id) {
		return repository.toggleFavorite(id);
	}

	public boolean moveUp(long id) {
		return repository.moveUp(id);
	}

	public boolean moveDown(long id) {
		return repository.moveDown(id);
	}

	public static String toEmbedUrl(String original) {
		if (original == null) return null;
		String id = null;
		try {
			URI u = new URI(original);
			String host = u.getHost();
			String path = u.getPath();

			if (host != null && host.contains("youtu.be")) {
				// short link: https://youtu.be/ABC
				if (path != null && path.length() > 1) {
					id = path.substring(1).split("[/?&]")[0];
				}
			} else if (host != null && host.contains("youtube.com")) {
				// check query for v=
				String query = u.getQuery();
				if (query != null) {
					String[] parts = query.split("&");
					for (String p : parts) {
						if (p.startsWith("v=")) {
							id = p.substring(2);
							break;
						}
					}
				}
				// if still null, maybe /embed/ID or last segment
				if (id == null && path != null) {
					if (path.contains("/embed/")) {
						String[] segs = path.split("/embed/");
						if (segs.length > 1) id = segs[1].split("[/?&]")[0];
					} else {
						String[] segs = path.split("/");
						if (segs.length > 0) id = segs[segs.length - 1];
					}
				}
			}
		} catch (Exception e) {
			// Not a valid URL, try fallback extraction
			String tmp = original;
			if (tmp.contains("v=")) {
				int idx = tmp.indexOf("v=");
				id = tmp.substring(idx + 2).split("[&?\\s]")[0];
			} else if (tmp.contains("youtu.be/")) {
				int idx = tmp.indexOf("youtu.be/");
				id = tmp.substring(idx + 9).split("[&?\\s]")[0];
			}
		}

		if (id == null || id.isEmpty()) {
			// fallback: return original so iframe may still try to load
			return original;
		}

		return "https://www.youtube.com/embed/" + id;
	}
}
