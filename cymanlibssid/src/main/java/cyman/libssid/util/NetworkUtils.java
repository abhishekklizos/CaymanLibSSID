

package cyman.libssid.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


public final class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getCanonicalName();


    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        }
        return false;
    }

    public static int getNetworkConnectionType(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
            Log.d("network_connectivity", ": WIFI");
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            Log.d("network_connectivity", ": MOBILE_DATA");
        } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE_DUN) {
            Log.d("network_connectivity", ": MOBILE_DATA_DUN");
        }
        return activeNetwork.getType();

    }

}
