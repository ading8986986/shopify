package ding.ting.shopify.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

import ding.ting.shopify.R;
import ding.ting.shopify.cache.ImageCacheManager;
import ding.ting.shopify.feeds.CollectFeed;


/**
 * Created by duqian on 10/15/2014.
 */
public class CollectsListAdapter extends BaseAdapter{
    Context mContext;
    private LayoutInflater inflater;
    private ArrayList<CollectFeed> CollectsFeeds;
    private View mLoadingView;

    public CollectsListAdapter(Context context){
        mContext=context;
        inflater=LayoutInflater.from(context);
        CollectsFeeds=new ArrayList<CollectFeed>();
//        this.mLoadingView=loadingView;
    }

    @Override
    public int getCount() {
        return CollectsFeeds==null?0:CollectsFeeds.size();
    }

    @Override
    public CollectFeed getItem(int position) {
        return CollectsFeeds.get(position);
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
            convertView=inflater.inflate(R.layout.collect_item_collection,null);
            viewHolder.productName=(TextView)convertView.findViewById(R.id.tv_name);
            viewHolder.updateDate=(TextView)convertView.findViewById(R.id.tv_update_date);
            viewHolder.produceImage=(NetworkImageView)convertView.findViewById(R.id.image_product);
            viewHolder.inventory=(TextView)convertView.findViewById(R.id.tv_inventory);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(ViewHolder)convertView.getTag();
        }
        viewHolder.productName.setText(CollectsFeeds.get(position).productName);
        viewHolder.updateDate.setText("Last Update:  "+CollectsFeeds.get(position).updateDate);
        viewHolder.inventory.setText("Total Inventory:   "+CollectsFeeds.get(position).inventory);
        viewHolder.produceImage.setVisibility(View.VISIBLE);
        viewHolder.produceImage.setImageUrl(CollectsFeeds.get(position).productImage, ImageCacheManager.getInstance(mContext).getImageLoader());
        return convertView;
    }

    public void setCollectsFeeds(ArrayList<CollectFeed> CollectsFeeds) {
        this.CollectsFeeds = CollectsFeeds;
    }

    private class ViewHolder{
        private TextView productName;
        private TextView inventory;
        private TextView updateDate;
        private NetworkImageView produceImage;
    }
}
