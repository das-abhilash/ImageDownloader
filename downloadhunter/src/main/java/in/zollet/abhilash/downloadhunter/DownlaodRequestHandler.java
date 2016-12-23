package in.zollet.abhilash.downloadhunter;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Future;


public class DownlaodRequestHandler {

    private Map<String, DownloadRequest> mCurrentRequests = new HashMap<String, DownloadRequest>();

    private HunterExecutorSupplier executorSupplier;

    private DownloadHunterWorker myDownloadWorker;

    public DownlaodRequestHandler(){
        executorSupplier = HunterExecutorSupplier.getInstance();
        myDownloadWorker = new DownloadHunterWorker(new DownloadHunterCallBack(new Handler(Looper.getMainLooper())));
    }
    public String add(final DownloadRequest request){
        final String downloadId = UUID.randomUUID().toString();

         Future future = executorSupplier.forHunterExecutor().submit(new Runnable() {
            @Override
            public void run() {
                myDownloadWorker.load(1, request);
            }
        }); /*() -> myDownloadWorker.load(1, request)*/
        request.setFuture(future);
        mCurrentRequests.put(downloadId, request);
        return downloadId;
    }

    public boolean cancel(String downloadId) {
        DownloadRequest request = mCurrentRequests.get(downloadId);
        return request.cancel();
    }

    public String resume(String downloadId) {
        DownloadRequest request = mCurrentRequests.get(downloadId);
        return add(request);
    }

    public void cancelAll(){

    }

    public boolean pause(String requestId) {
        DownloadRequest request = mCurrentRequests.get(requestId);
        return request.pause();
    }

}
