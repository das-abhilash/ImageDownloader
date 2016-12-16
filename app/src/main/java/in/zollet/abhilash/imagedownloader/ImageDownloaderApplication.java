package in.zollet.abhilash.imagedownloader;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import io.realm.Realm;

/**
 * Created by Abhilash on 12/14/2016.
 */

public class ImageDownloaderApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        // Initializing Realm.
        Realm.init(this);
        saveImageData(loadArrayList());

        File file = new File(getCacheDir(), "placeholder.PNG");
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.placeholder);
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);


    }

    private void saveImageData(ArrayList<ImageRealm> imageRealms) {
        // imageRealms.forEach(imageRealm ->   ImageRealmController.getInstance().saveImageRealmToDB(imageRealm));
        for( ImageRealm imageRealm : imageRealms)
            ImageRealmController.getInstance().saveImageRealmToDB(imageRealm);
    }

    private ArrayList<ImageRealm> loadArrayList() {
        ArrayList<ImageRealm> imageList= new ArrayList<ImageRealm>();
        imageList.add(new ImageRealm("http://www.hdwallpapers.in/walls/autumn_bench-HD.jpg"));
        imageList.add(new ImageRealm("https://d13yacurqjgara.cloudfront.net/users/488772/screenshots/2786933/ironman_1x.png"));
        imageList.add(new ImageRealm("http://cdn.wallpapersafari.com/25/73/5orn0c.jpg"));
        imageList.add(new ImageRealm("http://cdn.wallpapersafari.com/63/8/kGQg8j.jpg"));
        imageList.add(new ImageRealm("http://hddesktopwallpapers.in/wp-content/uploads/2015/06/Spiderman-Pictures-Wallpapers-HD-A10-Logo.jpg"));
        imageList.add(new ImageRealm("http://interfacelift.com/wallpaper/D8cfd622/03359_goatatmountpilatus_3840x2160.jpg"));
        imageList.add(new ImageRealm("http://www.planwallpaper.com/static/images/fall-leaves-images-hd-wallpaper-1080p1.jpg"));
        imageList.add(new ImageRealm("https://upload.wikimedia.org/wikipedia/commons/0/07/Azerbajian_landscape.jpg"));
        imageList.add(new ImageRealm("https://www.fritzundfraenzi.ch/system/article/medium_image/499/Ferien_Schule_Lehrer_Lehrerin_immer_frei_Arbeitsplatz.jpeg"));

        return imageList;
    }

}
