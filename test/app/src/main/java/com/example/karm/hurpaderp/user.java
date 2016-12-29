package com.example.karm.hurpaderp;

import android.content.Intent;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class user extends AppCompatActivity {
    String name=null;
    String freq =null;
    String uid = null;
    String[] menu = {"coffee","tea","juice"};
    int[] qty = new int[]{0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        final TextView name_v = (TextView)findViewById(R.id.name);
        final TextView freq_v = (TextView)findViewById(R.id.freq);
        final Button freq_order = (Button) findViewById(R.id.order_freq);
        final Button order = (Button) findViewById(R.id.order);


        ArrayAdapter adapter = new ArrayAdapter<String>(this,R.layout.item,menu);
        ListView listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(adapter);
        Intent i = getIntent();
        String json = i.getExtras().getString("json");

        try {
            JSONObject reader = new JSONObject(json);
            JSONObject sys  = reader.getJSONObject("json");
            name = sys.getString("name");
            freq = sys.getString("freq");
            uid = sys.getString("uid");
            name_v.setText(name);
            freq_v.setText(freq);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                qty[position]++;
            }
        });

        freq_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_freq foo = new send_freq();
                foo.execute();
            }
        });

        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_order foo = new send_order();
                foo.execute();
            }
        });
    }


    public class send_freq extends AsyncTask<Void,Void,Void> {
        OkHttpClient client = new OkHttpClient();
        Request request;

        String post(String url) throws IOException {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("who", uid)
                    .addFormDataPart("action", "order")
                    .addFormDataPart("what", freq)
                    .addFormDataPart("price", "100")
                    .addFormDataPart("qty", "1")
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
        protected Void doInBackground(Void... params) {

            String response = null;
            try {
                response = post("http://10.0.0.54:655");
            } catch (IOException e) {
                e.printStackTrace();
            }
            //System.out.println(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        }

    }

    public class send_order extends AsyncTask<Void,Void,Void> {
        OkHttpClient client = new OkHttpClient();
        Request request;

        String post(String url,String item, int qty) throws IOException {
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("who", uid)
                    .addFormDataPart("action", "order")
                    .addFormDataPart("what", item)
                    .addFormDataPart("price", "100")
                    .addFormDataPart("qty", String.valueOf(qty))
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
        protected Void doInBackground(Void... params) {

            String response = null;
            for(int i=0;i<menu.length;i++){
                try {
                    response = post("http://10.0.0.54:655",menu[i],qty[i]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            //System.out.println(response);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Intent i = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(i);

        }

    }
}
