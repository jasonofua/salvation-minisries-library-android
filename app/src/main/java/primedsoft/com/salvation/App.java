package primedsoft.com.salvation;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso built = builder.build();
        built.setIndicatorsEnabled(false);
        built.setLoggingEnabled(true);
        Picasso.setSingletonInstance(built);




    }

    public void sendSuccessfullRegistrationEmail(String userEmail){
        //Instantiate the object using your API key String
//        SendGrid sendgrid = new SendGrid("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");
//        SendGrid.Email email = new SendGrid.Email();
//        email.addTo(userEmail);
//        email.setFrom("info@smdl.com");
//        email.setSubject("Successful account registration");
//        email.setText("Welcome to Salvation Ministries Digital Library. Your Login detail are as follows: Email : "+ userEmail);
//
//        try {
//            SendGrid.Response response = sendgrid.send(email);
//            Toast.makeText(this, response.getMessage(), Toast.LENGTH_SHORT).show();
//        }catch (SendGridException e) {
//            Log.e("sendError", "Error sending email");
//        }

    }
}
