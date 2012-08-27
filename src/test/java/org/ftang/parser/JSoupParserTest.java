package org.ftang.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: marcin
 */
public class JSoupParserTest {

    private JSoupParser fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new JSoupParser();
    }

    private String convertStreamToString(java.io.InputStream is) {
        try {
            return new java.util.Scanner(is).useDelimiter("\\A").next();
        } catch (java.util.NoSuchElementException e) {
            return "";
        }
    }

    @Test
    public void testShouldParsePrograms3() throws Exception {
        List<Position> nowAiringPrograms = fixture.parse(convertStreamToString(getClass().getResourceAsStream("/sample.program.3.html")));
        assertProgramPosition(nowAiringPrograms, 0, "Biały Kieł 2: Legenda białego wilka", "film przygodowy, USA 1994", "20:20", true);
        assertProgramPosition(nowAiringPrograms, 1, "Sensacyjne lato w Jedynce: Determinator (1/13)", "serial kryminalny", "22:15", false);
    }

/*
    TODO FIXME assert program links are actually accessible
    private String[] excluded = {"TVN METEO", "ITV", "MANGO 24", "ASTERINFO", "MULTIPIP", "RUSSIA TODAY", "CNN"};
    @Test
    public void shouldIterateOverPrograms() throws Exception {
        List<String> programList = getProgramList();
        Map<String, List<Position>> programMapping = fixture.parse(convertStreamToString(getClass().getResourceAsStream("/sample.program.2.html")));
        System.out.println("Have this keys available:" + Arrays.toString(programMapping.keySet().toArray()));
        for (String programName : programList) {
            System.out.println("Processing " + programName);
            if (Arrays.asList(excluded).contains(programName))
                continue;
            List<Position> result = programMapping.get(programName);
            Assert.assertNotNull(result);
            Assert.assertTrue(result.size() > 0);
        }
    }*/

    private List<String> getProgramList() {
        List<String> programList = new ArrayList<String>();
        InputStream is = getClass().getResourceAsStream("/programs.html");
        for (String line : convertStreamToString(is).split("\n"))
            programList.add(line.split(",")[1].toUpperCase());
        return programList;
    }

    private void assertProgramPosition(List<Position> nowAiringPrograms, int idx, String expectedTitle, String expectedType, String expectedStartTime, boolean expectedAiring) {
        Assert.assertNotNull(nowAiringPrograms.get(idx));
        Assert.assertEquals(expectedTitle, nowAiringPrograms.get(idx).title);
        Assert.assertEquals(expectedType, nowAiringPrograms.get(idx).type);
        Assert.assertEquals(expectedStartTime, nowAiringPrograms.get(idx).startTime);
        Assert.assertEquals(expectedAiring, nowAiringPrograms.get(idx).nowAiring);
    }
}
