package com.example.simpleadapter;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends Activity {
    private String[] names = new String[]{
            "Lion","Tiger","Monkey","Dog","Cat","Elephant"};
    private int[] imageIds = new int[]{
            R.drawable.lion, R.drawable.tiger, R.drawable.monkey,
            R.drawable.dog, R.drawable.cat, R.drawable.elephant};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //创建list集合
        List<Map<String, Object>> listItems =
                new ArrayList<Map<String, Object>>();
        for (int i = 0; i < names.length; i++){
            Map<String, Object> listItem =
                    new HashMap<String, Object>();
            listItem.put("name", names[i]);
            listItem.put("image", imageIds[i]);
            listItems.add(listItem);
        }
        //创建一个SimpleAdapter
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, listItems,
                R.layout.simple_item, new String[] {"name", "image"},
                new int[] {R.id.name , R.id.header});
        final ListView listView = findViewById(R.id.list);
        //为ListView设置Adapter
        listView.setAdapter(simpleAdapter);

        //为listView单单击事件绑定事件监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast toast = Toast.makeText(MainActivity.this,
                        names[position], Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }
}
