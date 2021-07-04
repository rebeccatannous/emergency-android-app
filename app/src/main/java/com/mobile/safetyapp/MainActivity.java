package com.mobile.safetyapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements ContactDetailFragment.UpdateLayout {
    ArrayList<String> emergencyNumbers;
    String messagefrominterface;
    boolean fromInterface = false;
    boolean playing = false;
    //this version of the project gets the location and sends it as an sms with a custom message
    private static final int REQUEST_LOCATION = 1;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    Button getLocation;
    EditText messageToSend;
    String message;
    ImageView contacts;
    //some test cases until I implement the emergency contacts
    String mobile_number, mobile_number2;
    LocationManager locationManager;
    String latitude, longitude, mapslink;
    MediaPlayer mediaPlayer;

    // Timer
    Handler handler;
    int ticks = 0;
    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            ticks = savedInstanceState.getInt("ticks");
            running = savedInstanceState.getBoolean("running");
        }

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        emergencyNumbers = new ArrayList();
        getEmergencyContacts();
        sendLocation();
        Toolbar toolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        boolean sending_msg = intent.getBooleanExtra("sending", false);
        if (sending_msg) {
            String messageSent = intent.getStringExtra("messageSent");
            String number = intent.getStringExtra("number");
            boolean location = intent.getBooleanExtra("location", true);
            boolean sms = intent.getBooleanExtra("sms", true);
            send(messageSent, number, location, sms);
        }

        // Start Timer onClickListener
        Button start_timer = findViewById(R.id.start_timer_btn);
        start_timer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running) {
                    running = false;
                    start_timer.setText("Start Timer");
                    handler.removeCallbacksAndMessages(null);
                }

                else {
                    Spinner spinner = findViewById(R.id.spinner_timer);
                    start_timer.setText("I am Safe");
                    ticks = Integer.parseInt(spinner.getSelectedItem().toString()) * 60;
                    running = true;
                    runTimer();
                }
            }
        });

    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("ticks", ticks);
        outState.putBoolean("running", running);
    }

    public void runTimer() {
        TextView textView = findViewById(R.id.timer_text_view);
        handler = new Handler(Looper.getMainLooper());
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (running) {
                    ticks--;
                }

                if(ticks<=0) {
                    if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        onGPS();
                    }
                    message = "I am in danger, Please Help Me !!";
                    getLocation();
                    message += " " + mapslink;
                    Button start_timer = findViewById(R.id.start_timer_btn);
                    start_timer.setText("Start Timer");
                    running = false;
                    sendSMS(message, emergencyNumbers);
                }

                if(ticks>0) {
                    int seconds = ticks % 60;
                    int minutes = ticks / 60 % 60;
                    int hours = ticks / 3600;
                    String stringToDisplay = String.format("%d:%02d:%02d ", hours, minutes, seconds);
                    textView.setText(stringToDisplay);
                    handler.postDelayed(this, 1000); // set to 1000
                }
            }
        });
    }

    public void getEmergencyContacts() {
        SQLiteDatabase db;
        Cursor cursor;
        try {
            SQLiteOpenHelper sqLiteOpenHelper = new ContactSQLiteOpenHelper(this);
            db = sqLiteOpenHelper.getReadableDatabase();
            cursor = db.query("CONTACTS", new String[]{"PHONE"}, null, null, null, null, null);

            if (cursor.moveToFirst()) {
                String number = cursor.getString(0);
                emergencyNumbers.add(number);
            }
        } catch (Exception e) {
            Toast.makeText(this, "Database not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mainmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        if (item.getItemId() == R.id.contact_icon) {
            intent = new Intent(this, ContactsActivity.class);
            startActivity(intent);
            return true;
        } else if (item.getItemId() == R.id.calling) {
            intent = new Intent(this, EmergencyCalls.class);
            startActivity(intent);
            return true;
        } else

            return super.onOptionsItemSelected(item);
    }


    protected void sendSMSmessage() {
        messageToSend = findViewById(R.id.message_editText);
        message = messageToSend.getText().toString();
        getLocation();
        message += "  " + mapslink;
        sendSMS(message, emergencyNumbers);
    }

    protected void sendSMS(String messsage, ArrayList<String> Phone) {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.SEND_SMS)) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.SEND_SMS},
                        MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            if (Phone.size() != 0) {
                for (int i = 0; i < Phone.size(); i++) {
                    smsManager.sendTextMessage(Phone.get(i), null, messsage, null, null);
                    //smsManager.sendTextMessage(mobile_number2, null, message, null, null);
                }
                Toast.makeText(getApplicationContext(), "SMS sent.",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST_SEND_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                SmsManager smsManager = SmsManager.getDefault();
                if (fromInterface) {

                } else {
                    if (emergencyNumbers.size() != 0) {
                        for (int i = 0; i < emergencyNumbers.size(); i++) {

                            smsManager.sendTextMessage(emergencyNumbers.get(i), null, message, null, null);
                            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
            }
        }
    }

    private void sendWhatsAppMessage(String texto, String mobileno) {
        PackageManager packageManager = getPackageManager();
        try {
            packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent intent = new Intent(Intent.ACTION_VIEW);

            intent.setData(Uri.parse("http:/api.whatsapp.com/send?phone=" + "+961" + mobileno + "&text=" + texto));
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
        }
    }


    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGPS != null) {
                double lat = LocationGPS.getLatitude();
                double lon = LocationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                mapslink = "https://maps.google.com/?q=" + latitude + "," + longitude;
                System.out.println("Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + mapslink);
            } else if (LocationNetwork != null) {
                double lat = LocationNetwork.getLatitude();
                double lon = LocationNetwork.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                mapslink = "https://maps.google.com/?q=" + latitude + "," + longitude;
                System.out.println("Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + mapslink);
            } else if (LocationPassive != null) {
                double lat = LocationPassive.getLatitude();
                double lon = LocationPassive.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(lon);
                mapslink = "https://maps.google.com/?q=" + latitude + "," + longitude;
                System.out.println("Latitude: " + latitude + "\n" + "Longitude: " + longitude + "\n" + mapslink);
            } else {
                Toast.makeText(this, "Cannot get Location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void sendLocation() {
        getLocation = findViewById(R.id.getLocation_button);
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    onGPS();
                } else {
                    sendSMSmessage();
//                    sendWhatsAppMessage();
                }

            }
        });
    }

    private void onGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void startSiren(View view) {
        if (playing) {
            stop();
            playing = false;

        } else {
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer.create(this, R.raw.siren);
            }
            playing = true;
            mediaPlayer.start();
            Toast.makeText(this, "Click again to turn it off", Toast.LENGTH_SHORT).show();
        }

    }

    public void stop() {
        if (playing) {
            playing = false;

            if (mediaPlayer != null) {
                mediaPlayer.release();
                mediaPlayer = null;

            }
        }
    }

    public void stopSiren(View view) {
        stop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stop();
    }

    @Override
    public void delete() {

    }

    @Override
    public void updateContactList() {

    }

    @Override
    public void send(String messageSent, String number, boolean location, boolean sms) {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fromInterface = true;
        messagefrominterface = messageSent;
        if (location) {
            getLocation();
            messageSent += " " + mapslink;
            messagefrominterface += " " + mapslink;
        }
        if (sms) {
            ArrayList<String> number1 = new ArrayList<>();
            number1.add(number);
            sendSMS(messageSent, number1);
        } else {
            sendWhatsAppMessage(messageSent, number);
        }
    }
}