package com.gpslab.kaun.private_and_public;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gpslab.kaun.R;
import com.gpslab.kaun.retrofit.Log;

import java.util.ArrayList;
import java.util.List;

public class PrivatePubileAdapter extends RecyclerView.Adapter<PrivatePubileAdapter.ViewHolder> {
    public static Context mcontext;

    public boolean run = false;
    public static List<GetPrivateAndPubile> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;

    public GetPrivateAndPubile getselfdata;
    String Contest = "";


    ViewHolder viewHolder;

    public PrivatePubileAdapter(Context context, List<GetPrivateAndPubile> productList) {
        super();
        this.context = context;
        this.productList = productList;
        mcontext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.private_and_public_recyclerview_layout, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        getselfdata = productList.get(i);
        view = viewHolder;

        Log.d("Popupcheckknew","image path   ==     "+productList.get(i).getImg());

        Glide.with(context).load(productList.get(i).getImg()).placeholder(R.drawable.profile).into(viewHolder.tvicon);

   





    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder  {
        public TextView tvdesciption,  tvdate, tvamount;
        public TextView tvrupees;
        ImageView tvicon;
        public CountDownTimer timer;
        public EditText etproductno;
        public Spinner spscheme, spunit;
        //        private ItemClickListener clickListener;
        public ArrayAdapter<CharSequence> adapterscheme;
        public ArrayAdapter<CharSequence> adapterunit;
        public Animation myAnim;
        public TextView tvid,tvcategoryId;
        public ArrayList<String> getada;
        public LinearLayout layoutman;

        public ViewHolder(View itemView) {
            super(itemView);



            tvicon = (ImageView) itemView.findViewById(R.id.imgimgiv);



        }




    }

}


