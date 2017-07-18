package com.example.yu_enpit.myslideshow;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.BounceInterpolator;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ViewSwitcher;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static android.R.id;

public class MainActivity extends AppCompatActivity {

    ImageSwitcher mimageSwitcher;
    int[] mImageResources = {R.drawable.slide00,R.drawable.slide01,R.drawable.slide02,
            R.drawable.slide03, R.drawable.slide04, R.drawable.slide05, R.drawable.slide06,
            R.drawable.slide07, R.drawable.slide08, R.drawable.slide09};
    int mPosition = 0;

    boolean mIsSlieshow = false;
    MediaPlayer mMadiaPlayler;
    Timer mTimer = new Timer();
    TimerTask mTimerTask = new MainTimerTask();
    Handler mHandler = new Handler() {
        @Override
        public void publish(LogRecord logRecord) {

        }

        @Override
        public void flush() {

        }

        @Override
        public void close() throws SecurityException {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mimageSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitcher);
        mimageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView = new ImageView(getApplicationContext());
                return imageView;
            }
        });
        mimageSwitcher.setImageResource(mImageResources[0]);
        mTimer.schedule(mTimerTask, 0, 5000);
        mMadiaPlayler = MediaPlayer.create(this, R.raw.getdown);
        mMadiaPlayler.setLooping(true);
    }

    public void onAnimationButtonTapped(final View view) {
        float y = view.getY() + 100;
        view.animate().setDuration(1000).setInterpolator(new BounceInterpolator()).y(y);
        ViewPropertyAnimator animator = view.animate();
        animator.setDuration(3000);
        animator.rotation(360.0f * 5.0f);
    }

    private void movePosition(int move) {

        mPosition = mPosition + move;
        if (mPosition >= mImageResources.length) {
            mPosition = 0;
        } else if (mPosition < 0) {
            mPosition = mImageResources.length - 1;
        }
        mimageSwitcher.setImageResource(mImageResources[mPosition]);
    }

    public void onPrevButtonTapped(View view) {
        mimageSwitcher.setInAnimation(this, android.R.anim.fade_in);
        mimageSwitcher.setOutAnimation(this, android.R.anim.fade_out);
        movePosition(-1);
        findViewById(R.id.imageView).animate().setDuration(1000).alpha(0.0f);
    }

    public void onNextButtonTapped(View view) {
        mimageSwitcher.setInAnimation(this, android.R.anim.slide_in_left);
        mimageSwitcher.setOutAnimation(this, android.R.anim.slide_out_right);
        movePosition(1);
        findViewById(R.id.imageView).animate().setDuration(1000).alpha(0.0f);
    }

    public void onSlideshowButtonTapped(View view) {
        mIsSlieshow = !mIsSlieshow;

        if (mIsSlieshow) {
            mMadiaPlayler.start();
        } else {
            mMadiaPlayler.pause();
            mMadiaPlayler.seekTo(0);
        }
    }

    public class MainTimerTask extends TimerTask {
        @Override
        public void run() {
            if (mIsSlieshow) {
                mimageSwitcher.post(new Runnable() {
                    @Override
                    public void run() {
                        movePosition(1);
                    }
                });
            }
        }
    }




}
