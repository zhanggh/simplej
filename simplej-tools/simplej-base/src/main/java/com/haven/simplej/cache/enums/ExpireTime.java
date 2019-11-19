package com.haven.simplej.cache.enums;

/**
 * Created by haven.zhang on 2018/10/15.
 */
public enum ExpireTime {
    ONE_HOUR(3600),
    SIX_HOUR(21600),
    TWELVE_HOUR(43200),
    ONE_DAY(86400),
    TWO_DAY(172800),
    THREE_DAY(259200),
    ONE_WEEK(604800);

    long expireTime;

    ExpireTime(long expireTime){
        this.expireTime = expireTime;
    }

    public long get() {
        return expireTime;
    }
}
