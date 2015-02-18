package com.ngstudio.organaizer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.ngstudio.organaizer.model.Task;
import com.ngstudio.organaizer.ui.activities.BaseActivity;
import com.ngstudio.organaizer.ui.fragments.HandlingTaskFragment;
import com.ngstudio.organaizer.ui.fragments.ListFragment;


public class MainActivity extends BaseActivity {

    public static void startActivity(Context context/*, @Nullable Contact contact*/) {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("MAIN_ACTIVITY_DEBUG","onCreate");
    }


    @Override
    protected void onResume() {
        super.onResume();
        switchFragment(getLaunchingFragment(),false);
    }

    private Fragment getLaunchingFragment() {
        Intent intent = getIntent();
        if (intent != null)
            if (intent.getBooleanExtra(IF_START_BY_NOTIFICATION,false))
                return HandlingTaskFragment.newInstance(intent.getLongExtra(Task.TASK_ID, -1), HandlingTaskFragment.LOOK_MODE);
        return getDefaultFragment();
    }

    private Fragment getDefaultFragment() {
        //return ListFragment.newInstanceListTask(this);
        return ListFragment.newInstanceListEnumeration(this);
    }

    public static final String IF_START_BY_NOTIFICATION = "if_start_by_notification";

    protected  int getFragmentContainerId() { return R.id.container; }
}
