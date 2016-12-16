package in.zollet.abhilash.downloadhunter;




public interface DownloadStatusListener {

    void onDownloadComplete(DownloadRequest request, Downloader.DownloadResponse response);

    void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage);

    void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress);
}