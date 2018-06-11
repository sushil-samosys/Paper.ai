package com.samosys.paperai.activity.utils;

import android.content.Context;
import android.os.Build;
import android.os.StrictMode;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;


import com.parse.Parse;
import com.samosys.paperai.R;


/**
 * Created by samosys on 8/4/16.
 */
public class UILApplication extends MultiDexApplication {
    private static Context mContext;


    public UILApplication() {
    }

    @Override
    public void onCreate() {

        mContext = getApplicationContext();


//        MultiDex.install(this);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getResources().getString(R.string.parse_app_id))
                .clientKey(getResources().getString(R.string.parse_clientKey))
                .server(getResources().getString(R.string.parse_server_url))
                .build()
        );
       // sAnalytics = GoogleAnalytics.getInstance(this);

        //   CustomActivityOnCrash.install(this);

        if (AppConstants.Config.DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }

//        FirebaseApp.initializeApp(this);

        super.onCreate();

     //   initImageLoader(getApplicationContext());


//        FacebookSdk.sdkInitialize(getApplicationContext());



    }







//    public static void initImageLoader(Context context) {
//
//        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
//        config.threadPriority(Thread.NORM_PRIORITY - 2);
//        config.denyCacheImageMultipleSizesInMemory();
//        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
//        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
//        config.tasksProcessingOrder(QueueProcessingType.LIFO);
//        config.writeDebugLogs(); // Remove for release app
//
//        ImageLoader.getInstance().init(config.build());
//    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }
    public static synchronized UILApplication getInstance() {
        return UILApplication.getInstance();
    }
}