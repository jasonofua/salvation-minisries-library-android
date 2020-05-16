package salvation.com.salvationministries.Auth;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Picasso;

import mehdi.sakout.fancybuttons.FancyButton;
import salvation.com.salvationministries.App;
import salvation.com.salvationministries.Model.Upload;
import salvation.com.salvationministries.R;
import salvation.com.salvationministries.Utils.CircleTransform;
import salvation.com.salvationministries.landingpage.Home;

public class Signup extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    String country;
    String regionSelect = null;
    Button done;
    CountryCodePicker ccp;
    FirebaseAuth mAuth;
    LinearLayout linearLayout;
    EditText inputEmail, inputPassword;
    TextInputLayout inputLayoutEmail, inputLayoutPassword;
    SharedPreferences pref;
    FancyButton btnSign;
    ProgressDialog dialog;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    String currency;
    private ProgressBar mProgressBar;
    private ImageView circleImageView;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private StorageTask mUploadTask;
    private Spinner title;
    private Spinner region;
    private String email,password;
    private EditText firstName;
    private EditText lastName;
    private TextView textView,login;
    private VideoView videoBG;

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        dialog = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        linearLayout = findViewById(R.id.l1sign);
        inputEmail = findViewById(R.id.signupEmail);
        inputPassword = findViewById(R.id.signupPassword);


        inputLayoutEmail = findViewById(R.id.tilEmailSign);
        inputLayoutPassword = findViewById(R.id.tilPasswordSign);
        login = (TextView)findViewById(R.id.txtLogin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Signup.this,Login.class));
            }
        });

        pref = getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        btnSign = findViewById(R.id.btnSignupl);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("keyname1", inputEmail.getText().toString());
                startSignup();
            }
        });

    }

    private void startSignup() {

        PowerManager powerManager = (PowerManager) getSystemService(POWER_SERVICE);
        final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "MyApp::MyWakelockTag");
        wakeLock.acquire();

        validateEmail(inputEmail.getText().toString().trim());
        validatePassword(inputPassword.getText().toString().trim());
        isValidEmail(inputEmail.getText().toString().trim());

        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString().trim();



        if (!validateEmail(email)){
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (email.isEmpty() && password.isEmpty()){
            Toast.makeText(this, "Enter valid email", Toast.LENGTH_SHORT).show();
            return;
        }else {

            dialog.setMessage("Registering you..please wait");
            dialog.setCancelable(false);
            dialog.show();
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Signup.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
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
                                Toast.makeText(Signup.this, "You have successfully Signed up", Toast.LENGTH_SHORT).show();

                                sign_up2();

                            } else {
                                dialog.dismiss();

                            }
                        }
                    }).addOnFailureListener(Signup.this, new OnFailureListener() {
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
                    Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            });

        }


    }

    //validating email
    private boolean validateEmail(String email) {
        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    //validating password
    private boolean validatePassword(String password) {
        if (password.isEmpty() || password == null) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    // dialog for additional bio
    private void sign_up2() {
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        final Dialog dialog = new Dialog(Signup.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.sign_up2);
        done = dialog.findViewById(R.id.done);
        circleImageView = dialog.findViewById(R.id.circleimage);
        ccp = dialog.findViewById(R.id.ccp);
        mProgressBar = dialog.findViewById(R.id.progressBar2);
        title = dialog.findViewById(R.id.titlemain);
        //region = dialog.findViewById(R.id.region);
        firstName = dialog.findViewById(R.id.fist_name);
        lastName = dialog.findViewById(R.id.last_name);
        textView = dialog.findViewById(R.id.edit);
        dialog.show();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                uploadFile();
            }
        });

    }

    // image chooser
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(Signup.this).load(mImageUri).transform(new CircleTransform()).into(circleImageView);


        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile() {


        if (mImageUri != null && firstName != null && lastName != null) {
            done.setClickable(false);
            done.setText("processing...");

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));

            mUploadTask = fileReference.putFile(mImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mProgressBar.setProgress(0);
                                }
                            }, 500);

                            if (ccp.getSelectedCountryName().equalsIgnoreCase("nigeria")) {
                                currency = "naira";
                            } else {
                                currency = "dollars";
                            }

                            Toast.makeText(Signup.this, "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload();
                            upload.setFirstName(firstName.getText().toString().trim());
                            upload.setLastName(lastName.getText().toString().trim());
                            upload.setmImageUrl(taskSnapshot.getDownloadUrl().toString());
                            upload.setRegion(ccp.getSelectedCountryName());
                            upload.setCurrency(currency);
                            upload.setTitle(title.getSelectedItem().toString());
                            upload.setStatus("user");
                            upload.setWallet(0);

                            String uploadId = mAuth.getCurrentUser().getUid();
                            mDatabaseRef.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        done.setText("Done");
                                        done.setClickable(true);
                                        App app = new App();
                                        app.sendSuccessfullRegistrationEmail(email);
                                        dialog();
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Signup.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            done.setClickable(true);
                            done.setText("done");
                        }
                    });


        } else {
            Toast.makeText(this, "select image", Toast.LENGTH_SHORT).show();
        }
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }


    //dialog
    private void dialog() {
        final Dialog dialog = new Dialog(Signup.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.my_sheet_layout);
        Button button = dialog.findViewById(R.id.btnGOtIT);
        dialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openImagesActivity();

            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
           // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
          //  Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }


}



