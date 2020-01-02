package com.mpscexams.bhajaneapp.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mpscexams.bhajaneapp.DataDisplayActivity;
import com.mpscexams.bhajaneapp.R;


public class LyricsDisplayFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "index";
    private static final String ARG_PARAM2 = "title";


    private String mParam1;
    private String mParam2;

//    private OnFragmentInteractionListener mListener;

    public LyricsDisplayFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     *
     * @return A new instance of fragment LyricsDisplayFragment.
     */
    public static LyricsDisplayFragment newInstance(String param1, String param2) {
        LyricsDisplayFragment fragment = new LyricsDisplayFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_lyrics_display, container, false);
        TextView tv = (TextView) root.findViewById(R.id.viewPagerText);
        TextView tv1 = (TextView) root.findViewById(R.id.viewPagerTitle);
        DataDisplayActivity activity = (DataDisplayActivity) getActivity();
        activity.setTitleString(mParam2);
        activity.setTextString(mParam1);
        tv.setText(mParam1);
        tv1.setText(mParam2);

        return root;
    }
}
