package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

public class DeleteBusStop extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_bus_stop);

        //Initial View
        initialView();

        //Image Controller
        imageController();

        //Create ListView
        createListView();

    }   // Main Method

    private void createListView() {

        final String tag = "12AprilV1";
        try {

            final SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM busTABLE", null);
            cursor.moveToFirst();

            String[] nameBusStopStrings = new String[cursor.getCount()];
            String[] statusStrings = new String[cursor.getCount()];
            final String[] idStrings = new String[cursor.getCount()];

            for (int i=0;i<cursor.getCount();i++) {
                idStrings[i] = cursor.getString(0);
                nameBusStopStrings[i] = cursor.getString(1);
                statusStrings[i] = cursor.getString(5);
                cursor.moveToNext();
            }   // for

            //Create ListView
            MyAdapter myAdapter = new MyAdapter(DeleteBusStop.this,
                    nameBusStopStrings, statusStrings);
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(tag, "id ==> " + idStrings[i]);
                    confirmDelete(Integer.parseInt(idStrings[i]));
                }

                private void confirmDelete(final int id) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(DeleteBusStop.this);
                    builder.setCancelable(false);
                    builder.setIcon(R.mipmap.ic_bus);
                    builder.setTitle("Confirm Delete");
                    builder.setMessage("Are You Sure ?");
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            sqLiteDatabase.delete("busTABLE", "_id" + "=" + id, null);
                            createListView();
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();

                }   // confirmDelete


            });

            cursor.close();

        } catch (Exception e) {
            Log.d(tag, "e createListView ==> " + e.toString());
        }

    }   // createListView



    private void imageController() {
        imageView.setOnClickListener(DeleteBusStop.this);
    }

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
