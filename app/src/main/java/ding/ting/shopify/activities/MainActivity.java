package ding.ting.shopify.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ding.ting.shopify.R;
import ding.ting.shopify.adapters.CollectionListAdapter;
import ding.ting.shopify.feeds.CollectionFeed;
import ding.ting.shopify.request.Constants;
import ding.ting.shopify.request.RequestManager;


public class MainActivity extends Activity implements AdapterView.OnItemClickListener {

    private CollectionListAdapter collectionListAdapter;
    private ListView collectionlList;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.i(TAG,"load");
        setContentView(R.layout.activity_main);
        collectionlList=(ListView)findViewById(R.id.lv_collection);
        collectionListAdapter=new CollectionListAdapter(this);
        this.loadUserInfo();

        collectionlList.setAdapter(collectionListAdapter);
        collectionlList.setOnItemClickListener(this);

    }

    Handler uiHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            mLoadingView.setVisibility(View.GONE);
//            hotelInfos=hotelPriceGrabber.getHotelInfoArrayList();
            ArrayList<CollectionFeed> newFeeds = (ArrayList<CollectionFeed>) msg.obj;
            collectionListAdapter.setCollectionFeeds(newFeeds);
            collectionListAdapter.notifyDataSetInvalidated();
//            hotelListAdapter.notifyDataSetInvalidated();
        }
    };

    public void loadUserInfo() {

        JsonObjectRequest profileRequest = new JsonObjectRequest(Request.Method.GET,
                Constants.URL,null,
                profileGetSucceed(), profileGetFail()) {
        };

        RequestManager.getInstance(this)
                .addToRequestQueue(profileRequest, TAG);

    }

    public Response.Listener<JSONObject> profileGetSucceed() {
        return new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "profileGetSucceed:" + response);
                JSONArray list = response.optJSONArray("custom_collections");
                Message msg = uiHandler.obtainMessage();
                List<CollectionFeed> collectionFeeds = new ArrayList<CollectionFeed>();
                for(int i = 0; i < list.length();i++){
                    try {
                        JSONObject params = (JSONObject)list.get(i);
                        CollectionFeed feed = new CollectionFeed();
                        feed.name  = params.getString("title");
                        feed.updateDate = params.getString("updated_at");
                        JSONObject image = params.getJSONObject("image");
                        feed.imageUrl = image.getString("src");
                        feed.bodyHtml = params.getString("body_html");
                        feed.id = params.getString("id");
                        collectionFeeds.add(feed);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                msg.obj = collectionFeeds;
                msg.sendToTarget();
            }
        };
    }

    public Response.ErrorListener profileGetFail() {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println(error.toString());

            }
        };

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,CollectionDetailActivity.class);
        CollectionFeed collectionFeed = collectionListAdapter.getItem(position);
        Bundle bundle = new Bundle();
        bundle.putString("name", collectionFeed.name);
        bundle.putString("src", collectionFeed.imageUrl);
        bundle.putString("bodyHtml", collectionFeed.bodyHtml);
        bundle.putString("id", collectionFeed.id);
        intent.putExtra("info", bundle);
        startActivity(intent);
    }
}
