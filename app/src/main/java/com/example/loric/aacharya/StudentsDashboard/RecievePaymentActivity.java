package com.example.loric.aacharya.StudentsDashboard;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.loric.aacharya.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;


public class RecievePaymentActivity extends AppCompatActivity implements PaymentResultListener {
    Button sendBtn;
    TextView priceBook, titleBook;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth mAuth;
    String bookName, bookPrice;
    String DOCUMENT_ID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recieve_payment);

        Checkout.preload(getApplicationContext());

        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        sendBtn = findViewById(R.id.pay_btn_upi);
        priceBook = findViewById(R.id.pay_amount_upi);
        titleBook = findViewById(R.id.book_name_upi);


        bookName = getIntent().getStringExtra("BOOK_TITLE");
        bookPrice = getIntent().getStringExtra("BOOK_PRICE");
        DOCUMENT_ID = getIntent().getStringExtra("DOC_ID");

        String rs = getResources().getString(R.string.Rs);
        priceBook.setText(rs + " " + bookPrice);
        titleBook.setText("for " + Html.fromHtml("<b>" + bookName + "</b>"));
        if (bookName != null && bookPrice != null && DOCUMENT_ID != null) {
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startPayment();
                }
            });
        }
    }


    public void startPayment() {

        Integer amountPayout = Integer.parseInt(bookPrice)*100;

        Checkout checkout = new Checkout();
        checkout.setImage(R.drawable.logo_aacharya);
        final Activity activity = this;
        try {
            JSONObject options = new JSONObject();

            options.put("name", "Aacharya Institute");
            options.put("description", "Payment for book/notes purchase.");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");

            options.put("theme.color", "#3399cc");
            options.put("currency", "INR");
            options.put("amount", ""+amountPayout);//pass amount in currency subunits
            options.put("prefill.email", "loric.aacharya@gmail.com");
            options.put("prefill.contact", "7808722222");
            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in starting Razorpay Checkout", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onPaymentSuccess(String s) {
        firebaseFirestore.collection("BOOKS_NOTES").document(DOCUMENT_ID)
                .update("allowed_users", FieldValue.arrayUnion(mAuth.getCurrentUser().getUid()))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(RecievePaymentActivity.this, "Payment Successfull :" + s, Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RecievePaymentActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(this, "Payment unccessfull :" + s, Toast.LENGTH_SHORT).show();
    }
}