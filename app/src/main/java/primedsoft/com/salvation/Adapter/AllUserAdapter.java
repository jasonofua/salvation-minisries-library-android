package primedsoft.com.salvation.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import primedsoft.com.salvation.Model.Upload;
import primedsoft.com.salvation.R;

public class AllUserAdapter extends RecyclerView.Adapter<AllUserAdapter.MyViewHolder> {
    List<Upload> usersList = new ArrayList<>();
    Context context;


    public AllUserAdapter(List<Upload> messageModels, Context context) {
        this.usersList = messageModels;
        this.context = context;
    }

    @NonNull
    @Override
    public AllUserAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleuser,parent,false);
        context = parent.getContext();
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Upload user = usersList.get(position);

        holder.setUserName(user.getFirstName()+ " "+user.getLastName());
        holder.setUserTitleAuthor(user.getTitle());
        holder.setUserCountryAuthor(user.getRegion());
        holder.setUserImage(user.getmImageUrl(),context);

    }


    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private View mView;
        private ImageView userImage;
        private TextView userName,userTitle,userCountry;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            userImage = mView.findViewById(R.id.userImage);
            userName = mView.findViewById(R.id.userName);
            userTitle = mView.findViewById(R.id.userTitle);
            userCountry = mView.findViewById(R.id.userCountry);


        }

        public void setUserName(String status){
            userName.setText(status);
        }
        public void setUserTitleAuthor(String status){
            userTitle.setText(status);
        }
        public void setUserCountryAuthor(String status){
            userCountry.setText(status);
        }

        public void setUserImage(final String thumb_image, final Context context){
            // Picasso.get().load(thumb_image).placeholder(R.drawable.bas).into(messageImage);
            Picasso.with(context).load(thumb_image) .networkPolicy(NetworkPolicy.OFFLINE).into(userImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(thumb_image)
                            .into(userImage);


                }
            });

        }

    }
}
