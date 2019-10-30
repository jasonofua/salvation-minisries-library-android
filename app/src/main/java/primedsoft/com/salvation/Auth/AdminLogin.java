package primedsoft.com.salvation.Auth;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;

import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.R;

public class AdminLogin extends AppCompatActivity {

    private TextInputLayout tilEmail;
    private EditText resetEmail;
    private FancyButton btnResetPassword;
    private FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login);

        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(this);

        tilEmail = (TextInputLayout)findViewById(R.id.tilEmailLoginAdmin);
        resetEmail = (EditText)findViewById(R.id.edtResetPasswordEmail);
        btnResetPassword = (FancyButton)findViewById(R.id.btnResetPassword);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = resetEmail.getText().toString().trim();

                if (email.isEmpty()){
                    tilEmail.setError("Field can not be empty");
                    return;
                }

                dialog.setMessage("Resetting your password..please wait");
                dialog.show();

                auth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                if (task.isSuccessful()){
                                    Toast.makeText(AdminLogin.this, "Email sent, check your email for instruction to reset your password", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AdminLogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


    }
}
