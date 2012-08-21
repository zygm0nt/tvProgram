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
    public Map<String, List<Position>> parse(String in) throws IOException {
        Map<String, List<Position>> tvProgram = new HashMap<String, List<Position>>();

        Document doc = Jsoup.parse(in, "UTF-8");

        Iterator<Element> iter = doc.select("table#grid tr").iterator();
        while (iter.hasNext()) {
            Element elem = iter.next();
            if ("group".equals(elem.attr("class"))) { // skip 
            } else if ("ruler".equals(elem.attr("class"))) {
                hours.addAll(getProgramHours(elem.select("div")));        
            } else {
                String stationName = getStationName(elem.select("td.station").first());
                List<Position> positions = getProgramPositions(elem.select("td.cell > div"));
                tvProgram.put(stationName.toUpperCase(), positions);
            }
        }
        return tvProgram;
    }

    public List<String> getHours() {
        return hours;
    }

    private List<Position> getProgramPositions(Elements input) {
        List<Position> positions = new ArrayList<Position>();
        Iterator<Element> iter = input.iterator();
        while (iter.hasNext()) {
            Element elem = iter.next();
            positions.add(new Position(elem.select("a").text(),
                    elem.select("div.genre").text(), 
                    elem.select("span.time").text(),
                    elem.select("span.time").attr("data-stop"), 
                    !elem.select("span[title=trwa]").isEmpty()));
        }
        return positions;
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
