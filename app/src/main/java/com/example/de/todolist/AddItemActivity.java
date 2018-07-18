package com.example.de.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.util.Calendar;

public class AddItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener{

    public static final String ADD_NEW_ITEM="adding new item";
    public static final String ADD_NEW_DESC="adding new description";
    public static final String ADD_NEW_DATE="adding new date";
    public static final String ADD_NEW_TIME="adding new time";
    public static final String ADD_NEW_EPOCH="adding new epoch";
    public static final String ADD_NEW_YEAR="adding new year";
    public static final String ADD_NEW_MONTH="adding new month";
    public static final String ADD_NEW_DAY="adding new day";
    public static final String ADD_NEW_MINUTE="adding new minute";
    public static final String ADD_NEW_HOUR="adding new hour";
    public static final int ADD_ITEM_CODE=100;
    Button dateButton,timeButton;
    Calendar calendar,calendar1,eCalendar;
    int eYear,eMonth,eDay,eHour,eMinute;
    String newTime,newDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        dateButton=findViewById(R.id.datepickerbutton);
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment=new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(),"date picker");
            }
        });
        timeButton=findViewById(R.id.timepickerbutton);
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment=new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(),"time picker");
            }
        });
    }


    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar=Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        eYear=year;
        eMonth=month;
        eDay=dayOfMonth;
        newDate= DateFormat.getDateInstance().format(calendar.getTime());
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        calendar1=Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY,hour);
        calendar1.set(Calendar.MINUTE,minute);
        eHour=hour;
        eMinute=minute;
        newTime = calendar1.get(Calendar.HOUR) +":" + calendar1.get(Calendar.MINUTE);
    }



    public void saveItem(View view){
        eCalendar=Calendar.getInstance();
        eCalendar.set(eYear,eMonth,eDay,eHour,eMinute,0);
        long epochTime=eCalendar.getTimeInMillis();


        EditText itemEdit=findViewById(R.id.additem);
        EditText descEdit=findViewById(R.id.adddescription);

        String newItem=itemEdit.getText().toString();
        String newDesc=descEdit.getText().toString();

        if(newDate==null){
            newDate="NO DATE AVAILABLE";
            epochTime=0L;
        }
        if(newTime==null){
            newTime="NO TIME AVAILABLE";
            epochTime=0L;
        }

        Bundle saveBundle= new Bundle();
        saveBundle.putString(ADD_NEW_ITEM,newItem);
        saveBundle.putString(ADD_NEW_DESC,newDesc);
        saveBundle.putString(ADD_NEW_DATE,newDate);
        saveBundle.putString(ADD_NEW_TIME,newTime);
        saveBundle.putLong(ADD_NEW_EPOCH,epochTime);
        saveBundle.putInt(ADD_NEW_YEAR,eYear);
        saveBundle.putInt(ADD_NEW_MONTH,eMonth);
        saveBundle.putInt(ADD_NEW_DAY,eDay);
        saveBundle.putInt(ADD_NEW_HOUR,eHour);
        saveBundle.putInt(ADD_NEW_MINUTE,eMinute);

        Intent data=new Intent();
        data.putExtras(saveBundle);
        setResult(ADD_ITEM_CODE,data);
        finish();
    }

}
