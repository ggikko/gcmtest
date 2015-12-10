package ggikko.me.ggikkogcm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity {

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "MainActivity";

    private Button mGcm_Button;
    private ProgressBar mGcm_Progressbar;
    private BroadcastReceiver mGcm_BroadcastReceiver;
    private TextView mGcm_textview;

    //디바이스 토큰을 가져오고 service실행
    public void getInstanceIdToken() {
        if (checkPlayServices()) {
            // Start IntentService to register this application with GCM.
            Intent intent = new Intent(this, RegisterationIntentService.class);
            startService(intent);
        }
    }


    //구글 서비스 환경 체크
    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    //BroadcastReceiver 등
    private void setBroadcastReceiver() {
        mGcm_BroadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                //준비 중
                if(action.equals(GcmPreferences.READY)){
                    mGcm_Progressbar.setVisibility(ProgressBar.GONE);
                    mGcm_textview.setVisibility(View.GONE);
                }

                //만드는 중
                if(action.equals(GcmPreferences.GENERATING)){
                    mGcm_Progressbar.setVisibility(View.VISIBLE);
                    mGcm_textview.setVisibility(View.VISIBLE);
                    mGcm_textview.setText(getString(R.string.generating));
                }

                //완료 되었을 때
                if(action.equals(GcmPreferences.COMPLETE)){
                    mGcm_Progressbar.setVisibility(View.GONE);
                    mGcm_Button.setText(getString(R.string.complete));
                    String token = intent.getStringExtra("token");
                    mGcm_textview.setText(token);
                }

            }
        };

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setBroadcastReceiver();

        mGcm_textview = (TextView) findViewById(R.id.gcm_Textview);
        mGcm_textview.setVisibility(View.GONE);

        mGcm_Progressbar = (ProgressBar) findViewById(R.id.gcm_Progressbar);
        mGcm_Progressbar.setVisibility(View.GONE);

        mGcm_Button = (Button) findViewById(R.id.gcm_Button);
        mGcm_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInstanceIdToken();
            }
        });
    }

    //Resume 상태 시 LocalBroadCast에 등록
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mGcm_BroadcastReceiver,
                new IntentFilter(GcmPreferences.READY));
        LocalBroadcastManager.getInstance(this).registerReceiver(mGcm_BroadcastReceiver,
                new IntentFilter(GcmPreferences.GENERATING));
        LocalBroadcastManager.getInstance(this).registerReceiver(mGcm_BroadcastReceiver,
                new IntentFilter(GcmPreferences.COMPLETE));

    }


    //Pause상태 시 LocalBroadCast삭제
    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mGcm_BroadcastReceiver);
        super.onPause();
    }
}
