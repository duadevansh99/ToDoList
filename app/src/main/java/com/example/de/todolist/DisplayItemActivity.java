package com.example.de.todolist;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayItemActivity extends AppCompatActivity {

    TextView displayItem, displayDesc, displayDate, displayTime;
    Button saveEditButton;
    String item, desc, date, time;
    String finalItem, finalDesc, finalDate, finalTime;
    int index;
    long id, epoch;
    public static final String SEND_ITEM = "sending item";
    public static final String SEND_DESC = "sending description";
    public static final String SEND_INDEX = "sending index";
    public static final String SEND_DATE = "sending date";
    public static final String SEND_TIME = "sending time";
    public static final int SEND_REQUEST = 2;
    public static final int SEND_EDIT = 3;
    public static final String SEND_ITEM_MAIN = "sending item to main";
    public static final String SEND_DESC_MAIN = "sending description to main";
    public static final String SEND_INDEX_MAIN = "sending index to main";
    public static final String SEND_DATE_MAIN = "sending date to main";
    public static final String SEND_TIME_MAIN = "sending time to main";
    public static final String SEND_EPOCH_MAIN = "sending epoch to main";
    public static final String ITEM_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_item);
        Intent receiveData = getIntent();
        Bundle bundle;
        bundle = receiveData.getExtras();

        item = bundle.getString(MainActivity.DISPLAY_ITEM);
        desc = bundle.getString(MainActivity.DISPLAY_DESC);
        date = bundle.getString(MainActivity.DISPLAY_DATE);
        time = bundle.getString(MainActivity.DISPLAY_TIME);
        index = bundle.getInt(MainActivity.INDEX_ITEM);
        id = bundle.getLong("ID");

        displayItem = findViewById(R.id.displayitem);
        displayDesc = findViewById(R.id.displaydesc);
        displayDate = findViewById(R.id.datetextview);
        displayTime = findViewById(R.id.timetextview);
        displayItem.setText(item);
        displayDesc.setText(desc);
        displayDate.setText(date);
        displayTime.setText(time);
    }
}

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.display_main_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item1) {
//        int id=item1.getItemId();
//        if(id==R.id.editItemAction){
//            Bundle send=new Bundle();
//            send.putString(SEND_ITEM,item);
//            send.putString(SEND_DESC,desc);
//            send.putInt(SEND_INDEX,index);
//            send.putString(SEND_DATE,date);
//            send.putString(SEND_TIME,time);
//            send.putLong("ID",id);
//
//            Intent sendIntent=new Intent(this,EditItemActivity.class);
//            sendIntent.putExtras(send);
//            startActivityForResult(sendIntent,SEND_REQUEST);
//            saveEditButton.setEnabled(true);
//        }
//        return super.onOptionsItemSelected(item1);
//    }


//    public void saveEditInMain(View view){
//
//        Bundle saveEditBundle=new Bundle();
//        saveEditBundle.putString(SEND_ITEM_MAIN,finalItem);
//        saveEditBundle.putString(SEND_DESC_MAIN,finalDesc);
//        saveEditBundle.putInt(SEND_INDEX_MAIN,index);
//        saveEditBundle.putString(SEND_DATE_MAIN,finalDate);
//        saveEditBundle.putString(SEND_TIME_MAIN,finalTime);
//        saveEditBundle.putLong(SEND_EPOCH_MAIN,epoch);
//        saveEditBundle.putLong("ID",id);
//
//        Intent saveEdit=new Intent(DisplayItemActivity.this,MainActivity.class);
//        saveEdit.putExtras(saveEditBundle);
//        setResult(SEND_EDIT,saveEdit);
//        finish();
//    }
//}
