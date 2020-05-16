package salvation.com.salvationministries.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

import salvation.com.salvationministries.R;
import salvation.com.salvationministries.Welcome.WelcomeActivity;

public class AdminDashBoard extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_CODE = 0;
    private ImageView addMessage,allMessage,allUsers,settings,viewStats;

    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dash_board);

        auth = FirebaseAuth.getInstance();
        addMessage = findViewById(R.id.addMessage);
        allMessage = findViewById(R.id.viewAllMessage);
        allUsers = findViewById(R.id.allUsers);
        viewStats = findViewById(R.id.viewStats);

        viewStats.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoard.this,AllBoughtMessage.class));
            }
        });

        allUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashBoard.this,AllUsers.class));
            }
        });

        addMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAlert();
            }
        });

        allMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent messages = new Intent(AdminDashBoard.this,AdminAllMessage.class);
                startActivity(messages);
            }
        });

        settings = findViewById(R.id.settings);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String textToShow[] = {"Add A Admin","Logout"};
                AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashBoard.this);
                builder.setItems(textToShow, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        switch(which){
                            case 0:
                                //Do this and this
                                startActivity(new Intent(AdminDashBoard.this,AddAdmin.class));

                                break;
                            case 1:
                                //Do this and this
                                auth.signOut();
                                startActivity(new Intent(AdminDashBoard.this,WelcomeActivity.class));
                                break;
                            default: //For all other cases, do this        break;
                        }
                    }
                });
                builder.create();
                builder.show();
            }
        });

    }

    private void showAlert() {

        String textToShow[] = {"Add A Message","Add A News And Special Offer"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminDashBoard.this);
        builder.setItems(textToShow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which){
                    case 0:
                        //Do this and this
                        startActivity(new Intent(AdminDashBoard.this,AddMessage.class));

                        break;
                    case 1:
                        //Do this and this

                        startActivity(new Intent(AdminDashBoard.this,AddSpecialOffer.class));
                        break;
                    default: //For all other cases, do this        break;
                }
            }
        });
        builder.create();
        builder.show();
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
