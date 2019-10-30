package primedsoft.com.salvation.Activities;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
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
import com.jaredrummler.materialspinner.MaterialSpinner;

import java.util.List;

import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.R;


public class AddMessage extends AppCompatActivity {
    PowerManager powerManager;
    PowerManager.WakeLock wakeLock;

    public static final int FILE_PICKER_REQUEST_CODE = 1;
    public static final int FILE_PICKER_REQUEST_CODES = 2;
    private static final String[] spinnerGenre = {
            "Message Genre",
            "WORD" ,
            "SPECIAL EVENTS",
            "POWER",
            "PRAISE",
            "SUCCESS",
            "FAMILY/MARRIAGE",
            "REALITIES OF NEW BIRTH",
            "FAITH",
            "PROSPERITY/FINANCE" ,
            "WISDOM",
            "PRAYER",
            "HOLY SPIRIT/ANOINTING",
            "HEALING/MIRACLES",
            "VENGEANCE/JUDGEMENT",
            "BUSINESS/CAREER",
            "SOUL WINNING/SOUL WORD",
            "COMMUNION",
            "EXCELLENCE"
    };
    private static final String[] type = {
            "Message Type",
            "Video",
            "Audio",
            "Book"

    };
    private static final String[] extensions = {
            "Message Extensions",
            ".mp4",
            ".mp3",
            ".pdf"

    };


