package salvation.com.salvationministries.Activities;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import salvation.com.salvationministries.Model.MyMessages;
import salvation.com.salvationministries.R;

public class AllBoughtMessage extends AppCompatActivity {
    private DatabaseReference adminBought;
    private FirebaseAuth auth;
    int booksCount,audioCount,videoCount;
    private TextView audio,video,book;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_bought_message);

        adminBought = FirebaseDatabase.getInstance().getReference().child("AdminBoughtMessages");
        auth = FirebaseAuth.getInstance();
        dialog= new ProgressDialog(this);


        audio = (TextView)findViewById(R.id.txtAudioCount);
        video = (TextView)findViewById(R.id.txtVidCount);
        book = (TextView)findViewById(R.id.txtBooksCount);

        booksCount = 0;
        videoCount = 0;
        audioCount = 0;
        getMessageDownload();





    }

    private void getMessageDownload() {

        dialog.setMessage("Fetch new data,please wait...");
        dialog.show();
        adminBought.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    dialog.dismiss();

                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        MyMessages messages = snapshot.getValue(MyMessages.class);

                        if (messages.getType().equalsIgnoreCase("book")){
                            booksCount +=1;
                            book.setText(String.valueOf(booksCount));
                        }else if (messages.getType().equalsIgnoreCase("audio")){
                            audioCount += 1;
                            audio.setText(String.valueOf(audioCount));
                        }else {
                            videoCount += 1;
                            video.setText(String.valueOf(videoCount));
                        }

                    }

                }else{
                    dialog.dismiss();
                    Toast.makeText(AllBoughtMessage.this, "No Message found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
            }
        });
    }
}
