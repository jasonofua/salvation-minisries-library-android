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
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import primedsoft.com.salvation.Adapter.MessageAdapter;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Utils.ItemOffsetDecoration;

public class GenreListActivity extends AppCompatActivity {

    CollapsingToolbarLayout collapsingToolbarLayout;
    Button filter;
    private String genre;
    private DatabaseReference messageRef;
    private FirebaseAuth auth;
    private RecyclerView rvMessages;
    private LinearLayoutManager layoutManager;
    private MessageAdapter rvAdapter;
    private List<MessageModel> messageModels = new ArrayList<>();
    private TextView messages;
    private ImageButton back;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_genre_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        AppBarLayout appbar = findViewById(R.id.app_bar);
        genre = getIntent().getStringExtra("GenreName");
        Bundle bundle=this.getIntent().getExtras();
        int pic=bundle.getInt("image");

        collapsingToolbarLayout = findViewById(R.id.toolbar_layout);
        filter = findViewById(R.id.imageFilter);

        messages = (TextView)findViewById(R.id.noMessage);

        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlert();
            }
        });
        collapsingToolbarLayout.setExpandedTitleMarginBottom(20);
        collapsingToolbarLayout.setBackgroundResource(pic);

        collapsingToolbarLayout.setTitle(genre);



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
        dialog = new ProgressDialog(GenreListActivity.this);
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        rvMessages = findViewById(R.id.rvMessages);
        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        rvAdapter = new MessageAdapter(messageModels);
        rvMessages.setHasFixedSize(true);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setReverseLayout(true);
        rvMessages.setLayoutManager(layoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen.offset);
        rvMessages.addItemDecoration(itemDecoration);
        rvMessages.setAdapter(rvAdapter);

        fetchNewMessages();

    }

    private void showAlert() {

        String textToShow[] = {"All","Audio","Video"};
        AlertDialog.Builder builder = new AlertDialog.Builder(GenreListActivity.this);
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
                        String genreName = dataSnapshot.child("genre").getValue().toString();

                        if (genreName.equalsIgnoreCase(genre)) {
                            MessageModel message = dataSnapshot.getValue(MessageModel.class);
                            messageModels.add(message);
                            messages.setVisibility(View.GONE);
                            rvMessages.setVisibility(View.VISIBLE);
                            rvAdapter.notifyDataSetChanged();
                        } else {
                            dialog.dismiss();
//                            messageModels.clear();
                      ///      rvAdapter.notifyDataSetChanged();
                            //  Toast.makeText(GenreListActivity.this, "No Message for the selected genre", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        dialog.dismiss();
//                        messages.setVisibility(View.VISIBLE);
//                        rvMessages.setVisibility(View.GONE);
//                        messageModels.clear();
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
                    String genreName = dataSnapshot.child("genre").getValue().toString();
                    dialog.dismiss();
                    if (genreName.equalsIgnoreCase(genre)){
                        MessageModel message = dataSnapshot.getValue(MessageModel.class);
                        messageModels.add(message);
                        messages.setVisibility(View.GONE);
                        rvMessages.setVisibility(View.VISIBLE);
                        rvAdapter.notifyDataSetChanged();

                    }else {
//                        messages.setVisibility(View.VISIBLE);
//                        rvMessages.setVisibility(View.GONE);
                      //  Toast.makeText(GenreListActivity.this, "No Message for the selected genre", Toast.LENGTH_SHORT).show();
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
