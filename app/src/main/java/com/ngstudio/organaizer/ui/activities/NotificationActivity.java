package com.ngstudio.organaizer.ui.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.ngstudio.organaizer.MainActivity;
import com.ngstudio.organaizer.R;
import com.ngstudio.organaizer.model.Task;
import com.ngstudio.organaizer.ui.fragments.HandlingTaskFragment;

public class NotificationActivity extends BaseActivity  {

    Button buttonToList;

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_notification);

        buttonToList = (Button) findViewById(R.id.buttonToList);
        buttonToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.startActivity(NotificationActivity.this);
                finish();
            }
        });

        Intent intent = getIntent();
        Fragment fragment = HandlingTaskFragment.newInstance(intent.getLongExtra(Task.TASK_ID, -1), HandlingTaskFragment.LOOK_MODE);
        switchFragment(fragment,false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_task_notification, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
