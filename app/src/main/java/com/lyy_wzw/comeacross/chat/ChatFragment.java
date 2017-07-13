package com.lyy_wzw.comeacross.chat;


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
public class ChatFragment extends Fragment {
    private static MainActivity mainActivity;

    public ChatFragment() {
        // Required empty public constructor
    }

    public static ChatFragment instance(MainActivity activity){
        mainActivity = activity;
        return new ChatFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false);
    }



}
