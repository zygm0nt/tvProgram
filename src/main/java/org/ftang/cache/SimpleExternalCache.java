package org.ftang.cache;

import org.ftang.parser.Position;

import java.util.List;

/**
 * User: marcin
 */
public interface SimpleExternalCache {
    boolean store(String programName, String content);

    boolean flush();

    String get();

    List<Position> get(String programName);
    
    boolean isEmpty();

    boolean isUpToDate();

    boolean contains(String programName);
}
