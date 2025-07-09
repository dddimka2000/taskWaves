package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ScriptWavesService {

    public static List<List<Integer>> validateScripts(List<VulnerabilityScript> scripts) {
        List<List<Integer>> waves = new ArrayList<>();
        Set<Integer> existingIds = scripts.stream()
                .map(VulnerabilityScript::getScriptId)
                .collect(Collectors.toSet());

        List<VulnerabilityScript> cloneScripts = scripts.stream()
                .map(s -> {
                    List<Integer> filteredDeps = s.getDependencies().stream()
                            .filter(existingIds::contains)
                            .collect(Collectors.toList());
                    return new VulnerabilityScript(s.getScriptId(), filteredDeps);
                })
                .collect(Collectors.toList());

        while (!cloneScripts.isEmpty()) {
            List<Integer> currentWave = new ArrayList<>();
            // Find scripts with no dependencies for new wave
            for (VulnerabilityScript script : cloneScripts) {
                if (script.getDependencies().isEmpty()) {
                    currentWave.add(script.getScriptId());
                }
            }
            waves.add(currentWave);
            // Remove executed scripts
            cloneScripts.removeIf(s -> currentWave.contains(s.getScriptId()));

            // Remove their IDs from the dependencies of the remaining scripts
            for (int doneId : currentWave) {
                for (int i = 0; i < cloneScripts.size(); i++) {
                    VulnerabilityScript s = cloneScripts.get(i);
                    List<Integer> deps = s.getDependencies();
                    if (!deps.contains(doneId)) {
                        continue;
                    }
                    List<Integer> updatedDeps = new ArrayList<>(deps);
                    updatedDeps.removeIf(dep -> dep == doneId);
                    cloneScripts.set(i, new VulnerabilityScript(s.getScriptId(), updatedDeps));
                }
            }
        }
        return waves;
    }
    public static void showStatistic(List<List<Integer>> waves){
        System.out.println("Collection waves:"+waves);
        int totalWaves = waves.size();
        System.out.println("Total waves: " + totalWaves);
        int totalScripts = waves.stream()
                .mapToInt(List::size)
                .sum();
        System.out.println("Total scripts: " + totalScripts);
        System.out.println("Scripts per wave:");
        for (int i = 0; i < waves.size(); i++) {
            System.out.println("  Wave " + (i + 1) + ": " + waves.get(i).size() + " scripts");
        }
        double efficiency = totalScripts / (double) totalWaves;
        System.out.printf("Overall execution efficiency: %.2f scripts per wave%n", efficiency);
    }
}
