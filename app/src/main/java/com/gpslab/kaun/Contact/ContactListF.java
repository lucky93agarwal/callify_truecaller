package com.gpslab.kaun.Contact;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gpslab.kaun.fragment.ContactListFragment;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import io.fotoapparat.parameter.Flash;

public class ContactListF extends Fragment {
    private ContactListF contactListFragment;
    private RecyclerView recyclerView;
    public ContactListF()
    {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.f_contact_list, container, false);

        Log.d("ContactListCheckData","ContactList    ===      ");
        if (view instanceof RecyclerView)
        {
            Log.d("ContactListCheckData","ContactList    ===  2    ");
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            contactListFragment = this;

            new ImportContactsAsync(getActivity(), new ImportContactsAsync.ICallback()
            {
                @Override
                public void mobileContacts(ArrayList<ContactNew> contactList)
                {
                    ArrayList<ContactNew> listItem = contactList;

                    if(listItem==null)
                    {
                        listItem = new ArrayList<ContactNew>() ;
//                        Log.i(C.TAG_LIB, "Error in retrieving contacts");
                    }

                    if(listItem.isEmpty())
                    {
                        Toast.makeText(getActivity(), "No contacts found", Toast.LENGTH_LONG).show();
                    }

                    recyclerView.setAdapter(new ContactListViewAdapter(contactListFragment, listItem));
                }
            }).execute();
        }
        return view;
    }


    private class ContactListViewAdapter extends RecyclerView.Adapter<ContactListViewAdapter.ViewHolder>
    {

        private ContactListF fragment;
        private Context context;
        private ArrayList<ContactNew> contactList = new ArrayList<>();

        public ContactListViewAdapter(ContactListF fragment, ArrayList<ContactNew> contactList) {
            this.fragment = fragment;
            context = fragment.getActivity();
            this.contactList = contactList;
        }

        @Override
        public ContactListViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.newitem_contact, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ContactListViewAdapter.ViewHolder holder, final int position) {
            holder.vhContact = contactList.get(position);
            if(holder.vhContact.getFirstName().length() != 0){
                holder.name.setText(holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());
            }else {
                if(holder.vhContact.getNumbers().size()>0){
                    LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();
                    holder.name.setText(numberList.get(0).elementValue());
                }

            }
            if(holder.vhContact.getNumbers().size()>0){
                LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();
                holder.mobileno.setText(numberList.get(0).elementValue());
            }

            String imageUri = contactList.get(position).getPhotoUri();
            if(imageUri !=null) {
                holder.firstlatertext.setVisibility(View.GONE);
                holder.image.setVisibility(View.VISIBLE);
                Glide.with(context)
                        .load(imageUri)
                        .error(R.drawable.person)
                        .into(holder.image);
            }else {
                holder.firstlatertext.setVisibility(View.VISIBLE);
                holder.image.setVisibility(View.GONE);
                GradientDrawable gradientDrawable = (GradientDrawable) holder.firstlatertext.getBackground();
                gradientDrawable.setColor(getRandomColor());
                holder.firstlatertext.setTextColor(getRandomColorText());
                if(holder.vhContact.getFirstName().length()>1){

                    String serviceSubStringtwo = (holder.vhContact.getFirstName().substring(0, 1));
                    holder.firstlatertext.setText(serviceSubStringtwo.toUpperCase());
                }else {
                    if(holder.vhContact.getNumbers().size()>0){
                        LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();
                        String serviceSubString = (numberList.get(0).elementValue().substring(0, 1));
                        holder.firstlatertext.setText(serviceSubString);

                    }
                }

            }

            holder.name.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (contactList ==null )
                    {
                        return;
                    }

                    Intent i = new Intent(context, UserProfile.class);
                    if(holder.vhContact.getNumbers().size()>0){
                        LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();

                        i.putExtra("number", numberList.get(0).elementValue());

                    }else {
                        i.putExtra("number", "");
                    }
                    i.putExtra("duration","100");
                    i.putExtra("name",holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());
                    String imageUri = contactList.get(position).getPhotoUri();
                    if(imageUri !=null) {
                        i.putExtra("img", imageUri);

                    }else {
                        i.putExtra("img", "NA");
                    }
//                    GsonBuilder builder = new GsonBuilder();
//                    builder.excludeFieldsWithoutExposeAnnotation();
//                    Gson gsonBuilder = builder.create();
//                    String jsonContact = gsonBuilder.toJson(contactList.get(position));
//                    i.putExtra(C.CONTACT, jsonContact);
                    context.startActivity(i);
                }
            });



            holder.mobileno.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (contactList ==null )
                    {
                        return;
                    }

                    Intent i = new Intent(context, UserProfile.class);
                    if(holder.vhContact.getNumbers().size()>0){
                        LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();

                        i.putExtra("number", numberList.get(0).elementValue());

                    }else {
                        i.putExtra("number", "");
                    }
                    i.putExtra("duration","100");
                    i.putExtra("name",holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());
                    String imageUri = contactList.get(position).getPhotoUri();
                    if(imageUri !=null) {
                        i.putExtra("img", imageUri);

                    }else {
                        i.putExtra("img", "NA");
                    }
