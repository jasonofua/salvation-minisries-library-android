package primedsoft.com.salvation.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.Model.SpecialOfferModel;
import primedsoft.com.salvation.R;

public class AddSpecialOffer extends AppCompatActivity {
    private ImageView addMesage,addCover;
    private FancyButton addMess;
    private ProgressDialog dialog;
    private StorageReference mStorageRef,mCoverImageRef;
    private DatabaseReference mMessageRef;
    private StorageTask mUploadTask,taskUpload;
    private TextView messagePath;
    public static final int FILE_PICKER_REQUEST_CODE = 1;
    private Uri messageUri;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_special_offer);

        mStorageRef = FirebaseStorage.getInstance().getReference("Special Offers");
        mMessageRef = FirebaseDatabase.getInstance().getReference("Special Offers");
        dialog = new ProgressDialog(this);
        messagePath = findViewById(R.id.pathToSpecialOffer);
        addMesage = findViewById(R.id.imgAddOffer);
        addMess = findViewById(R.id.btnAddSpecialOffer);
        addCover = findViewById(R.id.offerPreview);

        addMesage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickerForMessage();
            }
        });
        addMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploadingMessage();
            }
        });
    }

    private void openPickerForMessage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FILE_PICKER_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String PathHolder = data.getData().getPath();
                    messageUri = data.getData();
                    messagePath.setText(PathHolder);
                    Picasso.with(AddSpecialOffer.this).load(messageUri).into(addCover);
                 //   Toast.makeText(AddMessage.this, PathHolder, Toast.LENGTH_LONG).show();
                }
                break;

        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void startUploadingMessage() {

        if (messageUri != null){
            dialog.setMessage("Uploading Special Offer....please wait");
            dialog.show();
            dialog.setCancelable(false);
            addMess.setEnabled(false);

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(messageUri));

            mUploadTask = fileReference.putFile(messageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageUrl = taskSnapshot.getDownloadUrl().toString();

                            SpecialOfferModel messageModel = new SpecialOfferModel();
                            messageModel.setImageUrl(imageUrl);

                            mMessageRef.push().setValue(messageModel)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                dialog.setMessage("Upload done");
                                                dialog.dismiss();
                                                startActivity(new Intent(AddSpecialOffer.this,AdminDashBoard.class));
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    dialog.dismiss();
                                    Toast.makeText(AddSpecialOffer.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });



                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                            // percentage in progress dialog
                            dialog.setMessage("Uploaded " + ((int) progress) + "%...");

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            Toast.makeText(AddSpecialOffer.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


        }else{
            Toast.makeText(this, "Please complete all required fields to upload a message", Toast.LENGTH_SHORT).show();
        }





    }

}
