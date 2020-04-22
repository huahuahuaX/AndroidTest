package com.example.optionmenu;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edit = (EditText) findViewById(R.id.editText);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.big:
                edit.setTextSize(20*2);
                return true;
            case R.id.middle:
                edit.setTextSize(16*2);
                return true;
            case R.id.small:
                edit.setTextSize(10*2);
                return true;
            case R.id.common:
                Toast toast = Toast.makeText(MainActivity.this,
                        "您单击了普通菜单项", Toast.LENGTH_LONG);
                toast.show();
                return true;
            case R.id.red:
                edit.setTextColor(Color.RED);
                return true;
            case R.id.black:
                edit.setTextColor(Color.BLACK);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
