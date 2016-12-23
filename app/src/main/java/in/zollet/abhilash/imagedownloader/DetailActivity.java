package in.zollet.abhilash.imagedownloader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

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
        String  destinationUri = extras.getString(ARG_IMAGE);

        Bitmap bitmap1 = BitmapFactory.decodeFile(destinationUri);
        if(bitmap1 != null)
        iv.setImageBitmap(bitmap1);
        else
        iv.setImageResource(R.drawable.placeholder);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }




}
