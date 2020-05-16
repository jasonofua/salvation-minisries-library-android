package salvation.com.salvationministries.Adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import salvation.com.salvationministries.Model.SpecialOfferModel;
import salvation.com.salvationministries.R;

public class SpecialOfferAdapter extends RecyclerView.Adapter<SpecialOfferAdapter.MyViewHolder> {

    Context context;
    List<SpecialOfferModel> messageModels = new ArrayList<>();


    public SpecialOfferAdapter(List<SpecialOfferModel> messageModels) {
        this.messageModels = messageModels;
    }

    @NonNull
    @Override
    public SpecialOfferAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singleofferview,parent,false);
        context = parent.getContext();
        return new SpecialOfferAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecialOfferAdapter.MyViewHolder holder, int position){
        final SpecialOfferModel model = messageModels.get(position);
        holder.setUserImage(model.getImageUrl(),context);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.fullviewimagedialog);
                ImageView fullScreen = dialog.findViewById(R.id.imgFullOffer);
                ImageView close = dialog.findViewById(R.id.closeDialog);

                close.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
                Picasso.with(context).load(model.getImageUrl()).into(fullScreen);
                dialog.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOffer;
        View mView;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            imgOffer = (ImageView)mView.findViewById(R.id.imgSpecialOffer);
        }

        public void setUserImage(final String thumb_image, final Context context){
            // Picasso.get().load(thumb_image).placeholder(R.drawable.bas).into(messageImage);
            Picasso.with(context).load(thumb_image) .networkPolicy(NetworkPolicy.OFFLINE).into(imgOffer, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(thumb_image)
                            .into(imgOffer);


                }
            });

        }

    }
}
