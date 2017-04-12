package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class EditBusStop extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private ListView listView;
    private String tag = "12AprilV2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_bus_stop);

        //Initial View
        initialView();

        //Image Controller
        imageView.setOnClickListener(EditBusStop.this);

        //Create ListView
        createListView();

    }   // Main Method

    @Override
    protected void onResume() {
        super.onResume();
        createListView();
    }

    private void createListView() {

        try {

            final SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM busTABLE", null);
            cursor.moveToFirst();

            final String[] nameBusStopStrings = new String[cursor.getCount()];
            String[] statusStrings = new String[cursor.getCount()];
            final String[] idStrings = new String[cursor.getCount()];

            for (int i=0;i<cursor.getCount();i++) {
                idStrings[i] = cursor.getString(0);
                nameBusStopStrings[i] = cursor.getString(1);
                statusStrings[i] = cursor.getString(5);
                cursor.moveToNext();
            }   // for

            //Create ListView
            MyAdapter myAdapter = new MyAdapter(EditBusStop.this,
                    nameBusStopStrings, statusStrings);
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(tag, "id ==> " + idStrings[i]);
                    Intent intent = new Intent(EditBusStop.this, AddBusStop.class);
                    intent.putExtra("id", idStrings[i]);
                    intent.putExtra("Edit", true);
                    intent.putExtra("Name", nameBusStopStrings[i]);
                    startActivity(intent);
                }
            });

        } catch (Exception e) {
            Log.d(tag, "e createListView ==> " + e.toString());
        }

    }   // createListView

    private void initialView() {
        imageView = (ImageView) findViewById(R.id.imvBack);
        listView = (ListView) findViewById(R.id.livBusStop);
    }

    @Override
    public void onClick(View view) {
        if (view == imageView) {
            finish();
        }
    }
}   // Main Class
