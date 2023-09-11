package com.gpslab.kaun.check;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gpslab.kaun.Contact.ContactViewModel;
import com.gpslab.kaun.Contact.NewContact;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.MainHomeActivity;
import com.gpslab.kaun.OTP.OTPActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.Service.GetContactsService;
import com.gpslab.kaun.Webapi.createnew;
import com.gpslab.kaun.internetcheck.InternetActivity;
import com.gpslab.kaun.model.ContactSyncKnow;
import com.gpslab.kaun.model.ContactSyncRequestJson;
import com.gpslab.kaun.model.ContactSyncResponseJson;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.ServiceGeneratorNew;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.VerifyUserResponseJson;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import believe.cht.fadeintextview.TextViewListener;
import cdflynn.android.library.checkview.CheckView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckActivity extends AppCompatActivity {
    public CheckView mCheckView;
    public SharedPreferences.Editor edit;
    private ContactViewModel contactViewModel;
    public SharedPreferences sharedPreferencesxx;


    public JSONObject jsonObject = new JSONObject();
    public List<String> listlistfinal = new ArrayList<String>();
    public List<JSONObject> listlistfinalnew = new ArrayList<JSONObject>();

    public MyDbHandler myDbHandler;

    String insertData = "";
    String insertSecData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setBackgroundColor(Color.WHITE);
        setContentView(R.layout.activity_check);
        sharedPreferencesxx = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferencesxx.edit();
        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);


        Temp.setMyDbHandler(myDbHandler);



        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected()) {
            initRecyclerView();

        } else {
            Intent intentnew = new Intent(CheckActivity.this, InternetActivity.class);
            intentnew.putExtra("msg", "No Internet");
            startActivity(intentnew);
        }


        mCheckView = (CheckView) findViewById(R.id.check);
        mCheckView.check();
        believe.cht.fadeintextview.TextView textView = findViewById(R.id.textView);
        textView.setListener(new TextViewListener() {
            @Override
            public void onTextStart() {
            }

            @Override
            public void onTextFinish() {

            }
        });
        textView.setLetterDuration(250);
        textView.setText("Success");
        textView.isAnimating();
        believe.cht.fadeintextview.TextView textViewtwo = findViewById(R.id.textViewtwo);
        textViewtwo.setListener(new TextViewListener() {
            @Override
            public void onTextStart() {
            }

            @Override
            public void onTextFinish() {
                Intent intent = new Intent(CheckActivity.this, MainHomeActivity.class);
                startActivity(intent);
                finish();
            }
        });
        textViewtwo.setLetterDuration(250);
        textViewtwo.setText("Let's continue");
        textViewtwo.isAnimating();
//


//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//                Intent intent = new Intent(CheckActivity.this, GetContactsService.class);
//                intent.putExtra("key",sharedPreferencesxx.getString("mobile",""));
//                startService(intent);
//                Log.d("WalletLucky",Thread.currentThread().getName());
//                Log.d("WalletLucky","Thread Complete");
//
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.setName("Lucky Agarwal");
//        thread.start();
    }

    private List<NewContact> filtered_icontacts = new ArrayList<>();

    private void initRecyclerView() {
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "initRecyclerView = = ");
        contactViewModel = new ContactViewModel(getApplicationContext());

        setContacts(contactViewModel.getContacts());
