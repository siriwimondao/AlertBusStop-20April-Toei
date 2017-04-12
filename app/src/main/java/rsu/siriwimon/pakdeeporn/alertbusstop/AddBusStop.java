package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AddBusStop extends FragmentActivity implements OnMapReadyCallback {
    // Explicit
    private GoogleMap mMap;
    private EditText editText;
    private Button button;
    private String nameBusStopString, pathAudioString;
    private ImageView recodImageView, playimImageView, backImageView;
    private boolean aBoolean = true; // nonrecord sound
    private Uri uri;
    private double laStartADouble = 13.964987;
    private double lngStartADouble = 100.585154;
    private double laBusStopADouble, lngBusStopADouble;
    private boolean locationABoolean = true;
    private CheckBox checkBox;
    private int checkboxAnInt = 0;
    private boolean editABoolean = false;
    private String idString;
    private String tag = "12AprilV2";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_addbusstop_layout);

        // Bind Widget
        bindWidget();

        //From EditBusStop
        fromEditBusStop();

        //Back Controller
        backController();

        //record controller
        recordController();

        // button controller ปุ่มคลิ๊ก
        buttonController();

        //play controller
        playController();

        // Create Fragment Map
        createFragmentMap();

    } // Main Method

    private void createFragmentMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void playController() {
        playimImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check record
                if (aBoolean) {
                    // non record ไม่มีการบันทึกเสียงให้แจ้ง
                    MyAlert myAlert = new MyAlert(AddBusStop.this, R.drawable.nobita48,
                            getResources().getString(R.string.title_record_sound),
                            getResources().getString(R.string.massage_record_sound));
                    myAlert.myDialog();
                } else {
                    // record ok ให้เสียงร้องเล่นเสียง
                    MediaPlayer mediaPlayer = MediaPlayer.create(AddBusStop.this, uri);
                    mediaPlayer.start();
                }
            } // onclick
        });
    }

    private void buttonController() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Get Value From EditText รับค่าจาก EditText
                nameBusStopString = editText.getText().toString().trim();

                // Check spece
                if (nameBusStopString.equals("")) {
                    // Have Space
                    MyAlert myAlert = new MyAlert(AddBusStop.this,
                            R.drawable.doremon48,
                            getResources().getString(R.string.title_have_space),
                            getResources().getString(R.string.massage_have_space));
                    myAlert.myDialog();

                } else if (aBoolean) {
                    // non record audio
                    MyAlert myAlert = new MyAlert(AddBusStop.this, R.drawable.kon48,
                            getResources().getString(R.string.title_record_sound),
                            getResources().getString(R.string.massage_record_sound));
                    myAlert.myDialog();
                } else if (locationABoolean) {
                    // non marker
                    MyAlert myAlert = new MyAlert(AddBusStop.this, R.drawable.bird48,
                            getResources().getString(R.string.title_mark),
                            getResources().getString(R.string.massage_mark));
                    myAlert.myDialog();

                } else {

                    //Delete Old Data
                    deleteOldData();

                    //update value to SQlite
                    updateValuetoSQlite();

                }  // if
            } // onClick
        });
    }

    private void deleteOldData() {
        if (editABoolean) {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM busTABLE", null);
            cursor.moveToFirst();
            sqLiteDatabase.delete("busTABLE", "_id" + "=" + Integer.parseInt(idString), null);

        }
    }

    private void recordController() {
        recodImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                startActivityForResult(intent, 0); //ใส่เลข 0 เป็น result
            } // onclick บันทึกเสียง
        });
    }

    private void backController() {
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void fromEditBusStop() {

        try {

            editABoolean = getIntent().getBooleanExtra("Edit", false);
            if (editABoolean) {
                idString = getIntent().getStringExtra("id");
                nameBusStopString = getIntent().getStringExtra("Name");
            }

            Log.d(tag, "id ==> " + idString);
            editText.setText(nameBusStopString);


        } catch (Exception e) {
            Log.d(tag, "e fromEdit ==> " + e.toString());
        }

    }   // fromEditBusStop

    private void bindWidget() {
        editText = (EditText) findViewById(R.id.editText);
        button = (Button) findViewById(R.id.button2);
        recodImageView = (ImageView) findViewById(R.id.imageView);
        playimImageView = (ImageView) findViewById(R.id.imageView2);
        checkBox = (CheckBox) findViewById(R.id.checkBox1);
        backImageView = (ImageView) findViewById(R.id.imvBack);
    }

    private void updateValuetoSQlite() {

        //Check CheckBox
        if (checkBox.isChecked()) {
            checkboxAnInt = 1;
        }

        Log.d("27febV1", "Name  ==> " + nameBusStopString);
        Log.d("27febV1", "Path  ==> " + pathAudioString);
        Log.d("27febV1", "Lat  ==> " + Double.toString(laBusStopADouble));
        Log.d("27febV1", "Lng  ==> " + Double.toString(lngBusStopADouble));
        Log.d("27febV1", "Destination  ==> " + Integer.toString(checkboxAnInt));

        //Add New Value to SQLite
        MyManage myManage = new MyManage(AddBusStop.this);
        myManage.addValueToSQLite(nameBusStopString, pathAudioString,
                Double.toString(laBusStopADouble),
                Double.toString(lngBusStopADouble),
                Integer.toString(checkboxAnInt));
        finish();

    } // updateValuetoSQlite บันทึกข้อมูลลงฐานข้อมูลsqlite

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if ((requestCode == 0) && (resultCode == RESULT_OK)) {
            Log.d("1novV1", "Result OK");
            aBoolean = false; //record sound ok
            uri = data.getData();

            //Find Path of Audio ที่อยู่ของเสียง
            String[] strings = {MediaStore.Audio.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, strings, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                int index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                pathAudioString = cursor.getString(index);
            } else {
                pathAudioString = uri.getPath();
            }

            Log.d("1novV1", "pathAudioString ==> " + pathAudioString);

        }//if การบันทึกเสียง ถ้าบันทึกเสร็จให้กลับมาหน้าเดิม


    }   // onActivityresult

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // create Map
        LatLng centerLatLng = new LatLng(laStartADouble, lngStartADouble);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(centerLatLng, 16)); // 16 คือตำแหน่งซูม มุมมอง


        // get event from clack map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {

                locationABoolean = false;

                mMap.clear(); //ลบมากเกอร์ต่างๆ
                mMap.addMarker(new MarkerOptions()
                        .position(latLng));

                laBusStopADouble = latLng.latitude; // เลือกค่าละลอง
                lngBusStopADouble = latLng.longitude;

                Log.d("1novV1", "Lat ==>" + laBusStopADouble);
                Log.d("1novV1", "lng ==>" + lngBusStopADouble);

            } // on map click
        });
    } // onMapReady
} // Main Class
