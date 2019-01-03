package com.nirvana.share;

import java.util.LinkedList;

/**
 * Created by lijianjun on 2017-07-31.
 */

public class ShareSuccessDispatch {
    private static LinkedList<OnShareSuccessListener> onShareSuccessListeners = new LinkedList<>();

    public static void addShareListener(OnShareSuccessListener onShareSuccessListener) {
        onShareSuccessListeners.add(onShareSuccessListener);
    }

    public static void removeShareListener(OnShareSuccessListener onShareSuccessListener) {
        onShareSuccessListeners.remove(onShareSuccessListener);
    }

    /**
     * dispatch share message
     * @param message
     */
    public static void dispatch(String message) {
        for (OnShareSuccessListener onShareSuccessListener: onShareSuccessListeners) {
            onShareSuccessListener.onShareSuccess(message);
        }
    }

    /**
     * clear share listeners
     */
    public static void clear() {
        onShareSuccessListeners.clear();
    }

    /**
     * share success listener
     */
    public interface OnShareSuccessListener {
        void onShareSuccess(String shareSuccessMessage);
    }
}
