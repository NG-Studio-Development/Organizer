package com.ngstudio.organaizer.ui.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.ngstudio.organaizer.MainActivity;
import com.ngstudio.organaizer.R;
import com.ngstudio.organaizer.model.Task;
import com.ngstudio.organaizer.service.receivers.SampleAlarmReceiver;

import java.util.Calendar;
import java.util.Date;

public class HandlingTaskFragment extends BaseFragment<MainActivity> {
    private static final String ID_TASK = "id_task";
    private static final String HANDLING_MODE = "handling_mode";

    public static final int LOOK_MODE = 0;
    public static final int CREATE_MODE = 1;
    public static final int UPDATE_MODE = 2;

    private ImageButton ibOk;
    private EditText etNaming;
    private EditText etDescription;
    private TextView tvNaming;
    private TextView tvDescription;
    protected TextView tvDeadlineData;
    protected TextView tvDeadlineTime;

    private int handlingMode;
    private long idTask;

    public static HandlingTaskFragment newInstance() {
        HandlingTaskFragment fragment = new HandlingTaskFragment();
        Bundle args = new Bundle();
        args.putInt(HANDLING_MODE, CREATE_MODE);
        fragment.setArguments(args);
        return fragment;
    }

    public static HandlingTaskFragment newInstance(long idTask, int handlingMode) {
        HandlingTaskFragment fragment = new HandlingTaskFragment();
        Bundle args = new Bundle();
        args.putLong(ID_TASK, idTask);
        args.putInt(HANDLING_MODE, handlingMode);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutResID() {
        return 0;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            handlingMode = getArguments().getInt(HANDLING_MODE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_handle_task, container, false);

        etNaming = (EditText) rootView.findViewById(R.id.etNaming);
        etDescription = (EditText) rootView.findViewById(R.id.etDescription);

        tvNaming = (TextView) rootView.findViewById(R.id.tvNaming);
        tvDescription = (TextView) rootView.findViewById(R.id.tvDescription);

        tvDeadlineData = (TextView) rootView.findViewById(R.id.tvDeadlineData);
        tvDeadlineTime = (TextView) rootView.findViewById(R.id.tvDeadlineTime);

        final DatePickerFragment datePickerFragment = new DatePickerFragment();
        final TimePickerFragment timePickerFragment = new TimePickerFragment();
        final SampleAlarmReceiver alarm = new SampleAlarmReceiver();

        ibOk = (ImageButton) rootView.findViewById(R.id.ibOk);
        ibOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Task task = null;

                if (handlingMode == UPDATE_MODE)
                    task = Task.load(Task.class,idTask);
                else
                    task = new Task();

                task.naming = etNaming.getText().toString();
                task.description = etDescription.getText().toString();
                task.priority = Task.PRIORITY_HIGH;

                long dateUnix = 0;

                if (datePickerFragment.isSetDate()) {
                    dateUnix += datePickerFragment.getDateInMillis();
                    Log.d("HANDLE_DATE", "Date = "+datePickerFragment.getDateInMillis());
                }


                if (timePickerFragment.isSetTime()) {
                    dateUnix += timePickerFragment.getTimeInMillis();
                    Log.d("HANDLE_DATE", "Time = "+timePickerFragment.getTimeInMillis());
                }


                task.dateUnix = dateUnix;
                long id = task.save();


                alarm.setAlarm(getHostActivity(),dateUnix,id);

                getHostActivity().popBackStackUpTo(ListFragment.class);
            }
        });

        ImageButton ibAddData = (ImageButton) rootView.findViewById(R.id.ibAddDeadlineDataData);
        ibAddData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                datePickerFragment.setViewSowingResult(tvDeadlineData);
                datePickerFragment.show(getFragmentManager(), "datePicker");
            }
        });

        ImageButton ibAddTime = (ImageButton) rootView.findViewById(R.id.ibAddDeadlineTime);
        ibAddTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timePickerFragment.show(getFragmentManager(), "timePicker");
            }
        });


        Button buttonToList = (Button) rootView.findViewById(R.id.buttonToList);
        buttonToList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHostActivity().switchFragment(new ListFragment(),false);
            }
        });

        tuningLayout();
        return rootView;
    }

    private void tuningLayout() {
        if(handlingMode == UPDATE_MODE) {
            idTask = getArguments().getLong(ID_TASK);
            setVisibilityViewsInLookMode(View.GONE);
            inputFieldDataInUpdateMode(Task.load(Task.class, idTask));
        } else if (handlingMode == LOOK_MODE) {
            idTask = getArguments().getLong(ID_TASK);
            setVisibilityViewsInEditMode(View.GONE);
            inputFieldDataInLookMode(Task.load(Task.class, idTask));
        } else if (handlingMode == CREATE_MODE) {
            setVisibilityViewsInLookMode(View.GONE);
        }
    }

    private void setVisibilityViewsInEditMode (int visibility) {
        etNaming.setVisibility(visibility);
        etDescription.setVisibility(visibility);
        ibOk.setVisibility(visibility);
    }

    private void setVisibilityViewsInLookMode(int visibility) {
        tvNaming.setVisibility(visibility);
        tvDescription.setVisibility(visibility);
    }

    private void inputFieldDataInUpdateMode (Task task) {
        etNaming.setText(task.naming);
        etDescription.setText(task.description);
        setDate(task.dateUnix);
    }

    private void inputFieldDataInLookMode (Task task) {
        tvNaming.setText(task.naming);
        tvDescription.setText(task.description);
        setDate(task.dateUnix);
    }

    private void setDate(long unixTime) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(unixTime);
        String time = calendar.get(Calendar.HOUR_OF_DAY)
                +":"+ calendar.get(Calendar.MINUTE);

        String date = calendar.get(Calendar.DAY_OF_MONTH) + "."
                + calendar.get(Calendar.MONTH) + "."
                + calendar.get(Calendar.YEAR);

        tvDeadlineTime.setText("Time: "+time);
        tvDeadlineData.setText("Date: "+date);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_handle_task, menu);
        menu.setGroupVisible(R.id.groupHandleTask, handlingMode == LOOK_MODE);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.actionEditTask) {
            getHostActivity().switchFragment(HandlingTaskFragment.newInstance(idTask,HandlingTaskFragment.UPDATE_MODE),true);
        } else if (item.getItemId() == R.id.actionRemoveTask) {
            Task.load(Task.class, idTask).delete();
            getHostActivity().popBackStackUpTo(ListFragment.class);
        }
        return super.onOptionsItemSelected(item);
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        protected Date date;
        private boolean isSetDate = false;
        private View view;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            return new DatePickerDialog(getActivity(),this,year,month,day);
        }

        long getDateInMillis() {
            return this.date.getTime();
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Calendar cal = Calendar.getInstance();
            cal.clear();
            cal.set(year, month, day);
            date = cal.getTime();
            isSetDate = true;
            fillViewsOnResultSetData(year,month,day);
        }

        public boolean isSetDate() {return isSetDate; }

        public void setViewSowingResult(View view) {
            this.view = view;
        }
        private void fillViewsOnResultSetData(int year, int month, int day) {
            if(view != null)
                ((TextView)view).setText(day+"."+(++month)+"."+year);
        }
    }

    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        int hour;
        int minute;
        boolean isSetTime = false;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            hour = c.get(Calendar.HOUR_OF_DAY);
            minute = c.get(Calendar.MINUTE);

            return new TimePickerDialog(getActivity(),this,hour,minute, DateFormat.is24HourFormat(getActivity()));
        }

        private void fillViewsOnResultSetTime(int time, int minute) {
            tvDeadlineTime.setText(time+":"+minute);
        }

        @Override
        public void onTimeSet(TimePicker timePicker, int hour, int minute) {
            this.hour = hour;
            this.minute = minute;
            fillViewsOnResultSetTime(hour,minute);
            isSetTime = true;
        }

        public boolean isSetTime() {return isSetTime; }

        protected long getTimeInMillis() { return ((hour*60*60)+(minute*60))*1000; }
    }



}
