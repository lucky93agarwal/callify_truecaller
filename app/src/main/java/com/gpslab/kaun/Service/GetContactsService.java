package com.gpslab.kaun.Service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.model.Contact;
import com.gpslab.kaun.model.ContactFetcher;

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class GetContactsService extends Service {
    public Context context;
    public ArrayList<Contact> listContacts;
    public FirstTableData user;
    public MyDbHandler myDbHandler;
    public String Mobile;
    public String resTxt = null;

    public List<String> listlistfinal = new ArrayList<String>();

    public JSONObject jsonObject, jsonObject1;
    public JSONArray jsonArray1;
    public ArrayList<String> mobilearray = new ArrayList<>();
    public ArrayList<String> namearray = new ArrayList<>();
    Integer notificationID = 100;

    @Override
    public void onCreate() {
        context = this;
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
//            startMyOwnForeground();
//        } else {
//            startForeground(1, new Notification());
//        }


        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);


        Temp.setMyDbHandler(myDbHandler);
        // Get all Contacts

    }
    @RequiresApi(Build.VERSION_CODES.O)
    private void startMyOwnForeground() {
        String NOTIFICATION_CHANNEL_ID = "example.permanence";
        String channelName = "Background Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }
    private void GetContactsIntoArrayList() {
        Log.d("WalletYuyckey", "jsonArray ==  1");
//        listContacts = new ContactFetcher(this).fetchAll();


        jsonObject = new JSONObject();
        jsonObject1 = new JSONObject();
        jsonArray1 = new JSONArray();


        user = new FirstTableData();




        firstqueryall();


    }

    private void firstqueryall() {

        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
//        long countnew = myDbHandler.getProfilesCount();
//        Log.d("walletwallet", "size = " + arrayList.size() + "   Size ====  " + countnew);


        //// check row on the table is null or not
        if (arrayList.size() != 0) {


        } else {
            Log.d("walletwallet", "Insert Query........");
            firstinsertdata();
        }
    }

    String insertData = "";
    String insertSecData = "";

    public void firstinsertdata() {
        listContacts = new ContactFetcher(this).fetchAll();
        Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart size = =  " + listContacts.size());

        try {
            for (int i = 0; i < listContacts.size(); i++) {
                JSONArray jsonArray = new JSONArray();
//                user = new FirstTableData();
                Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts 1   = " + i);
                if (listContacts.get(i).name != null) {


                    if (listContacts.get(i).name.length() > 0) {
//                        user.setName(listContacts.get(i).name);
                        jsonObject.put("name", listContacts.get(i).name);
                        String namenew = listContacts.get(i).name;
                        byte[] data = namenew.getBytes("UTF-8");
                        String base64 = Base64.encodeToString(data, Base64.DEFAULT);
                        insertData = insertData + "'" + base64 + "'";
                    } else {
                        jsonObject.put("name", listContacts.get(i).name);
                        insertData = insertData + "''";
//                        user.setName("");
                    }
                } else {
                    insertData = insertData + "''";
                }

                Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts 1   = " + jsonObject.toString());
                if (listContacts.get(i).id != null) {


                    if (listContacts.get(i).id.length() > 0) {

                        insertData = insertData + "," + "'" + listContacts.get(i).id + "'";
                    } else {

                        insertData = insertData + "," + "''";

                    }
                } else {
                    insertData = insertData + "," + "''";
                }


                if (listContacts.get(i).numbers.size() == 1) {
                    if (listContacts.get(i).numbers.size() > 0 && listContacts.get(i).numbers.get(0) != null) {
                        jsonArray.put(listContacts.get(i).numbers.get(0).number);
                        mobilearray.add(listContacts.get(i).numbers.get(0).number);
                        namearray.add(listContacts.get(i).name);
                        String onemobile = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                        byte[] datamobileone = onemobile.getBytes("UTF-8");
                        String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);
                        insertData = insertData + "," + "'" + base64mobileone + "'," + "''";
//                        Log.d("WalletYuyckeynumber", "Mobile ==  " + listContacts.get(i).numbers.get(0).number);

                        if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 10) {
//                            user.setMobileone("+91" + listContacts.get(i).numbers.get(0).number.replace(" ", ""));
//                            user.setMobiletwo("");


                        } else if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() > 10) {
                            if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 11) {
                                String number = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                                number = number.substring(1);
                                number = "+91" + number;
//                                user.setMobileone(number);
//
//                                user.setMobiletwo("");
                            } else {
//                                user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));
//                                user.setMobiletwo("");

                            }

                        } else {
//                            user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));
//                            user.setMobiletwo("");

                        }

                    }
                } else if (listContacts.get(i).numbers.size() == 2) {
                    if (listContacts.get(i).numbers.size() == 2 && listContacts.get(i).numbers.get(1) != null) {

//                        Log.d("WalletYuyckeynumber", "Mobile ==  " + listContacts.get(i).numbers.get(0).number);
                        mobilearray.add(listContacts.get(i).numbers.get(0).number);
                        mobilearray.add(listContacts.get(i).numbers.get(1).number);
                        namearray.add(listContacts.get(i).name);
                        namearray.add(listContacts.get(i).name);

                        jsonArray.put(listContacts.get(i).numbers.get(0).number);
                        jsonArray.put(listContacts.get(i).numbers.get(1).number);

                        String onemobile = listContacts.get(i).numbers.get(0).number;
                        byte[] datamobileone = onemobile.getBytes("UTF-8");
                        String base64mobileone = Base64.encodeToString(datamobileone, Base64.DEFAULT);

                        String twomobile = listContacts.get(i).numbers.get(1).number;
                        byte[] datamobiletwo = twomobile.getBytes("UTF-8");
                        String base64mobiletwo = Base64.encodeToString(datamobiletwo, Base64.DEFAULT);
                        insertData = insertData + ",'" + base64mobileone + "'" + ",'" + base64mobiletwo + "'";

                        if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 10) {
//                            user.setMobileone("+91" + listContacts.get(i).numbers.get(0).number.replace(" ", ""));


                        } else if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() > 10) {
                            if (listContacts.get(i).numbers.get(0).number.replace(" ", "").length() == 11) {
                                String number = listContacts.get(i).numbers.get(0).number.replace(" ", "");
                                number = number.substring(1);
                                number = "+91" + number;
//                                user.setMobileone(number);


                            } else {
//                                user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));

                            }

                        } else {
//                            user.setMobileone(listContacts.get(i).numbers.get(0).number.replace(" ", ""));


                        }


                        if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() == 10) {
//                            user.setMobiletwo("+91" + listContacts.get(i).numbers.get(1).number.replace(" ", ""));


                        } else if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() > 10) {
                            if (listContacts.get(i).numbers.get(1).number.replace(" ", "").length() == 11) {
                                String number = listContacts.get(i).numbers.get(1).number.replace(" ", "");
                                number = number.substring(1);
                                number = "+91" + number;
//                                user.setMobiletwo(number);

                            } else {
//                                user.setMobiletwo(listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                            }

                        } else {
//                            user.setMobiletwo(listContacts.get(i).numbers.get(1).number.replace(" ", ""));

                        }

                    }
                } else {
//                    user.setMobileone("");
//                    user.setMobiletwo("");
                    insertData = insertData + "," + "''" + "," + "''";
                }

                if (listContacts.get(i).numbers.size() == 0) {
                    jsonObject.put("mobile", "");
                } else {
                    jsonObject.put("mobile", jsonArray);
                }
                if (listContacts.get(i).emails.size() > 0 && listContacts.get(i).emails.get(0) != null) {
                    if (listContacts.get(i).id.equalsIgnoreCase(listContacts.get(i).emails.get(0).id)) {
                        jsonObject.put("email", listContacts.get(i).emails.get(0).address);
//                        user.setEmail(listContacts.get(i).emails.get(0).address);

                        String namenewemail = listContacts.get(i).emails.get(0).address;
                        byte[] dataemail = namenewemail.getBytes("UTF-8");
                        String base64email = Base64.encodeToString(dataemail, Base64.DEFAULT);


                        insertData = insertData + ", '" + base64email + "'";

                    }
                } else {
                    jsonObject.put("email", "");
//                    user.setEmail("");
                    insertData = insertData + "," + "''";
                }
                Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + jsonObject.toString());


                Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + listlistfinal);


                if (listContacts.get(i).imageuri != null) {


                    if (String.valueOf(listContacts.get(i).imageuri).length() > 0) {

                        String namenew = String.valueOf(listContacts.get(i).imageuri);
                        user.setPhoto(namenew);
//                        try {
//                        Uri u = Uri.parse(namenew);
//                            final InputStream imageStream = getContentResolver().openInputStream(u);
//                            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                            String encodedImage = encodeImage(selectedImage);

                        jsonObject.put("img", "encodedImage");
//                        } catch (FileNotFoundException e) {
//                            Log.d("WalletYuyckey", "jsonArray ==  " + e.getMessage());
//                        }
////

                        insertData = insertData + "," + "'" + namenew + "'";

                    } else {
                        jsonObject.put("img", "");
                        user.setPhoto("");
                        insertData = insertData + "," + "''";

                    }
                } else {
                    jsonObject.put("img", "");
                    user.setPhoto("");
                    insertData = insertData + "," + "''";
                }

                listlistfinal.add(jsonObject.toString());
                insertSecData = insertSecData + "(" + insertData + ")" + ",";
                insertData = "";


            }

            insertSecData = insertSecData.substring(0, insertSecData.length() - 1);


            final Handler handler = new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    myDbHandler.InsertDataIntoSQLiteDatabase(insertSecData);
                    SendDataToServer(insertSecData);
                    handler.postDelayed(this, 6000);
                }
            };
            handler.post(runnable);

            Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts inseart check = " + insertSecData);
