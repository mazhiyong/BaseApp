package com.lr.biyou.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;


/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * <p>
 * app  开启的时候 初始化 操作  减少app 开启的效率
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class InitService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.xinli.vkeeper.services.action.FOO";
 
 
    /**
     * Instantiates a new Init service.
     */
    public InitService() {
        super("InitService");
    }
 
    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @param context the context
     * @see IntentService
     */
// TODO: Customize helper method
    public static void startActionFoo(Context context) {
        try {
            Intent intent = new Intent(context, InitService.class);
            intent.setAction(ACTION_FOO);
 
        /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                context.startForegroundService(intent);
            } else {*/
                context.startService(intent);
           // }
        } catch (Exception e) {
            e.printStackTrace();
 
        }
 
    }
 
 
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                LoadInit();
            }
        }
    }
 
 
    /**
     * 初始化操作数据
     */
    private void LoadInit() {


    }
 
}
