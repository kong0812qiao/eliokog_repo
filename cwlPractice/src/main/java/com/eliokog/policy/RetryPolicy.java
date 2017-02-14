package com.eliokog.policy;

/**
 * Created by eliokog on 2017/2/14.
 */
public interface RetryPolicy {

    public boolean allowRetry();

    public long getSleepBeforeRetry();

    public boolean needRetry(String s);

}
