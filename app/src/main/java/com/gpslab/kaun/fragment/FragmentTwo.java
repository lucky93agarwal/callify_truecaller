package com.gpslab.kaun.fragment;

import android.Manifest;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.MyBrodcastRecieverService;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.adapter.ContectAdapter;
import com.gpslab.kaun.adapter.ContectSecAdapter;
import com.gpslab.kaun.model.ApplicationMain;
import com.gpslab.kaun.model.Contact;
import com.gpslab.kaun.model.ContactFetcher;
import com.gpslab.kaun.model.ContactList;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.model.MenuItem;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import android.os.Handler;

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

import java.util.logging.LogRecord;

public class FragmentTwo extends Fragment {


    public FragmentTwo() {
        // Required empty public constructor
    }


    ///////////// Contect List ///////////////////////

    public List<String> getnumber = new ArrayList<>();

    public ArrayList<Contact> listContacts;

    public List<GetContectData> productList = new ArrayList<>();
    public GetContectData getbcdata;
    public ContectSecAdapter adapter;
    public RecyclerView mRecyclerView;
    public FloatingActionButton floatingActionBtn;
    public RecyclerView.LayoutManager mLayoutManager;
    MyDbHandler myDbHandler = Temp.getMyDbHandler();

    int currentPage = -1;
    public EditText etsearch;


    public boolean checkscroll = false;
    public TextView tvone, tvtwo;
    public LinearLayout mlinearLayout;

    public ProgressBar mmmProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_two, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.datarecyclerview);

        etsearch = (EditText) view.findViewById(R.id.searchet);


        mmmProgressBar = (ProgressBar) view.findViewById(R.id.mProgressBar);
        mmmProgressBar.setVisibility(View.VISIBLE);

        tvone = (TextView) view.findViewById(R.id.newonetv);
        tvtwo = (TextView) view.findViewById(R.id.newtwotv);
        mlinearLayout = (LinearLayout) view.findViewById(R.id.newnewlinear);
        tvtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlinearLayout.setVisibility(View.GONE);
            }
        });
        tvone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mlinearLayout.setVisibility(View.GONE);
            }
        });


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);



                mLayoutManager.getItemCount();
                mLayoutManager.getChildCount();

//                final int scrollOffset = recyclerView.computeVerticalScrollOffset();
//                final int height = recyclerView.getHeight();
//                int page_no = scrollOffset / height;
//                Log.d("WalletWalletLuckyContact", "page_no = " + page_no);
//                Log.d("WalletWalletLuckyContact", "height = " + height);

//                if (page_no != currentPage) {
//                    currentPage = page_no;
//                    Log.d("WalletWalletLuckyContact", "currentPage = " + currentPage);
//                    try {
//
//                        onScrolledToBottom();
//                    } catch (UnsupportedEncodingException e) {
//                        e.printStackTrace();
//                    }
//
//                }

