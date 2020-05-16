package salvation.com.salvationministries.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.jaredrummler.materialspinner.MaterialSpinner;

import mehdi.sakout.fancybuttons.FancyButton;
import salvation.com.salvationministries.Model.MessageModel;
import salvation.com.salvationministries.R;

public class MessageEdit extends AppCompatActivity {
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

    private String messagetitle,author,messageGenre,messageExtension,messageType,messagePriceDollar,messagePriceNaira,messageKey;
    private EditText edtTitle,edtPriceDollar,edtPriceNaira;
    private MaterialSpinner genre,spinType,spinnerExten,spinnerAuthor;
    private FancyButton addMess;
    private ProgressDialog dialog;
    private DatabaseReference mMessageRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_edit);

        messagetitle = getIntent().getStringExtra("messageName");
        author = getIntent().getStringExtra("messageAuthor");
        messageExtension = getIntent().getStringExtra("messageExtension");
        messageGenre = getIntent().getStringExtra("messageGenre");
        messagePriceDollar = getIntent().getStringExtra("messageDollar");
        messagePriceNaira = getIntent().getStringExtra("messageNaira");
        messageType = getIntent().getStringExtra("messageType");
        messageKey = getIntent().getStringExtra("messageKey");
        mMessageRef = FirebaseDatabase.getInstance().getReference("Messages");
        edtTitle = findViewById(R.id.edtMessageNameEdit);
        edtPriceDollar = findViewById(R.id.edtMessagePriceDollarEdit);
        edtPriceNaira = findViewById(R.id.edtMessagePriceNairaEdit);
        genre = findViewById(R.id.messageGenreEdit);
        spinnerExten = findViewById(R.id.messageExtensionEdit);
        spinnerAuthor = findViewById(R.id.messageAuthorEdit);
        spinType = findViewById(R.id.messageTypeEdit);
        dialog = new ProgressDialog(this);
        addMess = findViewById(R.id.btnAddMessageEdit);

        addMess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUploadEditedMessage();
            }
        });

        edtTitle.setText(messagetitle);
        edtPriceDollar.setText(messagePriceDollar);
        edtPriceNaira.setText(messagePriceNaira);

        if (messageType.equalsIgnoreCase("video")){
            spinType.setSelectedIndex(1);
        }else if (messageType.equalsIgnoreCase("audio")){
            spinType.setSelectedIndex(2);
        }else if (messageType.equalsIgnoreCase("book")){
            spinType.setSelectedIndex(3);
        }


        if (messageExtension.equalsIgnoreCase(".mp4")){
            spinnerExten.setSelectedIndex(1);
        }else  if (messageExtension.equalsIgnoreCase(".mp3")){
            spinnerExten.setSelectedIndex(2);
        }else  if (messageExtension.equalsIgnoreCase(".pdf")){
            spinnerExten.setSelectedIndex(3);
        }

        if (author.equalsIgnoreCase("David Ibiyeomie")){
            spinnerAuthor.setSelectedIndex(1);
        }else if (author.equalsIgnoreCase("Peace Ibiyeomie")){
            spinnerAuthor.setSelectedIndex(2);
        }






        if (messageGenre.equalsIgnoreCase("WORD")){
            genre.setSelectedIndex(1);
        }else if (messageGenre.equalsIgnoreCase("SPECIAL EVENTS")){
            genre.setSelectedIndex(2);
        }else if (messageGenre.equalsIgnoreCase("POWER")){
            genre.setSelectedIndex(3);
        }else if (messageGenre.equalsIgnoreCase("PRAISE")){
            genre.setSelectedIndex(4);
        }else if (messageGenre.equalsIgnoreCase("SUCCESS")){
            genre.setSelectedIndex(5);
        }else if (messageGenre.equalsIgnoreCase("FAMILY/MARRIAGE")){
            genre.setSelectedIndex(6);
        }else if (messageGenre.equalsIgnoreCase("REALITIES OF NEW BIRTH")){
            genre.setSelectedIndex(7);
        }else if (messageGenre.equalsIgnoreCase("FAITH")){
            genre.setSelectedIndex(8);
        }else if (messageGenre.equalsIgnoreCase("PROSPERITY/FINANCE")){
            genre.setSelectedIndex(9);
        }else if (messageGenre.equalsIgnoreCase("WISDOM")){
            genre.setSelectedIndex(10);
        }else if (messageGenre.equalsIgnoreCase("PRAYER")){
            genre.setSelectedIndex(11);
        }else if (messageGenre.equalsIgnoreCase("HOLY SPIRIT/ANOINTING")){
            genre.setSelectedIndex(12);
        }else if (messageGenre.equalsIgnoreCase("HEALING/MIRACLES")){
            genre.setSelectedIndex(13);
        }else if (messageGenre.equalsIgnoreCase("VENGEANCE/JUDGEMENT")){
            genre.setSelectedIndex(14);
        }else if (messageGenre.equalsIgnoreCase("BUSINESS/CAREER")){
            genre.setSelectedIndex(15);
        }else if (messageGenre.equalsIgnoreCase("SOUL WINNING/SOUL WORD")){
            genre.setSelectedIndex(16);
        }else if (messageGenre.equalsIgnoreCase("COMMUNION")){
            genre.setSelectedIndex(17);
        }else if (messageGenre.equalsIgnoreCase("EXCELLENCE")){
            genre.setSelectedIndex(18);
        }

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
        genre.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {

            @Override public void onNothingSelected(MaterialSpinner spinner) {
                Snackbar.make(spinner, "Nothing selected", Snackbar.LENGTH_LONG).show();
            }
        });


        spinType.setItems(type);
        spinType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {

            @Override public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                messageType = item;
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

    private void startUploadEditedMessage() {
        dialog.setMessage("Uploading your edited message.. please wait a bit");
        dialog.setCancelable(false);
        dialog.show();

        MessageModel model = new MessageModel();
        model.setKey(messageKey);
        model.setAuthor(author);
        model.setPriceNaira(messagePriceNaira);
        model.setPriceDollar(messagePriceDollar);
        model.setType(messageType);
        model.setTitle(messagetitle);
        model.setGenre(messageGenre);
        model.setExetension(messageExtension);

        mMessageRef.child(messageKey).setValue(model)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(MessageEdit.this, "Message Successfully updated", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(MessageEdit.this,AdminDashBoard.class));
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MessageEdit.this, e.getMessage(), Toast.LENGTH_SHORT).show();
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
