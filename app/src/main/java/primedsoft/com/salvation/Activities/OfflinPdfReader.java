package primedsoft.com.salvation.Activities;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.io.File;

import primedsoft.com.salvation.R;

public class OfflinPdfReader extends AppCompatActivity {

    PDFView pdfView;
    String pdfFileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offlin_pdf_reader);

        pdfFileName = getIntent().getStringExtra("path");
        pdfView = (PDFView)findViewById(R.id.pdfViewOffline);

        Toast.makeText(this, "Loading book..please wait", Toast.LENGTH_SHORT).show();

        File book = new File(pdfFileName);
        pdfView.fromFile(book).load();



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
