package com.kodluyoruz.lilywashingmachine;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;

import pl.pawelkleczkowski.customgauge.CustomGauge;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference referenceColor;
    DatabaseReference referenceWhite;
    CustomGauge gaugeWhiteHumidity, gaugeWhiteFullness, gaugeWhiteTemp;
    CustomGauge gaugeColorHumidity, gaugeColorFullness, gaugeColorTemp;
    DatabaseReference referenceRunColor, referenceRunWhite;
    TextView txtWhiteHumidity, txtWhiteFullness, txtWhiteTemp;
    TextView txtColorHumidity, txtColorFullness, txtColorTemp;
    FloatingActionButton fabColorRun, fabWhiteRun;
    String textColor, textWhite;
    Handler handler;
    boolean state = true;
    private int mMaxProgress = 100;
    private LinkedList<ProgressType> mProgressTypes;
    private Handler mUiHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();


    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.e("onstart", "onStart");
        referenceColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot basketSnapshot : dataSnapshot.getChildren()) {
                    Basket basketColor = basketSnapshot.getValue(Basket.class);
                    txtColorHumidity.setText("Nem\n" + String.valueOf(basketColor.getHumidity()) + "%");
                    gaugeColorHumidity.setValue(basketColor.getHumidity());
                    txtColorFullness.setText(String.valueOf(basketColor.getDistance()) + "%");
                    gaugeColorFullness.setValue(basketColor.getDistance());
                    txtColorTemp.setText(String.valueOf(basketColor.getTemperature()) + "°C");
                    gaugeColorTemp.setValue(basketColor.getTemperature());
                    Log.e("onstart", "DataChange");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        referenceWhite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot basketSnapshot : dataSnapshot.getChildren()) {
                    Basket basketWhite = basketSnapshot.getValue(Basket.class);
                    txtWhiteHumidity.setText("Nem\n" + String.valueOf(basketWhite.getHumidity()) + "%");
                    gaugeWhiteHumidity.setValue(basketWhite.getHumidity());
                    txtWhiteFullness.setText(String.valueOf(basketWhite.getDistance()) + "%");
                    gaugeWhiteFullness.setValue(basketWhite.getDistance());
                    txtWhiteTemp.setText(String.valueOf(basketWhite.getTemperature()) + "°C");
                    gaugeWhiteTemp.setValue(basketWhite.getTemperature());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        referenceRunColor.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textColor = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        referenceRunWhite.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                textWhite = dataSnapshot.getValue(String.class);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initView() {
        referenceColor = database.getReference("devices/color");
        referenceWhite = database.getReference("devices/white");
        referenceRunColor = database.getReference("RunColor");
        referenceRunWhite = database.getReference("RunWhite");

        txtColorHumidity = (TextView) findViewById(R.id.txtColorHumidity);
        txtColorFullness = (TextView) findViewById(R.id.txtColorFullness);
        txtColorTemp = (TextView) findViewById(R.id.txtColorTemp);

        txtWhiteHumidity = (TextView) findViewById(R.id.txtWhiteHumidity);
        txtWhiteFullness = (TextView) findViewById(R.id.txtWhiteFullness);
        txtWhiteTemp = (TextView) findViewById(R.id.txtWhiteTemp);

        fabColorRun = (FloatingActionButton) findViewById(R.id.fabColorRun);
        fabWhiteRun = (FloatingActionButton) findViewById(R.id.fabWhiteRun);

        gaugeColorHumidity = (CustomGauge) findViewById(R.id.gaugeColorHumidity);
        gaugeColorTemp = (CustomGauge) findViewById(R.id.gaugeColorTemp);
        gaugeColorFullness = (CustomGauge) findViewById(R.id.gaugeColorFullness);

        gaugeWhiteHumidity = (CustomGauge) findViewById(R.id.gaugeWhiteHumidity);
        gaugeWhiteTemp = (CustomGauge) findViewById(R.id.gaugeWhiteTemp);
        gaugeWhiteFullness = (CustomGauge) findViewById(R.id.gaugeWhiteFullness);

        fabColorRun.setMax(mMaxProgress);
        fabWhiteRun.setMax(mMaxProgress);

        mProgressTypes = new LinkedList<>();
        for (ProgressType type : ProgressType.values()) {
            mProgressTypes.offer(type);
        }

        initEvent();
    }

    private void initEvent() {
        fabColorRun.setOnClickListener(this);
        fabWhiteRun.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if ((view.getId() == R.id.fabColorRun) && (textColor.equalsIgnoreCase("false"))) {
            referenceRunColor.setValue("true");
            Toast.makeText(getApplicationContext(), "Renkli", Toast.LENGTH_SHORT).show();
        } else if ((view.getId() == R.id.fabWhiteRun) && (textWhite.equalsIgnoreCase("false"))) {
            referenceRunWhite.setValue("true");
            Toast.makeText(getApplicationContext(), "Beyaz", Toast.LENGTH_SHORT).show();
        }
    }

    private void increaseProgress(final FloatingActionButton fab, int i) {
        if (i <= mMaxProgress) {
            fab.setProgress(i, false);
            final int progress = ++i;
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    increaseProgress(fab, progress);
                }
            }, 30);
        } else {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    fab.hideProgress();
                }
            }, 200);
            mProgressTypes.offer(ProgressType.PROGRESS_NO_ANIMATION);
        }
    }


    private enum ProgressType {
        INDETERMINATE, PROGRESS_POSITIVE, PROGRESS_NEGATIVE, HIDDEN, PROGRESS_NO_ANIMATION, PROGRESS_NO_BACKGROUND
    }

}
