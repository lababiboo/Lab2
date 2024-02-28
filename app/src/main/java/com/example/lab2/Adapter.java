package com.example.lab2;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.nio.file.Path;
import java.util.ArrayList;

public class Adapter extends BaseAdapter {
    private ArrayList<Contact> data;
    private LayoutInflater inflater;
    private Activity context;
    public void setData(ArrayList<Contact> data) {this.data = data;}
    public Adapter(ArrayList<Contact> data, Activity activity){
        this.data = data;
        this.context = activity;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if(v==null)
            v = inflater.inflate(R.layout.activity_contact,null);
        TextView txtName = v.findViewById(R.id.edName);
        txtName.setText(data.get(position).getName());
        TextView txtPhone = v.findViewById(R.id.edPhone);
        txtPhone.setText(data.get(position).getPhone());
        CheckBox cb = v.findViewById(R.id.checkBox);
        cb.setChecked(data.get(position).getStatus());
        ImageView img = v.findViewById(R.id.imageView3);

        String path = data.get(position).getImage();
        //cach 1
        img.setImageURI(Uri.parse(path));
        //cach 2
//        if(path!=null && !path.isEmpty()) {
//            Glide.with(context)
//                    .load(path)
//                    .into(img);
//        }

        Contact c = data.get(position);
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                c.setStatus(isChecked);
            }
        });
        return v;
    }
}

