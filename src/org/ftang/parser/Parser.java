package org.ftang.parser;

import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * User: marcin
 */
public interface Parser {
    Map<String, List<Position>> parse(String in) throws IOException;
}
