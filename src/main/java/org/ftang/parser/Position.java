package org.ftang.parser;

/**
 * User: marcin
 */
public class Position {
    public final String title;
    public final String type;
    public final String startTime;
    public final String excerpt;
    public final boolean nowAiring;

    public Position(String title, String type, String startTime, String excerpt, boolean nowAiring) {
        this.title = title;
        this.type = type;
        this.startTime = startTime;
        this.excerpt = excerpt;
        this.nowAiring = nowAiring;
    }

    @Override
    public String toString() {
        return "<li>" +
                (nowAiring ? "<b>" : "") + title +
                ((type != null && type.length() > 0)? " (" + type + ")" : "" ) +
                " [" + startTime + "]" + (nowAiring ? "</b>" : "") + "</li>";
    }
}