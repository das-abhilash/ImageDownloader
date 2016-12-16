package in.zollet.abhilash.imagedownloader;


import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmResults;

 class ImageRealmController {

    private static ImageRealmController instance;
    private final Realm realm ;

    private ImageRealmController() {
        realm = Realm.getDefaultInstance();
    }


    public static ImageRealmController getInstance() {
        if (instance == null) {
            instance = new ImageRealmController();
        }
        return instance;
    }

    public Realm getRealm() {
        return realm;
    }

     void saveImageRealmToDB(ImageRealm imageRealm){
         realm.executeTransactionAsync( realm -> realm.copyToRealmOrUpdate(imageRealm));
     }

    public RealmResults<ImageRealm> queriedGeoFence() {
        return realm.where(ImageRealm.class).findAll();
    }

    public OrderedRealmCollection<ImageRealm> orderedRealmCollection (){
        return realm.where(ImageRealm.class).findAllAsync();
    }

    public ImageRealm queriedRealmGeoFence( String url) {
        return realm.where(ImageRealm.class).equalTo("url",url).findFirst();
    }

}
