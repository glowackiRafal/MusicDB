package sample.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class SongsQueryData {
    private SimpleStringProperty artist;
    private SimpleStringProperty album;
    private SimpleIntegerProperty track;
    private SimpleStringProperty title;

    public SongsQueryData() {
        this.artist = new SimpleStringProperty();
        this.album = new SimpleStringProperty();
        this.track = new SimpleIntegerProperty();
        this.title = new SimpleStringProperty();
    }

    public String getArtist() {
        return artist.get();
    }

    public void setArtist(String artist) {
        this.artist.set(artist);
    }

    public String getAlbum() {
        return album.get();
    }

    public void setAlbum(String album) {
        this.album.set(album);
    }

    public int getTrack() {
        return track.get();
    }

    public void setTrack(int track) {
        this.track.set(track);
    }

    public String getTitle() {
        return title.get();
    }

    public void setTitle(String title) {
        this.title.set(title);
    }
}
