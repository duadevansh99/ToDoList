package com.example.de.todolist;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemAdapter extends ArrayAdapter {

    ArrayList<Item> objects;
    LayoutInflater inflater;
    ItemDeleteButtonClickListener clickDeleteListener;
    ItemEditButtonClickListener clickEditListener;

    public ItemAdapter(Context context, ArrayList<Item> objects, ItemDeleteButtonClickListener deleteButtonClickListener, ItemEditButtonClickListener editButtonClickListener) {
        super(context,0, objects);
        inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.objects=objects;
        clickDeleteListener=deleteButtonClickListener;
        clickEditListener=editButtonClickListener;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View output=convertView;
        if(output==null){
            output=inflater.inflate(R.layout.todo_row_layout,parent,false);
            TextView itemTextView=output.findViewById(R.id.item);
            TextView descriptionTextView=output.findViewById(R.id.description);
            TextView dateTextView=output.findViewById(R.id.date);
            TextView timeTextView=output.findViewById(R.id.time);
            ItemViewHolder viewHolder=new ItemViewHolder();
            viewHolder.title=itemTextView;
            viewHolder.desc=descriptionTextView;
            viewHolder.date=dateTextView;
            viewHolder.time=timeTextView;
            output.setTag(viewHolder);
        }
        ItemViewHolder viewHolder=(ItemViewHolder)output.getTag();
        Button deleteButton=output.findViewById(R.id.rowdeletebutton);
        Button editButton=output.findViewById(R.id.roweditbutton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteListener.rowDeleteButtonClicked(view,position);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickEditListener.rowEditButtonClicked(view,position);
            }
        });
        Item item=objects.get(position);
        viewHolder.title.setText(item.item);
        viewHolder.desc.setText(item.description);
        viewHolder.date.setText(item.getDate());
        viewHolder.time.setText(item.getTime());
        return output;
    }
}
