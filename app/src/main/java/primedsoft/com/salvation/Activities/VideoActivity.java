package primedsoft.com.salvation.Activities;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;

import primedsoft.com.salvation.R;

public class VideoActivity extends AppCompatActivity {

    VideoView videoVIew ;
    Uri videoFileUri;
    private String path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        path = getIntent().getStringExtra("path");

        videoVIew = (VideoView)findViewById(R.id.videoViewOffline);
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(videoVIew);
        videoVIew.setMediaController(mediaController);

        videoFileUri = Uri.fromFile(new File(path));

       videoVIew.setVideoURI(videoFileUri);


        videoVIew.setOnPreparedListener(new MediaPlayer.OnPreparedListener()  {
                                                    @Override
                                                    public void onPrepared(MediaPlayer mp) {
                                                        Toast.makeText(VideoActivity.this, "Starting video", Toast.LENGTH_SHORT).show();
                                                    }
                                                });

        videoVIew.start();

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
