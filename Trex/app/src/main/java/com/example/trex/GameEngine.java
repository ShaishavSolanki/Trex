package com.example.trex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

public class GameEngine extends SurfaceView implements Runnable {

    //screen Size
    int screenHeight;
    int screenWidth;

    double score = 0;


    //draw the dino on the platform Static position
    int rexX = 50;
    int rexY = 660;

    //
    int cacX = 1900;
    int cacY = 660;

    int platformX = 0;
    int platformRightSIde = 0;

    SurfaceHolder holder;
    Canvas canvas;
    Paint paintbrush;


    //Speed variables for background and platform
    int backgroundSpeed = 15;
    int platformSpeed = 30;
    int cactusSpeed = 30;


    Bitmap background, platform;

    boolean gameIsRunning = false;
    boolean game = false;

    Thread gameThread;

    int bgX;            // x-coordinate of the top-left corner of the cloud
    int bgXRight;

    Dino rex;
    Cactus cac;
    Random rCac = new Random();
    int cac1;
    int[] cactusImages = {R.drawable.cactus, R.drawable.cactus1};

    //for formatting the score in non-decimal
    NumberFormat formatter = new DecimalFormat("##");


    public GameEngine(Context context, int h, int w, Point p) {
        super(context);

        this.screenHeight = p.y;
        this.screenWidth = p.x;


        // Setup drawing variable
        holder = this.getHolder();
        paintbrush = new Paint();


        this.setupBackground();
        this.drawDino();
        this.drawCactus();

    }


    private int showCac() {
        int cacImage = rCac.nextInt(cactusImages.length);
        cac1 = (cactusImages[cacImage]);
        return cac1;
    }

    private void setupBackground() {
        // get the background image from `res` folder and
        // scale it to be the same size as the phone
        this.background = BitmapFactory.decodeResource(this.getResources(), R.drawable.cloud);
        this.background = Bitmap.createScaledBitmap(this.background, this.screenWidth, this.screenHeight, false);

        this.platform = BitmapFactory.decodeResource(this.getResources(), R.drawable.baseline);
        this.platform = Bitmap.createScaledBitmap(this.platform, this.screenWidth, this.screenHeight / 25, false);

    }

    private void drawDino() {

        rex = new Dino(this.getContext(), this.rexX, this.rexY);

    }

    private void drawCactus() {

        cac = new Cactus(this.getContext(), this.cacX, this.cacY, this.showCac());

    }

