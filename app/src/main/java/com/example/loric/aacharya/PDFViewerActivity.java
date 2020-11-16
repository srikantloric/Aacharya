package com.example.loric.aacharya;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.barteksc.pdfviewer.PDFView;
import com.leo.simplearcloader.SimpleArcLoader;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class PDFViewerActivity extends AppCompatActivity {

    private TextView textView, titleHeader;
    private PDFView pdfView;
    private String BOOK_URL, TITLE;
    private SimpleArcLoader loader;
    private ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        setContentView(R.layout.activity_p_d_f_viewer);
        pdfView = findViewById(R.id.pdf_viewer);
        titleHeader = findViewById(R.id.back_btn_container);
        textView = findViewById(R.id.text_pdf);
        backBtn = findViewById(R.id.back_btn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        loader = findViewById(R.id.loader);
        BOOK_URL = getIntent().getStringExtra("BOOK_URL");
        TITLE = getIntent().getStringExtra("HEADER_TITLE");
        titleHeader.setText(TITLE);
        new RetrivePdfStream().execute(BOOK_URL);

    }

    class RetrivePdfStream extends AsyncTask<String, Void, InputStream> {

        @Override
        protected InputStream doInBackground(String... strings) {
            InputStream inputStream = null;

            try {
                URL url = new URL(strings[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = new BufferedInputStream(urlConnection.getInputStream());

                }
            } catch (IOException e) {
                return null;
            }
            return inputStream;
        }

        @Override
        protected void onPostExecute(InputStream inputStream) {
            pdfView.fromStream(inputStream).load();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    loader.setVisibility(View.GONE);

                }
            }, 1000);
        }
    }
}