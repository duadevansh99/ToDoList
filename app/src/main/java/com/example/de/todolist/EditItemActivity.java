package com.example.de.todolist;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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

public class EditItemActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    public static final String EDIT_ITEM="editing item";
    public static final String EDIT_DESC="editing description";
    public static final String EDIT_DATE="editing date";
    public static final String EDIT_TIME="editing time";
    public static final String EDIT_INDEX="editing index";
    public static final String EDIT_ALARM="editing alarm";
    public static final String EDIT_YEAR="editing year";
    public static final String EDIT_MONTH="editing month";
    public static final String EDIT_DAY="editing day";
    public static final String EDIT_HOUR="editing hour";
    public static final String EDIT_MINUTE="editing minute";
    public static final int EDIT_CODE=200;
    public static final int EDIT_ITEM_CODE=8;
    public static final String EDIT_ID="id from edit";
    long id;
    int eYear,eMonth,eDay,eHour,eMinute,index;
    EditText itemEdit,descEdit;
    Calendar calendar,calendar1;
    Button dateEditButton,timeEditButton;
    String newTime,newDate,oldDate,oldTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_item);
        itemEdit=findViewById(R.id.additem);
        descEdit=findViewById(R.id.adddescription);
        dateEditButton=findViewById(R.id.datepickerbutton);
        dateEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment datePickerFragment=new DatePickerFragment();
                datePickerFragment.show(getSupportFragmentManager(),"date picker");
            }
        });
        timeEditButton=findViewById(R.id.timepickerbutton);
        timeEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePickerFragment=new TimePickerFragment();
                timePickerFragment.show(getSupportFragmentManager(),"time picker");
            }
        });

        Intent receivedIntent=getIntent();
        Bundle receivedBundle=receivedIntent.getExtras();
        String oldItem=receivedIntent.getStringExtra(MainActivity.EDIT_ITEM);
        String oldDesc=receivedBundle.getString(MainActivity.EDIT_DESC);
        oldDate=receivedBundle.getString(MainActivity.EDIT_DATE);
        oldTime=receivedBundle.getString(MainActivity.EDIT_TIME);
        id=receivedBundle.getLong(MainActivity.EDIT_ID);
        index=receivedBundle.getInt(MainActivity.EDIT_INDEX);
        eYear=receivedBundle.getInt(MainActivity.EDIT_YEAR);
        eMonth=receivedBundle.getInt(MainActivity.EDIT_MONTH);
        eDay=receivedBundle.getInt(MainActivity.EDIT_DAY);
        eHour=receivedBundle.getInt(MainActivity.EDIT_HOUR);
        eMinute=receivedBundle.getInt(MainActivity.EDIT_MINUTE);

        itemEdit.setText(oldItem);
        descEdit.setText(oldDesc);
    }

    public void saveItem(View view){

        String newItem=itemEdit.getText().toString();
        String newDesc=descEdit.getText().toString();
        Calendar epochCalendar=Calendar.getInstance();
        epochCalendar.set(eYear,eMonth,eDay,eHour,eMinute,0);
        long epoch=epochCalendar.getTimeInMillis();
        if(newDate==null){
            newDate=oldDate;
        }
        if(newTime==null){
            newTime=oldTime;
        }

        Bundle editBundle= new Bundle();
        editBundle.putString(EDIT_ITEM,newItem);
        editBundle.putString(EDIT_DESC,newDesc);
        editBundle.putString(EDIT_DATE,newDate);
        editBundle.putString(EDIT_TIME,newTime);
        editBundle.putLong(EDIT_ALARM,epoch);
        editBundle.putLong(EDIT_ID,id);
        editBundle.putInt(EDIT_INDEX,index);
        editBundle.putInt(EDIT_YEAR,eYear);
        editBundle.putInt(EDIT_MONTH,eMonth);
        editBundle.putInt(EDIT_DAY,eDay);
        editBundle.putInt(EDIT_HOUR,eHour);
        editBundle.putInt(EDIT_MINUTE,eMinute);

        Intent data2=new Intent();
        data2.putExtras(editBundle);
        setResult(EDIT_ITEM_CODE,data2);
        finish();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar= Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DAY_OF_MONTH,dayOfMonth);
        newDate= DateFormat.getDateInstance().format(calendar.getTime());
        eYear=year;
        eMonth=month;
        eDay=dayOfMonth;
    }

    @Override
    public void onTimeSet(TimePicker view, int hour, int minute) {
        calendar1=Calendar.getInstance();
        calendar1.set(Calendar.HOUR_OF_DAY,hour);
        calendar1.set(Calendar.MINUTE,minute);
        newTime = calendar1.get(Calendar.HOUR) +":" + calendar1.get(Calendar.MINUTE);
        eHour=hour;
        eMinute=minute;
    }
}
