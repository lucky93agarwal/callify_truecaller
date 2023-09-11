package com.gpslab.kaun.calling;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gpslab.kaun.Contact.ContactListActivity;
import com.gpslab.kaun.DB.GetChatContactList;
import com.gpslab.kaun.DB.MyDbHandler;
import com.gpslab.kaun.DB.Temp;
import com.gpslab.kaun.R;
import com.gpslab.kaun.adapter.ChatsAdapterNew;
import com.gpslab.kaun.model.Chat;
import com.gpslab.kaun.retrofit.VersionRequestJson;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.User;

import java.util.ArrayList;

import io.realm.RealmResults;

public class NewCallActivity extends AppCompatActivity{
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public ImageView ivback;
    private VersionRequestJson request;
    private ArrayList<Chat> chats;
    private RecyclerView rvChats;
    public String resTxt = "";
    public ImageView ivsearchiv;
    public TextView tvtotalcontact;
    private ChatsAdapterNew adapter;
    public LinearLayout msearchLayout;
    public boolean check = false;
    public ImageView ivcloseiviv, ivbackiviviv;
    public EditText etsearch;
    public MyDbHandler myDbHandler;
    private PerformCall performCall;
    private Window window;
    private int brightness = 200;

    String insertChat = "";
    String insertChatData = "";


    private void ScreenViewControl() {
        //       Get the current system brightness
        window = NewCallActivity.this.getWindow();
        WindowManager.LayoutParams layoutpars = window.getAttributes();
        layoutpars.screenBrightness = brightness / (float) 255;
        window.setAttributes(layoutpars);
    }
    private void initialize() {

        chats = new ArrayList<>();
        rvChats = (RecyclerView) findViewById(R.id.rvChats);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_call);

        myDbHandler = new MyDbHandler(getApplicationContext(), "userbd", null, 1);
        Temp.setMyDbHandler(myDbHandler);
        initialize();

        sharedPreferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        ArrayList<GetChatContactList> arrayList2 = myDbHandler.viewChatContact();


        tvtotalcontact = (TextView)findViewById(R.id.totalcontacttv);

        if(arrayList2.size()>0){
            tvtotalcontact.setText(String.valueOf(arrayList2.size())+" contacts");

            for(int i=0;i<arrayList2.size();i++){
                chats.add(new Chat(arrayList2.get(i).getContacts_image(), arrayList2.get(i).getContacts_name(), arrayList2.get(i).getContacts_status(), "",arrayList2.get(i).getContacts_id()));


            }
            adapter = new ChatsAdapterNew(NewCallActivity.this, chats);
            rvChats.setLayoutManager(new LinearLayoutManager(NewCallActivity.this));
            rvChats.setAdapter(adapter);
        }

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



}