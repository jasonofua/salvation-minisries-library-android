package salvation.com.salvationministries.landingpage;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

import salvation.com.salvationministries.Activities.AllMessageActivity;
import salvation.com.salvationministries.Activities.GenreListActivity;
import salvation.com.salvationministries.Adapter.MessageAdapter;
import salvation.com.salvationministries.Model.MessageModel;
import salvation.com.salvationministries.R;

public class Tab1 extends Fragment {

    String textToShow[] = {"Creative Thinkers don't leave the world the way they met it","Creativity sees possibility and solution in the face of challenges","Whatever is conceivable is achievable","If you say “I can't”, you will end up in a Can","The world will never ask a creative man to resign","Christianity is not synonymous with stupidity","Those who refuse change remain in chains","One of the greatest proofs of maturity is to accept change without been offended","Change can only take place when we accept what we preach to others and practice it. ","Change doesn't come by getting angry with the truth but accepting the truth","Every change is a function of Word encounter","Time does not change situations, except people do something about the situation","You cannot come out of that situation until you change your position","Always accept change if it is positive for change is what makes life dynamic","Great decision with action brings about change","When people refuse change, wisdom has to be on display","Nations don't change people, it is people that change nations","Anytime you are told the truth and you get angry, you don't want to change","When you make change, you take charge","If you cannot beat them change them","In life, you don't wait for a change; you enforce a change","The change you allow is the change you experience","Life on earth is too short to be wasted","Investment is good, but move beyond it to innovation","Think big, but start small and keep progressing","If you are moved to buy anything you see you do not  have a future in business","Revolutionists in business  are creative people","Time to acquire information is not a wasted time in business","Business is of God and God is the Author","You can't succeed! If you don't persist!","Success in business is matching  your gift with a problem", "Every vengeance from God will be provoked by the declaration of His children.", "You need understanding to be outstanding.", "To meet God with ease, offer Him a sacrifice of praise.", "Always fill your heart with the Word of God."};
    int messageCount = textToShow.length;
    // to keep current Index of text
    int currentIndex = 0;
    private DatabaseReference messageRef,users;
    private FirebaseAuth auth;
    private RecyclerView rvMessages;
    private LinearLayoutManager layoutManager;
    private MessageAdapter rvAdapter;
    private List<MessageModel> messageModels = new ArrayList<>();
    private ProgressDialog dialog;
    private Thread thread;
    private Query query;
    private ImageView img1, img2, img3, img4, img5, img6, img7, img8, img9, img10, img11, img12, img13, img14, img15, img16, img17, img18;
    private TextView allMessage, newRelease,allBooks;
    private TextSwitcher mSwitcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        auth = FirebaseAuth.getInstance();
        dialog = new ProgressDialog(getContext());
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        users = FirebaseDatabase.getInstance().getReference().child("Users");
        rvMessages = rootView.findViewById(R.id.rvNewMessage);
        mSwitcher = rootView.findViewById(R.id.simpleTextSwitcher);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAdapter = new MessageAdapter(messageModels);
        rvMessages.setHasFixedSize(true);
        rvMessages.setLayoutManager(layoutManager);
        //rvMessages.setStackFromBottom(true);
        rvMessages.setAdapter(rvAdapter);
        img1 = rootView.findViewById(R.id.img1);
        img2 = rootView.findViewById(R.id.img2);
        img3 = rootView.findViewById(R.id.img3);
        img4 = rootView.findViewById(R.id.img4);
        img5 = rootView.findViewById(R.id.img5);
        img6 = rootView.findViewById(R.id.img6);
        img7 = rootView.findViewById(R.id.img7);
        img8 = rootView.findViewById(R.id.img8);
        img9 = rootView.findViewById(R.id.img9);
        img10 = rootView.findViewById(R.id.img10);
        img11 = rootView.findViewById(R.id.img11);
        img12 = rootView.findViewById(R.id.img12);
        img13 = rootView.findViewById(R.id.img13);
        img14 = rootView.findViewById(R.id.img14);
        img15 = rootView.findViewById(R.id.img15);
        img16 = rootView.findViewById(R.id.img16);
        img17 = rootView.findViewById(R.id.img17);
        img18 = rootView.findViewById(R.id.img18);

        allMessage = rootView.findViewById(R.id.txtAllMessage);
        newRelease = rootView.findViewById(R.id.txtNewRelease);
        allBooks = rootView.findViewById(R.id.txtAllBooks);

        allMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMessages("ALL MESSAGES");
            }
        });
        newRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMessages("NEW RELEASE");
            }
        });
        allBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMessages("EBOOKS");
            }
        });

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("WORD", R.drawable.word);
            }
        });
        img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("SPECIAL EVENTS", R.drawable.fivenight);
            }
        });
        img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("POWER", R.drawable.power);
            }
        });
        img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("PRAISE", R.drawable.praise);
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("SUCCESS", R.drawable.success);
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("FAMILY/MARRIAGE", R.drawable.family);
            }
        });
        img7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("REALITIES OF NEW BIRTH", R.drawable.birth);
            }
        });
        img8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("FAITH", R.drawable.faith);
            }
        });
        img9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("PROSPERITY/FINANCE", R.drawable.prosperity);
            }
        });
        img10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("WISDOM", R.drawable.wisdom);
            }
        });
        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("PRAYER", R.drawable.prayer);
            }
        });
        img12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("HOLY SPIRIT/ANOINTING", R.drawable.holyspirit);
            }
        });
        img13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("HEALING/MIRACLES", R.drawable.healing);
            }
        });
        img14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("VENGEANCE/JUDGEMENT", R.drawable.vengence);
            }
        });
        img15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("BUSINESS/CAREER", R.drawable.business);
            }
        });
        img16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("SOUL WINNING/SOUL WORD", R.drawable.soul);
            }
        });
        img17.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("COMMUNION", R.drawable.communion);
            }
        });
        img18.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("EXCELLENCE", R.drawable.excellence);
            }
        });

        // messageModels.clear();
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(getContext());
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(18);
                myText.setTextColor(Color.WHITE);
                return myText;
            }
        });

        // Declare the in and out animations and initialize them
        Animation in = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(getContext(), android.R.anim.slide_out_right);

        // set the animation type of textSwitcher
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);


        showChangingText();
        fetchNewMessages();


        return rootView;
    }

    private void showChangingText() {
        thread = new Thread() {

            @Override
            public void run() {
                try {
                    while (!thread.isInterrupted()) {
                        Thread.sleep(50000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                currentIndex++;
                                // If index reaches maximum reset it
                                if (currentIndex == messageCount)
                                    currentIndex = 0;
                                mSwitcher.setText(textToShow[currentIndex]);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        thread.start();
    }

    private void goToMessages(String all_messages) {
        Intent showGenreList = new Intent(getContext(), AllMessageActivity.class);
        showGenreList.putExtra("tag", all_messages);
        getContext().startActivity(showGenreList);
    }

    private void goToList(String church_growth, int word) {

        Intent showGenreList = new Intent(getContext(), GenreListActivity.class);
        showGenreList.putExtra("GenreName", church_growth);
        Bundle bundle=new Bundle();
        bundle.putInt("image",word);
        showGenreList.putExtras(bundle);
        getContext().startActivity(showGenreList);
    }


    private void fetchNewMessages() {
        dialog.setMessage("Loading your messages.. please wait");
        dialog.show();
        messageModels.clear();
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    dialog.dismiss();
                    final MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(message);
                    rvAdapter.notifyDataSetChanged();

                } else {
                    dialog.dismiss();
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
    public void onResume() {
        super.onResume();
        fetchNewMessages();
        showChangingText();

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