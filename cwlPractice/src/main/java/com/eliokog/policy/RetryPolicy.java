package com.eliokog.policy;

/**
 * Created by eliokog on 2017/2/14.
 */
public interface RetryPolicy {

    public boolean allowRetry(int count, long startTimeOfExecution);

    public int getSleepBeforeRetry();

    public boolean needRetry(String s);

}
