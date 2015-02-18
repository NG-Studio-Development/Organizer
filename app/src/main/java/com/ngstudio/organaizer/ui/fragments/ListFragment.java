package com.ngstudio.organaizer.ui.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.ngstudio.organaizer.MainActivity;
import com.ngstudio.organaizer.R;
import com.ngstudio.organaizer.model.Enumeration;
import com.ngstudio.organaizer.model.Task;
import com.ngstudio.organaizer.ui.adapters.EnumerationItemAdapter;
import com.ngstudio.organaizer.ui.adapters.TaskItemAdapter;

public class ListFragment extends BaseFragment<MainActivity> {

    private static int TYPE_LIST_OF_TASK = 0;
    private static int TYPE_LIST_OF_LIST = 1;

    private int typeStartFragment;

    private ListView lvPlaces;
    private ImageButton ibAdd;
    private ArrayAdapter adapter; // Temperary antipattern field //


    public static ListFragment newInstanceListTask(Context context) {
        ListFragment fragment = new ListFragment();
        fragment.setAdapter(new TaskItemAdapter(context,R.layout.item_task, Task.getAll()));
        fragment.typeStartFragment = TYPE_LIST_OF_TASK;
        return fragment;
    }

    public static ListFragment newInstanceListEnumeration(Context context) {

        // *** Temporary for debug *** //
        if (Enumeration.getAllMainRoot().size() <= 0)
            Enumeration.debugAddListMainRoot();

        ListFragment fragment = new ListFragment();
        fragment.setAdapter(new EnumerationItemAdapter(context,R.layout.item_task, Enumeration.getAllMainRoot()));
        fragment.typeStartFragment = TYPE_LIST_OF_LIST;
        return fragment;
    }


    @Override
    public int getLayoutResID() {
        return R.layout.fragment_task_list;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_task_list, container, false);

        ibAdd = (ImageButton) rootView.findViewById(R.id.ibAdd);
        ibAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (typeStartFragment == TYPE_LIST_OF_TASK)
                    getHostActivity().switchFragment(HandlingTaskFragment.newInstance(), true);
                else if (typeStartFragment == TYPE_LIST_OF_LIST)
                    showDialogAddCategory(LayoutInflater.from(getActivity()).inflate(R.layout.view_dialog_add_category, null));
            }
        });

        lvPlaces = (ListView) rootView.findViewById(R.id.lvPlaces);
        lvPlaces.setAdapter(adapter);
        lvPlaces.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (typeStartFragment == TYPE_LIST_OF_TASK)
                    getHostActivity().switchFragment(HandlingTaskFragment.newInstance(l,HandlingTaskFragment.LOOK_MODE),true);
                else if (typeStartFragment == TYPE_LIST_OF_LIST)
                    getHostActivity().switchFragment(HandlingEnumerationFragment.newInstance(l),true);

            }
        });
        return rootView;
    }

    private void addItemToListView(Enumeration enumeration) {
        ArrayAdapter adapter = (ArrayAdapter) lvPlaces.getAdapter();
        adapter.add(enumeration);
        adapter.notifyDataSetChanged();
    }

    private void setAdapter(ArrayAdapter adapter ) {
        this.adapter = adapter;
    }

    public void showDialogAddCategory (View view) {
        View viewDialog = LayoutInflater.from(getHostActivity()).inflate(R.layout.view_dialog_add_category, null);
        final EditText etName = (EditText) viewDialog.findViewById(R.id.etName);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setPositiveButton("HARD_OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (etName.getText().toString().equals("")) {
                    Toast.makeText(getActivity(), "HARD_WRONG", Toast.LENGTH_SHORT).show();
                } else {
                    Enumeration enumeration = Enumeration.createMainRootParent();
                    enumeration.name = etName.getText().toString();
                    enumeration.save();
                    addItemToListView(enumeration);
                }
            }
        });
        builder.setNegativeButton("HARD_CANCEL", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        builder.setView(viewDialog);

        AlertDialog dialog = builder.create();
        dialog.show();

    }

}
