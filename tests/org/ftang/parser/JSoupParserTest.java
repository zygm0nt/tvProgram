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
    public void testShouldParsePrograms() throws Exception {
        String testProgram = "TVP 1";
        List<Position> nowAiringPrograms = fixture.parse(convertStreamToString(getClass().getResourceAsStream("sample.program.html"))).get(testProgram);
        assertProgramPosition(nowAiringPrograms, 0, "Malibu - atak rekinów", "thriller", "21:35", "23:05", true);
        assertProgramPosition(nowAiringPrograms, 1, "Sensacyjne lato w Jedynce: Glina (3/25)", "serial kryminalny", "23:05", "0:10", false);
    }

    @Test
    public void testShouldParsePrograms2() throws Exception {
        String testProgram = "TVP 1";
        List<Position> nowAiringPrograms = fixture.parse(convertStreamToString(getClass().getResourceAsStream("sample.program.2.html"))).get(testProgram);
        assertProgramPosition(nowAiringPrograms, 0, "Piłka nożna: Euro 2012", "święto radości", "20:25", "22:00", true);
        assertProgramPosition(nowAiringPrograms, 1, "Sensacyjne lato w Jedynce: Glina (6/25)", "serial kryminalny", "22:00", "23:05", false);
    }

    private String[] excluded = {"TVN METEO", "ITV", "MANGO 24", "ASTERINFO", "MULTIPIP", "RUSSIA TODAY", "CNN"};
    @Test
    public void shouldIterateOverPrograms() throws Exception {
        List<String> programList = getProgramList();
        Map<String, List<Position>> programMapping = fixture.parse(convertStreamToString(getClass().getResourceAsStream("sample.program.2.html")));
        System.out.println("Have this keys available:" + Arrays.toString(programMapping.keySet().toArray()));
        for (String programName : programList) {
            System.out.println("Processing " + programName);
            if (Arrays.asList(excluded).contains(programName))
                continue;
            List<Position> result = programMapping.get(programName);
            Assert.assertNotNull(result);
            Assert.assertTrue(result.size() > 0);
        }
        
    }

    private List<String> getProgramList() {
        List<String> programList = new ArrayList<String>();
        InputStream is = getClass().getResourceAsStream("/org/ftang/parser/programs.html");
        for (String line : convertStreamToString(is).split("\n"))
            programList.add(line.split(",")[1].toUpperCase());
        return programList;
    }

    private void assertProgramPosition(List<Position> nowAiringPrograms, int idx, String expectedTitle, String expectedType, String expectedStartTime, String expectedEndTime, boolean expectedAiring) {
        Assert.assertNotNull(nowAiringPrograms.get(idx));
        Assert.assertEquals(expectedTitle, nowAiringPrograms.get(idx).title);
        Assert.assertEquals(expectedType, nowAiringPrograms.get(idx).type);
        Assert.assertEquals(expectedStartTime, nowAiringPrograms.get(idx).startTime);
        Assert.assertEquals(expectedEndTime, nowAiringPrograms.get(idx).endTime);
        Assert.assertEquals(expectedAiring, nowAiringPrograms.get(idx).nowAiring);
    }
}