//        for (int i = 0; i < contactViewModel.getContacts().size(); i++) {
//            try {
//                JSONArray jsonArray = new JSONArray();
//                jsonObject.put("name", contactViewModel.getContacts().get(i).getName());
//                jsonArray.put(contactViewModel.getContacts().get(i).getPhoneNumber());
//                jsonObject.put("mobile", jsonArray);
//                jsonObject.put("email", "");
//                jsonObject.put("img", contactViewModel.getContacts().get(i).getPhotoUri());
//                listlistfinal.add(jsonObject.toString());
//            }catch (JSONException e){
//                e.printStackTrace();
//            }
//
//        }

    }


    public void requercive(final int totalsize,final int startingposition, final int currentposition,final int size){
        Log.i("checkknowtwo", "totalsize =   "+String.valueOf(totalsize)+"start =   "+String.valueOf(startingposition)+"current =   "+String.valueOf(currentposition)+"size =   "+String.valueOf(size) );
        if(currentposition<= size){
            for (int i = startingposition; i < size; i++) {
                onBind(filtered_icontacts.get(i));

                if(i == size-1){
                    int remen = totalsize - size;
                    Log.i("checkknowtwo","top remen  =  "+remen);
                    int newcurrentposition = size-1;
                    if(remen>150){
                        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
                        request.data = productList;

                        myDbHandler.InsertDataIntoSQLiteDatabase(insertSecData);
                        retrofit(totalsize,newcurrentposition,newcurrentposition,size+150);


                  //      Log.i("checkknowtwo", "more  totalsize =   "+String.valueOf(totalsize)+"start =   "+newcurrentposition+"current =   "+newcurrentposition+"size =   "+String.valueOf(size+50) );
//                        requercive(totalsize,newcurrentposition,newcurrentposition,size+50);
                    }else if(remen !=0){
                        Log.i("checkknowtwo","less remen  =  "+remen);
                        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
                        request.data = productList;

                        myDbHandler.InsertDataIntoSQLiteDatabase(insertSecData);
                        retrofit(totalsize,newcurrentposition,newcurrentposition,size+remen);



                    }else if (remen == 0){
//                        Intent intent = new Intent(CheckActivity.this, MainHomeActivity.class);
//                        startActivity(intent);
//                        finish();
                        Log.i("checkknowtwo","top end end  =  ");
                    }

                }
            }

//


        }else {
            Log.i("checkknowtwo","top end  =  ");
//            Log.i("checkknowtwo","less remen  =  "+remen);
//            insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
//            request.data = productList;
//
//            myDbHandler.InsertDataIntoSQLiteDatabase(insertSecData);
//            retrofit(totalsize,newcurrentposition,newcurrentposition,size+remen);
        }
    }

    public void setContacts(List<NewContact> contacts) {

        this.filtered_icontacts = contacts;

        if(contacts.size()>=150){
            Log.i("checkknowtwo", "data =  1 " );
            requercive(contacts.size(),0,0,150);
        }else {
            Log.i("checkknowtwo", "data =  2 " );
            requercive(contacts.size(),0,0,contacts.size());
        }

//        for (int i = 0; i < contacts.size(); i++) {
//            onBind(filtered_icontacts.get(i));
//        }
//        insertSecData = insertSecData.substring(0, insertSecData.length() - 1);
//        request.data = productList;
////
//        retrofit();
//        myDbHandler.InsertDataIntoSQLiteDatabase(insertSecData);
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts fname = = " + listlistfinal.toString());
        Log.d("CheckActivityPage", "GetContacts fname = = " + insertSecData.toString());
    }

    void onBind(NewContact contact) {
        try {
            datakey = new ContactSyncKnow();
            ArrayList<String> mobnum = new ArrayList<String>();
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("name", contact.getName());
            datakey.name = contact.getName();

            // database
            String namenew = contact.getName();
            byte[] data = namenew.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

            String mobile_input = contact.getPhoneNumber().toString();
            mobile_input = mobile_input.replace(" ", "");
//            if(mobile_input.length() >10){
//
//            }else
            if (mobile_input.length() == 10) {
                String str = mobile_input;
                String num = str;
                String nums = "91" + num;
                mobnum.add(nums);
                jsonArray.put(nums);
            } else if (mobile_input.length() == 11) {
                String str = mobile_input;
                String num = String.valueOf(str.substring(str.length() - 10));
                String nums = "91" + num;
                mobnum.add(nums);
                jsonArray.put(nums);
            } else if (mobile_input.length() == 12) {
                mobnum.add(mobile_input);
                jsonArray.put(mobile_input);
            } else if (mobile_input.length() == 13) {
                String str = mobile_input;
                String num = String.valueOf(str.substring(str.length() - 10));
                String nums = "91" + num;
                mobnum.add(nums);
                jsonArray.put(nums);
            } else {
                mobnum.add(contact.getPhoneNumber());
                jsonArray.put(contact.getPhoneNumber());
            }
            datakey.mobile = mobnum;
            Log.d("WalletYuyWalletYuyckeyckey", "jsonArray ==  " + jsonArray.toString());
            String onemobile = contact.getPhoneNumber();
            byte[] datamobileone = onemobile.getBytes("UTF-8");
            String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);


            jsonObject.put("mobile", jsonArray);


            if (contact.getPhotoUri() != null) {
                try {
                    String namenewnew = String.valueOf(contact.getPhotoUri());


                    Uri u = Uri.parse(namenewnew);

                    final InputStream imageStream = getContentResolver().openInputStream(u);

                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
//
//
//
//
//
//                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 10, stream);
                    byte[] imageInByte = stream.toByteArray();
                    long lengthbmp = imageInByte.length;
                    long totalsize = lengthbmp / 1024;
                    int totals = (int) totalsize;

                    if (totals <= 10) {

                        Bitmap resized = Bitmap. createScaledBitmap ( selectedImage , 100 , 100 , true ) ;
//                        ivImage .setImageBitmap(resized) ;
                        String encodedImage = encodeImage(resized);
                        encodedImage = encodedImage.replace("\n", "");
                        datakey.img = "data:image/jpeg;base64," + encodedImage;
                        jsonObject.put("img", "data:image/jpeg;base64," + encodedImage);
//                        jsonObject.put("img", "data:image/jpeg;base64," + encodedImage);
                        Log.i("checkknowtwo", "size ==  " + String.valueOf(totals));
                    }else{
                        jsonObject.put("img", "NA");
                    }

                } catch (FileNotFoundException e) {
                    Log.d("WalletYuyckey", "jsonArray ==  " + e.getMessage());
                }

//                jsonObject.put("img", contact.getPhotoUri());
                insertSecData = insertSecData + "(" + "'" + base64 + "'" + "," + "''" + "," + "'" + base64mobileone + "'" + "," + "''" + "," + "''" + "," + "'" + contact.getPhotoUri() + "'" + ")" + ",";
            } else {
                datakey.img = "NA";
                jsonObject.put("img", "NA");
                insertSecData = insertSecData + "(" + "'" + base64 + "'" + "," + "''" + "," + "'" + base64mobileone + "'" + "," + "''" + "," + "''" + "," + "''" + ")" + ",";
            }

//            Log.i("checkknowtwo","list = "+String.valueOf(insertSecData));
            datakey.email = "";

            jsonObject.put("email", "");
//            jsonObject.put("img", contact.getPhotoUri());

            productList.add(datakey);

            listlistfinal.add(jsonObject.toString());
//            listlistfinalnew.add(jsonObject);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    public String resTxt = null;
    public ArrayList<ContactSyncKnow> productList = new ArrayList<>();
    public ContactSyncKnow datakey;
    public ContactSyncRequestJson request = new ContactSyncRequestJson();

    private void retrofit(final int totalsize,final int startingposition,final int currentposition,final int size) {
        request.parent_no = sharedPreferencesxx.getString("mobile", "");
//        request.parent_no = "919554453444";
        Log.i("checkknowtwo", "request =   " + new Gson().toJson(request));
        UserService service = ServiceGeneratorNew.createService(UserService.class, null, null);
        service.contact_sync(request).enqueue(new Callback<List<ContactSyncResponseJson>>() {
            @Override
            public void onResponse(Call<List<ContactSyncResponseJson>> call, Response<List<ContactSyncResponseJson>> response) {
                Log.i("checkknowtwo", "nameValuePairs code  = " + response.code());
                Log.i("checkknowtwo", "nameValuePairs code  = " + new Gson().toJson(response.body()));

                if (response.isSuccessful()) {
                    insertSecData = "";
                    productList.clear();
                    Log.i("checkknowtwo", "less totalsize =   "+String.valueOf(totalsize)+"start =   "+startingposition+"current =   "+currentposition+"size =   "+String.valueOf(size) );
                    requercive(totalsize,startingposition,currentposition,size);

//
//                    Intent intent = new Intent(CheckActivity.this, MainHomeActivity.class);
//                    startActivity(intent);
//                    finish();

                } else {
                    switch (response.code()) {
                        case 401:
//                            Toast.makeText(Splash.this, "email and pass not check", Toast.LENGTH_SHORT).show();
                            break;
                        case 403:
//                            Toast.makeText(Splash.this, "ForbiddenException", Toast.LENGTH_SHORT).show();
                            break;
                        case 404:
//                            Toast.makeText(Splash.this, "not found", Toast.LENGTH_SHORT).show();
                            break;
                        case 500:
//                            Toast.makeText(Splash.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
//                            Toast.makeText(Splash.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
            //        Toast.makeText(CheckActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<List<ContactSyncResponseJson>> call, Throwable t) {
                t.printStackTrace();


                Log.i("checkknowtwo", "System Cause:  = " + t.getCause());

                Log.i("checkknowtwo", "System error:  = " + t.getLocalizedMessage());
                Log.e("System error:", t.getLocalizedMessage());

            }
        });

    }
//    private void SendDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//
//                String data = String.valueOf(Html.fromHtml(listlistfinal.toString(), Html.FROM_HTML_MODE_LEGACY));
////                QuickFirstName = Character.toString ((char) QuickFirstName);
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//                nameValuePairs.add(new BasicNameValuePair("parent_no", sharedPreferencesxx.getString("mobile", "")));
//                nameValuePairs.add(new BasicNameValuePair("data", listlistfinal.toString()));
//
//
//                Log.d("CheckActivityPage", "fname = = " + nameValuePairs);
//
//
////                JSONObject jsonObjectlucky = new JSONObject();
//                try {
////                    StringEntity se;
////                    se = new StringEntity(jsonObjectlucky.toString());
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "contact_sync/");
//
//                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPost);
//
//                    HttpEntity entity = response.getEntity();
//
//                    resTxt = EntityUtils.toString(entity);
//
//                } catch (ClientProtocolException e) {
//
//                } catch (IOException e) {
//
//                }
//                return resTxt;
//            }
//
//            @Override
//            protected void onPostExecute(String result) {
//                super.onPostExecute(result);
////                mprogress.setVisibility(View.GONE);
//                Log.d("CheckActivityPage", "GetContacts result = = " + result);
//                if (result.isEmpty()) {
//
//                } else {
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
////                    Intent intent = new Intent(LoginSecActivity.this, MainActivity.class);
////                    startActivity(intent);
////                    finish();
//
////                    if(jsonObject1.getString("id").equalsIgnoreCase("0")){
////                        Log.d("WalletLuckyYUYU", "response = = " + jsonObject1.getString("id"));
////
////                        edit.putString("fname",etfistname.getText().toString().trim());
////                        edit.putString("lname",etsecondname.getText().toString().trim());
////                        edit.putString("email",etemail.getText().toString().trim());
////                        edit.putString("mobile",Mobile);
////                        edit.putString("id",jsonObject1.getString("id"));
////                        edit.putString("img",jsonObject1.getString("image"));
////                        edit.apply();
////                    } else if(jsonObject1.getString("id").equalsIgnoreCase("1")){
////                        Toast.makeText(LoginSecActivity.this, "Please enter after some time.", Toast.LENGTH_SHORT).show();
////                    } else if(jsonObject1.getString("id").equalsIgnoreCase("2")){
////                        Toast.makeText(LoginSecActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
////                    }
////
//                    } catch (JSONException e) {
//                        e.getMessage();
//                    }
//                }
//
//
//            }
//        }
//        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
//        sendPostReqAsyncTask.execute(datasss);
//    }
}