package com.lyy_wzw.comeacross.addressbook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressBookFragment extends Fragment {

    private static MainActivity mainActivity;

    public AddressBookFragment() {

    }

    public static AddressBookFragment instance(MainActivity activity){
         mainActivity = activity;
        return new AddressBookFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_address_book, container, false);
    }

}
