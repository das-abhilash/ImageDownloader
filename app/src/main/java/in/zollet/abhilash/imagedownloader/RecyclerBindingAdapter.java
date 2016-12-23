package in.zollet.abhilash.imagedownloader;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import in.zollet.abhilash.imagedownloader.databinding.ListItemBinding;
import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;


public class RecyclerBindingAdapter extends RealmRecyclerViewAdapter<ImageRealm, RecyclerBindingAdapter.BindingHolder> {

    private Context context;
    ListenerHandlers.ItemClickHandler itemClickHandler;

    public RecyclerBindingAdapter(Context context, OrderedRealmCollection<ImageRealm> data, ListenerHandlers.ItemClickHandler itemClickHandler) {
        super(context, data, true);
        this.context = context;
        this.itemClickHandler = itemClickHandler;
    }

    @Override
    public RecyclerBindingAdapter.BindingHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ListItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_item,
                parent, false);
        return new BindingHolder(binding);
    }

    @Override
    public void onBindViewHolder(RecyclerBindingAdapter.BindingHolder holder, int position) {

        ImageRealm imageRealm = getData().get(position);
        imageRealm.setPath(String.valueOf(context.getCacheDir()));
        holder.getBinding().setImage(imageRealm);
        holder.getBinding().setClick(itemClickHandler);


        holder.getBinding().executePendingBindings();
    }

    @Override
    public int getItemCount() {
        return getData().size();
    }

    static class BindingHolder extends RecyclerView.ViewHolder {
        public BindingHolder(View itemView) {
            super(itemView);
        }

        private ListItemBinding mBinding;

        BindingHolder(ListItemBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public ListItemBinding getBinding() {
            return mBinding;
        }
    }
}
