package com.busyprojects.roomies.fargs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.busyprojects.roomies.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class List01Fragment extends Fragment {


    public List01Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list01, container, false);
    }

}
