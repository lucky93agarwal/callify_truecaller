package com.gpslab.kaun.adapter;

import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.model.ApplicationMain;
import com.gpslab.kaun.model.ContactList;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.model.GetContectData;
import com.gpslab.kaun.popup.simselectpopup;

import java.util.ArrayList;
import java.util.List;

public class ContectAdapter extends RecyclerView.Adapter<ContectAdapter.ViewHolder> {
    public static Context mcontext;

    public boolean run = false;
    public static List<GetContectData> productList;
    public static boolean runtimer = true;
    public Context context;
    ViewHolder view;

    public GetContectData getselfdata;
    String Contest = "";


    ViewHolder viewHolder;

    public ContectAdapter(Context context, List<GetContectData> productlist) {
        super();
        this.context = context;
        this.productList = productlist;
        mcontext = context;
    }

    public void filterList(ArrayList<GetContectData> filteredList) {
        productList = filteredList;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.list_product_new, viewGroup, false);
        viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        view = viewHolder;

        if ((i & 1) == 1) {
            viewHolder.mmlinear.setBackgroundResource(R.drawable.circledrawable);
            viewHolder.tvfirst.setTextColor(Color.parseColor("#55A655"));
        } else
        if ((i & 1) == 0 ) {
            viewHolder.tvfirst.setTextColor(Color.parseColor("#4F88AA"));
            viewHolder.mmlinear.setBackgroundResource(R.drawable.circledrawnew);
        } else {
            viewHolder.tvfirst.setTextColor(Color.parseColor("#ffffff"));
            viewHolder.mmlinear.setBackgroundResource(R.drawable.circledrawnewtwo);
        }

        Typeface custom = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-SemiBold.ttf");
        viewHolder.tvtitle.setText(productList.get(i).name);
        if (productList.get(i).getNumber().length() > 0 && productList.get(i).getNumber() != null) {
            viewHolder.tvnumber.setText(productList.get(i).getNumber());
        }

        viewHolder.tvtitle.setTypeface(custom);



        viewHolder.tvfirst.setText(productList.get(i).getName().substring(0,1));



   //     Glide.with(mcontext).load(productList.get(i).getImage()).apply(new RequestOptions().placeholder(R.mipmap.ic_launcher)).into(viewHolder.ivicon);






    }

    @Override
    public int getItemCount() {
        if(productList.size()>3){
            return 3;
        }else {
            return productList.size();
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvtitle,tvnumber,tvfirst,tvtwonum, tvemails;
        private final static String simSlotName[] = {
                "extra_asus_dial_use_dualsim",
                "com.android.phone.extra.slot",
                "slot",
                "simslot",
                "sim_slot",
                "subscription",
                "Subscription",
                "phone",
                "com.android.phone.DialingMode",
                "simSlot",
                "slot_id",
                "simId",
                "simnum",
                "phone_type",
                "slotId",
                "slotIdx"
        };
        public ImageView ivclosetv,ivcaller;

        LinearLayout mlinearLayout,mmlinear,mmdetailsllpage,mmimagellmm;
        //        private FirebaseFirestore firebaseFirestore;
//        DatabaseReference rootRef;
        public AlertDialog alertDialog;


        private static simselectpopup defaultpopupnew;


        public ViewHolder(View itemView) {
            super(itemView);
            tvtwonum = (TextView) itemView.findViewById(R.id.twonum);
            defaultpopupnew = new simselectpopup(mcontext);

            mmimagellmm = (LinearLayout)itemView.findViewById(R.id.imagellmm);

            mmdetailsllpage = (LinearLayout)itemView.findViewById(R.id.detailsllpage);

            mmlinear=(LinearLayout)itemView.findViewById(R.id.linearoneone);

            ivcaller = (ImageView)itemView.findViewById(R.id.calliv);



            tvemails = (TextView) itemView.findViewById(R.id.tvemail);

            tvfirst = (TextView)itemView.findViewById(R.id.firsttexttv);

            ivclosetv = (ImageView) itemView.findViewById(R.id.closebtniv);
            tvtitle = (TextView) itemView.findViewById(R.id.title);


            tvnumber = (TextView) itemView.findViewById(R.id.time);

            mlinearLayout = (LinearLayout)itemView.findViewById(R.id.linearlayout);


            mlinearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {



                }
            });
            mmimagellmm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(mcontext, UserProfile.class);
                    inten.putExtra("name",productList.get(getAdapterPosition()).getName());
                    inten.putExtra("number",productList.get(getAdapterPosition()).getNumber());
                    inten.putExtra("img",productList.get(getAdapterPosition()).getImage());
                    inten.putExtra("duration",String.valueOf("100"));
                    mcontext.startActivity(inten);
                }
            });
            mmdetailsllpage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inten = new Intent(mcontext, UserProfile.class);
                    inten.putExtra("name",productList.get(getAdapterPosition()).getName());
                    inten.putExtra("number",productList.get(getAdapterPosition()).getNumber());
                    inten.putExtra("img",productList.get(getAdapterPosition()).getImage());
                    inten.putExtra("duration",String.valueOf("100"));
                    mcontext.startActivity(inten);
                }
            });
            ivcaller.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    defaultpopupnew.defaultaddpopup(productList.get(getAdapterPosition()).getNumber());


                }
            });



            ivclosetv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {






                    alertDialog = new AlertDialog.Builder(mcontext)
//set icon
                            .setIcon(android.R.drawable.ic_dialog_alert)
//set title
                            .setTitle("Are you sure delete Society")
//set message
                            .setMessage("Deleting this Society will delete all data for this house. Are you sure you want to do this?")
//set positive button
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what would happen when positive button is clicked
                                    //  APIDelete(productList.get(getAdapterPosition()).getName());
                                    alertDialog.dismiss();
                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //set what should happen when negative button is clicked
//                                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
                                    alertDialog.dismiss();
                                }
                            })
                            .show();


                }
            });







        }




    }


}

