package primedsoft.com.salvation.Adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import jp.shts.android.library.TriangleLabelView;
import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.Activities.PaymentActivity;
import primedsoft.com.salvation.Activities.PdfReader;
import primedsoft.com.salvation.Activities.PlayerActivtiy;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.Model.Upload;
import primedsoft.com.salvation.PayU.ApiVersion;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.EnterpriseAPISoapService;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.SetTransaction;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.SetTransactionResponseMessage;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.WS_Enums;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.additionalInfo;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.basket;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.customer;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MyViewHolder> {

    //vars
    private static final String STAGING_API_URL = "https://secure.payu.co.za/service/PayUAPI";
    private static final String LIVE_API_URL = "https://secure.payu.co.za/service/PayUAPI";
    private static final String SUCCESS_URL = "http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payu-redirectpaymentpage/send-getTransaction-via-soap.php", FAIL_URL = "http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payu-redirectpaymentpage/cancel-page.php";
    private static final String USERNAME = "300110", PASSWORD = "fI4lju1d";
    private static final String SAFEKEY = "{C7ED23DB-7494-40FB-98F5-E3B2FBDD4C75}";

    private static SetTransactionResponseMessage setTransactionResponseMessage;

    //views
    WebView webview;
    private boolean isStaging = true;

    public static SetTransaction buildSetTransaction(MessageModel model,Upload userInfo) {
        SetTransaction setTransaction = new SetTransaction();
        setTransaction.set_Api(ApiVersion.ONE_ZERO.name());

        // AdditionalInfo
        additionalInfo addInfo = new additionalInfo();
        addInfo.merchantReference = "payment";
        addInfo.demoMode = "false";
        addInfo.cancelUrl = FAIL_URL;
        addInfo.returnUrlField = SUCCESS_URL;
        addInfo.notificationUrl = "http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payuredirectpayment-page/notification-page.php";
        addInfo.supportedPaymentMethods = "CREDITCARD";
        addInfo.redirectChannel = "responsive";

        setTransaction.set_AdditionalInformation(addInfo);


        if (userInfo.getCurrency().equalsIgnoreCase("dollars")) {
            int amountInCents = Integer.parseInt(model.getPriceDollar()) * 100;

            basket basket = new basket();
            basket.amountInCents = String.valueOf(amountInCents);
            basket.currencyCode = "USD";
            basket.description = "Payment for message: "+model.getTitle();
            setTransaction.set_Basket(basket);

        }else {
            int amountInCents = Integer.parseInt(model.getPriceNaira()) * 100;
            basket basket = new basket();
            basket.amountInCents = String.valueOf(amountInCents);
            basket.currencyCode = "NGN";
            basket.description = "Payment for message: "+model.getTitle();
            setTransaction.set_Basket(basket);

        }

        customer customer = new customer();
        customer.firstName = userInfo.getFirstName();
        customer.lastName = userInfo.getLastName();
        customer.email = "john@doe";
        customer.mobile = "0821234567";
        setTransaction.set_Customer(customer);


        setTransaction.set_Safekey(SAFEKEY);
        setTransaction.set_TransactionType(WS_Enums.transactionType.PAYMENT);

        return setTransaction;
    }

    List<MessageModel> messageModels = new ArrayList<>();
    Upload userInfo ;
    Context context;
    FirebaseStorage storage;
    StorageReference storageRef ;
    ProgressDialog progressDialog;
    FirebaseAuth auth;
    DatabaseReference userRef,boughtMessageRef;
    String currency;

    public MessageAdapter(List<MessageModel> messageModels) {
        this.messageModels = messageModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_message_view,parent,false);
        context = parent.getContext();

        storage = FirebaseStorage.getInstance();
        progressDialog = new ProgressDialog(context);
        auth = FirebaseAuth.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");
        boughtMessageRef = FirebaseDatabase.getInstance().getReference().child("Bought Messages");

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        loadSingleMessage(holder, position);
    }

    private void loadSingleMessage(@NonNull MyViewHolder holder, int position) {
        final MessageModel model = messageModels.get(position);

        final String uid = auth.getCurrentUser().getUid();
        String type = model.getType();


        userRef.child(uid)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            userInfo = dataSnapshot.getValue(Upload.class);
                            currency = dataSnapshot.child("currency").getValue().toString();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        holder.setUserStatus(model.getType());

        if (type.equalsIgnoreCase("audio")){
            holder.labelView.setTriangleBackgroundColorResource(R.color.colorAccentnew);
        }else if (type.equalsIgnoreCase("book")){
            holder.labelView.setTriangleBackgroundColorResource(R.color.red);
        }
        if(model != null|| !model.getImageUrl().isEmpty()|| !model.getImageUrl().equalsIgnoreCase("")) {
            holder.setUserImage(model.getImageUrl(),holder.messageImage.getContext());
        }
        holder.setMessageName(model.getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();

                rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild("BoughtMessages")){
                            DatabaseReference boughtMessageRef = FirebaseDatabase.getInstance().getReference().child("BoughtMessages");

                            boughtMessageRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(uid)){
                                        DatabaseReference ownedMessage = FirebaseDatabase.getInstance().getReference().child("BoughtMessages")
                                                .child(uid);
                                        ownedMessage.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChild(model.getKey())){
                                                    showPaidMethod(model);
                                                }else{

                                                    showBuyDialog(model);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    }else {
                                        showBuyDialog(model);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else {
                            showBuyDialog(model);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private void showPaidMethod(final MessageModel model) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.single_message_buy);
        TriangleLabelView labelView = dialog.findViewById(R.id.triangularLabelBuy);
        final ImageView messageImage = dialog.findViewById(R.id.buyImageThumbNail);
        TextView messageTitle = dialog.findViewById(R.id.messageTitle);
        TextView messageAuthor = dialog.findViewById(R.id.messageBuyAuthor);
        FancyButton button = dialog.findViewById(R.id.btnPlay);
        FancyButton buttonBuy = dialog.findViewById(R.id.btnBuy);
        ImageView back = dialog.findViewById(R.id.imgBack);


        buttonBuy.setVisibility(View.GONE);

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


        if (currency.equalsIgnoreCase("dollars")){
            buttonBuy.setText("BUY USD "+model.getPriceDollar());
        }else {
            buttonBuy.setText("BUY NGN "+model.getPriceNaira());
        }


        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO integrate the payu redirect

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        callWebService(model,userInfo);
                        return null;
                    }

                }.execute();


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
                    readPdf(model.getDownloadUrl());
                }
            });
        }
        dialog.show();


        messageTitle.setText(model.getTitle());
        messageAuthor.setText(model.getAuthor());
    }

    private void showBuyDialog(final MessageModel model) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.single_message_buy);
        TriangleLabelView labelView = dialog.findViewById(R.id.triangularLabelBuy);
        final ImageView messageImage = dialog.findViewById(R.id.buyImageThumbNail);
        TextView messageTitle = dialog.findViewById(R.id.messageTitle);
        TextView messageAuthor = dialog.findViewById(R.id.messageBuyAuthor);
        FancyButton button = dialog.findViewById(R.id.btnPlay);
        FancyButton buttonBuy = dialog.findViewById(R.id.btnBuy);
        ImageView back = dialog.findViewById(R.id.imgBack);

        button.setVisibility(View.GONE);

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


        if (currency.equalsIgnoreCase("dollars")){
            buttonBuy.setText("BUY USD "+model.getPriceDollar());
        }else {
            buttonBuy.setText("BUY NGN "+model.getPriceNaira());
        }


        buttonBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO integrate the payu redirect

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... params) {
                        callWebService(model,userInfo);
                        return null;
                    }

                }.execute();


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
                    readPdf(model.getDownloadUrl());
                }
            });
        }
        dialog.show();


        messageTitle.setText(model.getTitle());
        messageAuthor.setText(model.getAuthor());
    }

    public void callWebService(final MessageModel model, Upload user) {

        try {
            EnterpriseAPISoapService srv1 = new EnterpriseAPISoapService();
            srv1.setUrl(STAGING_API_URL);

            SetTransaction setTran = buildSetTransaction(model,user);
            setTransactionResponseMessage = srv1.setTransaction(setTran.get_Api(), setTran.get_SafeKey(), setTran.get_TransactionType(), true, false, false,
                    setTran.get_AdditionalInfo(), setTran.get_Customer(), setTran.get_baBasket(), null, null, null, null, null, null, null, null, null, null, null, null, USERNAME, PASSWORD);

            ((Activity) context).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //shake the webView here!
                    if (setTransactionResponseMessage != null) {
                        if (isStaging) {

                            Intent pdfReader = new Intent(context, PaymentActivity.class);
                            pdfReader.putExtra("url","https://secure.payu.co.za/rpp.do?PayUReference="+ setTransactionResponseMessage.payUReference);
                            pdfReader.putExtra("payuref",setTransactionResponseMessage.payUReference);
                            pdfReader.putExtra("messageKey",model.getKey());
                            pdfReader.putExtra("messageTitle",model.getTitle());
                            pdfReader.putExtra("messageDownloadUrl",model.getDownloadUrl());
                            pdfReader.putExtra("messageImageUrl",model.getImageUrl());
                            pdfReader.putExtra("messageType",model.getType());
                            pdfReader.putExtra("messageExtension",model.getExetension());
                        context.startActivity(pdfReader);

                        } else {
                            webview.loadUrl("https://secure.payu.co.za/rpp.do?PayUReference=" + setTransactionResponseMessage.payUReference);
                        }
                    }
                }
            });
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
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



    @Override
    public int getItemCount() {
        return messageModels.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TriangleLabelView labelView;
        View mView;
        ImageView messageImage;
        TextView messageName,messageAuthor;
        public MyViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
            labelView = mView.findViewById(R.id.triangularLabel);
            messageImage = mView.findViewById(R.id.imageThumb);
            messageName = mView.findViewById(R.id.txtMessageName);
            messageAuthor = mView.findViewById(R.id.messageAuthor);
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
//            Picasso.with(context).load(thumb_image) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
//                @Override
//                public void onSuccess() {
//
//                }
//
//                @Override
//                public void onError() {
//                    Picasso.with(context)
//                            .load(thumb_image)
//                            .into(messageImage);
//
//
//                }
//            });

            Glide.with(context)
                    .load(thumb_image)
                    .asBitmap()
                    .skipMemoryCache( false )
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(messageImage);

        }

    }



}
