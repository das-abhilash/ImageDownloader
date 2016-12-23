package in.zollet.abhilash.downloadhunter;




public interface DownloadStatusListener {

    void onDownloadComplete(DownloadRequest request, Downloader.DownloadResponse response , String  msg);

    void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage);

    void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress);

    void onPause(DownloadRequest request, long mContentLength, long downloadedBytes, int progress);
}