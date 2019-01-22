package ding.ting.shopify.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ding.ting.shopify.R;
import ding.ting.shopify.adapters.CollectsListAdapter;
import ding.ting.shopify.cache.ImageCacheManager;
import ding.ting.shopify.feeds.CollectFeed;
import ding.ting.shopify.feeds.CollectionFeed;
import ding.ting.shopify.request.Constants;
import ding.ting.shopify.request.RequestManager;


public class CollectionDetailActivity extends Activity {

    private CollectsListAdapter collectsListAdapter;
    private ListView collectsList;
    private NetworkImageView collectionImage;
    private TextView collectionTitle;
    private TextView collectionDescrp;
    private TextView inventory;
    private CollectionFeed myCollection;
    public static final String TAG = "CollectionDetail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_detail);
        collectsList=(ListView)findViewById(R.id.lv_collects);
        collectionTitle = (TextView) findViewById(R.id.tv_name);
        collectionDescrp = (TextView) findViewById(R.id.tv_description);
        inventory = (TextView) findViewById(R.id.tv_inventory);
        collectionImage = (NetworkImageView)  findViewById(R.id.image_collection);
        collectsListAdapter=new CollectsListAdapter(this);

        collectsList.setAdapter(collectsListAdapter);
        initView();
        this.loadCollectsInfo(myCollection.id);

    }

    Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == 0){
                loadCollectsDetail((String) msg.obj);
            } else {
                ArrayList<CollectFeed> newFeeds = (ArrayList<CollectFeed>) msg.obj;
                collectsListAdapter.setCollectsFeeds(newFeeds);
                collectsListAdapter.notifyDataSetInvalidated();
            }
//            hotelListAdapter.notifyDataSetInvalidated();
        }
    };

    private void initView(){
        Bundle bundle = getIntent().getBundleExtra("info");
        myCollection = new CollectionFeed(bundle.getString("id"), bundle.getString("name"),"", bundle.getString("src"),bundle.getString("bodyHtml"));
        collectionImage.setImageUrl(myCollection.imageUrl, ImageCacheManager.getInstance(this).getImageLoader());
        collectionTitle.setText("Collection Name:   " + myCollection.name);
        collectionDescrp.setText(myCollection.bodyHtml);
        this.loadCollectsInfo(myCollection.id);
    }

    public void loadCollectsInfo(String collectionId) {

        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.COLLECTION_URL_PREFIX + collectionId + Constants.COLLECTION_URL_SUFFIX,null,
                loadCollectsInfoSucceed(), loadCollectsInfoFail()) {
        };

        RequestManager.getInstance(this)
                .addToRequestQueue(profileRequest, TAG);

    }

    public void loadCollectsDetail(String productId) {
        Log.i(TAG, "URL="+Constants.COLLECT_URL_PREFIX + productId + Constants.COLLECTION_URL_SUFFIX);

        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.COLLECT_URL_PREFIX + productId + Constants.COLLECTION_URL_SUFFIX,null,
                loadCollectsDetailSucceed(), loadCollectsDetailFail()) {
        };

        RequestManager.getInstance(this)
                .addToRequestQueue(profileRequest, TAG);

    }

    public Response.Listener<JSONObject> loadCollectsInfoSucceed() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "success:" + response);
                JSONArray list = response.optJSONArray("collects");
                StringBuffer sb = new StringBuffer();
                for(int i = 0; i < list.length();i++){
                    try {
                        JSONObject params = (JSONObject)list.get(i);
                        sb.append(params.getString("product_id") + ",");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                sb.deleteCharAt(sb.length()-1);
                Message msg = uiHandler.obtainMessage();
                msg.what = 0;
                msg.obj = sb.toString();
                msg.sendToTarget();
            }
        };
    }

    public Response.ErrorListener loadCollectsInfoFail() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        };

    }

    public Response.Listener<JSONObject> loadCollectsDetailSucceed() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "success:" + response);
                JSONArray list = response.optJSONArray("products");
                Message msg = uiHandler.obtainMessage();
                List<CollectFeed> CollectFeeds = new ArrayList<CollectFeed>();
                for(int i = 0; i < list.length();i++){
                    try {
                        JSONObject params = (JSONObject)list.get(i);
                        JSONArray options = params.getJSONArray("options");

                        JSONArray variants = params.getJSONArray("variants");
                        int inventory = 0;
                        for(int j = 0; j < variants.length();j++){
                            JSONObject obj = (JSONObject)variants.get(j);
                            inventory += obj.getInt("inventory_quantity");
                        }

                        CollectFeed feed = new CollectFeed();
                        feed.productName  = ((JSONObject)options.get(0)).getString("name");
                        feed.inventory = inventory;
                        feed.updateDate = params.getString("updated_at");
                        JSONObject image = params.getJSONObject("image");
                        feed.productImage = image.getString("src");
                        CollectFeeds.add(feed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                msg.obj = CollectFeeds;
                msg.what = 1;
                msg.sendToTarget();
            }
        };
    }

    public Response.ErrorListener loadCollectsDetailFail() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        };

    }
}
