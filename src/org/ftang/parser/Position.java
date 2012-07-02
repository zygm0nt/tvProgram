package org.ftang.parser;

/**
 * User: marcin
 */
public class Position {
    public final String title;
    public final String type;
    public final String startTime;
    public final String endTime;
    public final boolean nowAiring;

    public Position(String title, String type, String startTime, String endTime, boolean nowAiring) {
        this.title = title;
        this.type = type;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nowAiring = nowAiring;
    }
}