package com.example.trex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;

public class Dino {


    int xPosition;
    int yPosition;
    int direction = -1;              // -1 = not moving, 0 = down, 1 = up
    Bitmap dinoImage;

    private Rect hitBox;

    public Dino(Context context, int x, int y) {


        this.dinoImage = BitmapFactory.decodeResource(context.getResources(), R.drawable.dino1);
        this.dinoImage = Bitmap.createScaledBitmap(this.dinoImage,150,150,false);
        this.xPosition = x;
        this.yPosition = y;
        this.hitBox = new Rect(this.xPosition, this.yPosition, this.xPosition + this.dinoImage.getWidth(), this.yPosition + this.dinoImage.getHeight());

    }

    public void updatePlayerPosition() {
        if (this.direction == 0) {
            // move down
            this.yPosition = this.yPosition - 20;
            if(this.yPosition <= 300){
                this.direction = 1;
                if (this.yPosition == 660){
                    this.direction = -1;
                }
            }
        }
        else if (this.direction == 1) {
            // move up

            this.yPosition = this.yPosition + 30;
            if(this.yPosition >= 660){
                this.direction = -1;
            }
            Log.d("rex_posi",String.valueOf(this.yPosition));        }

        // update the position of the hitbox
        this.updateHitbox();
    }

    public void updateHitbox() {
        // update the position of the hitbox
        this.hitBox.top = this.yPosition;
        this.hitBox.left = this.xPosition;
        this.hitBox.right = this.xPosition + this.dinoImage.getWidth();
        this.hitBox.bottom = this.yPosition + this.dinoImage.getHeight();
    }

    public Rect getHitbox() {
        return this.hitBox;
    }

    public void setXPosition(int x) {
        this.xPosition = x;
        this.updateHitbox();
    }
    public void setYPosition(int y) {
        this.yPosition = y;
       this.updateHitbox();
    }
    public int getXPosition() {
        return this.xPosition;
    }
    public int getYPosition() {
        return this.yPosition;
    }

    /**
     * Sets the direction of the player
     * @param i     0 = down, 1 = up
     */
    public void setDirection(int i) {

        this.direction = i;
    }
    public Bitmap getBitmap() {
        return this.dinoImage;

    }


}


