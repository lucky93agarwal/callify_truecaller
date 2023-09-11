package com.gpslab.kaun.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdView;
import com.gpslab.kaun.R;
import com.gpslab.kaun.ResURls;
import com.gpslab.kaun.chatfragment.ChatsAdapter;
import com.gpslab.kaun.retrofit.FBLoginData;
import com.gpslab.kaun.view.Chat;
import com.gpslab.kaun.model.MenuItem;
import com.gpslab.kaun.view.ChatActivity;
import com.gpslab.kaun.view.IntentUtils;
import com.gpslab.kaun.view.RealmHelper;
import com.gpslab.kaun.view.User;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmResults;

public class ChatsFragment  extends Fragment implements  ChatsAdapter.ChatsAdapterCallback{

    public static final String[] profileUrls = {"https://blog.rackspace.com/wp-content/uploads/2018/09/pumping-iron-arnold-schwarzenegger-1-1108x0-c-default-696x522.jpg",
            "https://www.rollingstone.com/wp-content/uploads/2018/06/rs-213329-R1247_FEA_Rogen_A.jpg?crop=900:600&width=440",
            "https://assets.entrepreneur.com/content/3x2/2000/20170118220227-GettyImages-471763092.jpeg",
            "https://static.ffx.io/images/$zoom_0.238%2C$multiply_1%2C$ratio_1.776846%2C$width_1059%2C$x_0%2C$y_55/t_crop_custom/w_800/q_86%2Cf_auto/cf3f16e35b79207935da03f7b4a7e7d6e484ff71"
    };

    public ChatsFragment() {
        // Required empty public constructor
    }
    public SharedPreferences sharedPreferences;
    public SharedPreferences.Editor edit;
    public String resTxt = "";
    private ArrayList<Chat> chats;

    private RecyclerView rvChats;
    private ChatsAdapter adapter;

    public ActionMode actionMode;
    public RealmResults<Chat> chatList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view;
        view = inflater.inflate(R.layout.fragment_chats, container, false);
        chatList = RealmHelper.getInstance().getAllChats();

//       Log.d("ChatsFragmentCheck","saveLastMessageForChat   ==   891 "+chatList.toString());
        sharedPreferences = getActivity().getSharedPreferences("data", Context.MODE_PRIVATE);
        edit = sharedPreferences.edit();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initialize(view);
        populateChats();
//        SendDataToServer("");
        setAdapter();

    }
    private void initialize(View view) {

        chats = new ArrayList<>();
        rvChats = view.findViewById(R.id.rvChats);

    }

    private void populateChats() {
        //Population logic goes here

//        chats.add(new Chat(profileUrls[0], "Arnold", "Sure", "8:15 PM",""));
//        chats.add(new Chat("Alice ", "That is superb! I am busy this week, so I'll let you know if I can make it.", "7.01 PM"));
//        chats.add(new Chat(profileUrls[1], "Joe Rogan", "Thanks man!", "3:15 PM",""));
//        chats.add(new Chat(profileUrls[2], "Elon Musk", "Okay", "Yesterday",""));
//        chats.add(new Chat("G.O.A.T ", "Lets go mate. See you sharp at 6.", "5/12/19"));
//        chats.add(new Chat("Rohan", "Call me when you are free", "5/14/19"));
//        chats.add(new Chat("Ali", "That is lit!", "5/20/19"));
//
//
//
//        chats.add(new Chat(profileUrls[3], "Conor McGregor", "I'm in Dublin. Training for July bout.", "5/22/19",""));

    }
    private void setAdapter() {

        adapter = new ChatsAdapter(chatList,true,requireActivity(), this);
        rvChats.setLayoutManager(new LinearLayoutManager(getContext()));
        rvChats.setAdapter(adapter);

    }


    @Override
    public void userProfileClicked(User user) {

    }

    public void onClick(Chat chat, View view){
        Log.d("ChatsFragmentCheck","id ==   "+chat.getUser().getUid());
        Log.d("ChatsFragmentCheck","name ==   "+chat.getUser().getUserName());
        Log.d("ChatsFragmentCheck","image ==   "+chat.getUser().getThumbImg());
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("ChatData",0);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("chat_id",chat.getUser().getUid());
        edit.apply();
       Intent intent = new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("image",chat.getUser().getThumbImg());
        intent.putExtra("name",chat.getUser().getUserName());
        intent.putExtra("id",chat.getUser().getUid());
       startActivity(intent);
    }

    public void onLongClick(Chat chat, View view){

    }
    public void onBind(int position,Chat chat){

    }
    public void userProfileClicked(Chat chat){

    }
    private void SendDataToServer(final String datasss) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {



                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("mobile", sharedPreferences.getString("mobile","")));
                Log.d("fragmentonefragmentone", "fname = = " + nameValuePairs);

                try {


                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(ResURls.baseURL + "get_app_contacts/");

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
                Log.d("fragmentonefragmentone", "result = = " + result);
                if (result.isEmpty()) {

                } else {


                    try {
                        JSONObject jsonObject = new JSONObject(result);
                        String status = jsonObject.getString("status");
                        if(status == "1"){
                            JSONArray response = jsonObject.getJSONArray("response");
                            Log.d("fragmentonefragmentone", "calltype 2 response = " + response.length());

                            if (chats.size()>0){
                                chats.clear();
                            }
                            for (int i = 0; i < response.length(); i++) {
//                                chats.add(new Chat(profileUrls[0], "Arnold", "Sure", "8:15 PM",response.getJSONObject(i).getString("id")));
                            }
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
