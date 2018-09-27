package sample.model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Datasource {
    public static final String DB_NAME = "music.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:C:\\Users\\Dell\\Desktop\\JavaPrograms\\MusicUI\\" + DB_NAME;

    public static final String TABLE_ALBUMS = "albums";
    public static final String COLUMN_ALBUM_ID = "_id";
    public static final String COLUMN_ALBUM_NAME = "name";
    public static final String COLUMN_ALBUM_ARTIST = "artist";


    public static final String TABLE_ARTISTS = "artists";
    public static final String COLUMN_ARTIST_ID = "_id";
    public static final String COLUMN_ARTIST_NAME = "name";
    public static final int INDEX_ARTIST_ID = 1;
    public static final int INDEX_ARTIST_NAME = 2;

    public static final String TABLE_SONGS = "songs";
    public static final String COLUMN_SONG_ID = "_id";
    public static final String COLUMN_SONG_TRACK = "track";
    public static final String COLUMN_SONG_TITLE = "title";
    public static final String COLUMN_SONG_ALBUM = "album";

    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    public static final String QUERY_SONGS =
            "SELECT " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", "
                    + TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE
                    + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID
                    + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID
                    + " ORDER BY " + TABLE_ARTISTS + "." + COLUMN_ARTIST_NAME + ", " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String QUERY_SONGS_BY_ARTIST =
            "SELECT " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE
                    + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID
                    + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID
                    + " WHERE " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID + " = ?"
                    + " ORDER BY " + TABLE_ALBUMS + "." + COLUMN_ALBUM_NAME + ", " + TABLE_SONGS + "." + COLUMN_SONG_TRACK;
    ;
    public static final String QUERY_SONGS_BY_ALBUM =
            "SELECT " + TABLE_SONGS + "." + COLUMN_SONG_TRACK + ", " + TABLE_SONGS + "." + COLUMN_SONG_TITLE
                    + " FROM " + TABLE_SONGS + " INNER JOIN " + TABLE_ALBUMS + " ON " + TABLE_SONGS + "." + COLUMN_SONG_ALBUM + " = " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID
                    + " INNER JOIN " + TABLE_ARTISTS + " ON " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ARTIST + " = " + TABLE_ARTISTS + "." + COLUMN_ARTIST_ID
                    + " WHERE " + TABLE_ALBUMS + "." + COLUMN_ALBUM_ID + " = ?"
                    + " ORDER BY " + TABLE_SONGS + "." + COLUMN_SONG_TRACK;

    public static final String INSERT_ARTIST =
            "INSERT INTO " + TABLE_ARTISTS + "(" + COLUMN_ARTIST_NAME + ") VALUES(?)";
    public static final String INSERT_ALBUM =
            "INSERT INTO " + TABLE_ALBUMS + "(" + COLUMN_ALBUM_NAME + ", " + COLUMN_ALBUM_ARTIST + ") VALUES(?, ?)";
    public static final String INSERT_SONG =
            "INSERT INTO " + TABLE_SONGS + "(" + COLUMN_SONG_TRACK + ", " + COLUMN_SONG_TITLE + ", " + COLUMN_SONG_ALBUM + ") VALUES(?, ?, ?)";

    public static final String QUERY_ALBUM_BY_ARTIST_ID = "SELECT * FROM " + TABLE_ALBUMS +
            " WHERE " + COLUMN_ALBUM_ARTIST + " = ? ORDER BY " + COLUMN_ALBUM_NAME + " COLLATE NOCASE";
    public static final String QUERY_ARTIST_BY_ALBUM_ID = "SELECT " + COLUMN_ALBUM_ARTIST + " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_ID + " = ?";

    public static final String QUERY_ARTIST = "SELECT " + COLUMN_ARTIST_ID + " FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_NAME + " = ?";
    public static final String QUERY_ALBUM = "SELECT " + COLUMN_ALBUM_ID + " FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_NAME + " = ?";
    public static final String QUERY_SONG = "SELECT " + COLUMN_SONG_ID + " FROM " + TABLE_SONGS + " WHERE " + COLUMN_SONG_TITLE + " = ?";
    public static final String UPDATE_ARTIST_QUERY =
            "UPDATE " + TABLE_ARTISTS + " SET " + COLUMN_ARTIST_NAME + " = ? WHERE " + COLUMN_ARTIST_ID + " = ?";
    public static final String DELETE_SONG = "DELETE FROM " + TABLE_SONGS + " WHERE " + COLUMN_SONG_ID + " = ?";
    public static final String DELETE_ALBUM = "DELETE FROM " + TABLE_ALBUMS + " WHERE " + COLUMN_ALBUM_ID + " = ?";
    public static final String DELETE_ARTIST = "DELETE FROM " + TABLE_ARTISTS + " WHERE " + COLUMN_ARTIST_ID + " = ?";
//    DELETE FROM tableName WHERE condition;

    private Connection connection;

    private PreparedStatement queryArtistIdByAlbum;
    private PreparedStatement querySong;
    private PreparedStatement insertIntoArtists;
    private PreparedStatement insertIntoAlbums;
    private PreparedStatement insertIntoSongs;
    private PreparedStatement queryArtists;
    private PreparedStatement queryAlbums;
    private PreparedStatement queryAlbumByArtistId;
    private PreparedStatement updateArtist;
    private PreparedStatement querySongsByArtist;
    private PreparedStatement querySongsByAlbum;
    private PreparedStatement deleteSong;
    private PreparedStatement deleteAlbum;
    private PreparedStatement deleteArtist;
    public static Datasource instance = new Datasource();


    private Datasource() {

    }

    public static Datasource getInstance() {
        return instance;
    }

    public boolean open() {
        try {
            connection = DriverManager.getConnection(CONNECTION_STRING);
            insertIntoArtists = connection.prepareStatement(INSERT_ARTIST, Statement.RETURN_GENERATED_KEYS);
            insertIntoAlbums = connection.prepareStatement(INSERT_ALBUM, Statement.RETURN_GENERATED_KEYS);
            insertIntoSongs = connection.prepareStatement(INSERT_SONG);
            queryArtists = connection.prepareStatement(QUERY_ARTIST);
            queryAlbums = connection.prepareStatement(QUERY_ALBUM);
            queryAlbumByArtistId = connection.prepareStatement(QUERY_ALBUM_BY_ARTIST_ID);
            updateArtist = connection.prepareStatement(UPDATE_ARTIST_QUERY);
            querySongsByArtist = connection.prepareStatement(QUERY_SONGS_BY_ARTIST);
            querySongsByAlbum = connection.prepareStatement(QUERY_SONGS_BY_ALBUM);
            querySong = connection.prepareStatement(QUERY_SONG);
            queryArtistIdByAlbum = connection.prepareStatement(QUERY_ARTIST_BY_ALBUM_ID);
            deleteSong = connection.prepareStatement(DELETE_SONG);
            deleteAlbum = connection.prepareStatement(DELETE_ALBUM);
            deleteArtist = connection.prepareStatement(DELETE_ARTIST);
            return true;
        } catch (Exception e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public void close() {
        try {
            if (querySong != null) {
                querySong.close();
            }
            if (queryArtistIdByAlbum != null) {
                queryArtistIdByAlbum.close();
            }
            if (deleteArtist != null) {
                deleteArtist.close();
            }
            if (deleteAlbum != null) {
                deleteAlbum.close();
            }
            if (deleteSong != null) {
                deleteSong.close();
            }
            if (insertIntoArtists != null) {
                insertIntoArtists.close();
            }
            if (insertIntoAlbums != null) {
                insertIntoAlbums.close();
            }
            if (insertIntoSongs != null) {
                insertIntoSongs.close();
            }
            if (queryArtists != null) {
                queryArtists.close();
            }
            if (queryAlbums != null) {
                queryAlbums.close();
            }
            if (queryAlbumByArtistId != null) {
                queryAlbumByArtistId.close();
            }
            if (updateArtist != null) {
                updateArtist.close();
            }
            if (querySongsByArtist != null) {
                querySongsByArtist.close();
            }
            if (querySongsByAlbum != null) {
                querySongsByAlbum.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public boolean updateArtist(String newName, int artistId) {
        try {
            updateArtist.setString(1, newName);
            updateArtist.setInt(2, artistId);
            int affectedRecord = updateArtist.executeUpdate();
            return affectedRecord == 1;
        } catch (Exception e) {
            System.out.println("Update query failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public List<SongsQueryData> songsQuery(int artistId, int albumId) {
        try {
            List<SongsQueryData> songs = new ArrayList<>();
            if (artistId == -1 && albumId == -1) {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(QUERY_SONGS);
                while (resultSet.next()) {
                    SongsQueryData song = new SongsQueryData();
                    song.setArtist(resultSet.getString(1));
                    song.setAlbum(resultSet.getString(2));
                    song.setTrack(resultSet.getInt(3));
                    song.setTitle(resultSet.getString(4));
                    songs.add(song);
                }

                return songs;
            } else if (albumId == -1) {
                querySongsByArtist.setInt(1, artistId);
                ResultSet resultSet = querySongsByArtist.executeQuery();
                while (resultSet.next()) {
                    SongsQueryData song = new SongsQueryData();
                    song.setAlbum(resultSet.getString(1));
                    song.setTrack(resultSet.getInt(2));
                    song.setTitle(resultSet.getString(3));
                    songs.add(song);
                }
                return songs;
            } else if (artistId == -1) {
                querySongsByAlbum.setInt(1, albumId);
                ResultSet resultSet = querySongsByAlbum.executeQuery();
                while (resultSet.next()) {
                    SongsQueryData song = new SongsQueryData();
                    song.setTrack(resultSet.getInt(1));
                    song.setTitle(resultSet.getString(2));
                    songs.add(song);
                }
                return songs;
            }
        } catch (Exception e) {
            System.out.println("Songs query failed: " + e.getMessage());
        }
        return null;
    }

    public List<Artist> queryArtists(int sortOrder) {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ARTISTS);
        sb.append(sortType(sortOrder, COLUMN_ARTIST_NAME));
        try (Statement statement = connection.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<Artist> artists = new ArrayList<>();
            while (results.next()) {
                Artist artist = new Artist();
                artist.setId(results.getInt(INDEX_ARTIST_ID));
                artist.setName(results.getString(INDEX_ARTIST_NAME));
                artists.add(artist);
            }
            return artists;
        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

    public List<Album> queryAlbumsForArtistById(int id) {
        try {
            queryAlbumByArtistId.setInt(1, id);
            ResultSet resultSet = queryAlbumByArtistId.executeQuery();
            List<Album> albums = new ArrayList<>();
            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getInt(1));
                album.setName(resultSet.getString(2));
                album.setArtistId(id);
                albums.add(album);
            }
            return albums;
        } catch (Exception e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }

//    private boolean deleteEntry(PreparedStatement statement, int entryId) {           TODO LATER
//        try {
//            statement.setInt(1, entryId);
//            connection.setAutoCommit(false);
//            int affectedRows = statement.executeUpdate();
//            if (affectedRows == 1) {
//                connection.commit();
//                return true;
//            } else {
//                throw new SQLException("Couldn't perform artist!");
//            }
//        } catch (Exception e) {
//            try {
//                System.out.println("Performing rollback");
//                connection.rollback();
//            } catch (SQLException e2) {
//                System.out.println("Ups! Things get really bad! Rollback exception: " + e2.getMessage());
//            }
//            return false;
//        } finally {
//            try {
//                System.out.println("Resetting default commit behavior");
//                connection.setAutoCommit(true);
//            } catch (SQLException e) {
//                System.out.println("Couldn't reset auto-commit! " + e.getMessage()
//                );
//            }
//        }
//    }
//
//    public boolean deleteArtist(int artistId){
//        return deleteEntry(deleteArtist, artistId);
//    }
//    public boolean deleteAlbum(int albumId){
//        return deleteEntry(deleteAlbum, albumId);
//    }
//    public boolean deleteSong(int songId){
//        return deleteEntry(deleteSong, songId);
//    }

    private int insertArtist(String artistName) throws SQLException {
        queryArtists.setString(1, artistName);
        ResultSet resultSet = queryArtists.executeQuery();
        if (resultSet.next()) {
            return resultSet.getInt(1);
        } else {
            insertIntoArtists.setString(1, artistName);
            int affectedRows = insertIntoArtists.executeUpdate();

            if (affectedRows != 1) {
                throw new SQLException("Couldn't insert artist!");
            }

            ResultSet generatedKeys = insertIntoArtists.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for artist!");
            }
        }
    }

    private int insertAlbum(String albumName, int artistId) throws SQLException {
        queryAlbums.setString(1, albumName);
        ResultSet albumsResultSet = queryAlbums.executeQuery();
        while (albumsResultSet.next()) {
            int albumId = albumsResultSet.getInt(1);
            queryArtistIdByAlbum.setInt(1, albumId);
            ResultSet artistIdResultSet = queryArtistIdByAlbum.executeQuery();
            if (artistIdResultSet.getInt(1) == artistId) {
                return albumId;
            }
        }
        insertIntoAlbums.setString(1, albumName);
        insertIntoAlbums.setInt(2, artistId);
        int affectedRows = insertIntoAlbums.executeUpdate();
        if (affectedRows != 1) {
            throw new SQLException("Couldn't insert album!");
        }
        ResultSet generatedKeys = insertIntoAlbums.getGeneratedKeys();
        if (generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Couldn't get _id for album!");
        }
    }


    public void insertSong(String songTitle, int track, String albumName, String artistName) {
        try {
            querySong.setString(1, songTitle);
            ResultSet songsResultSet = querySong.executeQuery();
            while (songsResultSet.next()) {
                queryAlbums.setString(1, albumName);
                ResultSet albumsResultSet = queryAlbums.executeQuery();
                while (albumsResultSet.next()) {
                    queryArtists.setString(1, artistName);
                    ResultSet artistsResultSet = queryArtists.executeQuery();
                    if (artistsResultSet.next()) {
                        return;
                    }
                }
            }
            connection.setAutoCommit(false);
            insertIntoSongs.setInt(1, track);
            insertIntoSongs.setString(2, songTitle);
            int artistId = insertArtist(artistName);
            int albumId = insertAlbum(albumName, artistId);
            insertIntoSongs.setInt(3, albumId);
            int affectedRows = insertIntoSongs.executeUpdate();
            if (affectedRows == 1) {
                connection.commit();
            } else {
                throw new SQLException("The song insert failed!");
            }

        } catch (Exception e) {
            System.out.println("Insert song exception: " + e.getMessage());
            try {

                System.out.println("Performing rollback");
                connection.rollback();
            } catch (SQLException e2) {
                System.out.println("Oh boy! Things get really bad! Rollback exception: " + e2.getMessage());
            }
        } finally {
            try {
                System.out.println("Resetting default commit behavior");
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                System.out.println("Couldn't reset auto-commit! " + e.getMessage());
            }
        }
    }

    private String sortType(int sortOrder, String orderingBy) {
        StringBuilder sb = new StringBuilder();
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(orderingBy);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }
        return sb.toString();
    }
}