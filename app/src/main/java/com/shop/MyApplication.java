package com.shop;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.SyncStateContract;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

/**
 * @author victory
 * @time 2018/6/26
 * @about
 */
public class MyApplication extends Application {
    private static MyApplication context;

    public static MyApplication getInstance() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    /* @GlideModule
    public final class MyAppGlideModule extends AppGlideModule {}*/
    public class AppRegister extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            final IWXAPI api = WXAPIFactory.createWXAPI(context, null);

            // 将该app注册到微信
            api.registerApp("");
        }
    }
}
