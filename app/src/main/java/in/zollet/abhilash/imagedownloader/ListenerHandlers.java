package in.zollet.abhilash.imagedownloader;


public class ListenerHandlers {

    public interface ItemClickHandler {
        void onItemDownloadClicked(ImageRealm imageRealm);
        void onItemImageClicked(ImageRealm imageRealm);
        void onItemShareClicked(ImageRealm imageRealm);
        void onItemResumeClicked(ImageRealm imageRealm);
        void onItemPauseClicked (ImageRealm imageRealm);
    }
}