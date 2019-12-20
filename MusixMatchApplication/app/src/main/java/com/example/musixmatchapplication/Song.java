package com.example.musixmatchapplication;

import java.util.Date;

public class Song {
    String track_name,album_name,artist_name,date,url;

    public Song() {
        this.track_name = track_name;
        this.album_name = album_name;
        this.artist_name = artist_name;
        this.date = date;
        this.url = url;

    }

    @Override
    public String toString() {
        return "Song{" +
                "track_name='" + track_name + '\'' +
                ", album_name='" + album_name + '\'' +
                ", artist_name='" + artist_name + '\'' +
                ", date='" + date + '\'' +
                ", url='" + url + '\'' +
                '}';
    }

    public String getTrack_name() {
        return track_name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setTrack_name(String track_name) {
        this.track_name = track_name;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public String getArtist_name() {
        return artist_name;
    }

    public void setArtist_name(String artist_name) {
        this.artist_name = artist_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
