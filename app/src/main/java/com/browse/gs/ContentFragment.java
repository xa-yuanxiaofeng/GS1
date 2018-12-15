package com.browse.gs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public  class ContentFragment extends Fragment {
    private static final String ARG_SECTION = "section";
    private View rootView;
    private Button  buttonFinish;
    public ContentFragment() {
    }

    public static ContentFragment newInstance(String section) {
        ContentFragment fragment = new ContentFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION, section);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(rootView==null){
            rootView = inflater.inflate(R.layout.activity_content, null);
            buttonFinish=rootView.findViewById(R.id.buttonFinish);
            buttonFinish.setOnClickListener(new Button.OnClickListener(){
                @Override
                public void onClick(View v) {
                    int i=MainActivity.getMainActivity().tabLayout.getSelectedTabPosition();
                    if(i>-1)
                        MainActivity.getMainActivity().viewPagerAdapter.removeTab(i);
                }
            });

        }
        return rootView;
    }
}