package com.sofia.playlist.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Video {

	private long id;
	private String name;
	private String url;
	private String embedUrl;
	private int likes;
	private boolean favorite;

	public Video() {
	}

	public Video(long id, String name, String url, String embedUrl, int likes, boolean favorite) {
		this.id = id;
		this.name = name;
		this.url = url;
		this.embedUrl = embedUrl;
		this.likes = likes;
		this.favorite = favorite;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getEmbedUrl() {
		return embedUrl;
	}

	public void setEmbedUrl(String embedUrl) {
		this.embedUrl = embedUrl;
	}

	public int getLikes() {
		return likes;
	}

	public void setLikes(int likes) {
		this.likes = likes;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}
}
