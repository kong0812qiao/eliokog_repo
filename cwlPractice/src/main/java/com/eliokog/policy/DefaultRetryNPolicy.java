package com.eliokog.policy;

/**
 * Created by eliokog on 2017/2/14.
 */
public class DefaultRetryNPolicy implements RetryPolicy {


    @Override
    public boolean allowRetry(int count, long startTimeOfExecution) {

        return false;
    }

    @Override
    public int getSleepBeforeRetry() {
        return 0;
    }

    @Override
    public boolean needRetry(String s) {
        return false;
    }
}
