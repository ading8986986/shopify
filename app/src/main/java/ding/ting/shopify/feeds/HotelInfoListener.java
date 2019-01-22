package ding.ting.shopify.feeds;

import java.util.ArrayList;

/**
 * Created by duqian on 10/15/2014.
 */
public interface HotelInfoListener {

    public abstract void onHotelInfoLoaded(ArrayList<CollectionFeed> hotelInfos);
}
