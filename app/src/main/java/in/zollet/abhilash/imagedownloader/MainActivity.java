package in.zollet.abhilash.imagedownloader;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.File;

import in.zollet.abhilash.downloadhunter.DefaultRetryPolicy;
import in.zollet.abhilash.downloadhunter.DownloadHunter;
import in.zollet.abhilash.downloadhunter.DownloadRequest;
import in.zollet.abhilash.downloadhunter.DownloadStatusListener;
import in.zollet.abhilash.downloadhunter.Downloader;
import in.zollet.abhilash.downloadhunter.RetryPolicy;
import in.zollet.abhilash.imagedownloader.databinding.ActivityMainBinding;
import rx.Observable;

public class MainActivity extends AppCompatActivity implements
        BottomNavigationView.OnNavigationItemSelectedListener {


    Observable button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MainViewModel main = new MainViewModel(this, clickHandler);
        binding.setMain(main);

        setSupportActionBar(binding.toolbar);
        binding.bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }

    ListenerHandlers.ItemClickHandler clickHandler = new ListenerHandlers.ItemClickHandler() {

        DownloadHunter downloadHunter = new DownloadHunter();
        DownloadRequest downloadRequest;

        @Override
        public void onItemDownloadClicked(ImageRealm imageRealm) {

            if (imageRealm.getIsDownloadStarted())
                downloadHunter.cancel(imageRealm.getRequestId());
            else {
                downloadRequest = createDownloadRequest(imageRealm);
                String requestId = downloadHunter.add(downloadRequest);
                imageRealm.setRequestId(requestId);
            }
        }

        @Override
        public void onItemResumeClicked(ImageRealm imageRealm) {
            String requestId = downloadHunter.resume(imageRealm.getRequestId());
            imageRealm.setRequestId(requestId);
            imageRealm.setDownloadState(false, true, false, false);
        }

        @Override
        public void onItemPauseClicked(ImageRealm imageRealm) {
            downloadHunter.pause(imageRealm.getRequestId());
        }

        @Override
        public void onItemShareClicked(ImageRealm imageRealm) {
            final Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("image/jpg");
            final File photoFile = new File(getCacheDir() + getLastBitFromUrl(imageRealm.getUrl()));
            shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(photoFile));
            startActivity(Intent.createChooser(shareIntent, "Share image using"));
        }

        @Override
        public void onItemImageClicked(ImageRealm imageRealm) {
            Intent intent = new Intent(getBaseContext(), DetailActivity.class);
            String path = getCacheDir() + getLastBitFromUrl(imageRealm.getUrl());
            intent.putExtra(DetailActivity.ARG_IMAGE, path);
            startActivity(intent);

        }
    };

    private DownloadRequest createDownloadRequest(ImageRealm imageRealm) {
        Uri destinationUri = Uri.parse(getCacheDir() + getLastBitFromUrl(imageRealm.getUrl()));
        RetryPolicy retryPolicy = new DefaultRetryPolicy();
        return new DownloadRequest(imageRealm.getUrl())
                .setDestinationURI(destinationUri).setPriority(DownloadRequest.Priority.LOW)
                .setRetryPolicy(retryPolicy)
                .setStatusListener(new DownloadStatusListener() {
                    @Override
                    public void onDownloadComplete(DownloadRequest request, Downloader.DownloadResponse response, String msg) {
                        if (msg.equals("Downloaded")) {
                            Toast.makeText(MainActivity.this, "File Downloaded successfully", Toast.LENGTH_SHORT).show();
                            imageRealm.setDownloadState(true, false, false, false);
                        } else if (msg.equals("Saved")) {
                            Toast.makeText(MainActivity.this, "File Saved successfully", Toast.LENGTH_SHORT).show();
                            imageRealm.setDownloadState(true, false, false, true);
                        }
                    }

                    @Override
                    public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                        Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                        imageRealm.setProgress(progress);
                        imageRealm.setDownloadState(false, true, false, false);
                    }

                    @Override
                    public void onPause(DownloadRequest request, long mContentLength, long downloadedBytes, int progress) {
                        imageRealm.setDownloadState(false, false, true, false);
                        imageRealm.setProgress(progress);
                        Toast.makeText(MainActivity.this, "Download Canceled", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private String getLastBitFromUrl(final String url) {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
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
