package com.lyy_wzw.comeacross.addressbook;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lyy_wzw.comeacross.MainActivity;
import com.lyy_wzw.comeacross.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddressBookFragment extends Fragment implements View.OnClickListener{

    private static MainActivity mainActivity;
    private Button button;

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
        View view = inflater.inflate(R.layout.fragment_address_book, container, false);
        button = ((Button) view.findViewById(R.id.friend));
        button.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View view) {


    }
}
