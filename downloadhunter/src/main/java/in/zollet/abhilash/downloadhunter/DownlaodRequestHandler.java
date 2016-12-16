package in.zollet.abhilash.downloadhunter;

import android.os.Handler;
import android.os.Looper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Abhilash on 12/16/2016.
 */

public class DownlaodRequestHandler {

    private AtomicInteger mSequenceGenerator = new AtomicInteger();

    private Map<Integer, DownloadRequest> mCurrentRequests = new HashMap<Integer, DownloadRequest>();

    HunterExecutorSupplier executorSupplier;

    DownloadHunterWorker myDownloadWorker;

    public DownlaodRequestHandler(){
        executorSupplier = HunterExecutorSupplier.getInstance();
        myDownloadWorker = new DownloadHunterWorker(new DownloadHunterCallBack(new Handler(Looper.getMainLooper())));
    }
    public int add(final DownloadRequest request){
        final int downloadId = getDownloadId();

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

    private int getDownloadId() {
        return mSequenceGenerator.incrementAndGet();
    }

    public boolean cancel(int downloadId) {

        DownloadRequest request = mCurrentRequests.get(downloadId);
        return request.cancel();
    }

    public void cancelAll(){

    }
}
