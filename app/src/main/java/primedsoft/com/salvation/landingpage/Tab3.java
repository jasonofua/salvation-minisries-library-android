package primedsoft.com.salvation.landingpage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.PowerManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import java.util.List;
import java.util.UUID;

import jp.shts.android.library.TriangleLabelView;
import primedsoft.com.salvation.Activities.OfflinPdfReader;
import primedsoft.com.salvation.Activities.OfflineAudioPlayer;
import primedsoft.com.salvation.Activities.PdfReader;
import primedsoft.com.salvation.Activities.PlayerActivtiy;
import primedsoft.com.salvation.Activities.VideoActivity;
import primedsoft.com.salvation.Model.MyMessages;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Utils.CircleTransform;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.content.Context.POWER_SERVICE;

public class Tab3 extends Fragment {
    private RecyclerView boughtMessageRv,downloadedMessageRv;
    FirebaseAuth auth;
    private DatabaseReference boughtMessage,downloadecMessages;
    private LinearLayoutManager mLayoutManager,mLayoutManager2;
    private String uid,files;
    MyMessages mod;
    FirebaseStorage storage;
    StorageReference storageRef ;
    private static final int PERMISSION_REQUEST_CODE = 200;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_three, container, false);

        boughtMessageRv = (RecyclerView)root.findViewById(R.id.rvBoughtMessages);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        storage = FirebaseStorage.getInstance();

        downloadedMessageRv = (RecyclerView)root.findViewById(R.id.rvDownloadedMessages);
        boughtMessage = FirebaseDatabase.getInstance().getReference().child("BoughtMessages").child(uid);
        downloadecMessages = FirebaseDatabase.getInstance().getReference().child("Downloaded Messages").child(uid);
        mLayoutManager = new LinearLayoutManager(getContext());
        mLayoutManager2 = new LinearLayoutManager(getContext());
        mLayoutManager.setReverseLayout(true);
        mLayoutManager2.setReverseLayout(true);
        boughtMessageRv.setLayoutManager(mLayoutManager);
        downloadedMessageRv.setLayoutManager(mLayoutManager2);
        return root;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // If there's a download in progress, save the reference so you can query it later
        if (storageRef != null) {
            outState.putString("reference", storageRef.toString());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if (savedInstanceState == null){

            return;

        }else {


            // If there was a download in progress, get its reference and create a new StorageReference
            final String stringRef = savedInstanceState.getString("reference");
            if (stringRef == null) {
                return;
            }
            storageRef = FirebaseStorage.getInstance().getReferenceFromUrl(stringRef);

            // Find all DownloadTasks under this StorageReference (in this example, there should be one)
            List<FileDownloadTask> tasks = storageRef.getActiveDownloadTasks();
            if (tasks.size() > 0) {
                // Get the task monitoring the download
                Toast.makeText(getContext(), "Download in progress", Toast.LENGTH_SHORT).show();
                FileDownloadTask task = tasks.get(0);

                // Add new listeners to the task using an Activity scope
                task.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot state) {


                        MyMessages myMessages = new MyMessages();
                        myMessages.setExtension(mod.getExtension());
                        myMessages.setType(mod.getType());
                        myMessages.setMessageKey(mod.getMessageKey());
                        myMessages.setMessageDownloadUrl(files);
                        myMessages.setMessageImageUrl(mod.getMessageImageUrl());
                        myMessages.setMessageTitle(mod.getMessageTitle());

                        downloadecMessages.child(mod.getMessageKey()).setValue(myMessages)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getContext(), "Message successfully downloaded", Toast.LENGTH_SHORT).show();

                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                });
            }

        }

    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getContext(), READ_EXTERNAL_STORAGE);
        int result1 = ContextCompat.checkSelfPermission(getContext(), WRITE_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {

        ActivityCompat.requestPermissions((Activity) getContext(), new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean locationAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean cameraAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (locationAccepted && cameraAccepted)
                        showMessageOKCancel("Permission Granted, you can now start your download", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                    else {

                        Toast.makeText(getContext(), "Permission Refused", Toast.LENGTH_SHORT).show();

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) {
                                showMessageOKCancel("You need to allow access to both the permissions",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                            PERMISSION_REQUEST_CODE);
                                                }
                                            }
                                        });
                                return;
                            }
                        }

                    }
                }


                break;
        }

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getContext())
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }


    @Override
    public void onStart() {
        super.onStart();
        initAdapter1();
        initAdapter2();
    }

    private void initAdapter2() {
        FirebaseRecyclerAdapter<MyMessages,DownloadedMessageViewHolder> downloadedMessageAdapter = new FirebaseRecyclerAdapter<MyMessages, DownloadedMessageViewHolder>(
                MyMessages.class,
                R.layout.single_my_message,
                DownloadedMessageViewHolder.class,
                downloadecMessages
        ) {
            @Override
            protected void populateViewHolder(final DownloadedMessageViewHolder viewHolder, final MyMessages model, int position) {

                viewHolder.setMessageName(model.getMessageTitle());
                viewHolder.setMeesageImage(model.getMessageImageUrl(),getContext());
                viewHolder.popupmenu.setVisibility(View.VISIBLE);
                viewHolder.progressBar.setVisibility(View.INVISIBLE);

                if (model.getType().equalsIgnoreCase("audio")){
                    viewHolder.labelView.setTriangleBackgroundColorResource(R.color.colorAccentnew);
                    viewHolder.labelView.setPrimaryText("Audio");
                }else if (model.getType().equalsIgnoreCase("book")){
                    viewHolder.labelView.setTriangleBackgroundColorResource(R.color.red);
                    viewHolder.labelView.setPrimaryText("Book");
                }



                viewHolder.popupmenu.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PopupMenu popup = new PopupMenu(getContext(), viewHolder.popupmenu);
                        popup.getMenuInflater()
                                .inflate(R.menu.popup, popup.getMenu());

                        //registering popup with OnMenuItemClickListener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()){
                                    case R.id.delete:
                                      downloadecMessages.child(model.getMessageKey()).removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(getContext(), "Message successfully deleted", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            });

                                }
                                return true;
                            }
                        });

                        popup.show();

                    }




                });

                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getType().equalsIgnoreCase("audio")){

                            Intent offlineAudio = new Intent(getContext(), OfflineAudioPlayer.class);
                            offlineAudio.putExtra("path",model.getMessageDownloadUrl());
                            offlineAudio.putExtra("image",model.getMessageImageUrl());
                            getContext().startActivity(offlineAudio);


                        }else if (model.getType().equalsIgnoreCase("video")){

                            Intent offlineVideo = new Intent(getContext(), VideoActivity.class);
                            offlineVideo.putExtra("path",model.getMessageDownloadUrl());
                            getContext().startActivity(offlineVideo);
                        }else {
                            Intent offlineVideo = new Intent(getContext(), OfflinPdfReader.class);
                            offlineVideo.putExtra("path",model.getMessageDownloadUrl());
                            getContext().startActivity(offlineVideo);
                        }
                    }
                });

            }
        };

        downloadedMessageRv.setAdapter(downloadedMessageAdapter);
    }

    private void initAdapter1() {

        FirebaseRecyclerAdapter<MyMessages,BoughtMessageViewHolder> boughtAdapter = new FirebaseRecyclerAdapter<MyMessages, BoughtMessageViewHolder>(

                MyMessages.class,
                R.layout.single_my_message,
                BoughtMessageViewHolder.class,
                boughtMessage
        ) {
            @Override
            protected void populateViewHolder(final BoughtMessageViewHolder viewHolder, final MyMessages model, int position) {

                viewHolder.setMessageName(model.getMessageTitle());
                viewHolder.setMeesageImage(model.getMessageImageUrl(),getContext());
                viewHolder.popupmenu.setVisibility(View.INVISIBLE);

                if (model.getType().equalsIgnoreCase("audio")){
                    viewHolder.labelView.setTriangleBackgroundColorResource(R.color.colorAccentnew);
                    viewHolder.labelView.setPrimaryText("Audio");
                }else if (model.getType().equalsIgnoreCase("book")){
                    viewHolder.labelView.setTriangleBackgroundColorResource(R.color.red);
                    viewHolder.labelView.setPrimaryText("Book");
                }


                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getType().equalsIgnoreCase("audio")){
                            showAlert("Listen",model.getMessageDownloadUrl(),viewHolder,model);
                        }else if (model.getType().equalsIgnoreCase("video")){
                            showAlert("Watch",model.getMessageDownloadUrl(),viewHolder,model);
                        }else {
                            showAlert("Read",model.getMessageDownloadUrl(),viewHolder,model);
                        }
                    }
                });

            }
        };

        boughtMessageRv.setAdapter(boughtAdapter);
    }


    private void readPdf(String downloadUrl) {
        Intent pdfReader = new Intent(getContext(), PdfReader.class);
        pdfReader.putExtra("pdfUrl",downloadUrl);
        getContext().startActivity(pdfReader);
    }


    private void ListenAudio(String downloadUrl) {
        Intent playAudio = new Intent(getContext(), PlayerActivtiy.class);
        playAudio.putExtra("url",downloadUrl);
        getContext().startActivity(playAudio);

    }

    private void watchVideo(String downloadUrl) {
        Intent playAudio = new Intent(getContext(), PlayerActivtiy.class);
        playAudio.putExtra("url",downloadUrl);
        getContext().startActivity(playAudio);
    }


    private void showAlert(final String action, final String downloadUrl, final BoughtMessageViewHolder viewHolder, final MyMessages model) {

        String textToShow[] = {action,"Download message","Delete Message"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setItems(textToShow, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // The 'which' argument contains the index position
                // of the selected item
                switch(which){
                    case 0:
                        //Do this and this
                       if (action.equalsIgnoreCase("Listen")){
                           ListenAudio(downloadUrl);
                       }else if (action.equalsIgnoreCase("Watch")){
                           watchVideo(downloadUrl);
                       }else {
                           readPdf(downloadUrl);
                       }
                        break;
                    case 1:
                        //Do this and this
                        startFireBaseDownload(model.getMessageDownloadUrl(),model.getType(),model.getExtension(),viewHolder,model);
                        break;
                    case 2:
                        //Do this and this:

                        boughtMessage.child(model.getMessageKey()).removeValue()
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(getContext(), "Message successfully deleted", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    default: //For all other cases, do this        break;
                }
            }
        });
        builder.create();
        builder.show();
    }



    private void startFireBaseDownload(String url, String type, String exetension, final BoughtMessageViewHolder viewHolder, final MyMessages model) {


        if (checkPermission()) {
            mod = model;

            PowerManager powerManager = (PowerManager) getContext().getSystemService(POWER_SERVICE);
            final PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                    "MyApp::MyWakelockTag");
            wakeLock.acquire();
            storageRef = storage.getReferenceFromUrl(url);

            File dir = new File(Environment.getExternalStorageDirectory(), "Salvation ministries media");
            final File file = new File(dir, UUID.randomUUID().toString() + exetension);
            try {
                if (!dir.exists()) {
                    dir.mkdir();
                }
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // final File localFile = File.createTempFile(type, exetension);

            final FileDownloadTask fileDownloadTask = storageRef.getFile(file);

            fileDownloadTask.addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {

                    //Toast.makeText(getContext(), "your file is saved at "+file.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    files = file.getAbsolutePath();
                    MyMessages myMessages = new MyMessages();
                    myMessages.setExtension(model.getExtension());
                    myMessages.setType(model.getType());
                    myMessages.setMessageKey(model.getMessageKey());
                    myMessages.setMessageDownloadUrl(file.getAbsolutePath());
                    myMessages.setMessageImageUrl(model.getMessageImageUrl());
                    myMessages.setMessageTitle(model.getMessageTitle());

                    downloadecMessages.child(model.getMessageKey()).setValue(myMessages)
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
                                        Toast.makeText(getContext(), "Message successfully downloaded", Toast.LENGTH_SHORT).show();
                                        viewHolder.progressBar.setVisibility(View.INVISIBLE);
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
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
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
                    Toast.makeText(getContext(), String.valueOf(exception), Toast.LENGTH_SHORT).show();
//                Helper.dismissProgressDialog();
//                mTextView.setText(String.format("Failure: %s", exception.getMessage()));
                }
            }).addOnProgressListener(new OnProgressListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onProgress(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    double progress =  (100 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());

                    viewHolder.progressBar.setVisibility(View.VISIBLE);
                    viewHolder.progressBar.setProgress(((int) progress));
//                Helper.setProgress(progress);
                }
            });



        } else {

           requestPermission();
        }



    }
    public static class BoughtMessageViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView messageImage;
        ImageView popupmenu;
        TextView messageName;
        ProgressBar progressBar;
        TriangleLabelView labelView;


        public BoughtMessageViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
           messageImage = (ImageView)mView.findViewById(R.id.imageThumbBought);
           popupmenu = (ImageView)mView.findViewById(R.id.showMenu);
           messageName = (TextView)mView.findViewById(R.id.txtBoughtMessageName);
           progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
            labelView = (TriangleLabelView)mView.findViewById(R.id.boughtTriangularLabel);
        }

        public void setMessageName(String name){

            messageName.setText(name);

        }

        public void setMeesageImage(final String status, final Context context){

            Picasso.with(context).load(status).transform(new CircleTransform()) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(status).into(messageImage);

                }
            });



        }




    }

    public static class DownloadedMessageViewHolder extends RecyclerView.ViewHolder {

        View mView;
        ImageView messageImage;
        ImageView popupmenu;
        TextView messageName;
        ProgressBar progressBar;
        TriangleLabelView labelView;


        public DownloadedMessageViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mView = itemView;
            messageImage = (ImageView)mView.findViewById(R.id.imageThumbBought);
            popupmenu = (ImageView)mView.findViewById(R.id.showMenu);
            messageName = (TextView)mView.findViewById(R.id.txtBoughtMessageName);
            progressBar = (ProgressBar)mView.findViewById(R.id.progressBar);
            labelView = (TriangleLabelView)mView.findViewById(R.id.boughtTriangularLabel);

        }

        public void setMessageName(String name){

            messageName.setText(name);

        }

        public void setMeesageImage(final String status, final Context context){

            Picasso.with(context).load(status).transform(new CircleTransform()) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(context)
                            .load(status).into(messageImage);

                }
            });



        }


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