package kr.co.royworld.smartcart.utils;

import android.util.Log;

import kr.co.royworld.smartcart.model.Constants;

/**
 * @author worry1318
 * @since 2019.05.17
 */
public class LogUtils {
    public static void debug(String msg){
        Log.d(Constants.TAG_NAME, msg);
    }

    public static void debug(String msg, Throwable tr){
        Log.d(Constants.TAG_NAME, msg, tr);
    }


    public static void info(String msg){
        Log.i(Constants.TAG_NAME, msg);
    }

    public static void error(String msg){
        Log.e(Constants.TAG_NAME, msg);
    }
}
