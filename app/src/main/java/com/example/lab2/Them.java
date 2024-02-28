package com.example.lab2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.Serializable;

public class Them extends AppCompatActivity {

    private EditText edId;
    private EditText edName;
    private EditText edPhone;
    private ImageView imageView;
    private Button btnAdd;
    private Button btnCancel;
    String img_path;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them);
        edId = findViewById(R.id.edId);
        edPhone = findViewById(R.id.edPhone);
        edName = findViewById(R.id.edName);
        imageView = findViewById(R.id.imageView);
        btnAdd = findViewById(R.id.btnAdd);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 250);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = Integer.parseInt(edId.getText().toString());
                String name = edName.getText().toString();
                String phone = edPhone.getText().toString();

                if(phone.length() != 10 || phone.matches("^[0-9]+$") == false)
                    Toast.makeText(Them.this, "Hãy nhập số điện thoại hợp lệ", Toast.LENGTH_LONG).show();
                else
                {
                    Intent intent = new Intent();
                    intent.putExtra("Id",id);
                    intent.putExtra("Name",name);
                    intent.putExtra("Phone",phone);
                    intent.putExtra("Image",img_path);
                    setResult(150, intent);
                    finish();
                }

                }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==250 && resultCode==RESULT_OK&&data!=null){
            Uri img = data.getData();
            String pic_path = getRealPathFromURI(img);
            img_path = img.toString();
            if(pic_path!=null){
                imageView.setImageURI(img);
            }

        }
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;
        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String picturePath = cursor.getString(columnIndex);
        cursor.close();
        return picturePath;
    }
}