package com.orobator.android.gramophone.view.fragments;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.fortysevendeg.android.swipelistview.SwipeListView;
import com.orobator.android.gramophone.R;
import com.orobator.android.gramophone.controller.listeners.SongClickListener;
import com.orobator.android.gramophone.controller.listeners.SongSwipeViewListener;
import com.orobator.android.gramophone.model.SongDatabaseHelper.SongCursor;
import com.orobator.android.gramophone.model.loaders.SongCursorLoader;
import com.orobator.android.gramophone.view.adapters.DummyListAdapter;
import com.orobator.android.gramophone.view.adapters.SongCursorAdapter;

/**
 * Fragment used to display all of the songs on the device.
 */
public class SongsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String TAG = "SongsFragment";
    private SwipeListView mSwipeListView;

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new SongCursorLoader(getActivity(), id, args);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        SongCursor songCursor = (SongCursor) cursor;
        SongCursorAdapter adapter = new SongCursorAdapter(getActivity().getApplicationContext(), songCursor, this);
        mSwipeListView.setAdapter(adapter);
        mSwipeListView.setOnItemClickListener(new SongClickListener(adapter, this));
        mSwipeListView.setSwipeListViewListener(new SongSwipeViewListener(adapter, this));
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        // Stop using the cursor (via the adapter)
        mSwipeListView.setAdapter(new DummyListAdapter());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);


        // Initialize the loader to load the list of songs 
        getLoaderManager().initLoader(SongCursorLoader.ALL_SONGS_ID, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view_songs, parent, false);

        if (view != null) {
            mSwipeListView = (SwipeListView) view.findViewById(R.id.list_view_songs);

        }

        LinearLayout emptyView = (LinearLayout) inflater.inflate(R.layout.list_view_songs_empty, parent, false);

        // TODO get this working.
        mSwipeListView.setEmptyView(emptyView);

        return view;
    }

}
