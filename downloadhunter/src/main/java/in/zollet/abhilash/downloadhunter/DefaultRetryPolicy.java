package in.zollet.abhilash.downloadhunter;



public class DefaultRetryPolicy implements RetryPolicy {

    /** The default socket timeout in milliseconds */
    public static final int DEFAULT_TIMEOUT_MS = 5000;
    public static final int DEFAULT_READTIMEOUT_MS = 5000;


    public DefaultRetryPolicy() {
        this(DEFAULT_TIMEOUT_MS, DEFAULT_READTIMEOUT_MS);
    }

    int mCurrentTimeoutMs;
    int mReadTImeout;

    public DefaultRetryPolicy(int initialTimeoutMs, int readtimeOut) {
        mCurrentTimeoutMs = initialTimeoutMs;
        mReadTImeout = readtimeOut;

    }


    @Override
    public int getCurrentTimeout() {
        return mCurrentTimeoutMs;
    }


    @Override
    public int getReadTimeOut() {
        return mReadTImeout;
    }



}
