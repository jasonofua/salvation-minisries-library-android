package primedsoft.com.salvation.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import ir.mirrajabi.searchdialog.StringsHelper;
import ir.mirrajabi.searchdialog.core.BaseSearchDialogCompat;
import ir.mirrajabi.searchdialog.core.SearchResultListener;
import ir.mirrajabi.searchdialog.core.Searchable;
import primedsoft.com.salvation.Model.MessageModel;
import primedsoft.com.salvation.R;

public class MessageSearchAdapter<T extends Searchable> extends RecyclerView.Adapter<MessageSearchAdapter.ViewHolder>{
    protected Context mContext;
    private List<T> mItems = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private int mLayout;
    private SearchResultListener mSearchResultListener;
    private AdapterViewBinder<T> mViewBinder;
    private String mSearchTag;
    private boolean mHighlightPartsInCommon = true;
    private String mHighlightColor = "#FFED2E47";
    private BaseSearchDialogCompat mSearchDialog;

    public MessageSearchAdapter(Context context, @LayoutRes int layout, List<T> items) {
        this(context, layout, null, items);
    }

    public MessageSearchAdapter(
            Context context, AdapterViewBinder<T> viewBinder,
            @LayoutRes int layout, List<T> items
    ) {
        this(context, layout, viewBinder, items);
    }

    public MessageSearchAdapter(
            Context context, @LayoutRes int layout,
            @Nullable AdapterViewBinder<T> viewBinder,
            List<T> items
    ) {
        this.mContext = context;
        this.mLayoutInflater = LayoutInflater.from(context);
        this.mItems = items;
        this.mLayout = layout;
        this.mViewBinder = viewBinder;
    }

    public MessageSearchAdapter<T> setViewBinder(AdapterViewBinder<T> viewBinder) {
        this.mViewBinder = viewBinder;
        return this;
    }

    @NonNull
    @Override
    public MessageSearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View convertView = mLayoutInflater.inflate(mLayout, parent, false);
        convertView.setTag(new ViewHolder(convertView));
        ViewHolder viewHolder = (ViewHolder) convertView.getTag();
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MessageSearchAdapter.ViewHolder holder, int position) {
        initializeViews(getItem(position), holder, position);

    }

    public List<T> getItems() {
        return mItems;
    }

    public void setItems(List<T> objects) {
        this.mItems = objects;
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void initializeViews(
            final T object, final MessageSearchAdapter.ViewHolder holder,
            final int position
    ) {
        if (mViewBinder != null) {
            mViewBinder.bind(holder, object, position);
        }
        LinearLayout root = holder.getViewById(R.id.root);
        TextView text = holder.getViewById(R.id.txtMessageNameSearch);
        final ImageView image = holder.getViewById(R.id.imageThumbSearch);

        Glide.with(mContext)
                .load(((MessageModel) object).getImageUrl())
                .asBitmap()
                .into(image);

        if (mSearchTag != null && mHighlightPartsInCommon) {
            text.setText(StringsHelper.highlightLCS(object.getTitle(), getSearchTag(),
                    Color.parseColor(mHighlightColor)
            ));
        } else {
            text.setText(object.getTitle());
        }

        if (mSearchResultListener != null) {
            holder.getBaseView().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mSearchResultListener.onSelected(mSearchDialog, object, position);
                }
            });
        }
    }


    public SearchResultListener getSearchResultListener() {
        return mSearchResultListener;
    }

    public void setSearchResultListener(SearchResultListener searchResultListener) {
        this.mSearchResultListener = searchResultListener;
    }

    public String getSearchTag() {
        return mSearchTag;
    }

    public MessageSearchAdapter setSearchTag(String searchTag) {
        mSearchTag = searchTag;
        return this;
    }

    public boolean isHighlightPartsInCommon() {
        return mHighlightPartsInCommon;
    }

    public MessageSearchAdapter setHighlightPartsInCommon(boolean highlightPartsInCommon) {
        mHighlightPartsInCommon = highlightPartsInCommon;
        return this;
    }

    public MessageSearchAdapter setHighlightColor(String highlightColor) {
        mHighlightColor = highlightColor;
        return this;
    }

    public MessageSearchAdapter setSearchDialog(BaseSearchDialogCompat searchDialog) {
        mSearchDialog = searchDialog;
        return this;
    }

    public interface AdapterViewBinder<T> {
        void bind(ViewHolder holder, T item, int position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private View mBaseView;

        public ViewHolder(View itemView) {
            super(itemView);
            mBaseView = itemView;
        }
        public View getBaseView() {
            return mBaseView;
        }

        public <T> T getViewById(@IdRes int id) {
            return (T) mBaseView.findViewById(id);
        }

        public void clearAnimation(@IdRes int id) {
            mBaseView.findViewById(id).clearAnimation();
        }
    }
}
