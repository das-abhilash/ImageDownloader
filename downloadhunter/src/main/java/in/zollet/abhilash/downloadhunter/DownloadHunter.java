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
    public int add(final DownloadRequest request) throws IllegalArgumentException {

        if (request == null) {
            throw new IllegalArgumentException("DownloadRequest cannot be null");
        }
        return downlaodRequestHandler.add(request);

    }

    @Override
    public boolean cancel(int downloadId) {
       /* checkReleased("cancel(...) called on a released ThinDownloadManager.");
        return mRequestQueue.cancel(downloadId);*/
        return downlaodRequestHandler.cancel(downloadId);

    }



    @Override
    public void cancelAll() {
        downlaodRequestHandler.cancelAll();
    }

    @Override
    public int query(int downloadId) {
        checkReleased("query(...) called on a released ThinDownloadManager.");
        return /*mRequestQueue.query(downloadId)*/1;
    }

    @Override
    public void release() {
        if (!isReleased()) {
            // mRequestQueue.release();
            //mRequestQueue = null;
        }
    }

    @Override
    public boolean isReleased() {
        return false;
    }

   /* @Override
    public boolean isReleased() {
        return mRequestQueue == null;
    }*/

    private void checkReleased(String errorMessage) {
        if (isReleased()) {
            throw new IllegalStateException(errorMessage);
        }
    }
}
