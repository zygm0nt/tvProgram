package org.ftang.cache;

import org.ftang.parser.Position;

import java.util.List;

/**
 * User: marcin
 */
public interface SimpleExternalCache {
    boolean store(String content);

    boolean flush();

    String get();

    boolean isEmpty();

    boolean isUpToDate();
}
