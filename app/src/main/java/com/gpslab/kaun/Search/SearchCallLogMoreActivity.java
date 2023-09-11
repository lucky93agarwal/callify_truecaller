package com.gpslab.kaun.Search;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gpslab.kaun.CallLogUtils;
import com.gpslab.kaun.R;

import com.gpslab.kaun.adapter.CallLogMoreAdapter;
import com.gpslab.kaun.model.CallLogtwoinfor;

import net.yslibrary.android.keyboardvisibilityevent.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

public class SearchCallLogMoreActivity extends AppCompatActivity {
    public EditText etsearch;
    public List<CallLogtwoinfor> productList_log = new ArrayList<>();
    public CallLogtwoinfor getbcdata_log;
    public CallLogMoreAdapter adapter_log;
    public RecyclerView mRecyclerView_log;
    public RecyclerView.LayoutManager mLayoutManager_log;
    public String getSearchWord;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_call_log_more);


        initViwes();

        getViewRecyclerViewShowDataCallLog();


        getData();
    }

    private void getData() {
        getSearchWord = getIntent().getStringExtra("searchword");
        etsearch.setText(getSearchWord);
        if(getSearchWord.length()>1){
            filter_log(getSearchWord);



        }
    }

    private void getViewRecyclerViewShowDataCallLog() {
        productList_log.clear();
        CallLogUtils callLogUtils = CallLogUtils.getInstance(this);
        if(callLogUtils.readCallLogs().size()>0){
            for(int i=0;i<callLogUtils.readCallLogs().size();i++){
                getbcdata_log = new CallLogtwoinfor();
                getbcdata_log.setName(callLogUtils.readCallLogs().get(i).getName());
                getbcdata_log.setNumber(callLogUtils.readCallLogs().get(i).getNumber());
                productList_log.add(getbcdata_log);
            }
            mRecyclerView_log.setHasFixedSize(true);
            adapter_log = new CallLogMoreAdapter(this,productList_log);
            mLayoutManager_log = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView_log.setLayoutManager(mLayoutManager_log);
            mRecyclerView_log.setAdapter(adapter_log);
            adapter_log.notifyDataSetChanged();
        }



    }
    private void initViwes() {
        mRecyclerView_log = (RecyclerView) findViewById(R.id.datarecyclerviewsss);


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
                    filter_log(s.toString());



                }else {

                }


            }
        });
    }
    private void filter_log(final String text) {
        ArrayList<CallLogtwoinfor> filteredList = new ArrayList<>();
        for (CallLogtwoinfor item : productList_log) {
            String Name = item.getName();
            String Number = item.getNumber();
            if(TextUtils.isEmpty(Name)){
                Name = "NA";
            }
            if (Name.toLowerCase().contains(text.toLowerCase())||Number.toLowerCase().contains(text.toLowerCase()) ) {
                filteredList.add(item);
            }
        }
        adapter_log.filterList(filteredList);
    }
    private void hideKeybaord(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(),0);
    }
}