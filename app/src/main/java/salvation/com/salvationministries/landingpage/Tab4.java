package salvation.com.salvationministries.landingpage;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.hbb20.CountryCodePicker;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import mehdi.sakout.fancybuttons.FancyButton;
import salvation.com.salvationministries.Activities.AddDebitCard;
import salvation.com.salvationministries.App;
import salvation.com.salvationministries.Model.Upload;
import salvation.com.salvationministries.R;
import salvation.com.salvationministries.Utils.CircleTransform;
import salvation.com.salvationministries.splashscreen;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab4 extends Fragment {


    private DatabaseReference users;
    private FirebaseAuth auth;
    private ImageView profileImage;
    private TextView name, logout,debitCard,help;
    private ProgressDialog dialog;
    private FancyButton editProfile;
    String country;
    String currency;
    private ProgressBar mProgressBar;
    private ImageView circleImageView;
    private Uri mImageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private TextView textView;
    private StorageTask mUploadTask;
    private Spinner title;
    private Spinner region;
    Button done;
    private String email,password;
    private EditText firstName;
    private EditText lastName;
    CountryCodePicker ccp;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Upload user;

    public Tab4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_tab4, container, false);

        auth = FirebaseAuth.getInstance();
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        dialog = new ProgressDialog(getContext());
        name = rootview.findViewById(R.id.userName);
        logout = rootview.findViewById(R.id.logout);
        debitCard = rootview.findViewById(R.id.addDebitCard);
        profileImage = rootview.findViewById(R.id.profileImage);
        editProfile = rootview.findViewById(R.id.btnEditProfie);
        help = rootview.findViewById(R.id.txtHelp);

        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfileNow();
            }
        });


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(getContext(), "you have logged out", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getContext(), splashscreen.class));
            }
        });

        debitCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDebitCardDialog();
            }
        });
        fetchUserData();


        // Inflate the layout for this fragment
        return rootview;
    }

    private void showAlert() {

        String textToShow[] = {"Call Us","Email us"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(textToShow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which){
                    case 0:
                        //Do this and this
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:+234 845 50 029 "));
                        getContext().startActivity(callIntent);

                        break;
                    case 1:
                        //Do this and this
                        sendEmail();
                        break;

                    default: //For all other cases, do this        break;
                }
            }
        });
        builder.create();
        builder.show();
    }



    private void sendEmail() {
        Log.i("Send email", "");

        String[] TO = {"itd@smhos.org","info@smhos.org"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
          getActivity().finish();
           /// Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(),
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void showAddDebitCardDialog() {
       startActivity(new Intent(getContext(), AddDebitCard.class));
    }

    private void fetchUserData() {
        String uid = auth.getCurrentUser().getUid();
        dialog.setMessage("Loading your profile.. please wait");
        dialog.show();

        users.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()) {
                    dialog.dismiss();
                     user = dataSnapshot.getValue(Upload.class);

                    Picasso.with(getContext()).load(user.getmImageUrl()).transform(new CircleTransform()).networkPolicy(NetworkPolicy.OFFLINE).into(profileImage, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(getContext())
                                    .load(user.getmImageUrl())
                                    .transform(new CircleTransform())
                                    .into(profileImage);


                        }
                    });
                    name.setText(user.getFirstName() + " " + user.getLastName());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void editProfileNow() {
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Users");
        final Dialog dialog = new Dialog(getContext());
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


        if (user.getTitle().equalsIgnoreCase("Mr")){
            title.setSelection(0);
        }else if (user.getTitle().equalsIgnoreCase("Mrs")){
            title.setSelection(1);
        }else if (user.getTitle().equalsIgnoreCase("brother")){
            title.setSelection(2);
        }else if (user.getTitle().equalsIgnoreCase("sister")){
            title.setSelection(3);
        }else if (user.getTitle().equalsIgnoreCase("pastor")){
            title.setSelection(4);
        }else if (user.getTitle().equalsIgnoreCase("Deacon")){
            title.setSelection(5);
        }else if (user.getTitle().equalsIgnoreCase("Deaconess")){
            title.setSelection(6);
        }
        firstName.setText(user.getFirstName());
        lastName.setText(user.getLastName());

        mImageUri = Uri.parse(user.getmImageUrl());

        Picasso.with(getContext()).load(mImageUri).transform(new CircleTransform()).into(circleImageView);


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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.with(getContext()).load(mImageUri).transform(new CircleTransform()).into(circleImageView);


        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getActivity().getContentResolver();
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

                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                            Upload upload = new Upload();
                            upload.setFirstName(firstName.getText().toString().trim());
                            upload.setLastName(lastName.getText().toString().trim());
                            upload.setmImageUrl(taskSnapshot.getDownloadUrl().toString());
                            upload.setRegion(ccp.getSelectedCountryName());
                            upload.setCurrency(currency);
                            upload.setTitle(title.getSelectedItem().toString());
                            upload.setStatus("user");

                            String uploadId = auth.getCurrentUser().getUid();
                            mDatabaseRef.child(uploadId).setValue(upload).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        done.setText("Done");
                                        done.setClickable(true);
                                        App app = new App();
                                        app.sendSuccessfullRegistrationEmail(email);
                                      //  dialog();
                                    }
                                }
                            });

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            done.setClickable(true);
                            done.setText("done");
                        }
                    });


        } else {
            Toast.makeText(getContext(), "select image", Toast.LENGTH_SHORT).show();
        }
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
