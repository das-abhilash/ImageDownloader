package in.zollet.abhilash.downloadhunter;


import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class HunterExecutorSupplier {
    /*
    * Number of cores to decide the number of threads
    */
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    /*
    * thread pool executor for background tasks
    */
    private final ThreadPoolExecutor mHunterExecutorSupplier;

    private static HunterExecutorSupplier sInstance;

    private DownloadHunterCallBack mDelivery;
    /*
    * returns the instance of DefaultExecutorSupplier
    */
    public static HunterExecutorSupplier getInstance() {
        if (sInstance == null) {
            synchronized (HunterExecutorSupplier.class) {
                sInstance = new HunterExecutorSupplier();
            }
            }
            return sInstance;

    }

        private HunterExecutorSupplier() {

          /*  ThreadFactory backgroundPriorityThreadFactory = new
                    PriorityThreadFactory(Process.THREAD_PRIORITY_BACKGROUND);*/


            mHunterExecutorSupplier = new ThreadPoolExecutor(
                    NUMBER_OF_CORES * 2,
                    NUMBER_OF_CORES * 2,
                    60L,
                    TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>()
                   // ,backgroundPriorityThreadFactory
            );
        }


    public ThreadPoolExecutor forHunterExecutor() {
        return mHunterExecutorSupplier;
    }
}