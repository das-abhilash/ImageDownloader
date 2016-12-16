package in.zollet.abhilash.downloadhunter;


public interface RetryPolicy {

    /**
     * Returns the current timeout (used for logging).
     */
    public int getCurrentTimeout();
    public int getReadTimeOut();

}