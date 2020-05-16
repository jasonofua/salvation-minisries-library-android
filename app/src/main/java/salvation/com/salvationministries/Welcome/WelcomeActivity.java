
package salvation.com.salvationministries.Welcome;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.VideoView;

import com.google.firebase.auth.FirebaseAuth;

import mehdi.sakout.fancybuttons.FancyButton;
import salvation.com.salvationministries.Auth.Login;
import salvation.com.salvationministries.Auth.Signup;
import salvation.com.salvationministries.R;

public class WelcomeActivity extends AppCompatActivity {

    FancyButton btnSignup, btnLogin;
    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        auth = FirebaseAuth.getInstance();
//        FirebaseUser currentUser = auth.getCurrentUser();
//        String uid = currentUser.getUid();
//
//        if (!uid.isEmpty()||uid != null){
//            startActivity(new Intent(this, Home.class));
//        }else {
//            Toast.makeText(this, "Please signing or login to continue", Toast.LENGTH_SHORT).show();
//        }

        btnLogin = findViewById(R.id.btnlogin);
        btnSignup = findViewById(R.id.btnsignup);




        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               startActivity(new Intent(WelcomeActivity.this, Login.class));

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(WelcomeActivity.this, Signup.class));

            }
        });
    }
}
