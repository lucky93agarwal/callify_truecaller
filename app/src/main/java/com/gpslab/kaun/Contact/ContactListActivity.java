package com.gpslab.kaun.Contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.gpslab.kaun.DB.GetChatContactList;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.Home.GetCallLogTable;
import com.gpslab.kaun.Login.LoginSecActivity;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.Webapi.ChatWebAPI;
import com.gpslab.kaun.adapter.ChatsAdapter;
import com.gpslab.kaun.adapter.UserRecyclerViewAdapter;
import com.gpslab.kaun.check.luckycontact;
import com.gpslab.kaun.databinding.ActivityContactList2Binding;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.privacyActivity;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit.VersionRequestJson;
import com.gpslab.kaun.retrofit.VersionResponseJson;
import com.gpslab.kaun.retrofit_model.AddUserRequestJson;
import com.gpslab.kaun.retrofit_model.AddUserResponseJson;
import com.gpslab.kaun.retrofit_model.AppContactsContactResponseJson;
import com.gpslab.kaun.retrofit_model.AppContactsRequestJsn;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.MessageType;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.User;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ContactListActivity extends AppCompatActivity {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public ImageView ivback;

    private ArrayList<Chat> chats;
    private RecyclerView rvChats;
    public String resTxt = "";
    public ImageView ivsearchiv;
    public TextView tvtotalcontact;
    private ChatsAdapter adapter;
    public LinearLayout msearchLayout;
    public boolean check = false;
    public ImageView ivcloseiviv, ivbackiviviv;
    public EditText etsearch;
    public MyDbHandler myDbHandler;

    private Window window;
    private int brightness = 200;

    String insertChat = "";
    String insertChatData = "";
//    public User user = new User();


    private void ScreenViewControl() {
        //       Get the current system brightness
        window = ContactListActivity.this.getWindow();
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }



    private ProgressBar progressBar;
    private ImageView ivimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_list2);




        progressBar = (ProgressBar)findViewById(R.id.progress);


        ivimage = (ImageView)findViewById(R.id.nodataiv);

        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);



        tvtotalcontact = (TextView)findViewById(R.id.totalcontacttv);


//        ScreenViewControl();
        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();
        initialize();

        if(arrayList2.size()>0){
            tvtotalcontact.setText(String.valueOf(arrayList2.size())+" contacts");

            for(int i=0;i<arrayList2.size();i++){
                chats.add(new Chat(arrayList2.get(i).getContacts_image(), arrayList2.get(i).getContacts_name(), arrayList2.get(i).getContacts_status(), "",arrayList2.get(i).getContacts_id()));

//                user.setUserName(arrayList2.get(i).getContacts_name());
//                user.setUid(arrayList2.get(i).getContacts_id());
//                user.setPhone(arrayList2.get(i).getContacts_id());
//                user.setStatus(arrayList2.get(i).getContacts_status());
//                user.setPhoto(arrayList2.get(i).getContacts_image());
//                user.setThumbImg(arrayList2.get(i).getContacts_image());
//
//
//                RealmHelper.getInstance().saveEmptyChat(user);
            }
            adapter = new ChatsAdapter(ContactListActivity.this, chats);
            rvChats.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
            rvChats.setAdapter(adapter);
        }else {

        }



        retrofit();





        ivcloseiviv = (ImageView)findViewById(R.id.closeiviv);
        ivbackiviviv = (ImageView)findViewById(R.id.backiviv);
        ivbackiviviv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                filter("");
                msearchLayout.setVisibility(View.GONE);
            }
        });
        msearchLayout = (LinearLayout)findViewById(R.id.searchlayout);
        etsearch = (EditText)findViewById(R.id.editesearch);
        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("fragmentonefSearch", "result = = " );
                if(s.length()>1){
                    filter(s.toString());
                    ivcloseiviv.setVisibility(View.VISIBLE);


                }else {
                    ivcloseiviv.setVisibility(View.GONE);
                }


            }
        });
        ivsearchiv = (ImageView)findViewById(R.id.searchiv);
        ivsearchiv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (check){
                    check = true;
                    msearchLayout.setVisibility(View.GONE);
                }else {
                    check = true;
                    msearchLayout.setVisibility(View.VISIBLE);
                }

            }
        });


        ivback = (ImageView)findViewById(R.id.backiv);
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

//        request = new VersionRequestJson();
//        request.mobile.add("917318240112");

