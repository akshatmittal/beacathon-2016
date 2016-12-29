package com.example.karm.hurpaderp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class hurdur extends AppCompatActivity {
String uuid = "0x0117c55667bc";
    String response = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hurdur);
        Log.d("MAINSHIT","LAYOUTSBITCH");
        send_req foo = new send_req();
        foo.execute();

    }

    public class send_req extends AsyncTask<String,Void,String>{
        OkHttpClient client = new OkHttpClient();
        Request request;

        String post(String url) throws IOException {
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uuid", uuid)
                .addFormDataPart("action", "fetch")
                .addFormDataPart("version", "v1")
                .build();

            request = new Request.Builder()
                    .url(url)
                    .method("POST", RequestBody.create(null, new byte[0]))
                    .post(requestBody)
                    .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
        }

        @Override
        protected String doInBackground(String... params) {

            try {
                response = post("http://10.0.0.54:655");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(response);
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            Intent i = new Intent(getApplicationContext(),user.class);
            i.putExtra("json",result);
            startActivity(i);
            System.out.println(result);
        }

    }
}
