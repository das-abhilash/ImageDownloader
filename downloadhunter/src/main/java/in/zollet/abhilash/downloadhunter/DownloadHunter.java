package in.zollet.abhilash.downloadhunter;

/**
 * Created by Abhilash on 12/13/2016.
 */

public class DownloadHunter implements DownloadManager {

    DownlaodRequestHandler downlaodRequestHandler;

    public DownloadHunter() {
        downlaodRequestHandler = new DownlaodRequestHandler();
    }


    @Override
    public String add(final DownloadRequest request) throws IllegalArgumentException {

        if (request == null) {
            throw new IllegalArgumentException("DownloadRequest cannot be null");
        }
        return downlaodRequestHandler.add(request);

    }

    @Override
    public boolean cancel(String downloadId) {
        return downlaodRequestHandler.cancel(downloadId);
    }

    public String resume(String downloadId) {
        return downlaodRequestHandler.resume(downloadId);

    }
    @Override
    public void cancelAll() {
        downlaodRequestHandler.cancelAll();
    }

    @Override
    public boolean pause(String requestId) {
        return downlaodRequestHandler.pause(requestId);
    }
}
