package org.example;
import java.util.List;


public class App {
    public static void main(String[] args) {
        List<VulnerabilityScript> scripts = List.of(
                new VulnerabilityScript(1, List.of()),
                new VulnerabilityScript(2, List.of(1)),
                new VulnerabilityScript(3, List.of(1)),
                new VulnerabilityScript(4, List.of(2, 3)),
                new VulnerabilityScript(5, List.of(9)),
                new VulnerabilityScript(6, List.of(10)),
                new VulnerabilityScript(9, List.of()));

        startApp(scripts);
    }
    public static void startApp(List<VulnerabilityScript> scripts){
        if (ScriptValidator.validateScripts(scripts)) {
            List<List<Integer>> waves = ScriptWavesService.validateScripts(scripts);
            ScriptWavesService.showStatistic(waves);
        }
    }
}
