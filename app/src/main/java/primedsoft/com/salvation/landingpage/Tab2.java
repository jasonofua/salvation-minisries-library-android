package primedsoft.com.salvation.landingpage;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import jp.shts.android.library.TriangleLabelView;
import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.Activities.GenreListActivity;
import primedsoft.com.salvation.Activities.PaymentActivity;
import primedsoft.com.salvation.Activities.PdfReader;
import primedsoft.com.salvation.Activities.PlayerActivtiy;
import primedsoft.com.salvation.Adapter.SpecialOfferAdapter;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.Model.SpecialOfferModel;
import primedsoft.com.salvation.Model.Upload;
import primedsoft.com.salvation.PayU.ApiVersion;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Utils.MessageSearchDialogCompact;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.EnterpriseAPISoapService;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.SetTransaction;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.SetTransactionResponseMessage;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.WS_Enums;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.additionalInfo;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.basket;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.customer;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab2 extends Fragment {


    private static final String STAGING_API_URL = "https://secure.payu.co.za/service/PayUAPI";
    private static final String LIVE_API_URL = "https://secure.payu.co.za/service/PayUAPI";
    private static final String SUCCESS_URL = "http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payu-redirectpaymentpage/send-getTransaction-via-soap.php", FAIL_URL = "http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payu-redirectpaymentpage/cancel-page.php";
    private static final String USERNAME = "300110", PASSWORD = "fI4lju1d";
    private static final String SAFEKEY = "{C7ED23DB-7494-40FB-98F5-E3B2FBDD4C75}";

    private static SetTransactionResponseMessage setTransactionResponseMessage;

    //views
    WebView webview;
    private boolean isStaging = true;

    public static SetTransaction buildSetTransaction(MessageModel model, Upload userInfo) {
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

    private DatabaseReference messageRef,offerRef;
    FirebaseStorage storage;
    private FirebaseAuth auth;
    Upload userInfo ;
    String currency;
   // private ArrayList<MessageModel> trendingTopics = new ArrayList<>();
    private RecyclerView rvOffers;
    private ProgressDialog dialog;
    DatabaseReference userRef,rootRef;
    private LinearLayoutManager layoutManager;
    private SpecialOfferAdapter rvAdapter;
    private List<SpecialOfferModel> specialOfferModels = new ArrayList<>();

    private ImageView img1, img3, img4, img5, img6, img8, img9, img11, img15;
    String uid;

    private ArrayList<MessageModel> messageModels = new ArrayList<>();


    public Tab2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_tab2, container, false);
        auth = FirebaseAuth.getInstance();
        uid = auth.getCurrentUser().getUid();
        dialog = new ProgressDialog(getContext());
        rvOffers = (RecyclerView)root.findViewById(R.id.rvSpecialOffer);
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setReverseLayout(true);
        layoutManager.setStackFromEnd(true);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvAdapter = new SpecialOfferAdapter(specialOfferModels);
        rvOffers.setHasFixedSize(true);
        rvOffers.setLayoutManager(layoutManager);
        rvOffers.setAdapter(rvAdapter);
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        offerRef = FirebaseDatabase.getInstance().getReference().child("Special Offers");
        rootRef = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        fetchNewMessages();
        fetchNewOffers();

        img1 = root.findViewById(R.id.img1);
        img3 = root.findViewById(R.id.img3);
        img4 = root.findViewById(R.id.img4);
        img5 = root.findViewById(R.id.img5);
        img6 = root.findViewById(R.id.img6);
        img8 = root.findViewById(R.id.img8);
        img9 = root.findViewById(R.id.img9);
        img11 = root.findViewById(R.id.img11);
        img15 = root.findViewById(R.id.img15);

        img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("WORD", R.drawable.word);
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

        img11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("PRAYER", R.drawable.prayer);
            }
        });

        img15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToList("BUSINESS/CAREER", R.drawable.business);
            }
        });
        Button search = (Button)root.findViewById(R.id.btnOpenSearch);




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



        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MessageSearchDialogCompact<>(getContext(), "Search...",
                        "What are you looking for...?", null, messageModels,
                        new SearchResultListener<MessageModel>() {
                            @Override
                            public void onSelected(
                                    BaseSearchDialogCompat dialog,
                                    final MessageModel item, int position
                            ) {
                                String type = item.getType();

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
                                                                if (dataSnapshot.hasChild(item.getKey())){
                                                                    showPaidMethod(item);
                                                                }else{

                                                                    showBuyDialog(item);
                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });

                                                    }else {
                                                        showBuyDialog(item);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }else {
                                            showBuyDialog(item);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });



                            }
                        }
                ).show();
            }
        });

        return  root;
    }

    private void showPaidMethod(final MessageModel model) {
        final Dialog dialog = new Dialog(getContext());
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

        Picasso.with(getContext()).load(model.getImageUrl()) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getContext())
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
        final Dialog dialog = new Dialog(getContext());
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

        Picasso.with(getContext()).load(model.getImageUrl()) .networkPolicy(NetworkPolicy.OFFLINE).into(messageImage, new Callback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onError() {
                Picasso.with(getContext())
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
    private void fetchNewOffers() {
        dialog.setMessage("Fetch Special Offers.. please wait");
        dialog.show();
        specialOfferModels.clear();
        offerRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot.exists()) {
                    dialog.dismiss();
                    SpecialOfferModel message = dataSnapshot.getValue(SpecialOfferModel.class);
                    specialOfferModels.add(message);
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

    private void goToList(String church_growth, int word) {

        Intent showGenreList = new Intent(getContext(), GenreListActivity.class);
        showGenreList.putExtra("GenreName", church_growth);
        Bundle bundle=new Bundle();
        bundle.putInt("image",word);
        showGenreList.putExtras(bundle);
        getContext().startActivity(showGenreList);
    }

    private void fetchNewMessages() {
        dialog.setMessage("updating your page.. please wait");
        dialog.show();
        messageModels.clear();
        messageRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                if (dataSnapshot.exists()) {
                    dialog.dismiss();
                    MessageModel message = dataSnapshot.getValue(MessageModel.class);
                    messageModels.add(message);
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
    public void callWebService(final MessageModel model, Upload user) {

        try {
            EnterpriseAPISoapService srv1 = new EnterpriseAPISoapService();
            srv1.setUrl(STAGING_API_URL);

            SetTransaction setTran = buildSetTransaction(model,user);
            setTransactionResponseMessage = srv1.setTransaction(setTran.get_Api(), setTran.get_SafeKey(), setTran.get_TransactionType(), true, false, false,
                    setTran.get_AdditionalInfo(), setTran.get_Customer(), setTran.get_baBasket(), null, null, null, null, null, null, null, null, null, null, null, null, USERNAME, PASSWORD);

            ((Activity) getContext()).runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    //shake the webView here!
                    if (setTransactionResponseMessage != null) {
                        if (isStaging) {

                            Intent pdfReader = new Intent(getContext(), PaymentActivity.class);
                            pdfReader.putExtra("url","https://secure.payu.co.za/rpp.do?PayUReference="+ setTransactionResponseMessage.payUReference);
                            pdfReader.putExtra("payuref",setTransactionResponseMessage.payUReference);
                            pdfReader.putExtra("messageKey",model.getKey());
                            pdfReader.putExtra("messageTitle",model.getTitle());
                            pdfReader.putExtra("messageDownloadUrl",model.getDownloadUrl());
                            pdfReader.putExtra("messageImageUrl",model.getImageUrl());
                            pdfReader.putExtra("messageType",model.getType());
                            pdfReader.putExtra("messageExtension",model.getExetension());
                            getContext().startActivity(pdfReader);

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
