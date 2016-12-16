package in.zollet.abhilash.imagedownloader;

/**
 * Created by Abhilash on 12/4/2016.
 */
public class ListenerHandlers {

    public interface ItemClickHandler {

        void onItemDownloadClicked(ImageRealm imageRealm);
        void onItemImageClicked(ImageRealm imageRealm);
        void onItemShareClicked(ImageRealm imageRealm);

    }

}