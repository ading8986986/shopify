package ding.ting.shopify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import ding.ting.shopify.R;
import ding.ting.shopify.cache.ImageCacheManager;
import ding.ting.shopify.feeds.CollectionFeed;


import java.util.ArrayList;


/**
 * Created by duqian on 10/15/2014.
 */
public class CollectionListAdapter extends BaseAdapter{
    Context mContext;
    private LayoutInflater inflater;
    private ArrayList<CollectionFeed> CollectionFeeds;
    private View mLoadingView;

    public CollectionListAdapter(Context context){
        mContext=context;
        inflater=LayoutInflater.from(context);
        CollectionFeeds=new ArrayList<CollectionFeed>();
//        this.mLoadingView=loadingView;
    }

    @Override
    public int getCount() {
        return CollectionFeeds==null?0:CollectionFeeds.size();
    }

    @Override
    public CollectionFeed getItem(int position) {
        return CollectionFeeds.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView==null){
            viewHolder=new ViewHolder();
            convertView=inflater.inflate(R.layout.collection_item_collection,null);
            viewHolder.name=(TextView)convertView.findViewById(R.id.tv_title);
            viewHolder.updateDate=(TextView)convertView.findViewById(R.id.tv_update_date);
            viewHolder.image=(NetworkImageView)convertView.findViewById(R.id.image_collection);
            convertView.setTag(viewHolder);

        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.name.setText(CollectionFeeds.get(position).name);
        viewHolder.updateDate.setText("Last Update:  "+ CollectionFeeds.get(position).updateDate);
        viewHolder.image.setVisibility(View.VISIBLE);
        viewHolder.image.setImageUrl(CollectionFeeds.get(position).imageUrl, ImageCacheManager.getInstance(mContext).getImageLoader());
        return convertView;
    }
    public void setCollectionFeeds(ArrayList<CollectionFeed> CollectionFeeds) {
        this.CollectionFeeds = CollectionFeeds;
    }
    private class ViewHolder{
        private TextView name;
        private TextView updateDate;
        private NetworkImageView image;
    }
}
