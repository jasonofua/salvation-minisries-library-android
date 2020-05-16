package salvation.com.salvationministries.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import mehdi.sakout.fancybuttons.FancyButton;
import salvation.com.salvationministries.Model.Admin;
import salvation.com.salvationministries.R;

public class AddAdmin extends AppCompatActivity {

    private TextInputLayout tiladminemail,tiladminpassword,tiladminconfirmpassword;
    private EditText edtAdminEmail,edtAdminPassword,edtAdminConfirmPassword;
    private FancyButton btnAddAdmin;
    private FirebaseAuth auth;
    private DatabaseReference userRef;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin);

        dialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        edtAdminEmail= (EditText)findViewById(R.id.edtAdminEmail);
        edtAdminPassword= (EditText)findViewById(R.id.edtAdminPassword);
        edtAdminConfirmPassword= (EditText)findViewById(R.id.edtAdminPasswordConfirm);
        tiladminemail = (TextInputLayout)findViewById(R.id.tilAdminEmail);
        tiladminpassword = (TextInputLayout)findViewById(R.id.tilAdminPassword);
        tiladminconfirmpassword = (TextInputLayout)findViewById(R.id.tilConfirmAdminPassword);

        btnAddAdmin = (FancyButton)findViewById(R.id.btnAddAdmin);

        btnAddAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewAdmin();
            }
        });
    }

    private void AddNewAdmin() {
        dialog.setMessage("Adding a new Admin.. please wait");
        dialog.setCancelable(false);
        dialog.show();

        String email = edtAdminEmail.getText().toString().trim();
        String password = edtAdminPassword.getText().toString().trim();
        String confirmPassword = edtAdminConfirmPassword.getText().toString().trim();


        if (email.isEmpty()||email == null){
            tiladminemail.setError("You need an Email to create an admin");
        }

        if (password.isEmpty()||password == null){
            tiladminpassword.setError("You seriously need a password to create an admin");
        }
        if (confirmPassword.isEmpty()||confirmPassword == null){
            tiladminconfirmpassword.setError("Wait, we need that to continue");
        }

        if (!password.equalsIgnoreCase(confirmPassword)){
            tiladminconfirmpassword.setError("Password does not match");
        }

        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String uid = auth.getCurrentUser().getUid();

                            Admin admin = new Admin();
                            admin.setFirstName("Admin");
                            admin.setLastName("admin");
                            admin.setStatus("admin");

                            userRef.child(uid).setValue(admin)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                Toast.makeText(AddAdmin.this, "Admin successfully created", Toast.LENGTH_SHORT).show();
                                                auth.signInWithEmailAndPassword("info.fumstobil@gmail.com","C0mpl3x@1")
                                                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                                if (task.isSuccessful()){
                                                                    startActivity(new Intent(AddAdmin.this,AdminDashBoard.class));
                                                                }
                                                            }
                                                        });
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(AddAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(AddAdmin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
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
