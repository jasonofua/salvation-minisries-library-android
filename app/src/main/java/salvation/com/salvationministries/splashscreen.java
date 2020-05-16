package salvation.com.salvationministries;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import mehdi.sakout.fancybuttons.FancyButton;
import salvation.com.salvationministries.Activities.AdminDashBoard;
import salvation.com.salvationministries.Welcome.WelcomeActivity;
import salvation.com.salvationministries.landingpage.Home;

public class splashscreen extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String STATUS = "status";
    LinearLayout l1;
    FancyButton btnsub;
    Animation uptodown;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    FirebaseAuth auth;
    SharedPreferences sharedpreferences;
    private VideoView videoBG;
    private String status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        auth = FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        status = sharedpreferences.getString(STATUS, "");
        FirebaseUser currentUser = auth.getCurrentUser();
        if (auth.getCurrentUser() != null) {
            if (!status.isEmpty() || status != null) {
                if (status.equalsIgnoreCase("admin")) {
                    startActivity(new Intent(this, AdminDashBoard.class));
                } else {
                    startActivity(new Intent(this, Home.class));
                }
            } else {
                Toast.makeText(this, "Please click the button to continue", Toast.LENGTH_SHORT).show();
            }

        } else {

            Toast.makeText(this, "Please click the button to continue", Toast.LENGTH_SHORT).show();
        }

        btnsub = findViewById(R.id.Btncontinued);
        l1 = findViewById(R.id.l1);

        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown);
        l1.setAnimation(uptodown);

        uptodown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                btnsub.setVisibility(View.VISIBLE);

                btnsub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(splashscreen.this, WelcomeActivity.class));
                        finish();
                    }
                });

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


}
