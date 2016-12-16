package in.zollet.abhilash.imagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import java.io.File;

public class DetailActivity extends AppCompatActivity {

    public static final String ARG_IMAGE = "image";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ImageView iv = (ImageView) findViewById(R.id.detail_demo);


        Bundle extras = getIntent().getExtras();
        //String p = getCacheDir()+ "placeholder.PNG";
        String  destinationUri = extras.getString(ARG_IMAGE,getCacheDir()+ "placeholder.PNG");


        new Thread() {
            public void run() {
                File image = new File(destinationUri);
                Bitmap myBitmap = BitmapFactory.decodeFile(image.getAbsolutePath());

                Bitmap finalBitmap = myBitmap;
                runOnUiThread(() -> {
                    iv.setImageBitmap(myBitmap);

                });
            }
        }.start();
        /*Drawable drawable = imageRealm.getDrawable();
        iv.setImageDrawable(drawable);*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
