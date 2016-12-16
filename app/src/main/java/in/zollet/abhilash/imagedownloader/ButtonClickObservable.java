package in.zollet.abhilash.imagedownloader;

import in.zollet.abhilash.downloadhunter.DownloadRequest;
import rx.Observable;
import rx.subjects.PublishSubject;

public class ButtonClickObservable {

    private static ButtonClickObservable instance;

    private PublishSubject<DownloadRequest> buttonClicks = PublishSubject.create();

    public static ButtonClickObservable getInstance() {
        if (instance == null) {
            instance = new ButtonClickObservable();
        }
        return instance;
    }

    /**
     * Pass a fragemnt down to event listeners.
     */
    public void setClick (DownloadRequest request) {
        buttonClicks.onNext(request);
    }

    /**
     * Subscribe to this Observable. On event, do something e.g. replace a fragment
     */
    public Observable<DownloadRequest> getEvents() {
        return buttonClicks;
    }
}
