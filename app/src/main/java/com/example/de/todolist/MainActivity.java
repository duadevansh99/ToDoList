package com.example.de.todolist;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

    ListView listView;
    ArrayList<Item> items=new ArrayList<>();
    AlarmManager alarmManager;
    PendingIntent pendingIntent;
    ItemAdapter adapter;
    public static final int ADD_REQUESTCODE=1;
    public static final int DISPLAY_REQUESTCODE=5;
    public static final int EDIT_REQUESTCODE=7;
    public static final String INDEX_ITEM="sending index to display";
    public static final String DISPLAY_ITEM="displaying item";
    public static final String DISPLAY_DESC="displaying description";
    public static final String DISPLAY_DATE="displaying date";
    public static final String DISPLAY_TIME="displaying time";
    public static final String CHANNEL_1="channel one";
    public static final String CHANNEL_2="channel two";
    public static final String ALARM_ITEM="sending item for alarm";
    public static final String ALARM_DESC="sending description for alarm";
    public static final String EDIT_ITEM="editing item from button";
    public static final String EDIT_DESC="editing desc from button";
    public static final String EDIT_DATE="editing date from button";
    public static final String EDIT_TIME="editing time from button";
    public static final String EDIT_INDEX="index from button";
    public static final String EDIT_ID="ID from button";
    public static final String EDIT_YEAR="year from button";
    public static final String EDIT_MONTH="month from button";
    public static final String EDIT_DAY="day from button";
    public static final String EDIT_HOUR="hour from button";
    public static final String EDIT_MINUTE="minute from button";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,AddItemActivity.class);
                intent.putExtra("itemtitle","abc");
                startActivityForResult(intent,ADD_REQUESTCODE);
            }
        });

        listView=findViewById(R.id.listview);
        adapter= new ItemAdapter(this, items, new ItemDeleteButtonClickListener() {
            @Override
            public void rowDeleteButtonClicked(View view, int position) {

                final Item item=items.get(position);
                final int pos=position;
                AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("CONFIRM DELETE");
                builder.setCancelable(false);
                builder.setMessage("Are you sure you want to delete this item?");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        ItemOpenHelper openHelper=new ItemOpenHelper(getApplicationContext());
                        SQLiteDatabase database=openHelper.getWritableDatabase();

                        long id=item.getId();
                        String[] selectionArgs={id + ""};
                        database.delete(Contract.TABLE_NAME,Contract.ID + " = ?",selectionArgs);

                        items.remove(pos);
                        Toast.makeText(MainActivity.this,"Item Deleted!",Toast.LENGTH_SHORT).show();
                        adapter.notifyDataSetChanged();

                        NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                            NotificationChannel channel2=new NotificationChannel(CHANNEL_2,"ADD TODO CHANNEL",NotificationManager.IMPORTANCE_LOW);
                            manager.createNotificationChannel(channel2);
                        }

                        NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_2);
                        builder.setContentTitle("TODO DELETED");
                        builder.setContentText("TODO item deleted!");
                        builder.setSmallIcon(R.drawable.ic_todo);
                        builder.setAutoCancel(true);

                        Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                        PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),3,intent1,0);
                        builder.setContentIntent(pendingIntent);

                        Notification notification=builder.build();
                        manager.notify(3,notification);

                        Intent alarmIntent=new Intent(MainActivity.this,MyReceiver.class);
                        pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),(int)id,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                        alarmManager.cancel(pendingIntent);

                    }
                });
                builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //cancel
                    }
                });
                AlertDialog dialog=builder.create();
                dialog.show();

            }
        }, new ItemEditButtonClickListener() {
            @Override
            public void rowEditButtonClicked(View view, int position) {

                Item item=items.get(position);
                Bundle editBundle=new Bundle();
                editBundle.putString(EDIT_ITEM,item.getItem());
                editBundle.putString(EDIT_DESC,item.getDescription());
                editBundle.putString(EDIT_DATE,item.getDate());
                editBundle.putString(EDIT_TIME,item.getTime());
                editBundle.putLong(EDIT_ID,item.getId());
                editBundle.putInt(EDIT_INDEX,position);
                editBundle.putInt(EDIT_YEAR,item.getEpYear());
                editBundle.putInt(EDIT_MONTH,item.getEpMonth());
                editBundle.putInt(EDIT_DAY,item.getEpDay());
                editBundle.putInt(EDIT_HOUR,item.getEpHour());
                editBundle.putInt(EDIT_MINUTE,item.getEpMinute());

                Intent intent=new Intent(MainActivity.this,EditItemActivity.class);
                intent.putExtras(editBundle);
                startActivityForResult(intent,EDIT_REQUESTCODE);
            }
        });
        listView.setOnItemClickListener(this);
        listView.setOnItemLongClickListener(this);

        ItemOpenHelper openHelper= new ItemOpenHelper(getApplicationContext());
        SQLiteDatabase database=openHelper.getReadableDatabase();
        Cursor cursor=database.query(Contract.TABLE_NAME,null,null,null,null,null,null);
        while(cursor.moveToNext()){
            String item=cursor.getString(cursor.getColumnIndex(Contract.COLUMN_ITEM));
            String desc=cursor.getString(cursor.getColumnIndex(Contract.COLUMN_DESC));
            int id = cursor.getInt(cursor.getColumnIndex(Contract.ID));
            String date= cursor.getString(cursor.getColumnIndex(Contract.COLUMN_DATE));
            String time= cursor.getString(cursor.getColumnIndex(Contract.COLUMN_TIME));
            int year=cursor.getInt(cursor.getColumnIndex(Contract.YEAR));
            int month=cursor.getInt(cursor.getColumnIndex(Contract.MONTH));
            int day=cursor.getInt(cursor.getColumnIndex(Contract.DAY));
            int hour=cursor.getInt(cursor.getColumnIndex(Contract.HOUR));
            int minute=cursor.getInt(cursor.getColumnIndex(Contract.MINUTE));
            Item itemdb= new Item(item,desc,date,time,year,month,day,hour,minute);
            itemdb.setId(id);
            Log.i("Item Title",item);
            items.add(itemdb);
        }
        Log.i("Item Title",items.size()+"");
        cursor.close();
        listView.setAdapter(adapter);

        alarmManager=(AlarmManager)getSystemService(ALARM_SERVICE);
        adapter.notifyDataSetChanged();
        }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        Item item=items.get(i);
        Bundle displayBundle=new Bundle();
        displayBundle.putString(DISPLAY_ITEM,item.getItem());
        displayBundle.putString(DISPLAY_DESC,item.getDescription());
        displayBundle.putString(DISPLAY_DATE,item.getDate());
        displayBundle.putString(DISPLAY_TIME,item.getTime());
        displayBundle.putInt(INDEX_ITEM,i);
        displayBundle.putLong("ID",item.getId());

        Intent displayData=new Intent(this,DisplayItemActivity.class);
        displayData.putExtras(displayBundle);
        startActivityForResult(displayData,DISPLAY_REQUESTCODE);
    }




