package com.gpslab.kaun.popup;

import android.content.Context;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.R;

import java.util.ArrayList;

public class language {

    public ArrayList<Getlanguage> productList = new ArrayList<>();
    HLVAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    Getlanguage getcontestdata;
    private RecyclerView mRecyclerView;
    public Dialog epicDialog;
    private final Context _context;


    public String[] mlng = {"English","हिन्दी","French","Urdu"};


    public language(Context context) {
        this._context = context;

        epicDialog = new Dialog(context, R.style.PauseDialog);
        

    }
    public void addpopup() {
        epicDialog.setContentView(R.layout.depositlayout);
        epicDialog.setCancelable(false);

        mRecyclerView = epicDialog.findViewById(R.id.recycler);


        for(int i=0;i<mlng.length;i++){
            getcontestdata = new Getlanguage();
            getcontestdata.setId(String.valueOf(i));
            getcontestdata.setLanguage(mlng[i]);


            productList.add(getcontestdata);
        }
        adapter = new HLVAdapter(_context, productList);
        mLayoutManager = new LinearLayoutManager(_context, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(adapter);
        epicDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); // popup background transparent
        epicDialog.show();
    }
}
