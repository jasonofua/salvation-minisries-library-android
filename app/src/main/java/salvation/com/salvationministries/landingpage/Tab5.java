package salvation.com.salvationministries.landingpage;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import salvation.com.salvationministries.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class Tab5 extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;


    public Tab5() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_tab5, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expListView = (ExpandableListView) view.findViewById(R.id.lvExp);

        // preparing list data
        prepareListData();

       // listAdapter = new ExpandableListAdapter(getContext(), listDataHeader, listDataChild);

        listAdapter = new salvation.com.salvationministries.Adapter.ExpandableListAdapter(getContext(),listDataHeader,listDataChild);
        // setting list adapter
        expListView.setAdapter(listAdapter);
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("How do i purchase a book");
        listDataHeader.add("How do i purchase Audio or video message");
        listDataHeader.add("How to Download purchased message");


        // Adding child data
        List<String> a = new ArrayList<String>();
        a.add("Login to your account, select the desired book from the EBOOK category on the home page or use the search Tab. Click on buy and you will be redirected to the payment area, use your desire debit or credit card");
        List<String> b = new ArrayList<String>();
        b.add("Login to your account, the desired audio or video message from the different Topic Genre on the home page or use the search Tab. Click on buy and you will be redirected to the payment area, use your desire debit or credit card");
        List<String> c = new ArrayList<String>();
        c.add("After successful payment for any of the message a user has selected, the message can be found in the third tab of the App(Message Tab).There are two options which are Message Bought and Downloaded message.\n\nMessage Bought : these are messages you have purchase and can only be played with internet access.\n\nDownloaded message : click on the message bought to download your message soit can be play without internet access.");


        listDataChild.put(listDataHeader.get(0), a);
        listDataChild.put(listDataHeader.get(1), b);
        listDataChild.put(listDataHeader.get(2), c);

    }
}
