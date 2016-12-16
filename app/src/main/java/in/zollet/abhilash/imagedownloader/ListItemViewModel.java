package in.zollet.abhilash.imagedownloader;

import android.databinding.BaseObservable;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

/**
 * Created by Abhilash on 12/14/2016.
 */

public class ListItemViewModel extends BaseObservable {

    ObservableInt progress;
    ObservableBoolean isDownloadComplete;
    ObservableBoolean isDownloadStarted;

    public void setProgress(int progress) {
        this.progress.set(progress);
    }

    public void setIsDownloadComplete(boolean isDownloadComplete) {
        this.isDownloadComplete.set(isDownloadComplete);
    }

    public void setIsDownloadStarted(boolean isDownloadStarted) {
        this.isDownloadStarted.set(isDownloadStarted);
    }
}
