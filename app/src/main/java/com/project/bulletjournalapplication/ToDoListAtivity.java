package com.project.bulletjournalapplication;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class ToDoListAtivity extends AppCompatActivity {
    Toolbar toolbar;
    BuJoSQLOpenHelper helper;
    SQLiteDatabase database;
    private SimpleDateFormat dateFormat;
    ListView todoList;
    SimpleAdapter Adapter;
    private Cursor res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list_ativity);
        toolbar = findViewById(R.id.toolbarToDO);
        setSupportActionBar(toolbar);

        helper = new BuJoSQLOpenHelper(this);
        database = helper.getWritableDatabase();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        todoList = findViewById(R.id.ToDOListView);

        ListViewing(); // list view for to do list
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return addListForToDo(); // to add a list
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Select an option..");
        menu.add(0, 1, 0, "Edit");
        menu.add(0, 2, 0, "Delete");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Edit") {
            editToDoList(item); // to edit a list
        } else if (item.getTitle() == "Delete") {
            deleteToDOList(item);
        } else {
            return false;
        }
        return true;

    }

    private void ListViewing() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        res = database.rawQuery("SELECT * FROM to_do", null);

        while (res.moveToNext()) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("content", res.getString(1));
            hm.put("date", res.getString(2));
            arrayList.add(hm);
        }
        String[] from = {"content", "date"};
        int[] to = {R.id.contentTV, R.id.date_tv};
        Adapter = new SimpleAdapter(this, arrayList, R.layout.view_date_content, from, to);
        todoList.setAdapter(Adapter);
        registerForContextMenu(todoList);
    }

    private boolean addListForToDo() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_date_content);
        final EditText content = dialog.findViewById(R.id.content_et);
        final EditText date = dialog.findViewById(R.id.date_et);
        date.setText(dateFormat.format(new Date()));
        Button dialogSave = dialog.findViewById(R.id.save);
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("INSERT INTO to_do (CONTENT,DATE) VALUES ('" + content.getText().toString() +
                        "','" + date.getText().toString() + "')");
                Toast.makeText(getApplicationContext(), "List Added", Toast.LENGTH_LONG).show();
                Adapter.notifyDataSetChanged();
                dialog.dismiss();
                Intent intent = new Intent(ToDoListAtivity.this, ToDoListAtivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
        return true;
    }


    private void editToDoList(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        res.moveToPosition(index);
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_date_content);
        final EditText content = dialog.findViewById(R.id.content_et);
        final EditText date = dialog.findViewById(R.id.date_et);
        Button dialogSave = dialog.findViewById(R.id.save);
        content.setText(res.getString(1));
        date.setText(res.getString(2));
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("UPDATE to_do SET CONTENT ='" + content.getText().toString()+"' , DATE= '" +
                        date.getText().toString()+"' WHERE to_do_ID = '"+ res.getString(0)+"'");
                Toast.makeText(getApplicationContext(), "List Updated", Toast.LENGTH_LONG).show();
                Adapter.notifyDataSetChanged();
                dialog.dismiss();
                Intent intent = new Intent(ToDoListAtivity.this, ToDoListAtivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private void deleteToDOList(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        res.moveToPosition(index);
        database.execSQL("DELETE FROM to_do WHERE to_do_ID='" +res.getString(0)+"'" );
        Toast.makeText(getApplicationContext(), "List Deleted", Toast.LENGTH_LONG).show();
        Adapter.notifyDataSetChanged();
        Intent intent = new Intent(ToDoListAtivity.this, ToDoListAtivity.class);
        startActivity(intent);
        finish();

    }
}
