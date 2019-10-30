package primedsoft.com.salvation.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import primedsoft.com.salvation.Adapter.AllMessagesAdmin;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Utils.ItemOffsetDecoration;

public class AdminAllMessage extends AppCompatActivity {
    CollapsingToolbarLayout collapsingToolbarLayout;
    Button filter;
    private String genre;
    private DatabaseReference messageRef;
    private FirebaseAuth auth;
    private RecyclerView rvMessages;
    private LinearLayoutManager layoutManager;
    private AllMessagesAdmin rvAdapter;
    private List<MessageModel> messageModels = new ArrayList<>();
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_all_message);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        AppBarLayout appbar = findViewById(R.id.app_bar);


        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        filter = findViewById(R.id.imageFilter);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });
        collapsingToolbarLayout.setExpandedTitleMarginBottom(20);

        collapsingToolbarLayout.setTitle("ALL MESSAGES");
        collapsingToolbarLayout.setBackgroundResource(R.drawable.bas);


        appbar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (Math.abs(verticalOffset)-appBarLayout.getTotalScrollRange() == 0)
                {
                    //  Collapsed
                    filter.setVisibility(View.INVISIBLE);

                }
                else
                {
                    //Expanded
                    filter.setVisibility(View.VISIBLE);

                }
            }
        });
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(AdminAllMessage.this);
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        rvMessages = findViewById(R.id.rvMessagesAdmin);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvAdapter = new AllMessagesAdmin(messageModels);
        rvMessages.setHasFixedSize(true);
        rvMessages.setLayoutManager(new GridLayoutManager(this, 2));
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.offset);
        rvMessages.addItemDecoration(itemDecoration);
        rvMessages.setAdapter(rvAdapter);

        fetchNewMessages();


    }

    private void showAlert() {

        String textToShow[] = {"All","Audio","Video","Book"};
        AlertDialog.Builder builder = new AlertDialog.Builder(AdminAllMessage.this);
        builder.setItems(textToShow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which){
                    case 0:
                        //Do this and this
                        fetchNewMessages();
                        filter.setText("All");
                        break;
                    case 1:
                        //Do this and this
                        fetchFilteredMessage("audio");
                        filter.setText("Audio");
                        break;
                    case 2:
                        //Do this and this:
                        fetchFilteredMessage("video");
                        filter.setText("Video");
                        break;
                    case 3:
                        //Do this and this:
                        fetchFilteredMessage("book");
                        filter.setText("Book");
                        break;
                    default: //For all other cases, do this        break;
                }
            }
        });
        builder.create();
        builder.show();
    }



    private void fetchFilteredMessage(final String audio) {
        dialog.setMessage("Loading your messages.. please wait");
        dialog.show();
        messageModels.clear();

        //  Query query = messageRef.child("type").equalTo(audio);
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()){
                    dialog.dismiss();
                    String type = dataSnapshot.child("type").getValue().toString();
                    if (type.equalsIgnoreCase(audio)) {
                        //  String genreName = dataSnapshot.child("genre").getValue().toString();
                        MessageModel message = dataSnapshot.getValue(MessageModel.class);
                        messageModels.add(message);
                        rvAdapter.notifyDataSetChanged();

                    }else {
                        dialog.dismiss();
                        //  messageModels.clear();
                        rvAdapter.notifyDataSetChanged();

                    }

                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void fetchNewMessages() {
        dialog.setMessage("Loading your messages.. please wait");
        dialog.show();
        messageModels.clear();
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()){
                    dialog.dismiss();
                    MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(message);
                    rvAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        fetchNewMessages();
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
