package com.example.smartlogbook.ui.main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartlogbook.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentCurrentRegister#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentCurrentRegister extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_SECTION_NUMBER = "section_number";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentCurrentRegister() {
        // Required empty public constructor
    }


    public static FragmentCurrentRegister newInstance(int index) {
        FragmentCurrentRegister fragment = new FragmentCurrentRegister();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_current_register, container, false);
    }
}