//                    GsonBuilder builder = new GsonBuilder();
//                    builder.excludeFieldsWithoutExposeAnnotation();
//                    Gson gsonBuilder = builder.create();
//                    String jsonContact = gsonBuilder.toJson(contactList.get(position));
//                    i.putExtra(C.CONTACT, jsonContact);
                    context.startActivity(i);
                }
            });

            holder.firstlatertext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (contactList ==null )
                    {
                        return;
                    }

                    Intent i = new Intent(context, UserProfile.class);
                    if(holder.vhContact.getNumbers().size()>0){
                        LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();

                        i.putExtra("number", numberList.get(0).elementValue());

                    }else {
                        i.putExtra("number", "");
                    }
                    i.putExtra("duration","100");
                    i.putExtra("name",holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());
                    String imageUri = contactList.get(position).getPhotoUri();
                    if(imageUri !=null) {
                        i.putExtra("img", imageUri);

                    }else {
                        i.putExtra("img", "NA");
                    }
//                    GsonBuilder builder = new GsonBuilder();
//                    builder.excludeFieldsWithoutExposeAnnotation();
//                    Gson gsonBuilder = builder.create();
//                    String jsonContact = gsonBuilder.toJson(contactList.get(position));
//                    i.putExtra(C.CONTACT, jsonContact);
                    context.startActivity(i);
                }
            });



            holder.mainConstrain.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (contactList ==null )
                    {
                        return;
                    }

                    Intent i = new Intent(context, UserProfile.class);
                    if(holder.vhContact.getNumbers().size()>0){
                        LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();

                        i.putExtra("number", numberList.get(0).elementValue());

                    }else {
                        i.putExtra("number", "");
                    }
                    i.putExtra("duration","100");
                    i.putExtra("name",holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());
                    String imageUri = contactList.get(position).getPhotoUri();
                    if(imageUri !=null) {
                        i.putExtra("img", imageUri);

                    }else {
                        i.putExtra("img", "NA");
                    }
//                    GsonBuilder builder = new GsonBuilder();
//                    builder.excludeFieldsWithoutExposeAnnotation();
//                    Gson gsonBuilder = builder.create();
//                    String jsonContact = gsonBuilder.toJson(contactList.get(position));
//                    i.putExtra(C.CONTACT, jsonContact);
                    context.startActivity(i);
                }
            });

            holder.image.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (contactList ==null )
                    {
                        return;
                    }

                    Intent i = new Intent(context, UserProfile.class);
                    if(holder.vhContact.getNumbers().size()>0){
                        LinkedList<NumberContainer> numberList = holder.vhContact.getNumbers();

                        i.putExtra("number", numberList.get(0).elementValue());

                    }else {
                        i.putExtra("number", "");
                    }
                    i.putExtra("duration","100");
                    i.putExtra("name",holder.vhContact.getFirstName() + " " + holder.vhContact.getLastName());
                    String imageUri = contactList.get(position).getPhotoUri();
                    if(imageUri !=null) {
                        i.putExtra("img", imageUri);

                    }else {
                        i.putExtra("img", "NA");
                    }
//                    GsonBuilder builder = new GsonBuilder();
//                    builder.excludeFieldsWithoutExposeAnnotation();
//                    Gson gsonBuilder = builder.create();
//                    String jsonContact = gsonBuilder.toJson(contactList.get(position));
//                    i.putExtra(C.CONTACT, jsonContact);
                    context.startActivity(i);
                }
            });
        }
        private int getRandomColor() {
            Random rnd = new Random();

            Ri=rnd.nextInt(156);
            G=rnd.nextInt(156);
            B=rnd.nextInt(156);
            return Color.argb(30, Ri, G, B);
        }

        int Ri,G,B;
        private int getRandomColorText() {


            return Color.argb(150, Ri, G, B);
        }
        @Override
        public int getItemCount() {
            return contactList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView name,mobileno,firstlatertext;
            public final CircleImageView image;
            public ContactNew vhContact;
            public ConstraintLayout mainConstrain;

            public ViewHolder(View view) {
                super(view);
                mView = view;


                mainConstrain = (ConstraintLayout)view.findViewById(R.id.selectedContact_layout);
                name = (TextView) view.findViewById(R.id.contact_name);
                firstlatertext = (TextView) view.findViewById(R.id.drawableTextView);
                image = (CircleImageView) view.findViewById(R.id.contact_photo);
                mobileno = (TextView)view.findViewById(R.id.contact_number);
            }

        }
    }
}
