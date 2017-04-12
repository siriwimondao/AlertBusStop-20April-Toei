package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // Explicit
    private ListView listView;
    private Button button;
    private MyManage myManage;
    private LocationManager locationManager;
    private Criteria criteria;
    private Double userLatADouble = 13.964987, userLngADouble = 100.585154, aDouble = 0.0;
    private boolean aBoolean = true, notificationABoolean = true;
    private ImageView editImageView, deleteImageView;
    private int anInt; // ค่า index ของระยะ ที่ใช้ 0==> 20, 1==>300

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //My SetUp
        mySetUp();

        //Bind Widget การผูกตัวแปร
        bindWidget();

        //Create ListView
        createListView();

        //Button controller
        buttonController();

        // Long Click Button Controller
        longClickButtonController();

        //Image Controller
        imageController();


        //My Loop
        myLoop();


    }// Main Medthod

    private void imageController() {
        editImageView.setOnClickListener(MainActivity.this);
        deleteImageView.setOnClickListener(MainActivity.this);
    }

    private void longClickButtonController() {
        button.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Log.d("31octV1", "You Click Long"); //ควบคุมการคลิก

                startActivity(new Intent(MainActivity.this, AddBusStop.class));//เคลื่อนย้ายการทำงาน

                return true;

            } // onLongClick
        });
    }

    private void buttonController() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mySoundEfect(R.raw.add_bus1);
            }// onClick
        });
    }

    private void bindWidget() {
        listView = (ListView) findViewById(R.id.livBusStop);
        button = (Button) findViewById(R.id.button);
        editImageView = (ImageView) findViewById(R.id.imvEdit);
        deleteImageView = (ImageView) findViewById(R.id.imvDelete);
    }

    private void mySetUp() {
        myManage = new MyManage(MainActivity.this);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setAltitudeRequired(false);
        criteria.setBearingRequired(false);
    }

    private void myNotification(String strSound) {

        Log.d("11AprilV1", "aInt ==> " + anInt);
        boolean status = false;
        if (anInt == 1) {
            status = true;
        }


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.drawable.doremon48);
        builder.setTicker("Help Me Please Arrive ");
        builder.setWhen(System.currentTimeMillis());
        builder.setContentTitle("Alert");
        builder.setContentText("Help Me Please Arrive ");
        builder.setAutoCancel(true);

        //Set Sound


        Uri uri;

        if (status) {
            // Destination

            if (notificationABoolean) {
                uri = Uri.parse("android.resource://" +
                        MainActivity.this.getPackageName() +
                        "/" +
                        R.raw.bells);
            } else {
                uri = Uri.parse("android.resource://" +
                        MainActivity.this.getPackageName() +
                        "/" +
                        R.raw.add_bus1);
            }

        } else {
            // BusStop
            uri = Uri.parse("file:" + strSound);
        }

        builder.setSound(uri);
        Notification notification = builder.build();

        if (status) {

            notification.flags |= Notification.DEFAULT_LIGHTS
                    | Notification.FLAG_AUTO_CANCEL
                    | Notification.FLAG_ONLY_ALERT_ONCE;
        } else {

            notification.flags |= Notification.FLAG_AUTO_CANCEL;
        }





        NotificationManager notificationManager = (NotificationManager)
                getSystemService(NOTIFICATION_SERVICE);

        Random random = new Random();
        int i = random.nextInt(1000);

        notificationManager.notify(i, notification);

    }   // myNoti

    //นี่คือ เมทอด ที่หาระยะ ระหว่างจุด
    private static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515 * 1.609344 * 1000;


        return (dist);
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }

    //เมธอดที่ ทำงานวนไปวนมา ทุก 1 sec
    private void myLoop() {

        //Doing
        afterResume();

        calculateAllDistance();


        //Post
        if (aBoolean) {
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    myLoop();
                }
            }, 5000);
        }


    }   // myLoop

    private void calculateAllDistance() {

        try {

            double[] seriousDistance = new double[]{20.0, 300.0};

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM busTABLE", null);
            cursor.moveToFirst();
            int intCursor = cursor.getCount();
            double[] destinationLatDoubles = new double[intCursor];
            double[] destinationLngDoubles = new double[intCursor];
            double[] distanceDoubles = new double[intCursor];
            int[] indexDistance = new int[intCursor];

            for (int i = 0; i < intCursor; i++) {

                destinationLatDoubles[i] = Double.parseDouble(cursor.getString(3));
                destinationLngDoubles[i] = Double.parseDouble(cursor.getString(4));
                distanceDoubles[i] = distance(userLatADouble, userLngADouble,
                        destinationLatDoubles[i], destinationLngDoubles[i]);
                indexDistance[i] = Integer.parseInt(cursor.getString(5));

                Log.d("27febV4", "ระยะห่างจากจุดที่ (" + i + ") ==> " + distanceDoubles[i]);
                Log.d("11AprilV1", "ระยะห่างจากจุดที่ (" + i + ") ==> " + distanceDoubles[i]);
                Log.d("27febV4", "index ==> " + indexDistance[i]);
                Log.d("27febV4", "ระยะคำนวน ==> " + seriousDistance[indexDistance[i]]);
                Log.d("27febV4", "boolean Notification ==> " + notificationABoolean);
                Log.d("11AprilV1", "boolean Notification ==> " + notificationABoolean);
                Log.d("27febV4", "aDouble ==> " + aDouble);

                //Check Distance
                if ((distanceDoubles[i] <= seriousDistance[indexDistance[i]])) {    // เมื่ออยู่ในวง
                    Log.d("27febV4", "Notification Work");


                    // ดูว่าเป็นการเข้าครั้งแรกปะ
                    //ค่า aDouble ค่าของ seriousDistance เพิ่มไป 10 เมตร
                    if (notificationABoolean) {
                        aDouble = seriousDistance[indexDistance[i]] + 10;

                        anInt = indexDistance[i];
                        myNotification(cursor.getString(2));
                        notificationABoolean = false;

                    }


                } else if (distanceDoubles[i] <= (aDouble)) {
                    if (!notificationABoolean) {
                        myNotification(cursor.getString(2));
                    }
                    notificationABoolean = true;

                }


                cursor.moveToNext();
            }   //for


            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }   // calculate

    @Override
    protected void onStop() {
        super.onStop();
        locationManager.removeUpdates(locationListener);
    }

    public Location myFindLocation(String strProvicer) {

        Location location = null;
        if (locationManager.isProviderEnabled(strProvicer)) {
            locationManager.requestLocationUpdates(strProvicer, 1000, 10, locationListener);
            location = locationManager.getLastKnownLocation(strProvicer);
        }

        return location;
    }

    public LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            userLatADouble = location.getLatitude();
            userLngADouble = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String s, int i, Bundle bundle) {

        }

        @Override
        public void onProviderEnabled(String s) {

        }

        @Override
        public void onProviderDisabled(String s) {

        }
    };


    @Override
    protected void onResume() {
        super.onResume();
        createListView();
        afterResume();
    }

    private void afterResume() {

        locationManager.removeUpdates(locationListener);

        Location networkLocation = myFindLocation(LocationManager.NETWORK_PROVIDER);
        if (networkLocation != null) {
            userLatADouble = networkLocation.getLatitude();
            userLngADouble = networkLocation.getLongitude();
        }

        Location gpsLocation = myFindLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            userLatADouble = gpsLocation.getLatitude();
            userLngADouble = gpsLocation.getLongitude();
        }

        Log.d("27febV3", "Lat ==> " + userLatADouble);
        Log.d("27febV3", "Lng ==> " + userLngADouble);

    }   // afterResume

    private void createListView() {

        try {

            final String tag = "11AprilV2";

            //Read All SQLite
            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            //Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM busTABLE WHERE Destination = 1", null);
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM busTABLE", null);
            cursor.moveToFirst();
            int intCursor = cursor.getCount();
            Log.d("27febV2", "intCursor ==> " + intCursor);

            final String[] idStrings = new String[intCursor];
            final String[] nameStrings = new String[intCursor];
            String[] statusStrings = new String[intCursor];
            for (int i = 0; i < intCursor; i++) {

                idStrings[i] = cursor.getString(0);
                nameStrings[i] = cursor.getString(1);
                statusStrings[i] = cursor.getString(5);
                cursor.moveToNext();

            }   // for

            //Show ListView
            MyAdapter myAdapter = new MyAdapter(MainActivity.this,
                    nameStrings, statusStrings);
            listView.setAdapter(myAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                    final String strID = idStrings[i];

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setCancelable(false);
                    builder.setIcon(R.mipmap.ic_bus);
                    builder.setTitle("โปรยืนยัน");
                    builder.setMessage("คุณต้องการเปลียน ปลายทางไปที่ " + nameStrings[i]);
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            editDestination(strID);
                            dialogInterface.dismiss();
                        }
                    });
                    builder.show();

                }   // onItem
            });


            cursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }   // createListView

    private void editDestination(String strID) {

        try {

            SQLiteDatabase sqLiteDatabase = openOrCreateDatabase(MyOpenHelper.database_name,
                    MODE_PRIVATE, null);
            sqLiteDatabase.execSQL("UPDATE busTABLE SET Destination = 1 WHERE _id=" + strID);

            createListView();

        } catch (Exception e) {
            Log.d("11AprilV2", "e edti ==> " + e.toString());
        }

    }   // editDestination

    private void mySoundEfect(int intSound) {
        MediaPlayer mediaPlayer = MediaPlayer.create(getApplicationContext(), intSound);
        mediaPlayer.start(); //ทำการร้อง

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release(); // คืนหน่วยความจำ
            }
        });
    } // mySoundEfect

    @Override
    public void onClick(View view) {

        //For Edit
        if (view == editImageView) {
            startActivity(new Intent(MainActivity.this, EditBusStop.class));
        }

        //For Delete
        if (view == deleteImageView) {
            startActivity(new Intent(MainActivity.this, DeleteBusStop.class));
        }

    }   // onClick

}// Main class
