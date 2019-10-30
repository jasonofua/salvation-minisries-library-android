package primedsoft.com.salvation.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.braintreepayments.cardform.view.CardForm;

import mehdi.sakout.fancybuttons.FancyButton;
import primedsoft.com.salvation.R;

public class AddDebitCard extends AppCompatActivity {
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String CARDNUMBER = "cardNumber";
    public static final String CARDNAME = "cardName";
    public static final String CARDCVC = "cardCvc";
    public static final String CARDEXPIREMONTH = "cardExpireMonth";
    public static final String CARDEXPIREYEAR = "cardExpireYear";
    SharedPreferences sharedpreferences;
    private EditText cardName;
    FancyButton btnCard;
    CardForm cardForm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_debit_card);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        btnCard = (FancyButton)findViewById(R.id.btnAddCard);
        cardName = (EditText)findViewById(R.id.edtCardName);
        cardForm = findViewById(R.id.card_form);
        cardForm.cardRequired(true)
                .expirationRequired(true)
                .cvvRequired(true)
                .postalCodeRequired(false)
                .mobileNumberRequired(false)
                .setup(AddDebitCard.this);
        cardForm.getCvvEditText().setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);

        btnCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString(CARDNUMBER, cardForm.getCardNumber());
                editor.putString(CARDEXPIREYEAR, cardForm.getExpirationYear());
                editor.putString(CARDEXPIREMONTH, cardForm.getExpirationMonth());
                editor.putString(CARDCVC, cardForm.getCvv());
                editor.putString(CARDNAME, cardName.getText().toString().trim());
                editor.apply();

                Toast.makeText(AddDebitCard.this, "Debit Card Successfully Added", Toast.LENGTH_SHORT).show();
            }
        });


    }
}
