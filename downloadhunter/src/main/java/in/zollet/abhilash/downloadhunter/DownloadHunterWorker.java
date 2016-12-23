package in.zollet.abhilash.downloadhunter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;


public class DownloadHunterWorker implements Downloader {

    private DownloadHunterCallBack hunterCallBack;

    public DownloadHunterWorker(DownloadHunterCallBack callBack) {
        hunterCallBack = callBack;
    }

    @Nullable
    @Override
    public void load(int networkPolicy, final DownloadRequest request)  {

        Request.Builder requestBuilder = new Request.Builder()
                .url(request.getUri().toString());
        HashMap<String, String> headers = request.getheaders();
        if (headers != null) {
            for (String headerName : headers.keySet()) {
                requestBuilder.addHeader(headerName, headers.get(headerName));
            }
        }
        final Request httpRequest = requestBuilder.build();

        final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(request.getRetryPolicy().getCurrentTimeout(), TimeUnit.MILLISECONDS)
                .readTimeout(request.getRetryPolicy().getReadTimeOut(), TimeUnit.MILLISECONDS)
                .addNetworkInterceptor( new Interceptor() {
                    @Override
                    public Response intercept(Interceptor.Chain chain)  {
                        okhttp3.Response originalResponse = null;
                        try {
                            originalResponse = chain.proceed(chain.request());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), request))
                                .build();
                    }}
                ).build();
        Response response = null;
        try {
            response =client.newCall(httpRequest).execute();
        } catch (IOException e) {
            e.printStackTrace();
            updateDownloadFailed(request, DownloadManager.STATUS_FAILED,"Unable to resolve host");
            return;
        }

        int responseCode = response.code();
        if (responseCode >= 300) {
            response.body().close();
            updateDownloadFailed(request, response.code(), response.message());
            return;
        }
        ResponseBody responseBody = response.body();

        Bitmap bmp = BitmapFactory.decodeStream(responseBody.byteStream());
        File file = new File(String.valueOf(request.getDestinationURI()));
        OutputStream outStream = null;
        try {
            outStream = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        boolean saved =bmp.compress(Bitmap.CompressFormat.PNG, 100, outStream);
        updateDownloadComplete(request,new DownloadResponse(responseBody),"Saved");


    }

    @Override
    public void shutdown() {

    }
    private void cleanupDestination(DownloadRequest request) {
        File destinationFile = new File(request.getDestinationURI().getPath());
        if (destinationFile.exists()) {
            destinationFile.delete();
        }
    }

    private void updateDownloadComplete(DownloadRequest request , DownloadResponse response, String msg) {
        hunterCallBack.postDownloadComplete(request, response, msg);
        request.setDownloadState(DownloadManager.STATUS_SUCCESSFUL);

    }

    private void updateDownloadFailed(DownloadRequest request, int errorCode, String errorMsg) {
        request.setDownloadState(DownloadManager.STATUS_FAILED);
        hunterCallBack.postDownloadFailed(request,errorCode, errorMsg);
    }

    private void updateDownloadPaused(DownloadRequest request, long mContentLength, int progress, long downloadedBytes) {
        request.setDownloadState(DownloadManager.STATUS_PAUSED);
        hunterCallBack.postDownloadPaused(request,mContentLength, downloadedBytes, progress);
    }

    private void updateDownloadProgress(DownloadRequest request, long mContentLength, int progress, long downloadedBytes) {
        hunterCallBack.postProgressUpdate(request, mContentLength, downloadedBytes, progress);
    }

    private class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private BufferedSource bufferedSource;
        private DownloadRequest request;

        public ProgressResponseBody(ResponseBody responseBody, DownloadRequest request/*, ProgressListener progressListener*/) {
            this.responseBody = responseBody;
            this.request = request;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;
                @Override
                public long read(Buffer sink, long byteCount) {
                    long bytesRead = 0;
                    try {
                        bytesRead = super.read(sink, byteCount);

                    } catch (IOException e) {
                        long downloadedBytes = totalBytesRead <= request.getTotalBytesRead() ? request.getTotalBytesRead() : totalBytesRead;
                        request.setTotalBytesRead(downloadedBytes);
                        updateDownloadPaused(request, responseBody.contentLength(), (int) ((downloadedBytes * 100) / responseBody.contentLength()), totalBytesRead);
                    }
                        totalBytesRead += bytesRead != -1 ? bytesRead : 0;


                    if(totalBytesRead <= request.getTotalBytesRead() )
                        return bytesRead;
                    int progress = (int) ((totalBytesRead * 100) / responseBody.contentLength());
                    updateDownloadProgress(request,responseBody.contentLength(), progress, totalBytesRead);

                    if(progress == 100) {
                        updateDownloadComplete(request, new DownloadResponse(responseBody),"Downloaded");
                    }
                    return bytesRead;
                }
            };
        }
    }
}
