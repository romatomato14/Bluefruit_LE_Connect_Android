package com.adafruit.bluefruit.le.connect.app;

/**
 * Created by Sausen on 2017-06-22.
 */

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.adafruit.bluefruit.le.connect.R;

import java.util.concurrent.TimeUnit;

public class MediaService extends AppCompatActivity
{
    private MediaPlayer mediaPlayer;
    private TextView songName, songDuration;
    private SeekBar seekBar;
    private double timeStart = 0, finalTime = 0;
    private int forwardTime = 2000, backwardTime = 2000;
    private Handler durationHandler = new Handler();
    public float leftVol;
    public float rightVol;

    PinIOActivity mPin = new PinIOActivity();
    boolean changeVol = mPin.isStretched;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mediaservice);
        songName = (TextView) findViewById(R.id.songName);
        songDuration = (TextView) findViewById(R.id.songDuration);
        mediaPlayer = MediaPlayer.create(this, R.raw.svenska);
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        songName.setText("svenska.mp3");
        seekBar.setMax((int) finalTime);
        seekBar.setClickable(false);
        System.out.println ("We are in media service on create now");
        mediaPlayer.setVolume(10,10);
    }
    private Runnable updateSeekBarTime = new Runnable() {
        public void run() {

            System.out.println ("We are running the music"+rightVol+"   "+leftVol);
            while(changeVol)
            {
                System.out.print("ITS STRETCHED");
                mediaPlayer.setVolume(rightVol,leftVol);
                rightVol+=10;
                leftVol+=10;
            }

            timeStart = mediaPlayer.getCurrentPosition();
            seekBar.setProgress((int) timeStart);
            double timeRemaining = finalTime - timeStart;
            songDuration.setText(String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining), TimeUnit.MILLISECONDS.toSeconds((long) timeRemaining) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes((long) timeRemaining))));
            durationHandler.postDelayed(this, 100);
        }
    };
    public void play(View view) {
        mediaPlayer.start();
        timeStart = mediaPlayer.getCurrentPosition();
        seekBar.setProgress((int) timeStart);
        durationHandler.postDelayed(updateSeekBarTime, 100);
    }
    public void pause(View view) {
        mediaPlayer.pause();
    }
    public void forward(View view) {
        if ((timeStart + forwardTime) <= finalTime) {
            timeStart = timeStart - backwardTime;
            mediaPlayer.seekTo((int) timeStart);
        }
    }
    public void backforward(View view) {
        //check if we can go back at backwardTime seconds after song starts
        if ((timeStart - backwardTime) > 0) {
            timeStart = timeStart - backwardTime;
            mediaPlayer.seekTo((int) timeStart);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
