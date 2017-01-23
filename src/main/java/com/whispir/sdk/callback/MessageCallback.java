package com.whispir.sdk.callback;

import com.whispir.sdk.WhispirResponse;
import com.whispir.sdk.exceptions.WhispirSDKException;

/**
 * Created by shapan on 1/24/17.
 */
public interface MessageCallback {
    // callback with response when message sending is finished.
    public void onMessageSend(WhispirResponse response, WhispirSDKException exception);
}