//            Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts fname = = " + listlistfinal.toString());


            Log.d("WalletYuyckey", "jsonArray ==  " + listlistfinal);
        } catch (JSONException | UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {


                String data = String.valueOf(Html.fromHtml(listlistfinal.toString(), Html.FROM_HTML_MODE_LEGACY));
//                QuickFirstName = Character.toString ((char) QuickFirstName);
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("parent_no", Mobile));
                nameValuePairs.add(new BasicNameValuePair("data", insertSecData));


                Log.d("dfsdfsdfsdfdfsluckyd", "fname = = " + nameValuePairs);


//                JSONObject jsonObjectlucky = new JSONObject();
                try {
//                    StringEntity se;
//                    se = new StringEntity(jsonObjectlucky.toString());

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "contact_sync/");

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
//                mprogress.setVisibility(View.GONE);
                Log.d("dfsdfsdfsdfdfsddfsdfsdfsdfdfsd", "GetContacts result = = " + result);
                if (result.isEmpty()) {

                } else {

                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));
//                    Intent intent = new Intent(LoginSecActivity.this, MainActivity.class);
//                    startActivity(intent);
//                    finish();

//                    if(jsonObject1.getString("id").equalsIgnoreCase("0")){
//                        Log.d("WalletLuckyYUYU", "response = = " + jsonObject1.getString("id"));
//
//                        edit.putString("fname",etfistname.getText().toString().trim());
//                        edit.putString("lname",etsecondname.getText().toString().trim());
//                        edit.putString("email",etemail.getText().toString().trim());
//                        edit.putString("mobile",Mobile);
//                        edit.putString("id",jsonObject1.getString("id"));
//                        edit.putString("img",jsonObject1.getString("image"));
//                        edit.apply();
//                    } else if(jsonObject1.getString("id").equalsIgnoreCase("1")){
//                        Toast.makeText(LoginSecActivity.this, "Please enter after some time.", Toast.LENGTH_SHORT).show();
//                    } else if(jsonObject1.getString("id").equalsIgnoreCase("2")){
//                        Toast.makeText(LoginSecActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
//                    }
//
                    } catch (JSONException e) {
                        e.getMessage();
                    }
                }


            }
        }
        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();
        sendPostReqAsyncTask.execute(datasss);
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public void onDestroy() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        // this getter is just for example purpose, can differ
        if (intent != null && intent.getExtras() != null) {
            String value = intent.getExtras().getString("key");
            Mobile = value;
            Log.d("WalletKey", "Mobile == ==  " + value);
        }
//        Runnable runnable = new Runnable() {
//            @Override
//            public void run() {
//
//                Log.d("WalletLucky",Thread.currentThread().getName());
//                Log.d("WalletLucky","Thread Complete");
//
//            }
//        };
//        Thread thread = new Thread(runnable);
//        thread.setName("Lucky Agarwal");
//        thread.start();

        GetContactsIntoArrayList();
        return START_STICKY;
    }
}

