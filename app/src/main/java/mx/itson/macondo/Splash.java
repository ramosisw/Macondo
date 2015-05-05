package mx.itson.macondo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import mx.itson.macondo.vistas.MainActivity;


public class Splash extends Activity {
    private static final int SPLASH_DURATION = 3000; // 2 seconds
    ImageView imgTitle, imgShadowTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        //overridePendingTransition(R.anim.splash_activity_fadeout, R.anim.splash_activity_fadein);
        Handler handler = new Handler();
        StartAnimations();

        // run a thread after 2 seconds to start the home screen
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // make sure we close the splash screen so the user won't come back when it presses back key
                finish();
                // start the home screen if the back button wasn't pressed already
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
            }
        }, SPLASH_DURATION); // time in milliseconds (1 second = 1000 milliseconds) until the run() method will be called

    }

    private void StartAnimations() {
        imgTitle = (ImageView) findViewById(R.id.imgTitle);
        imgShadowTitle = (ImageView) findViewById(R.id.imgShadowTitle);
        Animation titleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_title_anim);
        Animation shadowTitleAnim = AnimationUtils.loadAnimation(this, R.anim.splash_shadow_title_anim);
        imgTitle.clearAnimation();
        imgShadowTitle.clearAnimation();
        imgTitle.startAnimation(titleAnim);
        imgShadowTitle.startAnimation(shadowTitleAnim);
    }


}
