package com.project.bulletjournalapplication;

import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CollectionActivity extends AppCompatActivity {
    Toolbar toolbar;
    ListView collectionList;
    BuJoSQLOpenHelper helper;
    SQLiteDatabase database;
    SimpleAdapter Adapter;
    private Cursor res;
    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        toolbar=findViewById(R.id.toolbarCollection);
        setSupportActionBar(toolbar);

        collectionList=findViewById(R.id.listViewCollection);
        registerForContextMenu(collectionList);

        helper=new BuJoSQLOpenHelper(this);
        database=helper.getWritableDatabase();
        viewCollection();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return addCollection(); // to add a page
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
            editCollection(item); // to edit a page
        } else if (item.getTitle() == "Delete") {
            deleteCollection(item); // to delete page
        } else {
            return false;
        }
        return true;

    }

    private void viewCollection() {
        ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        res = database.rawQuery("SELECT * FROM collection", null);

        while (res.moveToNext()) {
            HashMap<String, String> hm = new HashMap<>();
            hm.put("title", res.getString(1));
            hm.put("content", res.getString(2));
            arrayList.add(hm);
        }
        String[] from = {"title", "content"};
        int[] to = {R.id.date_tv, R.id.contentTV};
        Adapter = new SimpleAdapter(this, arrayList, R.layout.view_date_content, from, to);
        collectionList.setAdapter(Adapter);
        registerForContextMenu(collectionList);
    }

    private boolean addCollection() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_title_content);
        final EditText content = dialog.findViewById(R.id.collectionContent_ed);
        final EditText title = dialog.findViewById(R.id.collectionTitle_ed);
        Button dialogSave = dialog.findViewById(R.id.collectionSave);
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("INSERT INTO collection (TITLE,CONTENT) VALUES ('" + title.getText().toString() +
                        "','" + content.getText().toString() + "')");
                Toast.makeText(getApplicationContext(), "Collection Added", Toast.LENGTH_LONG).show();
                Adapter.notifyDataSetChanged();
                dialog.dismiss();
                Intent intent = new Intent(CollectionActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
        return true;
    }

    private void editCollection(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        res.moveToPosition(index);
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.add_title_content);
        final EditText content = dialog.findViewById(R.id.collectionContent_ed);
        final EditText title = dialog.findViewById(R.id.collectionTitle_ed);
        Button dialogSave = dialog.findViewById(R.id.collectionSave);
        title.setText(res.getString(1));
        content.setText(res.getString(2));
        dialogSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.execSQL("UPDATE collection SET TITLE ='" + title.getText().toString()+"' , CONTENT= '" +
                        content.getText().toString()+"' WHERE collection_ID = '"+ res.getString(0)+"'");
                Toast.makeText(getApplicationContext(), "Collection Updated", Toast.LENGTH_LONG).show();
                Adapter.notifyDataSetChanged();
                dialog.dismiss();
                Intent intent = new Intent(CollectionActivity.this, CollectionActivity.class);
                startActivity(intent);
                finish();
            }
        });
        dialog.show();
    }

    private void deleteCollection(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int index = info.position;
        res.moveToPosition(index);
        database.execSQL("DELETE FROM collection WHERE collection_ID='" +res.getString(0)+"'" );
        Toast.makeText(getApplicationContext(), "Collection Deleted", Toast.LENGTH_LONG).show();
        Adapter.notifyDataSetChanged();
        Intent intent = new Intent(CollectionActivity.this, CollectionActivity.class);
        startActivity(intent);
        finish();
    }



}
