package primedsoft.com.salvation.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import primedsoft.com.salvation.Adapter.AllUserAdapter;
import primedsoft.com.salvation.Model.Upload;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Utils.CircleTransform;

public class AllUsers extends AppCompatActivity {
    private RecyclerView allUser;
    private DatabaseReference userRef;
    private FirebaseAuth auth;
    private LinearLayoutManager layoutManager;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_users);

        dialog = new ProgressDialog(AllUsers.this);
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        auth = FirebaseAuth.getInstance();

        layoutManager = new LinearLayoutManager(AllUsers.this);
        allUser = (RecyclerView)findViewById(R.id.rvAllUsers);
        allUser.setHasFixedSize(true);
        allUser.setLayoutManager(layoutManager);

    }

    private void fetchAllUsers() {

        FirebaseRecyclerAdapter<Upload,AllUserViewholder> adapter = new FirebaseRecyclerAdapter<Upload, AllUserViewholder>(
                Upload.class,
                R.layout.singleuser,
                AllUserViewholder.class,
                userRef
        ) {
            @Override
            protected void populateViewHolder(AllUserViewholder viewHolder, Upload model, int position) {

                if (model.getFirstName().equalsIgnoreCase("admin")){

                }else {
                    viewHolder.setDisplayName(model.getFirstName() + " " + model.getLastName());
                    viewHolder.setUserCountry(model.getCurrency());
                    viewHolder.setUserTitle(model.getTitle());
                    viewHolder.setUserImage(model.getmImageUrl(), AllUsers.this);

                }
            }
        };

        allUser.setAdapter(adapter);

    }


    public static class AllUserViewholder extends RecyclerView.ViewHolder{

        View mView;
        ImageView userImage;
        TextView username,userTitle,userCountry;

        public AllUserViewholder(View itemView) {
            super(itemView);

            mView = itemView;

            userImage = (ImageView)mView.findViewById(R.id.userImage);
            username = (TextView)mView.findViewById(R.id.userName);
            userTitle = (TextView)mView.findViewById(R.id.userTitle);
            userCountry = (TextView)mView.findViewById(R.id.userCountry);
        }


        public void setDisplayName(String sname){

            username.setText(sname);

        }


        public void setUserTitle(String name){

            userTitle.setText(name);

        }

        public void setUserCountry(String name){

            userCountry.setText(name);

        }



        public void setUserImage(final String status, final Context context){

            Picasso.with(context).load(status).transform(new CircleTransform()) .networkPolicy(NetworkPolicy.OFFLINE).into(userImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(status).into(userImage);

                }
            });



        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchAllUsers();
    }
}
