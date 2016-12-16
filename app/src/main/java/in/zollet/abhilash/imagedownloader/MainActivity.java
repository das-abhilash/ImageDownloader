package in.zollet.abhilash.imagedownloader;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import in.zollet.abhilash.downloadhunter.DefaultRetryPolicy;
import in.zollet.abhilash.downloadhunter.DownloadHunter;
import in.zollet.abhilash.downloadhunter.DownloadRequest;
import in.zollet.abhilash.downloadhunter.DownloadStatusListener;
import in.zollet.abhilash.downloadhunter.Downloader;
import in.zollet.abhilash.downloadhunter.RetryPolicy;
import in.zollet.abhilash.imagedownloader.databinding.ActivityMainBinding;
import okio.BufferedSink;
import okio.Okio;
import rx.Observable;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener,DownloadStatusListener {


    Observable button ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel main = new MainViewModel(this,clickHandler);
        binding.setMain(main);

        setSupportActionBar(binding.toolbar);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
        /*button.count()
                .map(new Func1<Integer, Integer>() {
                    private int counter; // Or AtomicInteger if needed.
                    @Override
                    public Integer call(Integer s) {
                        return new Integer(counter++);
                    }
                })
                //.map(Integer.Parse(count) -> count%3)
                .subscribe(count -> Toast.makeText(this, String.valueOf(count), Toast.LENGTH_SHORT).show());*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ListenerHandlers.ItemClickHandler clickHandler = new ListenerHandlers.ItemClickHandler(){

        DownloadHunter downloadHunter = new DownloadHunter();
        DownloadRequest downloadRequest;
        int requestId;

    @Override
    public void onItemDownloadClicked(ImageRealm imageRealm) {


        if(imageRealm.getIsDownloadStarted())
            downloadHunter.cancel(requestId);
        else {
            //downloadHunter.cancel(requestId);
            downloadRequest = createDownloadRequest(downloadHunter,downloadRequest,imageRealm);
            requestId = downloadHunter.add(downloadRequest);
        }
    }

    @Override
    public void onItemImageClicked(ImageRealm imageRealm) {
      /*  try {
            BufferedSink sink1 = Okio.buffer(Okio.sink(new File(getCacheDir()+getLastBitFromUrl(imageRealm.getUrl()))));
            sink1.writeAll(responseBody.source());
            sink1.close();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        Intent intent = new Intent(getBaseContext(),DetailActivity.class);
        String path = getCacheDir()+getLastBitFromUrl(imageRealm.getUrl() );
        intent.putExtra(DetailActivity.ARG_IMAGE, path);
        startActivity(intent);

    }

    @Override
    public void onItemShareClicked(ImageRealm imageRealm) {
        final Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/jpg");
        final File photoFile = new File(getCacheDir()+getLastBitFromUrl(imageRealm.getUrl()));
        shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
        startActivity(Intent.createChooser(shareIntent, "Share image using"));
    }
    };

    private DownloadRequest createDownloadRequest(DownloadHunter downloadHunter, DownloadRequest downloadRequest, ImageRealm imageRealm) {
        Uri destinationUri = Uri.parse(getCacheDir()+getLastBitFromUrl(imageRealm.getUrl()));
        RetryPolicy retryPolicy = new DefaultRetryPolicy();
        return new DownloadRequest(imageRealm.getUrl())
                 .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(retryPolicy)
                //.setDownloadContext("Download1")
                .setStatusListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(DownloadRequest request, Downloader.DownloadResponse response) {
                        new Thread() {
                            public void run() {
                                InputStream in = response.getInputStream();
                                Bitmap myBitmap = BitmapFactory.decodeStream(in);

                                try {
                                    BufferedSink sink1 = Okio.buffer(Okio.sink(new File(String.valueOf(destinationUri))));
                                    sink1.writeAll(response.getResponseBody().source());
                                    sink1.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                runOnUiThread(() -> {
                                    imageRealm.setDrawable(new BitmapDrawable(getResources(), myBitmap));
                                    imageRealm.setDownloadState(true,false,false);
                                });
                            }
                        }.start();
                    }
                    @Override
                    public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                        imageRealm.setDownloadState(false,false,true);
                        imageRealm.setProgress(0);
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                        imageRealm.setProgress(progress);
                        imageRealm.setDownloadState(false,true,false);
                    }
                });
    }

    private String getLastBitFromUrl(final String url){
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    @Override
    public void onDownloadComplete(DownloadRequest request, Downloader.DownloadResponse response) {
        Toast.makeText(this, "complete", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
        Toast.makeText(this, "failed : " + errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
        Toast.makeText(this, "progress", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_start_all:


                return true;

            case R.id.action_cancel_all:

                return true;
            case R.id.action_delete_all:

                return true;
        }
        return false;
    }
}
