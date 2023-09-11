package com.gpslab.kaun.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gpslab.kaun.DB.FirstTableData;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.ContectAdapter;
import com.gpslab.kaun.adapter.SearchMoreContectAdapter;
import com.gpslab.kaun.model.GetContectData;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class SearchMoreActivity extends AppCompatActivity {
    public List<GetContectData> productList = new ArrayList<>();
    public GetContectData getbcdata;
    public SearchMoreContectAdapter adapter;
    public RecyclerView mRecyclerView;
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    MyDbHandler myDbHandler = Temp.getMyDbHandler();
    public EditText etsearch;
    public RecyclerView.LayoutManager mLayoutManager;
    public String getSearchWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_more);


        initViwes();


        getData();
    }

    private void getData() {

        try {
            getViewRecyclerViewShowData();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    private void getViewRecyclerViewShowData() throws UnsupportedEncodingException {
        // सबसे पहले Team class का जो getMyDbHandler method को call करेगे।
        productList.clear();
        MyDbHandler myDbHandler = Temp.getMyDbHandler();
        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        Log.d("lsdjflsjdfljsdf","name = "+arrayList.size());
        if(arrayList.size()>0){
            for(int i=0;i<arrayList.size();i++){
                getbcdata = new GetContectData();
                getbcdata.setId(arrayList.get(i).getId());
                Log.d("lsdjflsjdfljsdf","name 2 = "+arrayList.get(i).getName());
                byte[] data = Base64.decode(arrayList.get(i).getName(), Base64.DEFAULT);
                String Name = new String(data, "UTF-8");

                byte[] datanew = Base64.decode(arrayList.get(i).getMobileone(), Base64.DEFAULT);
                String getMobileone = new String(datanew, "UTF-8");
                getbcdata.setName(Name);
                getbcdata.setNumber_two(arrayList.get(i).getMobiletwo());
                getbcdata.setEmail(arrayList.get(i).getEmail());
                getbcdata.setNumber(getMobileone);
                productList.add(getbcdata);
            }




            mRecyclerView.setHasFixedSize(true);
            adapter = new SearchMoreContectAdapter(this,productList);
            mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        }

        getSearchWord = getIntent().getStringExtra("searchword");
        etsearch.setText(getSearchWord);
        if(getSearchWord.length()>1){
            filter(getSearchWord);



        }

    }

    private void initViwes(){



        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();


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
                if(s.length()>1){
                    filter(s.toString());



                }else {

                }


            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.datarecyclerview);


//        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!recyclerView.canScrollVertically(1))
//                    onScrolledToBottom();
//            }
//        });
    }

    private void filter(String text) {
        ArrayList<GetContectData> filteredList = new ArrayList<>();

        for (GetContectData item : productList) {
            String Name = item.getName();
            String Number = item.getNumber();
            if (Name.toLowerCase().contains(text.toLowerCase())||Number.toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }

    private void onScrolledToBottom() {
        ArrayList<FirstTableData> arrayList = myDbHandler.viewUser();
        if (productList.size() < arrayList.size()) {
            int x, y;
            if ((arrayList.size() - productList.size()) >= 50) {
                x = productList.size();
                y = x + 50;
            } else {
                x = productList.size();
                y = x + arrayList.size() - productList.size();
            }
            for (int i = x; i < y; i++) {
//                songMainList.add(songAllList.get(i));

                getbcdata = new GetContectData();
                getbcdata.setId(arrayList.get(i).getId());
                getbcdata.setName(arrayList.get(i).getName());
                getbcdata.setNumber_two(arrayList.get(i).getMobiletwo());
                getbcdata.setEmail(arrayList.get(i).getEmail());
                getbcdata.setNumber(arrayList.get(i).getMobileone());
                productList.add(getbcdata);
            }
            adapter.notifyDataSetChanged();
        }

    }
}