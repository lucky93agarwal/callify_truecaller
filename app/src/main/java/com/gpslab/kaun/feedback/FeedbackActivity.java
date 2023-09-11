package com.gpslab.kaun.feedback;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.material.textfield.TextInputEditText;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.model.MenuItem;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FeedbackActivity extends AppCompatActivity {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;



    public String FullName, Userid, Email, Feedback;

    String resTxt = null;
    public ImageView ivsend,ivback;

    public TextInputEditText etname, etuserid, etemail, etfeedback;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        initViews();

        onClick();



        setData();
    }

    private void setData() {
        String FName = sharedPreferences.getString("fname", "xyz");
        FName = FName.substring(0, 1).toUpperCase() + FName.substring(1);
        String LName = sharedPreferences.getString("lname", "xyz");
        LName = LName.substring(0, 1).toUpperCase() + LName.substring(1);
        etname.setText(FName +" "+LName);
        etname.setEnabled(false);
        etuserid.setText(sharedPreferences.getString("mobile","0"));
        etuserid.setEnabled(false);
        String Email = sharedPreferences.getString("email","0");
        if(Email.length() >5){
            etemail.setEnabled(false);
            etemail.setText(sharedPreferences.getString("email","abc@gmail.com"));
        }

    }


    public void onClick(){
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etname.getText().toString().length() < 1){
                    Toast.makeText(FeedbackActivity.this, "Enter your name", Toast.LENGTH_SHORT).show();
                }else if(etuserid.getText().toString().length() < 1){

                }else if(etemail.getText().toString().length() < 1){
                    Toast.makeText(FeedbackActivity.this, "Enter your email", Toast.LENGTH_SHORT).show();
                }else if(etfeedback.getText().toString().length() < 1){
                    Toast.makeText(FeedbackActivity.this, "Enter your feedback", Toast.LENGTH_SHORT).show();
                }else {
                    FullName = etname.getText().toString();
                    Email = etemail.getText().toString();
                    Userid = etuserid.getText().toString();
                    Feedback = etfeedback.getText().toString();
                    SendDataToServer("");

                }
            }
        });
    }
    public void initViews(){
        ivback = (ImageView)findViewById(R.id.backiv);
        ivsend = (ImageView)findViewById(R.id.sendiv);

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        etname = (TextInputEditText)findViewById(R.id.etfnameet);
        etuserid = (TextInputEditText)findViewById(R.id.etfuseridet);
        etemail = (TextInputEditText)findViewById(R.id.etemail);
        etfeedback = (TextInputEditText)findViewById(R.id.etfeedbacket);
    }




    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickFirstName = datasss;




                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("feedback", Feedback));

                nameValuePairs.add(new BasicNameValuePair("id", sharedPreferences.getString("mobile","")));
                Log.d("fragmentonefeedback", "fname = = " + nameValuePairs);

                try {


                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "feedback_data/");

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    resTxt = EntityUtils.toString(entity);

                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return resTxt;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                Log.d("fragmentonefeedback", "result = = " + result);
                if (TextUtils.isEmpty(result)) {

                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray response = jsonObject.getJSONArray("response");
                        Log.d("fragmentonefragmentone", "calltype 2 response = " + response.length());
                        Toast.makeText(FeedbackActivity.this, "Your feedback is important to us.", Toast.LENGTH_SHORT).show();



                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
    }
}