//        retrofit();

    }

    private void filter(String text) {
        ArrayList<Chat> filteredList = new ArrayList<>();

        for (Chat item : chats) {

            String Name = item.getName();

            if (Name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
    private void initialize() {

        chats = new ArrayList<>();
        rvChats = (RecyclerView) findViewById(R.id.rvChats);

    }

    public AppContactsRequestJsn request = new AppContactsRequestJsn();



    public void retrofit(){
        ivimage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        request.mobile = sharedPreferences.getString("mobile","");
        Log.d("ContactListAPI", "Request  = " + new Gson().toJson(request));
        Log.i("checkcontactlisr","request = "+new Gson().toJson(request));
        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.get_app_contacts(request).enqueue(new Callback<AppContactsContactResponseJson>() {
            @Override
            public void onResponse(Call<AppContactsContactResponseJson> call, Response<AppContactsContactResponseJson> response) {
//                Log.d("ContactListAPI", "Response  = " + new Gson().toJson());
                Log.i("checkcontactlisr","request = "+new Gson().toJson(response.body()));
                Log.d("ContactListAPI", "nameValuePairs code  = " + response.code());
                if (response.isSuccessful()) {

                    String result = response.body().status;
                    if(result.equalsIgnoreCase("1")){
                        if (chats.size()>0){
                            chats.clear();
                        }
                        ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();
                        tvtotalcontact.setText(String.valueOf(response.body().response.size())+" contacts");
                        if(arrayList2.size()>0) {
                            Log.i("checkcontactlisr","size = "+response.body().response.size());

                            for (int i = 0; i < response.body().response.size(); i++) {

                                chats.add(new Chat(response.body().response.get(i).get(0).profile_image, response.body().response.get(i).get(0).fname+" "+response.body().response.get(i).get(0).lname, response.body().response.get(i).get(0).status, "",response.body().response.get(i).get(0).mobile));
                            }
                        }else {
                            Log.i("checkcontactlisr","size = "+response.body().response.size());
                            for (int i = 0; i < response.body().response.size(); i++) {
                                insertChat = insertChat+ "'" + response.body().response.get(i).get(0).mobile + "'";
                                insertChat = insertChat+"," + "'" + response.body().response.get(i).get(0).profile_image + "'";
                                insertChat = insertChat+"," + "'" + response.body().response.get(i).get(0).status + "'";

                                insertChat = insertChat+"," + "'" + response.body().response.get(i).get(0).fname+" "+response.body().response.get(i).get(0).lname + "'";
                                insertChatData = insertChatData + "(" + insertChat + ")" + ",";
                                insertChat = "";



                                Log.d("Contactlist5lucky", "fname = = " + response.body().response.get(i).get(0).fname);
                                chats.add(new Chat(response.body().response.get(i).get(0).profile_image, response.body().response.get(i).get(0).fname+" "+response.body().response.get(i).get(0).lname, response.body().response.get(i).get(0).status, "",response.body().response.get(i).get(0).mobile));
                            }
                            insertChatData = insertChatData.substring(0, insertChatData.length() - 1);
                            Log.d("GetAllMessage", "CallLogService inseart check = " + insertChatData);
                            myDbHandler.InsertContactSQLiteDatabase(insertChatData);
                        }

                        adapter = new ChatsAdapter(ContactListActivity.this, chats);
                        rvChats.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
                        rvChats.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                    }
                    ivimage.setVisibility(View.GONE);
                    progressBar.setVisibility(View.GONE);

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
                    Toast.makeText(ContactListActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }

            @Override
            public void onFailure(Call<AppContactsContactResponseJson> call, Throwable t) {
                t.printStackTrace();
                ivimage.setVisibility(View.GONE);
                progressBar.setVisibility(View.GONE);
                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }


//    private void SendDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//                Log.d("Contactlist5lucky", "fname = = " );
//
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("mobile", sharedPreferences.getString("mobile","")));
//                Log.d("Contactlist5lucky", "fname = = " + nameValuePairs);
//
//                try {
//
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPosts = new HttpPost(ResURls.baseURLChat + "get_app_contacts");
//                    Log.d("Contactlist5lucky", "URL = = " + ResURls.baseURLChat + "get_app_contacts");
//                    httpPosts.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//
//                    HttpResponse response = httpClient.execute(httpPosts);
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
//                Log.d("Contactlist5lucky", "result = = " + result);
//                myDbHandler.deletecontacts();
//                if (result.isEmpty()) {
//
//                } else {
//
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        String status = jsonObject.getString("status");
//                        Log.d("Contactlist5lucky", "statuse = " + status);
//                        if(status.equalsIgnoreCase("0")){
//                            JSONArray response = jsonObject.getJSONArray("response");
//                            tvtotalcontact.setText(String.valueOf(response.length())+" contacts");
//                            Log.d("Contactlist5lucky", "calltype 2 response = " + response.length());
//
//                            if (chats.size()>0){
//                                chats.clear();
//                            }
//
//                            ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();
//                            Log.d("ContactListActivitymmm", "GetContacts inseart check 2 = " + arrayList2.size());
//
//
//                            if(arrayList2.size()>0){
//                                for (int i = 0; i < response.length(); i++) {
////                                    user.setUserName(response.getJSONObject(i).getString("name"));
////                                    user.setUid(response.getJSONObject(i).getString("mobile"));
////                                    user.setPhone(response.getJSONObject(i).getString("mobile"));
////                                    user.setStatus(response.getJSONObject(i).getString("status"));
////                                    user.setPhoto(response.getJSONObject(i).getString("image"));
////                                    user.setThumbImg(response.getJSONObject(i).getString("image"));
////                                    RealmHelper.getInstance().saveEmptyChat(user);
////                                    insertChat = insertChat+ "'" + response.getJSONObject(i).getString("mobile") + "'";
////                                    insertChat = insertChat+ "'" + response.getJSONObject(i).getString("image") + "'";
////                                    insertChat = insertChat+ "'" + response.getJSONObject(i).getString("status") + "'";
////                                    insertChat = insertChat+ "'" + response.getJSONObject(i).getString("name") + "'";
////                                    insertChatData = insertChatData + "(" + insertChat + ")" + ",";
////                                    insertChat = "";
//
//
//
//                                    Log.d("Contactlist5lucky", "fname = = " + response.getJSONObject(i).getString("name"));
//                                    chats.add(new Chat(response.getJSONObject(i).getString("image"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("status"), "",response.getJSONObject(i).getString("mobile")));
//                                }
//                            }else {
//                                for (int i = 0; i < response.length(); i++) {
////                                    user.setUserName(response.getJSONObject(i).getString("name"));
////                                    user.setUid(response.getJSONObject(i).getString("mobile"));
////                                    user.setPhone(response.getJSONObject(i).getString("mobile"));
////                                    user.setStatus(response.getJSONObject(i).getString("status"));
////                                    user.setPhoto(response.getJSONObject(i).getString("image"));
////                                    user.setThumbImg(response.getJSONObject(i).getString("image"));
////                                    RealmHelper.getInstance().saveEmptyChat(user);
//
//
//                                    insertChat = insertChat+ "'" + response.getJSONObject(i).getString("mobile") + "'";
//                                    insertChat = insertChat+"," + "'" + response.getJSONObject(i).getString("image") + "'";
//                                    insertChat = insertChat+"," + "'" + response.getJSONObject(i).getString("status") + "'";
//
//                                    insertChat = insertChat+"," + "'" + response.getJSONObject(i).getString("name") + "'";
//                                    insertChatData = insertChatData + "(" + insertChat + ")" + ",";
//                                    insertChat = "";
//
//
//
//                                    Log.d("Contactlist5lucky", "fname = = " + response.getJSONObject(i).getString("name"));
//                                    chats.add(new Chat(response.getJSONObject(i).getString("image"), response.getJSONObject(i).getString("name"), response.getJSONObject(i).getString("status"), "",response.getJSONObject(i).getString("mobile")));
//                                }
//
//
//                                insertChatData = insertChatData.substring(0, insertChatData.length() - 1);
//                                Log.d("GetAllMessage", "CallLogService inseart check = " + insertChatData);
//                                myDbHandler.InsertContactSQLiteDatabase(insertChatData);
//                            }
//
//
//
//
//
//
//
//
//
//                            Log.d("Contactlist5lucky", "size = = " + chats.size());
//                        }
//
//                        adapter = new ChatsAdapter(ContactListActivity.this, chats);
//                        rvChats.setLayoutManager(new LinearLayoutManager(ContactListActivity.this));
//                        rvChats.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//
//
//
//
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