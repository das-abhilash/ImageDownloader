package in.zollet.abhilash.downloadhunter;


import android.os.Handler;

import java.util.concurrent.Executor;

public class DownloadHunterCallBack {

    private final Executor mCallBackExecutor;
/*
    public DownloadHunterCallBack(final Handler handler) {
        mCallBackExecutor = (handler::post);
    }

    public void postDownloadComplete(final DownloadRequest request , final Downloader.DownloadResponse response) {
        mCallBackExecutor.execute( () -> {
                if (request.getStatusListener() != null) {
                    request.getStatusListener().onDownloadComplete(response);
                }
            });
    }

    public void postDownloadFailed(final DownloadRequest request, final int errorCode, final String errorMsg) {
        mCallBackExecutor.execute(() -> {
            if (request.getStatusListener() != null) {
                request.getStatusListener().onDownloadFailed(request, errorCode,errorMsg);
            }
        });
    }

    public void postProgressUpdate(final DownloadRequest request, final long totalBytes, final long downloadedBytes, final int progress) {
        mCallBackExecutor.execute( () -> {
            if (request.getStatusListener() != null) {
                request.getStatusListener().onProgress(request, totalBytes, downloadedBytes, progress);
            }
        });
    }*/

    public DownloadHunterCallBack (final Handler handler) {
        // Make an Executor that just wraps the handler.
        mCallBackExecutor = new Executor() {
            @Override
            public void execute(Runnable command) {
                handler.post(command);
            }
        };
    }

    public void postDownloadComplete(final DownloadRequest request , final Downloader.DownloadResponse response) {
        mCallBackExecutor.execute(new Runnable() {
            public void run() {
                if (request.getStatusListener() != null) {
                    request.getStatusListener().onDownloadComplete(request,response);
                }
            }
        });
    }

    public void postDownloadFailed(final DownloadRequest request, final int errorCode, final String errorMsg) {
        mCallBackExecutor.execute(new Runnable() {
            public void run() {
                if (request.getStatusListener() != null) {
                    request.getStatusListener().onDownloadFailed(request, errorCode,errorMsg);
                }
            }
        });
    }

    public void postProgressUpdate(final DownloadRequest request, final long totalBytes, final long downloadedBytes, final int progress) {
        mCallBackExecutor.execute(new Runnable() {
            public void run() {
                if (request.getStatusListener() != null) {
                    request.getStatusListener().onProgress(request, totalBytes, downloadedBytes, progress);
                }
            }
        });
    }
}