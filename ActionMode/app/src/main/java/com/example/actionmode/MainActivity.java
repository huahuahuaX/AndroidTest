package com.example.actionmode2;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ArrayList<Models> listItems;
    private ListView listView;
    private MultipleAdapter multipleAdapter;
    private String[] names = new String[]{
            "One", "Two", "Three", "Four", "Five"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView)findViewById(R.id.list);
        multipleAdapter = new MultipleAdapter();
        listView.setAdapter(multipleAdapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                multipleAdapter.notifyDataSetChanged();
                listItems.get(position).setChoosed(checked);
                multipleAdapter.notifyDataSetChanged();
                mode.setSubtitle(listView.getCheckedItemCount() + " selectd");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu, menu);
                mode.setTitle("MENU");
                multipleAdapter.notifyDataSetChanged();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()){
                    case R.id.item_delete:
                        Toast toast1 = Toast.makeText(MainActivity.this,
                                "您单击了DELETE按钮", Toast.LENGTH_LONG);
                        toast1.show();;
                        return true;
                    case R.id.item_share:
                        Toast toast = Toast.makeText(MainActivity.this,
                                "您单击了SHARE按钮", Toast.LENGTH_LONG);
                        toast.show();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                for (Models models: listItems){
                    models.setChoosed(false);
                }
            }
        });
    }

    private class MultipleAdapter extends BaseAdapter {


        public MultipleAdapter(){
            listItems = new ArrayList<Models>();
            for (int i = 0; i < names.length; i++)
                listItems.add(new Models(names[i]));
        }

        @Override
        public int getCount() {
            return listItems.size();
        }

        @Override
        public Object getItem(int position) {
            return listItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            class ViewHolder{
                ImageView imageView;
                TextView textView;
            }
            ViewHolder viewHolder;
            if (convertView == null){
                convertView = getLayoutInflater().inflate(
                        R.layout.menu_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView)convertView.findViewById(R.id.header);
                viewHolder.textView = (TextView)convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            }else {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.textView.setText(listItems.get(position).getName());
            viewHolder.imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher_foreground));
            if (listItems.get(position).isChoosed() == true){
                viewHolder.textView.setBackgroundColor(Color.RED);
            }
            else {
                viewHolder.textView.setBackgroundColor(Color.WHITE);
            }
            return convertView;
        }
    }

    private class Models{
        private String name;
        private boolean isChoosed;
        public Models(String name){
            this.name = name;
            isChoosed = false;
        }

        public String getName(){
            return name;
        }

        public boolean isChoosed(){
            return  isChoosed;
        }

        public void setChoosed(boolean isChoosed){
            this.isChoosed = isChoosed;
        }
    }
}