//                Log.d("WalletWalletLuckyContact", "SCROLL_STATE_IDLE = getItemCount = " + mLayoutManager.getItemCount());
//                Log.d("WalletWalletLuckyContact", "SCROLL_STATE_IDLE = getChildCount = " + mLayoutManager.getChildCount());
//
//
//                Log.d("WalletWalletLuckyContact", "SCROLL_STATE_IDLE = " + mLayoutManager.findLastVisibleItemPosition());
//                Log.d("WalletWalletLuckyContact", "SCROLL_STATE_IDLE = " + mLayoutManager.getChildCount());
//                if (newState == RecyclerView.SCROLL_STATE_IDLE){
//                    int position = getCurrentItem();
////                    onPageChanged(position);
//                }
                if (!recyclerView.canScrollVertically(1)) {
                    try {

                        if(!checkscroll){
                            Log.d("WalletWalletntact", "SCROLL_STATE_IDLE = getChildCount = " +checkscroll);
                            checkscroll = true;
                            onScrolledToBottom();
                        }

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
        });





        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                try {
                    getViewRecyclerViewShowData();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, 500);




        etsearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        return view;
    }
    private int getCurrentItem(){
        return ((LinearLayoutManager)mRecyclerView.getLayoutManager())
                .findFirstVisibleItemPosition();
    }
    private void filter(String text) {
        ArrayList<GetContectData> filteredList = new ArrayList<>();

        for (GetContectData item : productList) {
            String Name = item.getName();
            String Number = item.getNumber();
            if (Name.toLowerCase().contains(text.toLowerCase()) || Number.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
    // songMainList productlist

    // songAllList
    private void onScrolledToBottom() throws UnsupportedEncodingException {
        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        if (productList.size() < arrayList.size()) {
            int x, y;
            Log.d("WalletWalletLuckyContact", "Size Scroll time = " + productList.size());

            if ((arrayList.size() - productList.size()) >= 50) {
                x = productList.size();
                y = x + 500;
            } else {
                x = productList.size();
                y = x + arrayList.size() - productList.size();
            }
            for (int i = x; i < y; i++) {
//                songMainList.add(songAllList.get(i));
                byte[] data = Base64.decode(arrayList.get(i).getName(), Base64.DEFAULT);
                String Name = new String(data, "UTF-8");


                byte[] dataMobileone = Base64.decode(arrayList.get(i).getMobileone(), Base64.DEFAULT);
                String Mobileone = new String(dataMobileone, "UTF-8");


                byte[] datagetMobiletwo = Base64.decode(arrayList.get(i).getMobiletwo(), Base64.DEFAULT);
                String Mobiletwo = new String(datagetMobiletwo, "UTF-8");


                getbcdata = new GetContectData();
                getbcdata.setId(arrayList.get(i).getId());
                getbcdata.setName(Name);
                getbcdata.setNumber_two(Mobiletwo);
                getbcdata.setEmail(arrayList.get(i).getEmail());
                getbcdata.setNumber(Mobileone);

                getbcdata.setImage("");

                productList.add(getbcdata);
//                adapter.notifyItemInserted(i);
//                adapter.notifyItemRangeChanged(i,productList.size());
            }

            adapter.notifyDataSetChanged();
            checkscroll = true;
        }

    }

    private void getViewRecyclerViewShowData() throws UnsupportedEncodingException {

        productList.clear();

        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();


        Log.d("WalletWalletLuckyContact", "Size = " + arrayList.size());
        if (arrayList.size() > 0) {
            for (int i = 0; i < 50; i++) {

                byte[] data = Base64.decode(arrayList.get(i).getName(), Base64.DEFAULT);
                String Name = new String(data, "UTF-8");


                byte[] dataMobileone = Base64.decode(arrayList.get(i).getMobileone(), Base64.DEFAULT);
                String Mobileone = new String(dataMobileone, "UTF-8");
                getnumber.add(arrayList.get(i).getMobileone());



                getbcdata = new GetContectData();
                getbcdata.setId(arrayList.get(i).getId());
                getbcdata.setName(Name);
                getbcdata.setNumber_two(Mobileone);
                getbcdata.setEmail(arrayList.get(i).getEmail());


                getbcdata.setImage("");


                getbcdata.setNumber(Mobileone);
                Log.d("WalletWalletLuckyContact", "   i= " + i);
                productList.add(getbcdata);
                Log.d("WalletWalletLuckyContact", "   i= " + productList.size());
            }

//            mRecyclerView.setHasFixedSize(true);
            adapter = new ContectSecAdapter(getActivity(), productList);
            mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
            mRecyclerView.setVisibility(View.VISIBLE);
            mmmProgressBar.setVisibility(View.GONE);
//            adapter.notifyDataSetChanged();
//            SendDataToServer(getnumber.toString());
        }


    }

    String resTxt = null;

    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String QuickFirstName = datasss;
                JSONObject jsonObjectlucky = new JSONObject();
                try {

                    jsonObjectlucky.put("data", getnumber);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String data = String.valueOf(Html.fromHtml(getnumber.toString(), Html.FROM_HTML_MODE_LEGACY));

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("mobiles", getnumber.toString()));

                Log.d("fragmentonefragmenttwo", "fname = = " + nameValuePairs);

                try {
                    StringEntity se;
                    se = new StringEntity(jsonObjectlucky.toString());

                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "refresh_data/");

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
                Log.d("fragmentonefragmenttwo", "result = = " + result);
                if (result.isEmpty()) {

                } else {

                    adapter.notifyDataSetChanged();
                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        JSONArray response = jsonObject.getJSONArray("response");
                        Log.d("fragmentonefragmenttwo", "calltype 2 response = " + response.length());
                        for (int i = 0; i < response.length(); i++) {


//                            Object item = recyclerViewItems.get(i);
//                            if (!(item instanceof AdView)) {
//                                Log.d("fragmentonefragmentone", "calltype 3 namexx = " + j);

//                                namexx = String.valueOf(getnamexx.get(i));
//                                if (namexx.equalsIgnoreCase("null")) {
//                                    namexx = response.getJSONObject(i).getString("name");
//                                    apicheck = "1";
//                                }
//
                            if (response.getJSONObject(i).getString("image").equalsIgnoreCase("NA")) {


                            } else {


                                String image = response.getJSONObject(i).getString("image");

                                getbcdata = new GetContectData();


                                getbcdata.setImage(image);

                            }


//                                if (i >= j) {
//                                    MenuItem menuItem = new MenuItem(namexx, getnumber.get(i), getcalltype.get(i),
//                                            getdatexx.get(i), getduration.get(i), getsimtype.get(i), image, apicheck);
                            productList.set(i, getbcdata);
                            adapter.notifyItemChanged(i);
//                                }

//                                j = j + 1;
//                                throw new ClassCastException("Expected item at index " + i + " to be a banner ad" + " ad.");
//                            } else {
////                                j = j + 1;
//                            }


                            Log.d("fragmentonefragmentone", "calltype 3 namexx = " + i);
//                            Log.d("fragmentonefragmentone", "calltype 33 namexx = " + namexx);
////                            namexx ="1";
//                            Log.d("fragmentonefragmentone", "calltype 3333 namexx = " + namexx.length());
                        }


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