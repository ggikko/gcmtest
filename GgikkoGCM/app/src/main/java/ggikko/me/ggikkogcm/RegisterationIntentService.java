package ggikko.me.ggikkogcm;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

/**
 * Created by ggikko on 2015. 12. 2..
 */
public class RegisterationIntentService extends IntentService {

    private static final String TAG = "RegisterationIntentService";

    public RegisterationIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        // GCM Instance ID의 토큰을 가져오는 작업이 시작되면 LocalBoardcast로 ProgressBar가 동작하도록 한다.
        LocalBroadcastManager.getInstance(this).sendBroadcast(new Intent(GcmPreferences.GENERATING));



    }
}
