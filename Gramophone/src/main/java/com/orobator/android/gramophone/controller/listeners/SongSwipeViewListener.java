package com.orobator.android.gramophone.controller.listeners;import android.app.Fragment;import android.content.Intent;import android.widget.ListAdapter;import com.fortysevendeg.android.swipelistview.BaseSwipeListViewListener;import com.orobator.android.gramophone.model.Song;import com.orobator.android.gramophone.view.activities.NowPlayingActivity;public class SongSwipeViewListener extends BaseSwipeListViewListener {    private ListAdapter mListAdapter;    private Fragment mFragment;    public SongSwipeViewListener(ListAdapter listAdapter, Fragment fragment) {        mListAdapter = listAdapter;        mFragment = fragment;    }    @Override    public void onOpened(int position, boolean toRight) {    }    @Override    public void onClosed(int position, boolean fromRight) {    }    @Override    public void onListChanged() {    }    @Override    public void onMove(int position, float x) {    }    @Override    public void onStartOpen(int position, int action, boolean right) {    }    @Override    public void onStartClose(int position, boolean right) {    }    @Override    public void onClickFrontView(int position) {        Song song = (Song) mListAdapter.getItem(position);        Intent intent = new Intent(mFragment.getActivity(), NowPlayingActivity.class);        intent.putExtra(Song.KEY_SONG, song);        intent.putExtra(Song.KEY_CURSOR_POSITION, position);        intent.putExtra(Song.KEY_SONG_COLLECTION_TYPE, Song.KEY_COLLECTION_TYPE_ALL);        mFragment.startActivity(intent);    }    @Override    public void onClickBackView(int position) {    }    @Override    public void onDismiss(int[] reverseSortedPositions) {    }}