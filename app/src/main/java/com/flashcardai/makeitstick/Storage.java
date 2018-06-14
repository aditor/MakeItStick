package com.flashcardai.makeitstick;

import android.graphics.Bitmap;

public class Storage {
    private static Storage instance;
    private Bitmap bitmap;

    private Storage() {}

    public static Storage getInstance() {
        if(instance == null){
            instance = new Storage();
        }

        return instance;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
