package ding.ting.shopify.feeds;

import android.os.Parcel;
import android.os.Parcelable;

public class CollectionFeed {
    public String name;
    public String updateDate;
    public String imageUrl;
    public String bodyHtml;
    public String id;
    public CollectionFeed(String id, String name, String updateDate, String imageUrl, String bodyHtml){
        this.name=name;
        this.updateDate=updateDate;
        this.imageUrl=imageUrl;
        this.bodyHtml = bodyHtml;
        this.id = id;
    }

    public CollectionFeed(){

    }
}
