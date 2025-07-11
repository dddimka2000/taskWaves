package org.example;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    @Test
    void testValidateScripts_SimpleValidCase() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of()),
                new VulnerabilityScript(2, List.of(1)),
                new VulnerabilityScript(3, List.of(1)),
                new VulnerabilityScript(4, List.of(2, 3))
        );

        List<List<Integer>> waves = ScriptWavesService.validateScripts(scripts);

        assertEquals(3, waves.size());
        assertEquals(List.of(1), waves.get(0));
        assertTrue(waves.get(1).containsAll(List.of(2, 3)));
        assertEquals(List.of(4), waves.get(2));
    }

    @Test
    void testValidateScripts_MissingDependenciesIgnored() {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of(99)),
                new VulnerabilityScript(2, List.of(1))
        );

        List<List<Integer>> waves = ScriptWavesService.validateScripts(scripts);

        assertEquals(2, waves.size());
        assertEquals(List.of(1), waves.get(0));
        assertEquals(List.of(2), waves.get(1));
    }

    @Test
    void testShowStatistic_PrintsCorrectOutput() {
        List<List<Integer>> waves = List.of(
                List.of(1),
                List.of(2, 3),
                List.of(4)
        );

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        ScriptWavesService.showStatistic(waves);

        String output = out.toString();

        assertTrue(output.contains("Total waves: 3"));
        assertTrue(output.contains("Total scripts: 4"));
        assertTrue(output.contains("Wave 1: 1 scripts"));
        assertTrue(output.contains("Wave 2: 2 scripts"));
        assertTrue(output.contains("Wave 3: 1 scripts"));
        assertTrue(output.contains("Overall execution efficiency:"));

    }
    @Test
    void testValidateScripts_EmptyList() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        boolean result = ScriptValidator.validateScripts(List.of());

        assertFalse(result);
        assertTrue(out.toString().contains("No scripts provided."));
    }

    @Test
    void testValidateScripts_DuplicateIds() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));

        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of()),
                new VulnerabilityScript(1, List.of())
        );

        boolean result = ScriptValidator.validateScripts(scripts);

        assertFalse(result);
        assertTrue(out.toString().contains("Duplicate script ID"));
    }

    @Test
    void testValidateScripts_MissingDependency() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of(99)),
                new VulnerabilityScript(2, List.of())
        );
        boolean result = ScriptValidator.validateScripts(scripts);
        assertTrue(result);
        assertTrue(out.toString().contains("missing dependency"));
    }

    @Test
    void testValidateScripts_ValidScripts() {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        System.setOut(new PrintStream(out));
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of()),
                new VulnerabilityScript(2, List.of(1)),
                new VulnerabilityScript(3, List.of(2))
        );
        boolean result = ScriptValidator.validateScripts(scripts);
        assertTrue(result);
        assertEquals("", out.toString().trim());
    }
}