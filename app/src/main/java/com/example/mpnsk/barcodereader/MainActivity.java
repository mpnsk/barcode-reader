package com.example.mpnsk.barcodereader;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.lang.ref.WeakReference;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (scanResult != null) {
            // handle scan result
            System.out.println(scanResult);
            new HttpRequestTask(this).execute(scanResult.getContents());
            scan(null);
        }
    }

    public void scan(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.initiateScan(Arrays.asList("EAN_8", "EAN_13"));
    }

    static class HttpRequestTask extends AsyncTask<String, Void, Article> {
        private final WeakReference<Activity> reference;

        HttpRequestTask(Activity activity) {
            this.reference = new WeakReference<>(activity);
        }

        @Override
        protected Article doInBackground(String... params) {
            try {
                final String url = "http://my.ddns.net:8080/?ean=";
                final String ean = params[0];
                RestTemplate restTemplate = new RestTemplate();
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
                Article greeting = restTemplate.getForObject(url + ean, Article.class);
                return greeting;
            } catch (Exception e) {
                Log.e("MainActivity", e.getMessage(), e);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Article article) {
            try {
                Log.d("rest-response", article.toString());
                Toast.makeText(reference.get(), article.toString(), Toast.LENGTH_LONG).show();
            } catch (NullPointerException e){
                Log.d("rest-response",  e.getMessage());
                Log.getStackTraceString(e);
            }

        }

    }
}
