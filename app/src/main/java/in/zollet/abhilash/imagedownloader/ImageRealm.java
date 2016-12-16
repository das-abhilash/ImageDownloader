package in.zollet.abhilash.imagedownloader;

import android.content.Context;
import android.databinding.Bindable;
import android.databinding.BindingAdapter;
import android.databinding.Observable;
import android.databinding.PropertyChangeRegistry;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.realm.RealmObject;
import io.realm.annotations.Ignore;
import io.realm.annotations.PrimaryKey;

/*@Parcel(implementations = { ImageRealmRealmProxy.class },
        value = Parcel.Serialization.BEAN, analyze = { ImageRealm.class })*/
public class ImageRealm extends RealmObject implements Observable {

    @PrimaryKey
    private String url;
    @Ignore
    private int progress;
    @Ignore
    private boolean isDownloadComplete;
    @Ignore
    private
    boolean isDownloadStarted = false;
    @Ignore
    private
    boolean isDownloadFailed;
    @Ignore
    private int downloadIcon;
@Ignore
    private Drawable drawable;
    @Ignore
    private String path;

    @Bindable
    public Drawable getDrawable() {
        return drawable;
    }

    public void setDrawable(Drawable drawable) {
        this.drawable = drawable;
        notifyPropertyChanged(BR.drawable);
    }

    @Bindable
    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
        notifyPropertyChanged(BR.path);
    }

    public ImageRealm() {
    }

    public ImageRealm(String url) {
        this.url = url;
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
    public boolean getIsDownloadFailed() {
        return isDownloadFailed;
    }


    @BindingAdapter({"bind:imageDrawble"})
    public static void loadImage(ImageView view,Drawable  drawable) {
        view.setImageDrawable(drawable);
    }

    public void setDownloadState(boolean downloadComplete, boolean downloadStarted, boolean downloadFailed) {
        isDownloadComplete = downloadComplete;
        isDownloadStarted = downloadStarted;
        isDownloadFailed = downloadFailed;
        notifyPropertyChanged(BR.isDownloadComplete);
        notifyPropertyChanged(BR.isDownloadStarted);
        notifyPropertyChanged(BR.isDownloadFailed);
        if(isDownloadComplete)
            setDownloadIcon(R.drawable.ic_check_circle_black_24dp);
        else if(isDownloadStarted)
            setDownloadIcon(R.drawable.ic_clear_black_24dp);
        else if(isDownloadFailed)
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
