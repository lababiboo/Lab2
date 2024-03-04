package com.example.lab2;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private int SelectedItemId;
    private EditText edName;
    private EditText edPhone;
    private CheckBox checkBox;
    private Button btnThem;
    private Button btnXoa;
    private ListView lstContact;
    private ArrayList<Contact> ContactList;
    protected Adapter ListAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Cap quyen truy cap doc thu vien
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập vào bộ nhớ
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},
                    300);
        }
        edName = findViewById(R.id.txtName);
        btnThem = findViewById(R.id.btnThem);
        btnXoa = findViewById(R.id.btnXoa);
        lstContact = findViewById(R.id.lstContact);

        ContactList = new ArrayList<Contact>();
        ListAdapter = new Adapter(ContactList,this);
        lstContact.setAdapter(ListAdapter);

        registerForContextMenu(lstContact);
        ArrayList<Integer> list = new ArrayList<Integer>();;
        lstContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                edName.setText(ListAdapter.getItem(position).toString());
            }
        });
        btnXoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b = new AlertDialog.Builder(MainActivity.this);
                b.setTitle("Xác nhận");
                b.setMessage("Bạn có chắc chắn muốn xoá không?");
                b.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Iterator<Contact> iterator = ContactList.iterator();
                        while (iterator.hasNext()) {
                            Contact x = iterator.next();
                            if (x.getStatus()) {
                                iterator.remove();
                            }
                        }
                        ListAdapter = new Adapter(ContactList,MainActivity.this);
                        lstContact.setAdapter(ListAdapter);
                    }
                });
                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog al = b.create();
                al.show();
            }
        });
        btnThem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Them.class);
                startActivityForResult(intent,100);
            }
        });
        lstContact.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                SelectedItemId = position;
                return false;
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 100 && resultCode ==150){
            int id = data.getIntExtra("Id",0);
            String name = data.getStringExtra("Name");
            String phone = data.getStringExtra("Phone");
            String image = data.getStringExtra("Image");
            Contact newcontact = new Contact(id, name,phone, false,image);
            //truong hop them
            ContactList.add(newcontact);
            ListAdapter.notifyDataSetChanged();
        }
        else if(requestCode == 200 && resultCode == 150){
            int id = data.getIntExtra("Id",0);
            String name = data.getStringExtra("Name");
            String phone = data.getStringExtra("Phone");
            String image = data.getStringExtra("Image");
            Contact newcontact = new Contact(id, name,phone, false,image);
            //truong hop sua
            ContactList.set(SelectedItemId, newcontact);
            ListAdapter.notifyDataSetChanged();
        }



    }
    public boolean validateId(ArrayList<Contact> c, int id){
        boolean res = true;
        for(Contact x : c){
            if(x.getId() == id){
                res = false;

            }
        }
        return res;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater mmnuInflater = new MenuInflater(this);
        mmnuInflater.inflate(R.menu.contextmenu,menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        Contact c = ContactList.get(SelectedItemId);
        if(item.getItemId()==R.id.mnuEdit){
            Intent i = new Intent(MainActivity.this,Them.class);
            Bundle b = new Bundle();
            b.putInt("Id", c.getId());
            b.putString("Name", c.getName());
            b.putString("Phone", c.getPhone());
            b.putString("Image", c.getImage());
            i.putExtras(b);
            startActivityForResult(i,200);
        }
        return super.onContextItemSelected(item);
    }
}