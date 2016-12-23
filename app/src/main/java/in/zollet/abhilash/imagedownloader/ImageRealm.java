package in.zollet.abhilash.imagedownloader;

import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

public class ImageRealm extends RealmObject implements Observable {

    @PrimaryKey
    private String url;
    @Ignore
    String requestId;
    @Ignore
    private int progress;
    @Ignore
    private boolean isDownloadComplete;
    @Ignore
    private boolean isDownloadStarted = false;
    @Ignore
    private boolean isDownloadFailed;
    @Ignore
    private boolean isFileSaved = false;
    @Ignore
    private int downloadIcon;
    @Ignore
    private String path;
    @Ignore
    private String placeholderPath;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }



    @Bindable
    public String getPlaceholderPath() {
        return placeholderPath;
    }

    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String filePath) {
        this.path = filePath + getLastBitFromUrl(url);
        this.placeholderPath = filePath + "placeholder.PNG";
        notifyPropertyChanged(BR.path);
        notifyPropertyChanged(BR.placeholderPath);
    }

    public ImageRealm() {
    }

    public ImageRealm(String url) {
        this.url = url;
    }

    public static String getLastBitFromUrl(final String url) {
        return url.replaceFirst(".*/([^/?]+).*", "$1");
    }

    @Bindable
    public String getUrl() {
        return url;
    }

    @Bindable
    public boolean getIsDownloadComplete() {
        return isDownloadComplete;
    }

    @Bindable
    public int getProgress() {
        return progress;
    }

    @Bindable
    public boolean getIsDownloadStarted() {
        return isDownloadStarted;
    }

    @Bindable
    public boolean getIsFileSaved() {
        return isFileSaved;
    }

    @Bindable
    public boolean getIsDownloadFailed() {
        return isDownloadFailed;
    }


    @BindingAdapter({"bind:imageDrawble", "bind:error"})
    public static void loadImage(ImageView view, String path, String error) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap1 = BitmapFactory.decodeFile(path, options);
        if (bitmap1 == null) {
            view.setImageDrawable(view.getContext().getResources().getDrawable(R.drawable.placeholder));
            return;
        }
        view.setImageBitmap(bitmap1);
    }

    public void setDownloadState(boolean downloadComplete, boolean downloadStarted, boolean downloadFailed, boolean fileSaved) {
        isDownloadComplete = downloadComplete;
        isDownloadStarted = downloadStarted;
        isDownloadFailed = downloadFailed;
        isFileSaved = fileSaved;
        notifyPropertyChanged(BR.isDownloadComplete);
        notifyPropertyChanged(BR.isFileSaved);
        notifyPropertyChanged(BR.isDownloadStarted);
        notifyPropertyChanged(BR.isDownloadFailed);

        if (isFileSaved) {
            notifyPropertyChanged(BR.path);
        } else if (isDownloadComplete) {
            setDownloadIcon(R.drawable.ic_check_circle_black_24dp);
        } else if (isDownloadStarted)
            setDownloadIcon(R.drawable.ic_clear_black_24dp);
        else if (isDownloadFailed)
            setDownloadIcon(R.drawable.ic_file_download_black_24dp);
        else
            setDownloadIcon(R.drawable.ic_file_download_black_24dp);

    }

    @Bindable
    public int getDownloadIcon() {
        return downloadIcon;
    }

    public void setDownloadIcon(int downloadIcon) {
        this.downloadIcon = downloadIcon;
        notifyPropertyChanged(BR.downloadIcon);
    }

    public void setDownloadStarted(boolean downloadStarted) {
        isDownloadStarted = downloadStarted;
    }

    public void setProgress(int progress) {
        this.progress = progress;
        notifyPropertyChanged(BR.progress);

    }

    public void setUrl(String url) {
        this.url = url;
    }

    // BaseObservable implements Observable. So  RealmGeoFence is made to implement all the contents of BaseObservable
    @Ignore
    private transient PropertyChangeRegistry mCallbacks;

    // from observable interface
    @Override
    public synchronized void addOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (mCallbacks == null) {
            mCallbacks = new PropertyChangeRegistry();
        }
        mCallbacks.add(callback);
    }

    // from observable interface
    @Override
    public synchronized void removeOnPropertyChangedCallback(OnPropertyChangedCallback callback) {
        if (mCallbacks != null) {
            mCallbacks.remove(callback);
        }
    }

    /**
     * Notifies listeners that all properties of this instance have changed.
     */
    public synchronized void notifyChange() {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, 0, null);
        }
    }

    /**
     * Notifies listeners that a specific property has changed. The getter for the property
     * that changes should be marked with {@link Bindable} to generate a field in
     * <code>BR</code> to be used as <code>fieldId</code>.
     *
     * @param fieldId The generated BR id for the Bindable field.
     */
    public void notifyPropertyChanged(int fieldId) {
        if (mCallbacks != null) {
            mCallbacks.notifyCallbacks(this, fieldId, null);
        }
    }
}
