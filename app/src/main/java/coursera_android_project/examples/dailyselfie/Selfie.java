package coursera_android_project.examples.dailyselfie;

import android.graphics.Bitmap;

/**
 * Created by santosh on 18-07-2015.
 */
public class Selfie {
    private String selfieName;
//    private Bitmap thumbnail;
    private String imagePath;

    public Selfie(String selfieName, String imagePath/*, Bitmap thumbnail*/){/////////////////////
        this.selfieName = selfieName;
 //       this.thumbnail = thumbnail;
        this.imagePath = imagePath;
    }

    public String getSelfieName() {
        return selfieName;
    }

    public String getImagePath() {
        return imagePath;
    }

 /*   public Bitmap getThumbnail() {
        return thumbnail;
    }*/
}
