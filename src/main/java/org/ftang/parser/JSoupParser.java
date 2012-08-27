package org.ftang.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * User: marcin
 * 
 * Parser with internal state.
 */
public class JSoupParser implements Parser {

    private final List<String> hours = new ArrayList<String>();

    @Override
    public List<Position> parse(String in) throws IOException {
        List<Position> tvProgram = new ArrayList<Position>();

        Document doc = Jsoup.parse(in, "UTF-8");

        Iterator<Element> iter = doc.select("table#station_listing tr").iterator();
        while (iter.hasNext()) {
            Element elem = iter.next();
            tvProgram.add(getProgramPosition(elem.select("th").text(), elem.select("td"),
                    !elem.select("span[title=trwa]").isEmpty()));
        }
        return tvProgram;
    }

    public List<String> getHours() {
        return hours;
    }

    private Position getProgramPosition(String hour, Elements elem, boolean airing) {
        return new Position(elem.select("a.prog_title").text(),
                    elem.select("div.genre").text(), 
                    hour,
                    elem.select("p.excerpt").text(),
                    airing);
    }

    private String getStationName(Element stationTag) {
        Elements tag = stationTag.select("a");
        if (!tag.select("span.hd").isEmpty())
            tag.select("span.hd").remove();
        return tag.text();
    }

    private Collection<? extends String> getProgramHours(Elements div) {
        List<String> hours = new ArrayList<String>();
        Iterator<Element> iter = div.iterator();
        while (iter.hasNext()) {
            Element elem = iter.next();
            hours.add(elem.text());
        }
        return hours;
    }
}
