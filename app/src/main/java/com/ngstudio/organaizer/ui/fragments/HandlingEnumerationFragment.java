package com.ngstudio.organaizer.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.ngstudio.organaizer.R;
import com.ngstudio.organaizer.model.Enumeration;
import com.ngstudio.organaizer.ui.adapters.EnumerationItemAdapter;

import java.util.List;


public class HandlingEnumerationFragment extends Fragment {

    private final static String ARG_ID_ROOT_ENUMERATION = "arg_id_root_enumeration";

    private EditText etNote;
    private ListView listView;
    private ImageButton ibAddNote;


    private long idRootEnumeration;

    public static HandlingEnumerationFragment newInstance(long idRootEnumeration) {
        HandlingEnumerationFragment fragment = new HandlingEnumerationFragment();
        Bundle args = new Bundle();

        args.putLong(ARG_ID_ROOT_ENUMERATION, idRootEnumeration);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.idRootEnumeration = getArguments().getLong(ARG_ID_ROOT_ENUMERATION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_handling_enumeration, container, false);
        final Enumeration rootEnumeration = Enumeration.load(Enumeration.class, idRootEnumeration);
        final List<Enumeration> list = Enumeration.getAll(rootEnumeration);
        final EnumerationItemAdapter enumerationAdapter = new EnumerationItemAdapter(getActivity(),R.layout.item_enumeration, list);

        etNote = (EditText) rootView.findViewById(R.id.etNote);
        listView = (ListView) rootView.findViewById(R.id.lvEnumerations);
        listView.setAdapter(enumerationAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                boolean itemIsAchieved = enumerationAdapter.getItem(i).isAchieved();
                int visibility = itemIsAchieved ? View.VISIBLE : View.INVISIBLE;
                view.findViewById(R.id.viewLine).setVisibility(visibility);
                enumerationAdapter.getItem(i).setAchievedState(!itemIsAchieved);
            }
        });

        ibAddNote = (ImageButton) rootView.findViewById(R.id.ibAddNote);
        ibAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Enumeration e = new Enumeration();
                e.name = etNote.getText().toString();
                e.parent = rootEnumeration;
                e.save();
                list.add(e);
                enumerationAdapter.notifyDataSetChanged();
                etNote.setText(null);
            }
        });

        return rootView;
    }

}
