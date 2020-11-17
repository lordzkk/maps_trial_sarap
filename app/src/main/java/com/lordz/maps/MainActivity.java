package com.lordz.maps;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.lordz.maps.nearby.NearbyPlaces;

public class MainActivity extends AppCompatActivity {
    Button maps,nearby;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent mapsh = new Intent(MainActivity.this, Maps.class);
        startActivity(mapsh);

        maps = (Button) findViewById(R.id.mapss);
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapsh = new Intent(MainActivity.this, Maps.class);
                startActivity(mapsh);
            }
        });

        nearby = (Button) findViewById(R.id.nearby);
        nearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapsh = new Intent(MainActivity.this, NearbyPlaces.class);
                startActivity(mapsh);
            }
        });
    }
}