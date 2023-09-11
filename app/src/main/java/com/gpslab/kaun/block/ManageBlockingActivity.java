package com.gpslab.kaun.block;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.BlockAdapter;

import java.util.ArrayList;
import java.util.List;

public class ManageBlockingActivity extends AppCompatActivity {
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;

    private BlockAdapter adapter;
    public GetBlockData getBlockData;
    private ArrayList<GetBlockData> chats;
    public RecyclerView mRecycler;
    private BlockedNumberDao blockedNumberDao;
    public ImageView ivback, ivnodataiv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_blocking);


        initViews();

        onClick();

    }


    public void onClick(){
        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initViews(){

        ivback = (ImageView)findViewById(R.id.backiv);


        ivnodataiv = (ImageView)findViewById(R.id.nodataiv);


        mRecycler = (RecyclerView)findViewById(R.id.recyclerView);


        chats = new ArrayList<>();
        blockedNumberDao = BlockedNumberDatabase.getInstance(ManageBlockingActivity.this).blockedNumberDao();
        AsyncExecutorUtil.getInstance().getExecutor().execute(() -> {
            List<BlockedNumber> data =blockedNumberDao.getAll();
            if(chats.size()>0){
                chats.clear();
            }
            if(data.size() == 0){
                ivnodataiv.setVisibility(View.VISIBLE);
            }else {
                ivnodataiv.setVisibility(View.GONE);
                Log.d("CheckDataBlocked","Size   =   "+data.size());
                for (int i=0;i<data.size();i++){
                    getBlockData = new GetBlockData();
                    getBlockData.setName(data.get(i).getPhoneName());
                    getBlockData.setNumber(data.get(i).getPhoneNumber());
                    chats.add(getBlockData);
                }
                adapter = new BlockAdapter(ManageBlockingActivity.this, chats);
                mRecycler.setLayoutManager(new LinearLayoutManager(ManageBlockingActivity.this));
                mRecycler.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

        });
    }
}