    public void redrawSprites() {
        if (this.holder.getSurface().isValid()) {
            this.canvas = this.holder.lockCanvas();

            this.canvas.drawColor(Color.argb(255, 255, 255, 255));
            paintbrush.setColor(Color.WHITE);



            canvas.drawBitmap(this.background, this.bgX, 0, null);

            canvas.drawBitmap(this.background, this.bgXRight, 0, null);


            paintbrush.setColor(Color.BLACK);
            paintbrush.setTextSize(100);
            canvas.drawText("Score: " + this.formatter.format(this.score), 0, 100, paintbrush);


            //drawing a platform for Dino
            canvas.drawBitmap(this.platform, this.platformX, 800, null);
            canvas.drawBitmap(this.platform, this.platformRightSIde, 800, null);
            //Drawing a Dino
            canvas.drawBitmap(this.rex.getBitmap(), this.rex.getXPosition(), this.rex.getYPosition(), paintbrush);
            // Log.d("GetDino", String.valueOf(this.rex.getXPosition()));

            //drawing cactus on the platform
            canvas.drawBitmap(cac.cacImage, cac.xPosition, cac.yPosition, null);

            paintbrush.setColor(Color.RED);
            paintbrush.setTextSize(100);
            if (rex.getHitbox().intersect(cac.getHitbox())){
                canvas.drawText("Game Over",600,300,paintbrush);
                canvas.drawText("Your Score is: " + this.formatter.format(this.score), 600, 400, paintbrush);
            }
            //SHow hitbox on dino
            paintbrush.setColor(Color.RED);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);
            Rect dinoHitbox = rex.getHitbox();
            canvas.drawRect(dinoHitbox.left, dinoHitbox.top, dinoHitbox.right, dinoHitbox.bottom, paintbrush);


            //Show hitbox on Cactus
            paintbrush.setColor(Color.RED);
            paintbrush.setStyle(Paint.Style.STROKE);
            paintbrush.setStrokeWidth(5);
            Rect cacHitbox = cac.getHitbox();
            canvas.drawRect(cacHitbox.left, cacHitbox.top, cacHitbox.right, cacHitbox.bottom, paintbrush);


            this.score = score + 0.5;


            this.holder.unlockCanvasAndPost(canvas);
        }
    }

    private void updateBackgroundPosition() {


        // move the cloud left
        if (gameIsRunning == true) {
            this.bgX = this.bgX - backgroundSpeed;

            // Log.d("backgroundWIdth",String.valueOf(this.background.getWidth()));
            // Log.d("bgx", String.valueOf(this.bgX));
            // remove the "white gap" between the two images
            this.bgXRight = background.getWidth() - (-this.bgX);

            if (bgXRight <= 0) {
                // when the 2nd copy of the cloud scrolls off the screen,
                // reset the starting point of 1st copy to (0,0)
                this.bgX = 0;
            }


            //for moving the platform
            this.platformX = this.platformX - platformSpeed;

            this.platformRightSIde = platform.getWidth() - (-this.platformX);
            if (platformRightSIde <= 0) {
                // when the 2nd copy of the cloud scrolls off the screen,
                // reset the starting point of 1st copy to (0,0)
                this.platformX = 0;
            }


            //for moving Cactus

            cac.xPosition = cac.xPosition - this.cactusSpeed;
        Log.d("CAc",String.valueOf(cac.xPosition));
            if (cac.xPosition <= -200) {
                this.drawCactus();
            }

        }

    }


    public void updatePositions() {

        this.updateBackgroundPosition();
        this.cac.updateHitbox();
        this.rex.updatePlayerPosition();
        this.checkScore();


        //Collision detection

        if (rex.getHitbox().intersect(cac.getHitbox())) {

            MediaPlayer ring = MediaPlayer.create(getContext(), R.raw.die);
            ring.start();
            game = true;
            gameIsRunning = false;
            this.backgroundSpeed = 0;
            this.cactusSpeed = 0;
            this.platformSpeed = 0;
            //this.score= 0;
            //ring.stop();

        }
    }

    private void restartGame() {

        this.rexX = 50;
         this.rexY = 660;
         this.cac.xPosition = 1900;
         this.cacY = 660;
         this.backgroundSpeed = 15;
         this.platformSpeed = 30;
         this.cactusSpeed = 30;
         this.score= 0;

    }

    @Override
    public void run() {
        while (gameIsRunning == true) {
            this.updatePositions();
            this.redrawSprites();

        }

    }

    public void pauseGame() {
        gameIsRunning = false;

    }


    public void startGame() {

        // gameIsRunning = true;
        gameThread = new Thread(this);
        gameThread.start();


    }



    public void checkScore() {
        int n = 9999;
        for (int i = 1; i < n; i++) {
            int finalScore = 100 * i;
            if (this.score == finalScore) {
                Log.d("INterger", String.valueOf(this.score));
                Log.d("i", String.valueOf(i));
                MediaPlayer ring = MediaPlayer.create(getContext(), R.raw.checkpoint);
                ring.start();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int userAction = event.getActionMasked();
        //@TODO: What should happen when person touches the screen?

        MediaPlayer ring = MediaPlayer.create(getContext(), R.raw.jump);



            if (userAction == MotionEvent.ACTION_DOWN) {

                if (game == true) {
                   this.restartGame();
                    game = false;
                } else {
                    // move player up
                    gameIsRunning = true;
                    startGame();
                    this.rex.setDirection(1);
                    ring.start();
                }

            } else if (userAction == MotionEvent.ACTION_UP) {

                this.rex.setDirection(0);

            }


        return true;
    }




}

