package com.eliokog.policy;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by eliokog on 2017/2/14.
 */
public class DefaultRetryNPolicy implements RetryPolicy {

    private int retryCount;

    private static final int DEFAULTCOUNT = 3;
    private static final long DEFAULTSLEEP = 10000;

    private long sleep;

    public DefaultRetryNPolicy() {
        retryCount = DEFAULTCOUNT;
        sleep = DEFAULTSLEEP;
    }

    public DefaultRetryNPolicy(int retryCount, long sleep) {
        this.retryCount = retryCount;
        this.sleep = sleep;
    }

    @Override
    public boolean allowRetry() {
        retryCount--;
        return retryCount>-1;
    }

    @Override
    public long getSleepBeforeRetry() {
        return sleep;
    }

    @Override
    public boolean needRetry(String s) {
        if(StringUtils.contains(s, "抱歉")){
            return true;
        }

        return false;
    }
}
