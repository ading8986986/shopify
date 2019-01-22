package ding.ting.shopify.feeds;

public class CollectFeed {
    public String productName;
    public String productImage;
    public int inventory;
    public String updateDate;
    public String id;
    public CollectFeed(String id, String productName, String productImage, String updateDate, int inventory){
        this.inventory=inventory;
        this.updateDate=updateDate;
        this.productName=productName;
        this.productImage=productImage;
        this.id = id;
    }

    public CollectFeed(){

    }
}
