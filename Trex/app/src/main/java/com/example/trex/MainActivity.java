package com.example.trex;

import android.content.Intent;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;


public class MainActivity extends AppCompatActivity {

    GameEngine dinoEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        dinoEngine = new GameEngine(this, size.x, size.y, size);

        setContentView(dinoEngine);
    }

    @Override
    protected void onResume() {
        super.onResume();
        dinoEngine.startGame();
    }

    @Override
    protected void onPause() {
        super.onPause();
        dinoEngine.pauseGame();

    }

}