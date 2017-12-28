package ovenues.com.ovenue;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.iid.FirebaseInstanceId;

import ovenues.com.ovenue.utils.Const;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences sharepref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);


        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        //Log.d("tokengot",FirebaseInstanceId.getInstance().getToken());
        sharepref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        sharepref.edit().putString(Const.PREF_USER_TOKEN,refreshedToken).apply();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                if(sharepref.getString(Const.PREF_LOGINKEY, "no").equals("yes")){

                    startActivity(new Intent(SplashScreen.this,
                            MainNavigationScreen.class));
                    finish();

                }else if(!sharepref.getString(Const.PREF_LOGINKEY, "no").equals("yes")
                        && !sharepref.getString(Const.INTRO_DONE, "no").equals("yes")){

                    TaskStackBuilder.create(SplashScreen.this)
                            .addNextIntentWithParentStack(new Intent(SplashScreen.this, MainNavigationScreen.class))
                            .addNextIntent(new Intent(SplashScreen.this, IntroActivity.class))
                            .startActivities();
                    finish();

                }else if(!sharepref.getString(Const.PREF_LOGINKEY, "no").equals("yes")
                        && sharepref.getString(Const.INTRO_DONE, "no").equals("yes")){

                    startActivity(new Intent(SplashScreen.this,
                            Login.class));
                    finish();
                }

            }
        }, 3500);



    }
 }

