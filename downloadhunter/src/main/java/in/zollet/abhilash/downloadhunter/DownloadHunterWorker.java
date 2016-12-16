package in.zollet.abhilash.downloadhunter;

import android.support.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
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

/**
 * Created by Abhilash on 12/16/2016.
 */

public class DownloadHunterWorker implements Downloader {

    private DownloadHunterCallBack hunterCallBack;

    Call mCall;

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
            /* try {
            BufferedSink sink1 = Okio.buffer(Okio.sink(new File(getCacheDir()+getLastBitFromUrl(imageRealm.getUrl()))));
            sink1.writeAll(responseBody.source());
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        } catch (IOException e) {
            e.printStackTrace();
            updateDownloadFailed(request, DownloadManager.STATUS_FAILED,"Unable to resolve host");
            return;
        }

        int responseCode = response.code();
        if (responseCode >= 300) {
            response.body().close();
            updateDownloadFailed(request, response.code(), response.message());
        }
        ResponseBody responseBody = response.body();
        updateDownloadComplete(request,new DownloadResponse(responseBody));

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
    public void quit() {
        if(mCall!= null) {
            mCall.cancel();
        }
    }

    private void updateDownloadComplete(DownloadRequest request , DownloadResponse response) {
        hunterCallBack.postDownloadComplete(request, response);
        request.setDownloadState(DownloadManager.STATUS_SUCCESSFUL);

    }

    private void updateDownloadFailed(DownloadRequest request, int errorCode, String errorMsg) {
        // shouldAllowRedirects = false;
        request.setDownloadState(DownloadManager.STATUS_FAILED);
        hunterCallBack.postDownloadFailed(request,errorCode, errorMsg);
    }

    private void updateDownloadProgress(DownloadRequest request, long mContentLength, int progress, long downloadedBytes) {
        hunterCallBack.postProgressUpdate(request, mContentLength, downloadedBytes, progress);
    }

    private class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        //private final ProgressListener progressListener;
        private BufferedSource bufferedSource;
        private DownloadRequest request;

        public ProgressResponseBody(ResponseBody responseBody, DownloadRequest request/*, ProgressListener progressListener*/) {
            this.responseBody = responseBody;
            this.request = request;
            //this.progressListener = progressListener;
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
                public long read(Buffer sink, long byteCount)/* throws IOException*/ {
                    long bytesRead = 0;
                    try {
                        bytesRead = super.read(sink, byteCount);
                    } catch (IOException e) {
                        e.printStackTrace();
                        updateDownloadFailed(request, DownloadManager.STATUS_FAILED,"Connection timed out");
                    }
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    int progress = (int) ((totalBytesRead * 100) / responseBody.contentLength());
                    updateDownloadProgress(request,responseBody.contentLength(), progress, totalBytesRead);
                    //progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }
}
