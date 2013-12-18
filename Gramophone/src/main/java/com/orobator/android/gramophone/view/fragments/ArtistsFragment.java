package com.orobator.android.gramophone.view.fragments;import android.os.Bundle;import android.support.v4.app.ListFragment;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.ArrayAdapter;import android.widget.ListView;import android.widget.SectionIndexer;import android.widget.TextView;import android.widget.Toast;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Artist;import com.orobator.android.gramophone.model.Library;import java.util.ArrayList;import java.util.HashMap;import java.util.Vector;public class ArtistsFragment extends ListFragment {    private static final String TAG = "ArtistsFragment";    ArtistAdapter mAdapter;    @Override    public void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setRetainInstance(true);        Library library = Library.getLibrary(getActivity().getApplicationContext());        long startTime = System.currentTimeMillis();        mAdapter = new ArtistAdapter(library.getArtists());        setListAdapter(mAdapter);        long endTime = System.currentTimeMillis();        int artistCount = mAdapter.mArtists.size();        double timeInSeconds = (endTime - startTime) / 1000.0;        Toast toast = Toast.makeText(getActivity(), "Loaded " + artistCount                + " artists in " + timeInSeconds + " seconds,", Toast.LENGTH_LONG);        toast.show();    }    @Override    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {        View view = inflater.inflate(R.layout.list_view_artists, parent, false);        return view;    }    @Override    public void onListItemClick(ListView listView, View view, int position, long id) {        Artist artist = (Artist) getListAdapter().getItem(position);        Toast toast = Toast.makeText(getActivity(), artist.getName(), Toast.LENGTH_LONG);        toast.show();    }    private class ArtistAdapter extends ArrayAdapter<Artist> implements SectionIndexer {        private ArrayList<Artist> mArtists;        private Vector<String> mSections;        private HashMap<String, Integer> sectionMap;        public ArtistAdapter(ArrayList<Artist> artists) {            super(getActivity(), android.R.layout.simple_list_item_1, artists);            mArtists = artists;            mSections = new Vector<String>();            sectionMap = new HashMap<String, Integer>();            initializeSections();        }        private void initializeSections() {            for (int i = 0; i < mArtists.size(); i++) {                String title = mArtists.get(i).getName();                if (title.toLowerCase().startsWith("the ")) {                    title = title.substring(3);                }                String firstLetter = title.substring(0, 1).toUpperCase();                Integer myInt = Integer.getInteger(firstLetter);                if (myInt != null) {                    firstLetter = "123";                }                if (!sectionMap.containsKey(firstLetter)) {                    if (startsWithAlphaNum(firstLetter)) {                        sectionMap.put(firstLetter, i);                        mSections.add(firstLetter);                    }                }            }        }        @Override        public View getView(int position, View convertView, ViewGroup parent) {            //TODO if optimization is needed, use ViewHolder pattern            Artist artist = getItem(position);            if (convertView == null) {                convertView = getActivity().getLayoutInflater()                        .inflate(R.layout.list_item_artist, null);            }            TextView artistNameTextView = (TextView) convertView.findViewById(R.id.artistName_textView);            artistNameTextView.setText(artist.getName());            return convertView;        }        private boolean startsWithAlphaNum(String str) {            if (str == null) return false;            // TODO Clean up code with REGEX            return !(str.toLowerCase().startsWith("~")                    || str.toLowerCase().startsWith("!")                    || str.toLowerCase().startsWith("@")                    || str.toLowerCase().startsWith("#")                    || str.toLowerCase().startsWith("$")                    || str.toLowerCase().startsWith("%")                    || str.toLowerCase().startsWith("^")                    || str.toLowerCase().startsWith("&")                    || str.toLowerCase().startsWith("*")                    || str.toLowerCase().startsWith("(")                    || str.toLowerCase().startsWith(")")                    || str.toLowerCase().startsWith("_")                    || str.toLowerCase().startsWith("-")                    || str.toLowerCase().startsWith("+")                    || str.toLowerCase().startsWith("=")                    || str.toLowerCase().startsWith("`")                    || str.toLowerCase().startsWith("[")                    || str.toLowerCase().startsWith("]")                    || str.toLowerCase().startsWith("{")                    || str.toLowerCase().startsWith("}")                    || str.toLowerCase().startsWith("\\")                    || str.toLowerCase().startsWith("|")                    || str.toLowerCase().startsWith(":")                    || str.toLowerCase().startsWith(";")                    || str.toLowerCase().startsWith("'")                    || str.toLowerCase().startsWith("\"")                    || str.toLowerCase().startsWith("<")                    || str.toLowerCase().startsWith(">")                    || str.toLowerCase().startsWith(",")                    || str.toLowerCase().startsWith(".")                    || str.toLowerCase().startsWith("?")                    || str.toLowerCase().startsWith("/"));        }        @Override        public Object[] getSections() {            return mSections.toArray();        }        @Override        public int getPositionForSection(int section) {            return sectionMap.get(mSections.get(section));        }        @Override        public int getSectionForPosition(int position) {            Artist artist = getItem(position);            String title = artist.getName();            String firstLetter = title.substring(0, 1);            for (int i = 0; i < mSections.size(); i++) {                if (firstLetter.equals(mSections.get(i))) {                    return i;                }            }            return 0;        }    }}