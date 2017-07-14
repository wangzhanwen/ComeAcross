package com.lyy_wzw.comeacross.bean;

/**
 * Created by yidong9 on 17/7/13.
 */

public class ReleaseFootPrintEvent {
    private long  eventTime;
    private FootPrint footPrint;


    public ReleaseFootPrintEvent() {
    }

    public ReleaseFootPrintEvent(long eventTime, FootPrint footPrint) {
        this.eventTime = eventTime;
        this.footPrint = footPrint;
    }

    public ReleaseFootPrintEvent(FootPrint footPrint) {
        this.footPrint = footPrint;
    }

    public long getEventTime() {
        return eventTime;
    }

    public FootPrint getFootPrint() {
        return footPrint;
    }

    public void setEventTime(long eventTime) {
        this.eventTime = eventTime;
    }

    public void setFootPrint(FootPrint footPrint) {
        this.footPrint = footPrint;
    }
}
