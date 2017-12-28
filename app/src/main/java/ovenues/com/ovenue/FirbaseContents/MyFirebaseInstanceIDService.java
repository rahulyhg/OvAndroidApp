package ovenues.com.ovenue.FirbaseContents;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import java.util.ArrayList;
import java.util.List;

import ovenues.com.ovenue.utils.Const;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    String res;

    SharedPreferences sharepref;
    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d("tokengot",FirebaseInstanceId.getInstance().getToken());
         sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
          sharepref.edit().putString(Const.PREF_USER_TOKEN,refreshedToken).apply();

        // TODO: Implement this method to send any registration to your app's servers.
        // sendRegistrationToServer(refreshedToken);
        //token_id=refreshedToken;

    }
    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     *
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */



}