//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main_menu,menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id=item.getItemId();
//        if(id==R.id.addItemAction){
//
//            Intent intent=new Intent(this,AddItemActivity.class);
//            intent.putExtra("itemtitle","abc");
//            startActivityForResult(intent,ADD_REQUESTCODE);
//
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==ADD_REQUESTCODE){
            if(resultCode==AddItemActivity.ADD_ITEM_CODE) {
                String newItem = data.getStringExtra(AddItemActivity.ADD_NEW_ITEM);
                String newDesc = data.getStringExtra(AddItemActivity.ADD_NEW_DESC);
                String newDate= data.getStringExtra(AddItemActivity.ADD_NEW_DATE);
                String newTime= data.getStringExtra(AddItemActivity.ADD_NEW_TIME);
                Long epoch=data.getLongExtra(AddItemActivity.ADD_NEW_EPOCH,0L);
                int newYear=data.getIntExtra(AddItemActivity.ADD_NEW_YEAR,0);
                int newMonth=data.getIntExtra(AddItemActivity.ADD_NEW_MONTH,0);
                int newDay=data.getIntExtra(AddItemActivity.ADD_NEW_DAY,0);
                int newHour=data.getIntExtra(AddItemActivity.ADD_NEW_HOUR,0);
                int newMinute=data.getIntExtra(AddItemActivity.ADD_NEW_MINUTE,0);
                Item item = new Item(newItem, newDesc, newDate, newTime,newYear,newMonth,newDay,newHour,newMinute);

                ItemOpenHelper openHelper=new ItemOpenHelper(getApplicationContext());
                SQLiteDatabase database=openHelper.getWritableDatabase();

                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.COLUMN_ITEM,item.getItem());
                contentValues.put(Contract.COLUMN_DESC,item.getDescription());
                contentValues.put(Contract.COLUMN_DATE,item.getDate());
                contentValues.put(Contract.COLUMN_TIME,item.getTime());
                contentValues.put(Contract.YEAR,item.getEpYear());
                contentValues.put(Contract.MONTH,item.getEpMonth());
                contentValues.put(Contract.DAY,item.getEpDay());
                contentValues.put(Contract.HOUR,item.getEpHour());
                contentValues.put(Contract.MINUTE,item.getEpMinute());


                long id=database.insert(Contract.TABLE_NAME,null,contentValues);
                Log.i("MainActivity","ID added one "+id);
                item.setId(id);
                items.add(item);
                adapter.notifyDataSetChanged();

                NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel1=new NotificationChannel(CHANNEL_1,"HIGH TODO CHANNEL",NotificationManager.IMPORTANCE_HIGH);
                    manager.createNotificationChannel(channel1);
                }

                NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_1);
                builder.setContentTitle("TODO ADDED");
                builder.setContentText("New TODO item added!");
                builder.setSmallIcon(R.drawable.ic_todo);
                builder.setAutoCancel(true);

                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),1,intent1,0);
                builder.setContentIntent(pendingIntent);

                Notification notification=builder.build();
                manager.notify(1,notification);

                Bundle alarmBundle=new Bundle();
                alarmBundle.putString(ALARM_ITEM,newItem);
                alarmBundle.putString(ALARM_DESC,newDesc);

                Intent alarmIntent=new Intent(this,MyReceiver.class);
                alarmIntent.putExtras(alarmBundle);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),(int)id,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,epoch,pendingIntent);
            }
        }
        else if(requestCode==EDIT_REQUESTCODE){
            if(resultCode==EditItemActivity.EDIT_ITEM_CODE){

                Bundle editBundle;
                editBundle = data.getExtras();
                String editItem = editBundle.getString(EditItemActivity.EDIT_ITEM);
                String editDesc = editBundle.getString(EditItemActivity.EDIT_DESC);
                String editDate= editBundle.getString(EditItemActivity.EDIT_DATE);
                String editTime= editBundle.getString(EditItemActivity.EDIT_TIME);
                Long editEpoch=editBundle.getLong(EditItemActivity.EDIT_ALARM);
                long id=editBundle.getLong(EditItemActivity.EDIT_ID);
                int index=editBundle.getInt(EditItemActivity.EDIT_INDEX);
                int editYear=editBundle.getInt(EditItemActivity.EDIT_YEAR);
                int editMonth=editBundle.getInt(EditItemActivity.EDIT_MONTH);
                int editDay=editBundle.getInt(EditItemActivity.EDIT_DAY);
                int editHour=editBundle.getInt(EditItemActivity.EDIT_HOUR);
                int editMinute=editBundle.getInt(EditItemActivity.EDIT_MINUTE);
                Item item = new Item(editItem, editDesc,editDate,editTime,editYear,editMonth,editDay,editHour,editMinute);
                items.set(index,item);
                adapter.notifyDataSetChanged();
                item.setId(id);

                ItemOpenHelper openHelper=new ItemOpenHelper(getApplicationContext());
                SQLiteDatabase database=openHelper.getWritableDatabase();

                ContentValues contentValues=new ContentValues();
                contentValues.put(Contract.COLUMN_ITEM,item.getItem());
                contentValues.put(Contract.COLUMN_DESC,item.getDescription());
                contentValues.put(Contract.COLUMN_DATE,item.getDate());
                contentValues.put(Contract.COLUMN_TIME,item.getTime());
                contentValues.put(Contract.YEAR,item.getEpYear());
                contentValues.put(Contract.MONTH,item.getEpMonth());
                contentValues.put(Contract.DAY,item.getEpDay());
                contentValues.put(Contract.HOUR,item.getEpHour());
                contentValues.put(Contract.MINUTE,item.getEpMinute());

                id = item.getId();

                String[] selectionArgs={id + ""};
                int checkValue = database.update(Contract.TABLE_NAME,contentValues,Contract.ID + " = ?",selectionArgs);

                NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                    NotificationChannel channel2=new NotificationChannel(CHANNEL_2,"LOW TODO CHANNEL",NotificationManager.IMPORTANCE_LOW);
                    manager.createNotificationChannel(channel2);
                }

                NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_2);
                builder.setContentTitle("TODO EDITED");
                builder.setContentText("TODO item edited!");
                builder.setSmallIcon(R.drawable.ic_todo);
                builder.setAutoCancel(true);

                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
                PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),2,intent1,0);
                builder.setContentIntent(pendingIntent);

                Notification notification=builder.build();
                manager.notify(2,notification);

                Bundle alarmBundle=new Bundle();
                alarmBundle.putString(ALARM_ITEM,editItem);
                alarmBundle.putString(ALARM_DESC,editDesc);

                Intent alarmIntent=new Intent(this,MyReceiver.class);
                alarmIntent.putExtras(alarmBundle);
                pendingIntent=PendingIntent.getBroadcast(getApplicationContext(),(int)id,alarmIntent,PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.set(AlarmManager.RTC_WAKEUP,editEpoch,pendingIntent);

            }
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {


        Toast.makeText(this,"long clicked",Toast.LENGTH_SHORT).show();
//        final Item item=items.get(i);
//        final int pos=i;
//        AlertDialog.Builder builder=new AlertDialog.Builder(this);
//        builder.setTitle("CONFIRM DELETE");
//        builder.setCancelable(false);
//        builder.setMessage("Are you sure you want to delete this item?");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//
//                ItemOpenHelper openHelper=new ItemOpenHelper(getApplicationContext());
//                SQLiteDatabase database=openHelper.getWritableDatabase();
//
//                long id=item.getId();
//                String[] selectionArgs={id + ""};
//                database.delete(Contract.TABLE_NAME,Contract.ID + " = ?",selectionArgs);
//
//                items.remove(pos);
//                Toast.makeText(MainActivity.this,"Item Deleted!",Toast.LENGTH_SHORT).show();
//                adapter.notifyDataSetChanged();
//
//                NotificationManager manager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
//
//                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
//                    NotificationChannel channel2=new NotificationChannel(CHANNEL_2,"ADD TODO CHANNEL",NotificationManager.IMPORTANCE_LOW);
//                    manager.createNotificationChannel(channel2);
//                }
//
//                NotificationCompat.Builder builder=new NotificationCompat.Builder(getApplicationContext(),CHANNEL_2);
//                builder.setContentTitle("TODO DELETED");
//                builder.setContentText("TODO item deleted!");
//                builder.setSmallIcon(R.drawable.ic_todo);
//                builder.setAutoCancel(true);
//
//                Intent intent1=new Intent(getApplicationContext(),MainActivity.class);
//                PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(),3,intent1,0);
//                builder.setContentIntent(pendingIntent);
//
//                Notification notification=builder.build();
//                manager.notify(3,notification);
//
//            }
//        });
//        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                //cancel
//            }
//        });
//        AlertDialog dialog=builder.create();
//        dialog.show();
      return true;
    }

}
