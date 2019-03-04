package com.example.trex;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;

public class Cactus {

    int xPosition;
    int yPosition;
    int direction;
    Bitmap cacImage;


    private Rect hitBox;

    public Cactus(Context context, int x, int y, int cactusImages) {
        this.cacImage = BitmapFactory.decodeResource(context.getResources(), cactusImages);
        this.cacImage = Bitmap.createScaledBitmap(this.cacImage,250,150,false);
        this.xPosition = x;
        this.yPosition = y;

        this.hitBox = new Rect(this.xPosition, this.yPosition, this.xPosition + this.cacImage.getWidth(), this.yPosition + this.cacImage.getHeight());
    }

    public void updateCactusPosition() {
        this.xPosition = this.xPosition - 15;

         //update the position of the hitbox
//        this.hitBox.left = this.xPosition;
//        this.hitBox.right = this.xPosition + this.cacImage.getWidth();
        //this.updateHitbox();
    }

    public void updateHitbox() {
        // update the position of the hitbox
        this.hitBox.top = this.yPosition;
        this.hitBox.left = this.xPosition;
        this.hitBox.right = this.xPosition + this.cacImage.getWidth();
        this.hitBox.bottom = this.yPosition + this.cacImage.getHeight();
    }
//
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

    public Bitmap getBitmap() {
        return this.cacImage;
    }



}
