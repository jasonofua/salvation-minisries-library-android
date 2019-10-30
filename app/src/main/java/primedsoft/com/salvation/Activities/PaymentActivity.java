package primedsoft.com.salvation.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import primedsoft.com.salvation.Model.MyMessages;
import primedsoft.com.salvation.R;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.GetTransactionResponseMessage;
import primedsoft.com.salvation.Wsdl2Code.WebServices.EnterpriseAPISoapService.IWsdl2CodeEvents;
import primedsoft.com.salvation.landingpage.Home;
import primedsoft.com.salvation.splashscreen;

public class PaymentActivity extends AppCompatActivity implements IWsdl2CodeEvents{

    //vars
    private static final String STAGING_API_URL = "https://secure.payu.co.za/service/PayUAPI";
   // private static final String LIVE_API_URL = "https://secure.payu.co.za/service/PayUAPI";
   private static final String SUCCESS_URL = "http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payu-redirectpaymentpage/send-getTransaction-via-soap.php", FAIL_URL = ">http://qa.payu.co.za/integration-qa/internal-tools/demos/developer/payu-redirectpaymentpage/cancel-page.php";
    private static final String USERNAME = "300110", PASSWORD = "fI4lju1d";
    private DatabaseReference myMessage,adminBought;
    private FirebaseAuth auth;
    private static final String SAFEKEY = "{C7ED23DB-7494-40FB-98F5-E3B2FBDD4C75}";
    static Activity activity;
    ProgressDialog dialog;
    //views
    WebView webview;
    GetTransactionResponseMessage getTransactionResponseMessage;
    private String url,payUReference,messageTitle,messageDownloadUrl,messageImageUrl,messageKey,type,extension;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        myMessage = FirebaseDatabase.getInstance().getReference().child("BoughtMessages");
        adminBought = FirebaseDatabase.getInstance().getReference().child("AdminBoughtMessages");
        auth = FirebaseAuth.getInstance();

        url = getIntent().getStringExtra("url");
        payUReference = getIntent().getStringExtra("payuref");
        messageTitle = getIntent().getStringExtra("messageTitle");
        messageImageUrl = getIntent().getStringExtra("messageImageUrl");
        messageDownloadUrl = getIntent().getStringExtra("messageDownloadUrl");
        messageKey = getIntent().getStringExtra("messageKey");
        type = getIntent().getStringExtra("messageType");
        extension = getIntent().getStringExtra("messageExtension");
        dialog = new ProgressDialog(this);


        dialog.setMessage("Loading....");
        dialog.show();

        activity = this;

        //get viewswe
        webview = (WebView) findViewById(R.id.webview);

        //setup soap
        //setup webview
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setBuiltInZoomControls(true);
        webview.getSettings().setDisplayZoomControls(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.setWebViewClient(new WebViewClient(){

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                dialog.show();
                if (url.startsWith(SUCCESS_URL)) {
                   dialog.dismiss();
                   //look for success/failure URL
//                    EnterpriseAPISoapService srv1 = new EnterpriseAPISoapService((IWsdl2CodeEvents) activity);
//                   srv1.setUrl(STAGING_API_URL);
//                    additionalInfo additionalInfo = new additionalInfo();
//                   additionalInfo.payUReference = payUReference;
//                   additionalInfo.merchantReference = payUReference;
//                   try {
//                        srv1.getTransactionAsync("ONE_ZERO", SAFEKEY, additionalInfo, USERNAME, PASSWORD);
//                   } catch (Exception e) {
//                        e.printStackTrace();
//                       Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }\\

                    Toast.makeText(activity, "Yay, payment success!", Toast.LENGTH_SHORT).show();
                    saveMessageToDB();


                }else
                    view.loadUrl(url);


                return true;
            }
            @Override
            public void onPageFinished(WebView view, final String url) {
                dialog.dismiss();
            }
        });

        webview.loadUrl(url);

    }


    @Override
    public void Wsdl2CodeStartedRequest() {
        Log.e("Wsdl2Code", "Wsdl2CodeStartedRequest");

    }

    @Override
    public void Wsdl2CodeFinished(String methodName, Object Data) {
        Log.e("Wsdl2Code", "Wsdl2CodeFinished");
        Log.e("Wsdl2Code", methodName);
        if (methodName.equals("getTransaction")) {
            getTransactionResponseMessage = ((GetTransactionResponseMessage) Data);

            if (getTransactionResponseMessage != null && getTransactionResponseMessage.successful) {
                //do success stuff here


            } else {
                //do failure stuff here
                Toast.makeText(activity, "Aah, payment failed!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(PaymentActivity.this, splashscreen.class));
            }
        }
    }

    private void saveMessageToDB() {
        String uid = auth.getCurrentUser().getUid();
        final MyMessages messages = new MyMessages();
        messages.setMessageTitle(messageTitle);
        messages.setMessageImageUrl(messageImageUrl);
        messages.setMessageDownloadUrl(messageDownloadUrl);
        messages.setMessageKey(messageKey);
        messages.setType(type);
        messages.setExtension(extension);

        myMessage.child(uid).child(messageKey).setValue(messages)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            adminBought.push().setValue(messages).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                        Toast.makeText(PaymentActivity.this, "Congratulations, message successfully bought. Go to your directory tab to download or play it", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(PaymentActivity.this,Home.class));

                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                             }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PaymentActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void Wsdl2CodeFinishedWithException(Exception ex) {
        Log.e("Wsdl2Code", "Wsdl2CodeFinishedWithException");

    }

    @Override
    public void Wsdl2CodeEndedRequest() {
        Log.e("Wsdl2Code", "Wsdl2CodeEndedRequest");
    }

}
