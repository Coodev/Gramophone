package com.orobator.android.gramophone.view.adapters;import android.app.DialogFragment;import android.app.Fragment;import android.app.FragmentManager;import android.content.Context;import android.content.res.Resources;import android.database.Cursor;import android.graphics.Bitmap;import android.graphics.BitmapFactory;import android.graphics.drawable.BitmapDrawable;import android.graphics.drawable.Drawable;import android.os.AsyncTask;import android.os.Bundle;import android.util.Log;import android.util.LruCache;import android.util.TypedValue;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.CursorAdapter;import android.widget.ImageButton;import android.widget.ImageView;import android.widget.SectionIndexer;import android.widget.TextView;import android.widget.Toast;import com.fortysevendeg.android.swipelistview.SwipeListView;import com.orobator.android.gramophone.R;import com.orobator.android.gramophone.model.Album;import com.orobator.android.gramophone.model.Song;import com.orobator.android.gramophone.model.SongDatabaseHelper;import com.orobator.android.gramophone.view.fragments.AlbumSongsFragment;import com.orobator.android.gramophone.view.fragments.ArtistsAlbumsFragment;import com.orobator.android.gramophone.view.fragments.MoreSongOptionsDialogFragment;import java.lang.ref.WeakReference;import java.util.ArrayList;import java.util.HashMap;import java.util.Vector;public class SongCursorAdapter extends CursorAdapter implements SectionIndexer {    private static final String TAG = "SongCursorAdapter";    private static final int THUMBNAIL_WIDTH = 80;    private static final int THUMBNAIL_HEIGHT = 80;    private Vector<String> mSections;    private HashMap<String, Integer> sectionMap;    private SongDatabaseHelper.SongCursor mSongCursor;    private ArrayList<Song> mSongs;    private Context mContext;    private Fragment mFragment;    private int callCount = 0;    private Bitmap albumArtPlaceHolder;    private LruCache<String, Bitmap> mMemoryCache;// TODO Use a diskCache if necessary http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html    public SongCursorAdapter(Context context, SongDatabaseHelper.SongCursor cursor, Fragment fragment) {        super(context, cursor, 0);        mContext = context;        mSongCursor = cursor;        mSections = new Vector<String>();        sectionMap = new HashMap<String, Integer>();        mSongs = new ArrayList<Song>();        mFragment = fragment;        initializeSections();        // First decode with inJustDecodeBounds=true to check dimensions        final BitmapFactory.Options options = new BitmapFactory.Options();        options.inJustDecodeBounds = true;        BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher, options);        // Calculate inSampleSize        options.inSampleSize = 1;        options.inJustDecodeBounds = false;        albumArtPlaceHolder = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_launcher, options);        // Get max available VM memory, exceeding this amount will throw an        // OutOfMemory exception. Stored in kilobytes as LruCache takes an        // int in its constructor.        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);        // Use 1/8th of the available memory for this memory cache.        final int cacheSize = maxMemory / 8;        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {            @Override            protected int sizeOf(String key, Bitmap bitmap) {                // The cache size will be measured in kilobytes rather than                // number of items.                return bitmap.getByteCount() / 1024;            }        };    }    /**     * Creates the sections for the SectionIndexer to use     */    private void initializeSections() {        mSongCursor.moveToFirst();        while (!mSongCursor.isAfterLast()) {            mSongs.add(mSongCursor.getSong());            String title = mSongCursor.getSong().getTitle();            String firstLetter = title.substring(0, 1).toUpperCase();            Integer myInt = Integer.getInteger(firstLetter);            if (myInt != null) {                firstLetter = "123";            }            if (!sectionMap.containsKey(firstLetter)) {                if (startsWithAlphaNum(firstLetter)) {                    sectionMap.put(firstLetter, mSongCursor.getPosition());                    mSections.add(firstLetter);                }            }            mSongCursor.moveToNext();        }    }    private boolean startsWithAlphaNum(String str) {        if (str == null) return false;        // TODO Clean up code with REGEX        return !(str.toLowerCase().startsWith("~")                || str.toLowerCase().startsWith("!")                || str.toLowerCase().startsWith("@")                || str.toLowerCase().startsWith("#")                || str.toLowerCase().startsWith("$")                || str.toLowerCase().startsWith("%")                || str.toLowerCase().startsWith("^")                || str.toLowerCase().startsWith("&")                || str.toLowerCase().startsWith("*")                || str.toLowerCase().startsWith("(")                || str.toLowerCase().startsWith(")")                || str.toLowerCase().startsWith("_")                || str.toLowerCase().startsWith("-")                || str.toLowerCase().startsWith("+")                || str.toLowerCase().startsWith("=")                || str.toLowerCase().startsWith("`")                || str.toLowerCase().startsWith("[")                || str.toLowerCase().startsWith("]")                || str.toLowerCase().startsWith("{")                || str.toLowerCase().startsWith("}")                || str.toLowerCase().startsWith("\\")                || str.toLowerCase().startsWith("|")                || str.toLowerCase().startsWith(":")                || str.toLowerCase().startsWith(";")                || str.toLowerCase().startsWith("'")                || str.toLowerCase().startsWith("\"")                || str.toLowerCase().startsWith("<")                || str.toLowerCase().startsWith(">")                || str.toLowerCase().startsWith(",")                || str.toLowerCase().startsWith(".")                || str.toLowerCase().startsWith("?")                || str.toLowerCase().startsWith("/"));    }    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {        if (getBitmapFromMemCache(key) == null) {            mMemoryCache.put(key, bitmap);        }    }    public Bitmap getBitmapFromMemCache(String key) {        return mMemoryCache.get(key);    }    @Override    public Object getItem(int position) {        return mSongs.get(position);    }    @Override    public View getView(int position, View convertView, ViewGroup parent) {        final Song song = mSongs.get(position);        ViewHolder holder;        if (convertView == null) {            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);            convertView = inflater.inflate(R.layout.list_item_song, null);            holder = new ViewHolder();            holder.titleTextView = (TextView) convertView.findViewById(R.id.songTitle_TextView);            holder.artistTextView = (TextView) convertView.findViewById(R.id.songArtist_TextView);            holder.albumTextView = (TextView) convertView.findViewById(R.id.songAlbum_TextView);            holder.albumArtImageView = (ImageView) convertView.findViewById(R.id.songListItemAlbumArt_imageView);            holder.playNextImageButton = (ImageButton) convertView.findViewById(R.id.playNext_imageButton);            holder.addToQueueImageButton = (ImageButton) convertView.findViewById(R.id.addToQueue_imageButton);            holder.artistImageButton = (ImageButton) convertView.findViewById(R.id.goToArtist_imageButon);            holder.albumImageButton = (ImageButton) convertView.findViewById(R.id.goToAlbum_imageButton);            holder.moreImageButton = (ImageButton) convertView.findViewById(R.id.viewMore_imageButton);            convertView.setTag(holder);        } else {            holder = (ViewHolder) convertView.getTag();        }        ((SwipeListView) parent).recycle(convertView, position);        holder.titleTextView.setText(song.getTitle());        holder.artistTextView.setText(song.getArtist());        holder.albumTextView.setText(song.getAlbum());        if (song.hasArtwork()) {            Bitmap albumArtBitmap = getBitmapFromMemCache(song.toString());            if (albumArtBitmap != null) {                holder.albumArtImageView.setImageBitmap(albumArtBitmap);            } else {                if (cancelPotentialWork(song, holder.albumArtImageView)) {                    BitmapWorkerTask task = new BitmapWorkerTask(holder.albumArtImageView);                    AsyncDrawable asyncDrawable = new AsyncDrawable(mContext.getResources(), albumArtPlaceHolder, task);                    holder.albumArtImageView.setImageDrawable(asyncDrawable);                    task.execute(song);                }            }        } else {            holder.albumArtImageView.setImageDrawable(new BitmapDrawable(mContext.getResources(), albumArtPlaceHolder));        }        holder.playNextImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(mContext, "Play next " + song.getTitle() + " - " + song.getArtist(), Toast.LENGTH_SHORT);                toast.show();            }        });        holder.addToQueueImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(mContext, "Add to Queue " + song.getTitle() + " - " + song.getArtist(), Toast.LENGTH_SHORT);                toast.show();            }        });        holder.artistImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                String artist = song.getArtist();                Bundle args = new Bundle();                args.putString(Album.KEY_ALBUM_ARTIST, artist);                ArtistsAlbumsFragment fragment = new ArtistsAlbumsFragment();                fragment.setArguments(args);                FragmentManager fm = mFragment.getFragmentManager();                fm.beginTransaction()                        .replace(R.id.content_frame, fragment)                        .addToBackStack(artist)                        .commit();            }        });        holder.albumImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Album album = new Album(song.getAlbum(), song.getAlbumArtist());                Bundle args = new Bundle();                args.putString(Album.KEY_ALBUM_NAME, album.getAlbumName());                args.putString(Album.KEY_ALBUM_ARTIST, album.getAlbumArtist());                AlbumSongsFragment albumSongsFragment = new AlbumSongsFragment();                albumSongsFragment.setArguments(args);                FragmentManager fm = mFragment.getActivity().getFragmentManager();                fm.beginTransaction()                        .replace(R.id.content_frame, albumSongsFragment)                        .addToBackStack(album.getAlbumName())                        .commit();            }        });        holder.moreImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Bundle args = new Bundle();                args.putSerializable(Song.KEY_SONG, song);                DialogFragment fragment = new MoreSongOptionsDialogFragment();                fragment.setArguments(args);                fragment.show(mFragment.getFragmentManager(), "More options");            }        });        return convertView;    }    public boolean cancelPotentialWork(Song song, ImageView imageView) {        final BitmapWorkerTask bitmapWorkerTask = getBitmapWorkerTask(imageView);        if (bitmapWorkerTask != null) {            final Song bitmapSong = bitmapWorkerTask.mSong;            if (bitmapSong != song) {                // Cancel previous task                bitmapWorkerTask.cancel(true);            } else {                // The same work is already in progress                return false;            }        }        // No task associated with the ImageView, or an existing task was cancelled        return true;    }    BitmapWorkerTask getBitmapWorkerTask(ImageView imageView) {        if (imageView != null) {            final Drawable drawable = imageView.getDrawable();            if (drawable instanceof AsyncDrawable) {                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;                return asyncDrawable.getBitmapWorkerTask();            }        }        return null;    }    @Override    public View newView(Context context, Cursor cursor, ViewGroup parent) {        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        View view = inflater.inflate(R.layout.list_item_song, parent, false);        return view;    }    @Override    public void bindView(View view, Context context, Cursor cursor) {        Log.d(TAG, "bindView() has been called " + callCount++ + " times");        final Song song = mSongCursor.getSong();        ViewHolder holder = new ViewHolder();        holder.titleTextView = (TextView) view.findViewById(R.id.songTitle_TextView);        holder.artistTextView = (TextView) view.findViewById(R.id.songArtist_TextView);        holder.playNextImageButton = (ImageButton) view.findViewById(R.id.playNext_imageButton);        holder.addToQueueImageButton = (ImageButton) view.findViewById(R.id.addToQueue_imageButton);        holder.artistImageButton = (ImageButton) view.findViewById(R.id.goToArtist_imageButon);        holder.albumImageButton = (ImageButton) view.findViewById(R.id.goToAlbum_imageButton);        holder.moreImageButton = (ImageButton) view.findViewById(R.id.viewMore_imageButton);        holder.albumArtImageView = (ImageView) view.findViewById(R.id.songListItemAlbumArt_imageView);        view.setTag(holder);        holder.titleTextView.setText(song.getTitle());        holder.artistTextView.setText(song.getArtist());        if (song.hasArtwork()) {            Bitmap albumArt = decodeSampledBitmapFromSong(song);            holder.albumArtImageView.setImageDrawable(new BitmapDrawable(mContext.getResources(), albumArt));        }        holder.playNextImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(mContext, "Play next " + song.getTitle() + " - " + song.getArtist(), Toast.LENGTH_SHORT);                toast.show();            }        });        holder.addToQueueImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(mContext, "Add to Queue " + song.getTitle() + " - " + song.getArtist(), Toast.LENGTH_SHORT);                toast.show();            }        });        holder.artistImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                String artist = song.getArtist();                Bundle args = new Bundle();                args.putString(Album.KEY_ALBUM_ARTIST, artist);                ArtistsAlbumsFragment fragment = new ArtistsAlbumsFragment();                fragment.setArguments(args);                FragmentManager fm = mFragment.getFragmentManager();                fm.beginTransaction()                        .replace(R.id.content_frame, fragment)                        .addToBackStack(artist)                        .commit();            }        });        holder.albumImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Album album = new Album(song.getAlbum(), song.getAlbumArtist());                Bundle args = new Bundle();                args.putString(Album.KEY_ALBUM_NAME, album.getAlbumName());                args.putString(Album.KEY_ALBUM_ARTIST, album.getAlbumArtist());                AlbumSongsFragment albumSongsFragment = new AlbumSongsFragment();                albumSongsFragment.setArguments(args);                FragmentManager fm = mFragment.getActivity().getFragmentManager();                fm.beginTransaction()                        .replace(R.id.content_frame, albumSongsFragment)                        .addToBackStack(album.getAlbumName())                        .commit();            }        });        holder.moreImageButton.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                Toast toast = Toast.makeText(mContext, "MOAR " + song.getTitle() + " - " + song.getArtist(), Toast.LENGTH_SHORT);                toast.show();            }        });    }    private Bitmap decodeSampledBitmapFromSong(Song song) {        // First decode with inJustDecodeBounds=true to check dimensions        final BitmapFactory.Options options = new BitmapFactory.Options();        options.inJustDecodeBounds = true;        byte[] albumBytes = song.getArtworkByteArray();        BitmapFactory.decodeByteArray(albumBytes, 0, albumBytes.length, options);        // Calculate inSampleSize        options.inSampleSize = calculateInSampleSize(options, dipToPixels(THUMBNAIL_WIDTH), dipToPixels(THUMBNAIL_HEIGHT));        options.inJustDecodeBounds = false;        return BitmapFactory.decodeByteArray(albumBytes, 0, albumBytes.length, options);    }    private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {        // Raw height and width of image        final int height = options.outHeight;        final int width = options.outWidth;        int inSampleSize = 1;        if (reqHeight == 0 || reqWidth == 0) return inSampleSize;        if (height > reqHeight || width > reqWidth) {            // Calculate ratios of height and width to requested height and width            final int heightRatio = Math.round((float) height / (float) reqHeight);            final int widthRatio = Math.round((float) width / (float) reqWidth);            // Choose the smallest ratio as inSampleSize value. This will guarantee            // a final image with both dimensions larger than or equal to the            // requested height and width            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;        }        return inSampleSize;    }    private int dipToPixels(int dip) {        Resources r = mContext.getResources();        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, (float) dip, r.getDisplayMetrics());    }    public int getSize() {        return mSongCursor.getCount();    }    @Override    public Object[] getSections() {        return mSections.toArray();    }    @Override    public int getPositionForSection(int sectionIndex) {        return sectionMap.get(mSections.get(sectionIndex));    }    @Override    public int getSectionForPosition(int position) {        Song song = mSongs.get(position);        String title = song.getTitle();        String firstLetter = title.substring(0, 1).toUpperCase();        for (int i = 0; i < mSections.size(); i++) {            if (firstLetter.equals(mSections.get(i))) {                return i;            }        }        return 0;    }    /**     * ViewHolder is a class to cache calls to findViewById() for performance     * increases     */    static class ViewHolder {        TextView titleTextView;        TextView artistTextView;        TextView albumTextView;        ImageView albumArtImageView;        ImageButton playNextImageButton;        ImageButton addToQueueImageButton;        ImageButton albumImageButton;        ImageButton artistImageButton;        ImageButton moreImageButton;    }    static class AsyncDrawable extends BitmapDrawable {        private final WeakReference<BitmapWorkerTask> bitmapWorkerTaskReference;        public AsyncDrawable(Resources res, Bitmap bitmap,                             BitmapWorkerTask bitmapWorkerTask) {            super(res, bitmap);            bitmapWorkerTaskReference =                    new WeakReference<BitmapWorkerTask>(bitmapWorkerTask);        }        public BitmapWorkerTask getBitmapWorkerTask() {            return bitmapWorkerTaskReference.get();        }    }    class BitmapWorkerTask extends AsyncTask<Song, Void, Bitmap> {        // Stored as a weak reference so framework can garbage collect ImageView        // when needed        private final WeakReference<ImageView> mImageViewReference;        private Song mSong;        public BitmapWorkerTask(ImageView imageView) {            mImageViewReference = new WeakReference<>(imageView);        }        // Decode image in background        @Override        protected Bitmap doInBackground(Song... params) {            mSong = params[0];            ImageView thumbnail = mImageViewReference.get();            Bitmap bitmap = decodeSampledBitmapFromSong(mSong);            addBitmapToMemoryCache(mSong.toString(), bitmap);            return bitmap;        }        // Once complete, see if ImageView is still around and set Bitmap        @Override        protected void onPostExecute(Bitmap bitmap) {            if (isCancelled()) {                bitmap = null;            }            if (mImageViewReference != null && bitmap != null) {                final ImageView imageView = mImageViewReference.get();                final BitmapWorkerTask bitmapWorkerTask =                        getBitmapWorkerTask(imageView);                if (this == bitmapWorkerTask && imageView != null) {                    imageView.setImageBitmap(bitmap);                }            }        }    }}