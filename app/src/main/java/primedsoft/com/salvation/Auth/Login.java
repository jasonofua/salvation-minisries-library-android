package primedsoft.com.salvation.Auth;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.Activities.AdminDashBoard;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.landingpage.Home;

public class Login extends AppCompatActivity {

    public static final String MyPREFERENCES = "MyPrefs";
    public static final String STATUS = "status";
    FirebaseAuth mAuth;
    EditText inputEmail, inputPassword;
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    FancyButton btnLogin;
    LinearLayout linearLayout;
    ProgressDialog dialog;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    SharedPreferences sharedpreferences;
    private DatabaseReference userRef;
    private VideoView videoBG;
    private TextView signup;
    private TextView forget;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dialog = new ProgressDialog(this);


        mAuth = FirebaseAuth.getInstance();
        linearLayout = findViewById(R.id.l1login);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);


        inputEmail = findViewById(R.id.loginEmail);
        inputPassword = findViewById(R.id.loginPassword);
        signup = (TextView)findViewById(R.id.txtSignup);
        forget = (TextView)findViewById(R.id.txtForgotPass);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Signup.class));
            }
        });

        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,AdminLogin.class));
            }
        });

        inputLayoutEmail = findViewById(R.id.tilEmailLogin);
        inputLayoutPassword = findViewById(R.id.tilPasswordLogin);

        btnLogin = findViewById(R.id.btnLoginl);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLogin();
            }
        });


    }

    private void startLogin() {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

        validateEmail(inputEmail.getText().toString().trim());
        validatePassword(inputPassword.getText().toString().trim());
        isValidEmail(inputEmail.getText().toString().trim());


        if (!validateEmail(inputEmail.getText().toString().trim())){
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }


        if (inputEmail.getText().toString().trim().isEmpty() && inputPassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Field must be completed", Toast.LENGTH_SHORT).show();
            return;
        }else {

            dialog.setMessage("Login in....please wait");
            dialog.setCancelable(false);
            dialog.show();

            mAuth.signInWithEmailAndPassword(inputEmail.getText().toString().trim(), inputPassword.getText().toString().trim())
                    .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                dialog.dismiss();
                                String uid = mAuth.getCurrentUser().getUid();

                                userRef.child(uid)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.exists()) {
                                                    if (wakeLock.isHeld()) {
                                                        wakeLock.release();
                                                        try {
                                                            wakeLock.release();
                                                        } catch (Throwable th) {
                                                            // ignoring this exception, probably wakeLock was already released
                                                        }
                                                    } else {
                                                        // should never happen during normal workflow

                                                    }
                                                    String status = dataSnapshot.child("status").getValue().toString();

                                                    if (status.equalsIgnoreCase("admin")) {
                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString(STATUS, "admin");
                                                        editor.apply();

                                                        startActivity(new Intent(Login.this, AdminDashBoard.class));
                                                        Toast.makeText(Login.this, "You have successfully Logged in as an admin", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    } else {

                                                        SharedPreferences.Editor editor = sharedpreferences.edit();
                                                        editor.putString(STATUS, "user");
                                                        editor.apply();
                                                        startActivity(new Intent(Login.this, Home.class));
                                                        Toast.makeText(Login.this, "You have successfully Logged in", Toast.LENGTH_SHORT).show();
                                                        finish();
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {
                                                if (wakeLock.isHeld()) {
                                                    wakeLock.release();
                                                    try {
                                                        wakeLock.release();
                                                    } catch (Throwable th) {
                                                        // ignoring this exception, probably wakeLock was already released
                                                    }
                                                } else {
                                                    // should never happen during normal workflow

                                                }
                                            }
                                        });


                            } else {
                                if (wakeLock.isHeld()) {
                                    wakeLock.release();
                                    try {
                                        wakeLock.release();
                                    } catch (Throwable th) {
                                        // ignoring this exception, probably wakeLock was already released
                                    }
                                } else {
                                    // should never happen during normal workflow

                                }
                                dialog.dismiss();

                            }
                        }
                    }).addOnFailureListener(Login.this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    if (wakeLock.isHeld()) {
                        wakeLock.release();
                        try {
                            wakeLock.release();
                        } catch (Throwable th) {
                            // ignoring this exception, probably wakeLock was already released
                        }
                    } else {
                        // should never happen during normal workflow

                    }
                    dialog.dismiss();
                    Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }

    }

    private boolean validateEmail(String email) {
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword(String password) {
        if (password.isEmpty() || password == null) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
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
