package com.orobator.android.gramophone.view.fragments;import android.app.FragmentManager;import android.app.ListFragment;import android.app.LoaderManager;import android.content.Context;import android.content.Loader;import android.database.Cursor;import android.os.Bundle;import android.util.Log;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ListView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Album;import com.orobator.android.gramophone.model.Library;import com.orobator.android.gramophone.model.SongDatabaseHelper;import com.orobator.android.gramophone.model.loaders.SQLiteCursorLoader;import com.orobator.android.gramophone.view.adapters.AlbumCursorAdapter;public class AlbumsFragment extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor> {    private static final String TAG = "AlbumsFragment";    @Override    public Loader<Cursor> onCreateLoader(int id, Bundle args) {        return new AlbumsListCursorLoader(getActivity());    }    @Override    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {        Library library = Library.getLibrary(getActivity().getApplicationContext());        AlbumCursorAdapter mAdapter = new AlbumCursorAdapter(getActivity().getApplicationContext(), library.getAlbums());        setListAdapter(mAdapter);    }    @Override    public void onLoaderReset(Loader<Cursor> loader) {        setListAdapter(null);    }    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setRetainInstance(true);        getLoaderManager().initLoader(0, null, this);    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        View view = inflater.inflate(R.layout.list_view_albums, parent, false);        Log.i(TAG, "onCreateView");        return view;    }    @Override    public void onListItemClick(ListView listView, View view, int position, long id) {        Album album = (Album) getListAdapter().getItem(position);        Bundle args = new Bundle();        args.putString(AlbumCursorAdapter.KEY_ALBUM_NAME, album.getAlbumName());        args.putString(AlbumCursorAdapter.KEY_ALBUM_ARTIST, album.getAlbumArtist());        AlbumSongsFragment albumSongsFragment = new AlbumSongsFragment();        albumSongsFragment.setArguments(args);        FragmentManager fm = getActivity().getFragmentManager();        fm.beginTransaction()                .replace(R.id.content_frame, albumSongsFragment)                .addToBackStack(album.getAlbumName())                .commit();    }    private static class AlbumsListCursorLoader extends SQLiteCursorLoader {        public AlbumsListCursorLoader(Context context) {            super(context);        }        @Override        protected Cursor loadCursor() {            return new SongDatabaseHelper(getContext()).queryAlbums();        }    }}