package primedsoft.com.salvation.Activities;

import android.content.res.Configuration;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Utils.CircleTransform;

public class OfflineAudioPlayer extends AppCompatActivity {

    private FancyButton stop;

    // Used when update audio progress thread send message to progress bar handler.
    private static final int UPDATE_AUDIO_PROGRESS_BAR = 3;

    // Used to control audio (start, pause , stop etc).
    private MediaPlayer audioPlayer = null;

    // Show played audio progress.
    private ProgressBar playAudioProgress;

    private ImageView playBtn;

    private TextView audioStatus;
    // Used to distinguish log data.
    public static final String TAG_PLAY_AUDIO = "PLAY_AUDIO";

    // Wait update audio progress thread sent message, then update audio play progress.
    private Handler audioProgressHandler = null;

    // The thread that send message to audio progress handler to update progress every one second.
    private Thread updateAudioPalyerProgressThread = null;
    private Uri audioFileUri;
    private String filePath,imageUrl;

    // Record whether audio is playing or not.
    private boolean audioIsPlaying = false;
    private boolean clicked = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_audio_player);


        playAudioProgress = (ProgressBar) findViewById(R.id.progressBarAudio);
        playBtn = (ImageView) findViewById(R.id.startAudioPlay);
        audioStatus = (TextView) findViewById(R.id.audioState);
        filePath = getIntent().getStringExtra("path");
        imageUrl = getIntent().getStringExtra("image");
        stop = (FancyButton)findViewById(R.id.btnPauseAudio);




        audioFileUri = Uri.fromFile(new File(filePath));

        Picasso.with(OfflineAudioPlayer.this).load(imageUrl).transform(new CircleTransform()).into(playBtn);

        /* Initialize audio progress handler. */
        if (audioProgressHandler == null) {
            audioProgressHandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    if (msg.what == UPDATE_AUDIO_PROGRESS_BAR) {

                        if (audioPlayer != null) {
                            // Get current play time.
                            int currPlayPosition = audioPlayer.getCurrentPosition();

                            // Get total play time.
                            int totalTime = audioPlayer.getDuration();

                            // Calculate the percentage.
                            int currProgress = (currPlayPosition * 1000) / totalTime;

                            // Update progressbar.
                            playAudioProgress.setProgress(currProgress);
                        }
                    }
                }
            };
        }


        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clicked = true;


                    audioStatus.setText("Tap button to stop audio");
      //              playBtn.setImageResource(R.drawable.ic_microphone);
                    stopCurrentPlayAudio();
                    initAudioPlayer();
                    stop.setVisibility(View.VISIBLE);

                    audioPlayer.start();

                    audioIsPlaying = true;

                    // Display progressbar.
                    playAudioProgress.setVisibility(ProgressBar.VISIBLE);


                    if (updateAudioPalyerProgressThread == null) {

                        // Create the thread.
                        updateAudioPalyerProgressThread = new Thread() {
                            @Override
                            public void run() {
                                try {
                                    while (audioIsPlaying) {

                                        if (audioProgressHandler != null) {
                                            // Send update audio player progress message to main thread message queue.
                                            Message msg = new Message();
                                            msg.what = UPDATE_AUDIO_PROGRESS_BAR;
                                            audioProgressHandler.sendMessage(msg);

                                            Thread.sleep(1000);
                                        }
                                    }
                                } catch (InterruptedException ex) {
                                    Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
                                }
                            }
                        };
                        updateAudioPalyerProgressThread.start();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please specify an audio file to play.", Toast.LENGTH_LONG).show();
                    }



                }

        });


        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (audioIsPlaying) {
                    audioPlayer.stop();
                    audioPlayer.release();
                    audioPlayer = null;
                    updateAudioPalyerProgressThread = null;
                    playAudioProgress.setProgress(0);
                    playAudioProgress.setVisibility(ProgressBar.INVISIBLE);

                    audioIsPlaying = false;
                }
                audioStatus.setText("Tap to start audio");
               // playBtn.setImageResource(R.drawable.ic_micro);
            }
        });
    }

    /* Initialize media player. */
    private void initAudioPlayer() {
        try {
            if (audioPlayer == null) {
                audioPlayer = new MediaPlayer();


                Log.d(TAG_PLAY_AUDIO, filePath);

                if (filePath.toLowerCase().startsWith("http://")) {
                    // Web audio from a url is stream music.
                    audioPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                    // Play audio from a url.
                    audioPlayer.setDataSource(filePath);
                } else {
                    if (audioFileUri != null) {
                        // Play audio from selected local file.
                        audioPlayer.setDataSource(getApplicationContext(), audioFileUri);
                    }
                }
                audioPlayer.prepare();
            }
        } catch (IOException ex) {
            Log.e(TAG_PLAY_AUDIO, ex.getMessage(), ex);
        }
    }

    /* Stop current play audio before play another. */
    private void stopCurrentPlayAudio() {
        if (audioPlayer != null && audioPlayer.isPlaying()) {
            audioPlayer.stop();
            audioPlayer.release();
            audioPlayer = null;
        }
    }

    @Override
    protected void onDestroy() {
        if (audioPlayer != null) {
            if (audioPlayer.isPlaying()) {
                audioPlayer.stop();
            }

            audioPlayer.release();
            audioPlayer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            //   Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            //  Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }
}
