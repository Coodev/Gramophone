package com.orobator.android.gramophone.view.fragments;import android.app.ListFragment;import android.app.LoaderManager;import android.content.Intent;import android.content.Loader;import android.database.Cursor;import android.os.Bundle;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.CursorAdapter;import android.widget.ListView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Song;import com.orobator.android.gramophone.model.SongDatabaseHelper.SongCursor;import com.orobator.android.gramophone.model.loaders.SongCursorLoader;import com.orobator.android.gramophone.view.activities.SongMetadataActivity;import com.orobator.android.gramophone.view.adapters.SongCursorAdapter;public class AlbumSongsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {    private static final String TAG = "AlbumSongsFragment";    @Override    public Loader<Cursor> onCreateLoader(int id, Bundle args) {        return new SongCursorLoader(getActivity(), id, args);    }    @Override    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {        SongCursor songCursor = (SongCursor) cursor;        SongCursorAdapter adapter = new SongCursorAdapter(getActivity().getApplicationContext(), songCursor);        setListAdapter(adapter);    }    @Override    public void onLoaderReset(Loader<Cursor> loader) {        setListAdapter(null);    }    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setRetainInstance(true);        Bundle args = getArguments();        getLoaderManager().initLoader(SongCursorLoader.ALBUM_SONGS_ID, args, this);    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        return inflater.inflate(R.layout.list_view_songs, parent, false);    }    @Override    public void onListItemClick(ListView listView, View view, int position, long id) {        Song song = (Song) getListAdapter().getItem(position);        Intent intent = new Intent(getActivity(), SongMetadataActivity.class);        intent.putExtra(SongCursorAdapter.KEY_SONG, song);        startActivity(intent);    }    @Override    public void onDestroy() {        CursorAdapter cursorAdapter = (CursorAdapter) getListAdapter();        Cursor cursor = cursorAdapter.getCursor();        if (cursor != null) {            cursor.close();        }        super.onDestroy();    }}