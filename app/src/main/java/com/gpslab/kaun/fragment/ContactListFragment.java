package com.gpslab.kaun.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.gpslab.kaun.Notification.UserProfile;
import com.gpslab.kaun.R;
import com.gpslab.kaun.Contact.ContactAdapter;
import com.gpslab.kaun.databinding.FragmentContactListBinding;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.gpslab.kaun.Contact.ContactViewModel;
import com.trendyol.bubblescrollbarlib.BubbleScrollBar;
import com.trendyol.bubblescrollbarlib.BubbleTextProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ContactListFragment extends Fragment implements View.OnClickListener {

    private ContactViewModel contactViewModel;

    private FragmentContactListBinding binding;
    private boolean isSearch = false;
    public LinearLayout mlinear;
    ContactAdapter adapter;


    public ContactListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ContactListFragment.
     */
    public static ContactListFragment newInstance() {
        return new ContactListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        contactViewModel = new ContactViewModel(getContext().getApplicationContext());

        binding = FragmentContactListBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        binding.datanewrecyclerView.setVisibility(View.VISIBLE);
        initRecyclerView();
        binding.searchView.setOnClickListener(this);
        binding.etSearch.setOnClickListener(this);

        binding.bubbleScroll.attachToRecyclerView(binding.contactRecyclerView);

        binding.bubbleScroll.setBubbleTextProvider(new BubbleTextProvider() {
            @Override
            public String provideBubbleText(int i) {
                return new StringBuilder(contactViewModel.getContacts().get(i).getName().substring(0,1)).toString();
            }
        });

        return view;
    }


    private void initRecyclerView() {
        binding.contactRecyclerView.setLayoutManager(new LinearLayoutManager(binding.contactRecyclerView.getContext()));
//        binding.contactRecyclerView.addItemDecoration(new DividerItemDecoration(binding.contactRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
        adapter = new ContactAdapter(binding.contactRecyclerView.getContext());
        adapter.setContacts(contactViewModel.getContacts());
        binding.contactRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener((view, obj, position) -> {
            Toast.makeText(getContext(), "Contact Selected\n"+obj.getName()+obj.getPhoneNumber(), Toast.LENGTH_LONG).show();



            Intent notificationIntent = new Intent(getContext(), UserProfile.class);

            notificationIntent.putExtra("number", obj.getPhoneNumber());

            notificationIntent.putExtra("duration","100");

            notificationIntent.putExtra("name",obj.getName());
            if(obj.getPhotoUri() !=null){
                notificationIntent.putExtra("img", obj.getPhotoUri());

            }else {
                notificationIntent.putExtra("img", "NA");
            }

            startActivity(notificationIntent);
        });
        binding.datanewrecyclerView.setVisibility(View.GONE);
    }


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.et_search) {
            isSearch = true;
            binding.searchLayout.setVisibility(View.INVISIBLE);
            binding.searchView.setVisibility(View.VISIBLE);
            setUpSearch();
        }
    }

    /**
     * Close Search
     */
    private void closeSearch() {
        if (isSearch) {
            isSearch = false;
            binding.searchLayout.setVisibility(View.VISIBLE);
            binding.searchView.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * Set up search.
     */
    private void setUpSearch() {
        binding.searchView.setIconified(false);
        binding.searchView.setQueryHint("Enter name");
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s);
                return true;
            }
        });
        binding.searchView.setOnCloseListener(() -> {
            closeSearch();
            return true;
        });
    }
}
