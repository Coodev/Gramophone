package com.orobator.android.gramophone.model;

import android.provider.BaseColumns;

/**
 * The LibraryContract specifies the schemas of the SQL tables used for
 * accessing collections of music.
 */
public final class LibraryContract {

    /**
     * LibraryContract has an empty constructor to avoid accidental
     * instantiation.
     */
    public LibraryContract() {
    }

    /* By implementing the BaseColumns interface, inner classes receive a
       primary key _ID field, which is needed for cursor adapters. */

    public static abstract class SongEntry implements BaseColumns {
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_NAME_ALBUM = "album";
        public static final String COLUMN_NAME_ALBUM_ARTIST = "album_artist";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_BIT_RATE = "bit_rate";
        public static final String COLUMN_NAME_COLOR_BACKGROUND = "background_color";
        public static final String COLUMN_NAME_COLOR_PRIMARY = "primary_color";
        public static final String COLUMN_NAME_COLOR_SECONDARY = "secondary_color";
        public static final String COLUMN_NAME_COLOR_DETAIL = "detail_color";
        public static final String COLUMN_NAME_COMPILATION = "compilation";
        public static final String COLUMN_NAME_COMPOSER = "composer";
        public static final String COLUMN_NAME_DATE_MODIFIED = "modified";
        public static final String COLUMN_NAME_DISC_NUMBER = "disc_number";
        public static final String COLUMN_NAME_DISC_TOTAL = "total_discs";
        public static final String COLUMN_NAME_DURATION = "duration";
        public static final String COLUMN_NAME_EQUALIZER_PRESET = "equalizer_preset";
        public static final String COLUMN_NAME_FILE_LOCATION = "location";
        public static final String COLUMN_NAME_GENRE = "genre";
        public static final String COLUMN_NAME_HAS_ARTWORK = "artwork";
        public static final String COLUMN_NAME_LAST_PLAYED = "last_played";
        public static final String COLUMN_NAME_PLAY_COUNT = "play_count";
        public static final String COLUMN_NAME_RATING = "rating";
        public static final String COLUMN_NAME_SAMPLE_RATE = "sample_rate";
        public static final String COLUMN_NAME_SIZE = "size";
        public static final String COLUMN_NAME_SKIP_COUNT = "skip_count";
        public static final String COLUMN_NAME_SKIP_ON_SHUFFLE = "shuffle_skip";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_TRACK_NUMBER = "track_number";
        public static final String COLUMN_NAME_TRACK_TOTAL = "total_tracks";
        public static final String COLUMN_NAME_WRITER = "writer";
        public static final String COLUMN_NAME_YEAR = "year";
    }

    public static abstract class AlbumEntry implements BaseColumns {
        public static final String TABLE_NAME = "albums";
        public static final String COLUMN_NAME_ALBUM_NAME = "album_name";
        public static final String COLUMN_NAME_ALBUM_ARTIST = "album_artist";
    }

    public static abstract class ArtistEntry implements BaseColumns {
        public static final String TABLE_NAME = "artists";
        public static final String COLUMN_NAME_ARTIST_NAME = "artist_name";
    }

    public static abstract class GenreEntry implements BaseColumns {
        public static final String TABLE_NAME = "genres";
        public static final String COLUMN_NAME_GENRE_NAME = "genre_name";
    }

}
