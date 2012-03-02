package com.steo.europlanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GroupFragment extends Fragment {

    private final String mGroupName;

    public GroupFragment(String groupName) {
        mGroupName = groupName;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View fragView = inflater.inflate(R.layout.group_fragment, container, false);
        TextView groupName = (TextView)fragView.findViewById(R.id.groupFragmentText);
        groupName.setText(mGroupName);

        return fragView;
    }

}
