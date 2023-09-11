package com.gpslab.kaun.Search;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.gson.Gson;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.adapter.ContectAPIAdapter;
import com.gpslab.kaun.adapter.ContectAPIMoreAdapter;
import com.gpslab.kaun.model.GetApiSearchData;
import com.gpslab.kaun.model.SearchContactMainResponseJson;
import com.gpslab.kaun.retrofit.ServiceGenerator;
import com.gpslab.kaun.retrofit.UserService;
import com.gpslab.kaun.retrofit_model.SearchContactRequestJson;
import com.gpslab.kaun.retrofit_model.SearchContactResponseJson;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

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

public class SearchCallifyMoreActivity extends AppCompatActivity {
    public RecyclerView mRecyclerView_api;
    public RecyclerView.LayoutManager mLayoutManager_api;
    public List<GetApiSearchData> productList_api = new ArrayList<>();
    public GetApiSearchData getbcdata_api;
    public ContectAPIMoreAdapter adapter_api;
    public EditText etsearch;

    String resTxt = null;
    public String getSearchWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_callify_more);



        initView();


        getData();
    }

    private void getData() {
        getSearchWord = getIntent().getStringExtra("searchword");
        etsearch.setText(getSearchWord);

        if(isNumeric(getSearchWord)){
            if (getSearchWord.length() == 10){
                SearchMobile = "91"+getSearchWord;
                SearchText ="";
                retrofit();


            }else  if (getSearchWord.length() == 12){
                SearchMobile = getSearchWord;
                SearchText ="";
                retrofit();
            }
            Log.i("checkthenum","response = 1 ");
        }
        else
        {
            SearchText = getSearchWord;
            SearchMobile = "";
            retrofit();
            Log.i("checkthenum","response = 2 ");
        }
//        SendDataToServer(getSearchWord);
    }

    private void initView() {
        mRecyclerView_api = (RecyclerView) findViewById(R.id.datarecyclerview);

        etsearch = (EditText)findViewById(R.id.searchet);


        etsearch.requestFocus();
        etsearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Log.d("CheckDataSearch","Search id = " +actionId);
                    Log.d("CheckDataSearch","Search IME_ACTION_SEARCH = " +EditorInfo.IME_ACTION_SEARCH);

                    hideKeybaord(v);
                    handled = true;
                }
                return handled;
            }
        });
        etsearch.setFocusable(true);
        UIUtil.showKeyboard(this,etsearch);
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
                int seachword = etsearch.getText().length();
//                if(seachword>3){
//                    SendDataToServer(s.toString());
//                }
                if (s.length() > 3) {

                    if(isNumeric(s.toString())){
                        if (s.length() == 10){
                            SearchMobile = "91"+s.toString();
                            SearchText ="";
                            retrofit();


                        }else  if (s.length() == 12){
                            SearchMobile = s.toString();
                            SearchText ="";
                            retrofit();
                        }
                        Log.i("checkthenum","response = 1 ");
                    }
                    else
                    {
                        SearchText = s.toString();
                        SearchMobile = "";
                        retrofit();
                        Log.i("checkthenum","response = 2 ");
                    }

                }

            }
        });
    }
    public String SearchText,SearchMobile;
    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    public SearchContactRequestJson request = new SearchContactRequestJson();
    private void retrofit() {


        request.username = SearchText;
        request.mobileno = SearchMobile;

        Log.d("CCpWalletCCpWallet", "nameValuePairs Signup  = " + new Gson().toJson(request));

        UserService service = ServiceGenerator.createService(UserService.class, null, null);
        service.search_contact(request).enqueue(new Callback<SearchContactResponseJson>() {
            @Override
            public void onResponse(Call<SearchContactResponseJson> call, Response<SearchContactResponseJson> response) {
                Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + response.code());
                if (response.isSuccessful()) {

                    Log.d("CCpWalletCCpWallet", "nameValuePairs code  = " + new Gson().toJson(response.body().response));
                    if (productList_api.size() > 0) {
                        productList_api.clear();
                    }
                    if(response.body().status.equalsIgnoreCase("1")){

                        for (int i = 0; i < response.body().response.get(0).size(); i++) {
                            GetApiSearchData getApiSearchData = new GetApiSearchData();
                            getApiSearchData.setName(response.body().response.get(0).get(i).Name);
                            getApiSearchData.setNumber(response.body().response.get(0).get(i).Number);
                            getApiSearchData.setEmail(response.body().response.get(0).get(i).Email);
                            getApiSearchData.setImage(response.body().response.get(0).get(i).Image);
                            productList_api.add(getApiSearchData);
                        }

                        mRecyclerView_api.setHasFixedSize(true);
                        adapter_api = new ContectAPIMoreAdapter(SearchCallifyMoreActivity.this,productList_api);
                        mLayoutManager_api = new LinearLayoutManager(SearchCallifyMoreActivity.this, LinearLayoutManager.VERTICAL, false);
                        mRecyclerView_api.setLayoutManager(mLayoutManager_api);
                        mRecyclerView_api.setAdapter(adapter_api);
                        adapter_api.notifyDataSetChanged();
                    }else {
                        Toast.makeText(SearchCallifyMoreActivity.this, "Result not found", Toast.LENGTH_SHORT).show();
                    }



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
                    Toast.makeText(SearchCallifyMoreActivity.this, "Something error please try again", Toast.LENGTH_SHORT).show();


                }
            }



            @Override
            public void onFailure(Call<SearchContactResponseJson> call, Throwable t) {
                t.printStackTrace();

                Log.e("System error:", t.getLocalizedMessage());

            }
        });
    }

//    private void SendDataToServer(final String datasss) {
//        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
//            @Override
//            protected String doInBackground(String... params) {
//
//                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
//
//                nameValuePairs.add(new BasicNameValuePair("string", datasss.toString()));
//
//                Log.d("fragmentonefSearch", "result = = " + nameValuePairs);
//
//
//                try {
//
//
//                    HttpClient httpClient = new DefaultHttpClient();
//
//                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "search_contact/");
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
//                Log.d("fragmentonefSearch", "result = = " + result);
//                if (result.isEmpty()) {
//
//                } else {
//
//                    try {
//                        JSONObject jsonObject = new JSONObject(result);
//                        JSONArray response = jsonObject.getJSONArray("response");
//                        for(int i=0;i<response.length();i++){
//
//                            GetApiSearchData getApiSearchData = new GetApiSearchData();
//                            getApiSearchData.setName(response.getJSONObject(i).getString("name"));
//                            getApiSearchData.setNumber(response.getJSONObject(i).getString("mobile"));
//                            getApiSearchData.setEmail(response.getJSONObject(i).getString("email"));
//                            getApiSearchData.setImage(response.getJSONObject(i).getString("image"));
//                            productList_api.add(getApiSearchData);
//                        }
//
//
//
//
//
//                        mRecyclerView_api.setHasFixedSize(true);
//                        adapter_api = new ContectAPIMoreAdapter(SearchCallifyMoreActivity.this,productList_api);
//                        mLayoutManager_api = new LinearLayoutManager(SearchCallifyMoreActivity.this, LinearLayoutManager.VERTICAL, false);
//                        mRecyclerView_api.setLayoutManager(mLayoutManager_api);
//                        mRecyclerView_api.setAdapter(adapter_api);
//                        adapter_api.notifyDataSetChanged();
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