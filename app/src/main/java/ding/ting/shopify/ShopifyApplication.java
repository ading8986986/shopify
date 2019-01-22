
package ding.ting.shopify;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import java.io.File;

import ding.ting.shopify.cache.DiskCacheManager;
import ding.ting.shopify.cache.ImageCacheManager;
import ding.ting.shopify.request.RequestManager;

public class ShopifyApplication extends Application {
    public static ShopifyApplication instance = null;
    public static boolean isLogin = false;
    public static boolean isExpired = false;
    public static String mAccessToken = "";
    public static String mUid = "";
    public static String mOpenId = "";
    private static int DISK_IMAGECACHE_SIZE = 1024 * 1024 * 10;
    public static String SESSION_ID = "";
    public static String USER_ID = "";
    public static String USER_NAME = "";
    public static String USER_DESC = "";
    public static String USER_AVATAR_URL = "";
    public static int mScreenWidth;
    public static int mScreenHeight;
    

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        init();
        initPicWidthHeight();
        RequestManager.getInstance(getApplicationContext()).init(getApplicationContext());
        createImageCache();
        initImageLoader(getApplicationContext());

    }

    public void init() {

    }

    private void createImageCache() {
        ImageCacheManager.getInstance(getApplicationContext()).init(DISK_IMAGECACHE_SIZE);
        DiskCacheManager.getInstance().init(getApplicationContext());
    }

    public static ShopifyApplication getApplication() {
        if (instance == null) {
            synchronized (ShopifyApplication.class) {
                if (instance == null) {
                    instance = new ShopifyApplication();
                }
            }

        }
        return instance;
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you
        // may tune some of them,
        // or you can create default configuration by
        // ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .diskCacheSize(50 * 1024 * 1024) // 50 Mb
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }




    private void initPicWidthHeight() {
        mScreenWidth = this.getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = this.getResources().getDisplayMetrics().heightPixels;

    }
}
