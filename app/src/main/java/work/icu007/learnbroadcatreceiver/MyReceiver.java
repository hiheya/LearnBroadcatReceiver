package work.icu007.learnbroadcatreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class MyReceiver extends BroadcastReceiver {

    private static final String TAG = "LCR add : ";
    public static final String ACTION = "work.icu007.learnbroadcatreceiver.intent.action.MyReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        Log.d(TAG, "onReceive: 接收到了消息,消息是：" + intent.getStringExtra("data"));
    }


}