package be.howest.nmct.android.nmctwallpaper.extension;

import android.content.Intent;
import android.net.Uri;
import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

/**
 * Created by Niels on 24/02/14.
 */
public class NMCTArtSource extends RemoteMuzeiArtSource {
    private static final String TAG = "NMCTWallpaper";
    private static final String SOURCE_NAME="NMCTWallpaper";

    private static final int ROTATE_TIME_MILLIS = 3 * 60 * 60 * 1000; // rotate every 3 hours

    private static String[] artworks = {"CITY","NMCT","CLOUD"};
    private static final String baseUri="http://www.nmct.be/wallpaper/";
    private enum Resolution{
        HIGH("1920x1200"),
        XHIGH("2280x1800");
        private String res;
        private Resolution(String res)
        {
            this.res=res;
        }

        @Override
        public String toString() {
            return res;
        }
    }

    public NMCTArtSource()
    {
        super(SOURCE_NAME);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    @Override
    protected void onTryUpdate(int reason) throws RetryException {
        String currentToken = (getCurrentArtwork() != null) ? getCurrentArtwork().getToken() : "-1";
        int curToken = Integer.parseInt(currentToken);
        if(curToken==artworks.length-1)
            curToken=0;
        else curToken++;

        int width = getWallpaperDesiredMinimumWidth();
        int height = getWallpaperDesiredMinimumHeight();


        Resolution res = Resolution.HIGH;

        if(width>1920&&height>1200)
            res=Resolution.XHIGH;


        Uri uri= Uri.parse(baseUri+artworks[curToken]+"-"+res+".png");

        publishArtwork(new Artwork.Builder()
        .title(artworks[curToken])
        .byline("Angelo Fallein")
        .imageUri(uri)
                .token(""+curToken)
                .viewIntent(
                        new Intent(Intent.ACTION_VIEW,
                                uri)
                )
                .build()
        );



        scheduleUpdate(System.currentTimeMillis() + ROTATE_TIME_MILLIS);

    }
}
