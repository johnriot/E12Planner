package com.steo.europlanner;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupFragment extends Fragment {

    private final Group mGroup;
    Context mContext;

    public GroupFragment(Group group, Context context) {
        mGroup = group;
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);
        TextView groupName = (TextView)fragView.findViewById(R.id.groupFragmentText);
        groupName.setText(mGroup.getName(mContext));

        return fragView;
    }
}
