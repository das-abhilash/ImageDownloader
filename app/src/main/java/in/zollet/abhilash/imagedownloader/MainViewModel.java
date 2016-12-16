package in.zollet.abhilash.imagedownloader;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;


public class MainViewModel extends BaseObservable {

    public final RecyclerConfiguration recyclerConfiguration = new RecyclerConfiguration();
    private Context context;
    private ListenerHandlers.ItemClickHandler itemClickHandler;

    MainViewModel(Context context, ListenerHandlers.ItemClickHandler itemClickHandler) {

        this.context = context;
        this.itemClickHandler = itemClickHandler;
        initRecycler();
    }

    private void initRecycler() {
        recyclerConfiguration.setLayoutManager(new LinearLayoutManager(context));
        recyclerConfiguration.setItemAnimator(new DefaultItemAnimator());
        recyclerConfiguration.setAdapter(new
                RecyclerBindingAdapter(context, ImageRealmController.getInstance().orderedRealmCollection(),itemClickHandler ));
    }
}
