package org.example;

import java.util.*;

public class ScriptValidator {
    public static boolean validateScripts(List<VulnerabilityScript> scripts) {
        if (scripts == null || scripts.isEmpty()) {
            System.out.println("Error: No scripts provided.");
            return false;
        }
        Set<Integer> ids = new HashSet<>();
        boolean isValid = true;

        for (VulnerabilityScript script : scripts) {
            if (!ids.add(script.getScriptId())) {
                System.out.println("Error: Duplicate script ID found: " + script.getScriptId());
                isValid = false;
            }
        }

        for (VulnerabilityScript script : scripts) {
            for (Integer dep : script.getDependencies()) {
                if (!ids.contains(dep)) {
                    System.out.println("Warning: Script " + script.getScriptId() +
                            " has missing dependency " + dep);
                }
            }
        }

        return isValid;
    }
}