    private static final String[] Author = {
            "Author",
            "David Ibiyeomie",
            "Peace Ibiyeomie"

    };
    String path;
    private TextInputLayout tilTitle;
    private String messagetitle,author,messageGenre,messageExtension,messageType,downloadUrl,imageUrl,messagePriceDollar,messagePriceNaira;
    private Uri messageUri,coverImageUri;
    private EditText edtTitle,edtPriceDollar,edtPriceNaira;
    private ImageView addMesage,addCover;
    private MaterialSpinner genre,spinType,spinnerExten,spinnerAuthor;
    private FancyButton addMess;
    private ProgressDialog dialog;
    private StorageReference mStorageRef,mCoverImageRef;
    private DatabaseReference mMessageRef,onlyBooksRef;
    private StorageTask mUploadTask,taskUpload;
    private TextView messagePath,coverImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_message);

        tilTitle = findViewById(R.id.tilMessageName);
        mStorageRef = FirebaseStorage.getInstance().getReference("messages");
        mCoverImageRef = FirebaseStorage.getInstance().getReference("cover images");
        mMessageRef = FirebaseDatabase.getInstance().getReference("Messages");
        onlyBooksRef = FirebaseDatabase.getInstance().getReference("Books");
        edtTitle = findViewById(R.id.edtMessageName);
        edtPriceDollar = findViewById(R.id.edtMessagePriceDollar);
        edtPriceNaira = findViewById(R.id.edtMessagePriceNaira);
        genre = findViewById(R.id.messageGenre);
        spinnerExten = findViewById(R.id.messageExtension);
        spinnerAuthor = findViewById(R.id.messageAuthor);
        spinType = findViewById(R.id.messageType);
        dialog = new ProgressDialog(this);
        messagePath = findViewById(R.id.pathToMessage);
        coverImagePath = findViewById(R.id.pathToCoverImage);


        addMesage = findViewById(R.id.imgAddMessage);
        addCover = findViewById(R.id.imgAddMessageCover);
        addMess = findViewById(R.id.btnAddMessage);
        addMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploadingMessage();
            }
        });

        addMesage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickerForMessage();
            }
        });

        addCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPickerForMessageCover();
            }
        });


        genre.setItems(spinnerGenre);
        genre.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                messageGenre = item;
            }
        });
        genre.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        spinnerAuthor.setItems(Author);
        spinnerAuthor.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                author = item;
            }
        });
        spinnerAuthor.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });


        spinType.setItems(type);
        spinType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                messageType = item;
                if (messageType.equalsIgnoreCase("BOOK")){
                    genre.setVisibility(View.GONE);
                }
            }
        });
        spinType.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

        spinnerExten.setItems(extensions);
        spinnerExten.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                messageExtension = item;
            }
        });
        spinnerExten.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's an upload in progress, save the reference so you can query it later
        if (mStorageRef != null) {
            outState.putString("reference", mStorageRef.toString());
        }
    }



    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);


        if (savedInstanceState == null ){
            return;

        }else {


            // If there was an upload in progress, get its reference and create a new StorageReference
            final String stringRef = savedInstanceState.getString("reference");
            if (stringRef == null) {
                return;
            }
            Toast.makeText(this, "Upload resuming", Toast.LENGTH_SHORT).show();
            mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

            // Find all UploadTasks under this StorageReference (in this example, there should be one)
            List<UploadTask> tasks = mStorageRef.getActiveUploadTasks();
            if (tasks.size() > 0) {
                // Get the task monitoring the upload
                UploadTask task = tasks.get(0);

                // Add new listeners to the task using an Activity scope
                task.addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot state) {
                        // Success!
                        // ...
                        downloadUrl = state.getDownloadUrl().toString();

                        StorageReference fileReference = mCoverImageRef.child(System.currentTimeMillis()
                                + "." + getFileExtension(coverImageUri));

                        mUploadTask = fileReference.putFile(coverImageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imageUrl = taskSnapshot.getDownloadUrl().toString();


                                        DatabaseReference newMessages = mMessageRef.push();
                                        String key = newMessages.getKey();
                                        MessageModel messageModel = new MessageModel();
                                        messageModel.setDownloadUrl(downloadUrl);
                                        messageModel.setExetension(messageExtension);
                                        messageModel.setGenre(messageGenre);
                                        messageModel.setImageUrl(imageUrl);
                                        messageModel.setTitle(messagetitle);
                                        messageModel.setType(messageType);
                                        messageModel.setPriceDollar(messagePriceDollar);
                                        messageModel.setPriceNaira(messagePriceNaira);
                                        messageModel.setAuthor(author);
                                        messageModel.setKey(key);


                                        if (messageType.equalsIgnoreCase("Book")) {
                                            messageModel.setGenre("Ebook");
                                            newMessages.setValue(messageModel)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                DatabaseReference booksRef = onlyBooksRef.push();
                                                                String key = booksRef.getKey();
                                                                MessageModel messageModel = new MessageModel();
                                                                messageModel.setDownloadUrl(downloadUrl);
                                                                messageModel.setExetension(messageExtension);
                                                                messageModel.setGenre("Ebook");
                                                                messageModel.setImageUrl(imageUrl);
                                                                messageModel.setTitle(messagetitle);
                                                                messageModel.setType(messageType);
                                                                messageModel.setPriceDollar(messagePriceDollar);
                                                                messageModel.setPriceNaira(messagePriceNaira);
                                                                messageModel.setAuthor(author);
                                                                messageModel.setKey(key);

                                                                booksRef.setValue(messageModel)
                                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                if (task.isSuccessful()) {
                                                                                    dialog.setMessage("Upload done");
                                                                                    dialog.dismiss();
                                                                                    startActivity(new Intent(AddMessage.this, AdminDashBoard.class));

                                                                                }
                                                                            }
                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                    @Override
                                                                    public void onFailure(@NonNull Exception e) {
                                                                        dialog.dismiss();
                                                                        Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                    }
                                                                });

                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.dismiss();
                                                    Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                }
                                            });
                                        } else {
                                            newMessages.setValue(messageModel)
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                dialog.setMessage("Upload done");
                                                                dialog.dismiss();
                                                                startActivity(new Intent(AddMessage.this, AdminDashBoard.class));
                                                            }
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    dialog.dismiss();
                                                    Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                        }

                                    }
                                });
                    }
                });
            }
        }
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void startUploadingMessage() {


        messagetitle = edtTitle.getText().toString().trim();
        messagePriceDollar = edtPriceDollar.getText().toString().trim();
        messagePriceNaira = edtPriceNaira.getText().toString().trim();

        if (messageUri != null||coverImageUri != null || !messagetitle.isEmpty()||!messagePriceNaira.isEmpty()||!messagePriceDollar.isEmpty()||!messageExtension.isEmpty()|| !messageGenre.isEmpty()||!messageType.isEmpty()){
            dialog.setMessage("Uploading Message....please wait");
            dialog.show();
            dialog.setCancelable(false);
            addMess.setEnabled(false);
            powerManager = (PowerManager) getSystemService(POWER_SERVICE);
            wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyApp::MyWakelockTag");
            wakeLock.acquire();

            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(messageUri));

            mUploadTask = fileReference.putFile(messageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            downloadUrl = taskSnapshot.getDownloadUrl().toString();

                            StorageReference fileReference = mCoverImageRef.child(System.currentTimeMillis()
                                    + "." + getFileExtension(coverImageUri));

                            mUploadTask = fileReference.putFile(coverImageUri)
                                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        imageUrl= taskSnapshot.getDownloadUrl().toString();


                                            DatabaseReference newMessages = mMessageRef.push();
                                            String key = newMessages.getKey();
                                            MessageModel messageModel = new MessageModel();
                                            messageModel.setDownloadUrl(downloadUrl);
                                            messageModel.setExetension(messageExtension);
                                            messageModel.setGenre(messageGenre);
                                            messageModel.setImageUrl(imageUrl);
                                            messageModel.setTitle(messagetitle);
                                            messageModel.setType(messageType);
                                            messageModel.setPriceDollar(messagePriceDollar);
                                            messageModel.setPriceNaira(messagePriceNaira);
                                            messageModel.setAuthor(author);
                                            messageModel.setKey(key);


                                            if (messageType.equalsIgnoreCase("Book")){
                                                messageModel.setGenre("Ebook");
                                                newMessages.setValue(messageModel)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()){
                                                                    DatabaseReference booksRef = onlyBooksRef.push();
                                                                    String key = booksRef.getKey();
                                                                    MessageModel messageModel = new MessageModel();
                                                                    messageModel.setDownloadUrl(downloadUrl);
                                                                    messageModel.setExetension(messageExtension);
                                                                    messageModel.setGenre("Ebook");
                                                                    messageModel.setImageUrl(imageUrl);
                                                                    messageModel.setTitle(messagetitle);
                                                                    messageModel.setType(messageType);
                                                                    messageModel.setPriceDollar(messagePriceDollar);
                                                                    messageModel.setPriceNaira(messagePriceNaira);
                                                                    messageModel.setAuthor(author);
                                                                    messageModel.setKey(key);

                                                                    booksRef.setValue(messageModel)
                                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                @Override
                                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                                    if (task.isSuccessful()){
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
                                                                                        dialog.setMessage("Upload done");
                                                                                        dialog.dismiss();
                                                                                        startActivity(new Intent(AddMessage.this,AdminDashBoard.class));

                                                                                    }
                                                                                }
                                                                            }).addOnFailureListener(new OnFailureListener() {
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
                                                                            Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                                        }
                                                                    });

                                                                             }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
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
                                                        Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                                                    }
                                                });
                                            }else {
                                                newMessages.setValue(messageModel)
                                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
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
                                                                    dialog.setMessage("Upload done");
                                                                    dialog.dismiss();
                                                                    startActivity(new Intent(AddMessage.this, AdminDashBoard.class));
                                                                }
                                                            }
                                                        }).addOnFailureListener(new OnFailureListener() {
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
                                                        Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                });

                                            }

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
                            Toast.makeText(AddMessage.this, e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });


        }else{
            Toast.makeText(this, "Please complete all required fields to upload a message", Toast.LENGTH_SHORT).show();
        }





    }

    private void openPickerForMessageCover() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        startActivityForResult(intent, FILE_PICKER_REQUEST_CODES);

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
                    Toast.makeText(AddMessage.this, PathHolder, Toast.LENGTH_LONG).show();
                }
                break;

            case FILE_PICKER_REQUEST_CODES:
                if (resultCode == RESULT_OK) {
                    String PathHolder = data.getData().getPath();
                    coverImageUri = data.getData();
                    coverImagePath.setText(PathHolder);
                    Toast.makeText(AddMessage.this, PathHolder, Toast.LENGTH_LONG).show();
                }
        }
    }

}
