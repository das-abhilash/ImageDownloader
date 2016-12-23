package in.zollet.abhilash.downloadhunter;


import android.net.Uri;

import java.util.HashMap;
import java.util.concurrent.Future;

public class DownloadRequest {

    private boolean mCancelled;

    public enum Priority {
        LOW,
        NORMAL,
        HIGH,
        IMMEDIATE
    }

    private int mDownloadState;

    private Future future;

    private int mDownloadId;

    private Uri mUri;

    private Uri mDestinationURI;

    private long totalBytesRead = 0;


    private DownloadStatusListener mDownloadStatusListener;

    private HashMap<String, String> header;
    private Priority mPriority = Priority.NORMAL;

    public DownloadRequest(Uri uri) {
        if (uri == null) {
            throw new NullPointerException();
        }

        String scheme = uri.getScheme();
        if (scheme == null || (!scheme.equals("http") && !scheme.equals("https"))) {
            throw new IllegalArgumentException("Can only download HTTP/HTTPS URIs: " + uri);
        }
        header = new HashMap<>();
        mDownloadState = DownloadManager.STATUS_PENDING;
        mUri = uri;
    }
    private RetryPolicy mRetryPolicy;


    public DownloadRequest(String uri) {
        this(Uri.parse(uri));
    }

    public Uri getDestinationURI() {

        return mDestinationURI;
    }

    public RetryPolicy getRetryPolicy() {
        return mRetryPolicy == null ? new DefaultRetryPolicy() : mRetryPolicy;
    }

    public DownloadRequest setRetryPolicy(RetryPolicy mRetryPolicy) {
        this.mRetryPolicy = mRetryPolicy;
        return this;
    }

    public DownloadRequest setDestinationURI(Uri destinationURI) {
        this.mDestinationURI = destinationURI;
        return this;
    }

    public long getTotalBytesRead() {
        return totalBytesRead;
    }

    public void setTotalBytesRead(long totalBytesRead) {
        this.totalBytesRead = totalBytesRead;
    }

    public DownloadRequest setPriority(Priority priority) {
        mPriority = priority;
        return this;
    }

    public DownloadRequest addheader(String key, String value) {
        header.put(key, value);
        return this;
    }

    public void setFuture(Future future) {
        this.future = future;
    }

    public Future getFuture() {
        return future;
    }

    public final int getDownloadId() {
        return mDownloadId;
    }

    final void setDownloadId(int downloadId) {
        mDownloadId = downloadId;
    }

    int getDownloadState() {
        return mDownloadState;
    }

    public void setDownloadState(int mDownloadState) {
        this.mDownloadState = mDownloadState;
    }

    DownloadStatusListener getStatusListener() {
        return mDownloadStatusListener;
    }

    public DownloadRequest setStatusListener(DownloadStatusListener downloadStatusListener) {
        mDownloadStatusListener = downloadStatusListener;
        return this;
    }

    public Uri getUri() {
        return mUri;
    }

    public DownloadRequest setUri(Uri mUri) {
        this.mUri = mUri;
        return this;
    }

    public boolean pause() {
        return cancel();
    }

    public boolean cancel() {
        mCancelled = true;
        if(future != null) {
            future.cancel(true);
            return true;
        }
        return false;
    }

    public boolean resume() {
        mCancelled = true;
        new Thread() {
            public void run() {
                synchronized(this){
                    notifyAll();
                }
            }
        }.start();
        return true;
    }

    public boolean isCancelled() {
        return mCancelled;
    }

    public HashMap<String, String> getheaders() {
        return header;
    }

    void quit() {
       future.cancel(true);
    }
}