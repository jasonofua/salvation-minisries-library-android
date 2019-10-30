package primedsoft.com.salvation.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jp.shts.android.library.TriangleLabelView;
import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.Activities.MessageEdit;
import primedsoft.com.salvation.Activities.PdfReader;
import primedsoft.com.salvation.Activities.PlayerActivtiy;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.R;

public class AllMessagesAdmin extends RecyclerView.Adapter<AllMessagesAdmin.MyViewHolder> {
    List<MessageModel> messageModels = new ArrayList<>();
    Context context;
    FirebaseStorage storage;
    StorageReference storageRef ;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference userRef;
    String currency;


    public AllMessagesAdmin(List<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlemessageedit,parent,false);
        context = parent.getContext();
        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(context);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Messages");

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        loadSingleMessage(holder, position);
    }

    private void loadSingleMessage(@NonNull MyViewHolder holder, int position) {
        final MessageModel model = messageModels.get(position);
        String type = model.getType();
        holder.setUserStatus(model.getType());

        if (type.equalsIgnoreCase("audio")){
            holder.labelView.setTriangleBackgroundColorResource(R.color.colorAccentnew);
        }else if (type.equalsIgnoreCase("book")){
            holder.labelView.setTriangleBackgroundColorResource(R.color.red);
        }
        if(model != null ||!model.getImageUrl().isEmpty()|| !model.getImageUrl().equalsIgnoreCase("")) {
            holder.setUserImage(model.getImageUrl(),holder.messageImage.getContext());
        }
        holder.setMessageName(model.getTitle());

        holder.editMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showAlert(model);
            }
        });

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog = new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.single_message_buy);
                TriangleLabelView labelView = dialog.findViewById(R.id.triangularLabelBuy);
                final ImageView messageImage = dialog.findViewById(R.id.buyImageThumbNail);
                TextView messageTitle = dialog.findViewById(R.id.messageTitle);
                TextView messageAuthor = dialog.findViewById(R.id.messageBuyAuthor);
                FancyButton button = dialog.findViewById(R.id.btnPlay);
                FancyButton buttonBuy = dialog.findViewById(R.id.btnBuy);
                buttonBuy.setVisibility(View.GONE);
                ImageView back = dialog.findViewById(R.id.imgBack);

                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                Picasso.with(context).load(model.getImageUrl()) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(context)
                                .load(model.getImageUrl())
                                .into(messageImage);


                    }
                });



                if (model.getType().equalsIgnoreCase("video")){
                    labelView.setPrimaryText("video");
                    button.setText("Watch");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            watchVideo(model.getDownloadUrl());
                        }
                    });

                }else if (model.getType().equalsIgnoreCase("audio")){
                    labelView.setPrimaryText("audio");
                    labelView.setTriangleBackgroundColorResource(R.color.colorAccentnew);
                    button.setText("Listen");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ListenAudio(model.getDownloadUrl());
                        }
                    });
                }else {
                    labelView.setPrimaryText("book");
                    labelView.setTriangleBackgroundColorResource(R.color.red);
                    button.setText("Read");
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(context, "Working on this feature now, try downloading it", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                dialog.show();


                messageTitle.setText(model.getTitle());
                messageAuthor.setText(model.getAuthor());

            }
        });
    }

    private void readPdf(String downloadUrl) {
        Intent pdfReader = new Intent(context, PdfReader.class);
        pdfReader.putExtra("pdfUrl",downloadUrl);
        context.startActivity(pdfReader);
    }


    private void ListenAudio(String downloadUrl) {
        Intent playAudio = new Intent(context, PlayerActivtiy.class);
        playAudio.putExtra("url",downloadUrl);
        context.startActivity(playAudio);

    }

    private void watchVideo(String downloadUrl) {

        Intent playAudio = new Intent(context, PlayerActivtiy.class);
        playAudio.putExtra("url",downloadUrl);
        context.startActivity(playAudio);
    }

    private void startFireBaseDownload(String url, String type,String exetension) {
        progressDialog.setTitle("Downloading...");
        progressDialog.setMessage(null);
        progressDialog.show();


        storageRef = storage.getReferenceFromUrl(url);
        try {
            final File localFile = File.createTempFile(type, exetension);

            storageRef.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    Toast.makeText(context, "your file is saved at "+localFile.getAbsolutePath(), Toast.LENGTH_LONG).show();

                    progressDialog.dismiss();

                    // Local temp file has been created
                    // use localFile
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context, String.valueOf(exception), Toast.LENGTH_SHORT).show();
                    // Handle any errors
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                    // percentage in progress dialog
                    progressDialog.setMessage("Downloaded " + ((int) progress) + "%...");
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return messageModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TriangleLabelView labelView;
        View mView;
        ImageView messageImage,editMessage;
        TextView messageName,messageAuthor;


        public MyViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            labelView = mView.findViewById(R.id.triangularLabel);
            messageImage = mView.findViewById(R.id.imageThumb);
            messageName = mView.findViewById(R.id.txtMessageName);
            messageAuthor = mView.findViewById(R.id.messageAuthor);
            editMessage = mView.findViewById(R.id.editMessage);
        }

        public void setUserStatus(String status){
            labelView.setPrimaryText(status);
        }

        public void setMessageName(String status){
            messageName.setText(status);
        }
        public void setMessageAuthor(String status){
            messageAuthor.setText(status);
        }

        public void setUserImage(final String thumb_image, final Context context){
            // Picasso.get().load(thumb_image).placeholder(R.drawable.bas).into(messageImage);
            Picasso.with(context).load(thumb_image) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(thumb_image)
                            .into(messageImage);


                }
            });

        }
    }

    private void showAlert(final MessageModel model) {

        String textToShow[] = {"Edit Message","Delete Message"};
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setItems(textToShow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which){
                    case 0:
                        //Do this and this
                      Intent editMessage = new Intent(context, MessageEdit.class);
                      editMessage.putExtra("messageName",model.getTitle());
                      editMessage.putExtra("messageType",model.getType());
                      editMessage.putExtra("messageAuthor",model.getAuthor());
                      editMessage.putExtra("messageGenre",model.getGenre());
                      editMessage.putExtra("messageDollar",model.getPriceDollar());
                      editMessage.putExtra("messageNaira",model.getPriceNaira());
                      editMessage.putExtra("messageExtension",model.getExetension());
                      editMessage.putExtra("messageKey",model.getKey());
                      context.startActivity(editMessage);


                        break;
                    case 1:
                        //Do this and this
                        userRef.child(model.getKey()).removeValue();
                        notifyDataSetChanged();

                        break;
                    default: //For all other cases, do this        break;
                }
            }
        });
        builder.create();
        builder.show();
    }
}
