package in.zollet.abhilash.downloadhunter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.InputStream;

import okhttp3.ResponseBody;

/**
 * Created by Abhilash on 12/16/2016.
 */

public interface Downloader {

    @Nullable
    void load(int networkPolicy, DownloadRequest request) ;

    void shutdown();

    class DownloadResponse {
        final InputStream stream;
        final long contentLength;
        ResponseBody responseBody;

        public DownloadResponse(@NonNull InputStream stream) {
            this.stream = stream;
            this.contentLength = -1;
        }

        public DownloadResponse(@NonNull ResponseBody responseBody) {
            this.responseBody = responseBody;
            this.stream = responseBody.byteStream();
            this.contentLength = -1;
        }

        public ResponseBody getResponseBody() {
            return responseBody;
        }

        public DownloadResponse(@NonNull InputStream stream, long contentLength) {
            if (stream == null) {
                throw new IllegalArgumentException("Stream may not be null.");
            }
            this.stream = stream;
            this.contentLength = contentLength;
        }

        @Nullable
        public InputStream getInputStream() {
            return stream;
        }
        public long getContentLength() {
            return contentLength;
        }
    }
}
