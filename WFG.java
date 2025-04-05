import java.util.*;

class WFGDeadlockDetector {
    private Map<Integer, List<Integer>> waitForGraph;

    public WFGDeadlockDetector() {
        waitForGraph = new HashMap<>();
    }

    public void addWait(int fromProcess, int toProcess) {
        waitForGraph.computeIfAbsent(fromProcess, k -> new ArrayList<>()).add(toProcess);
    }

    public void removeWait(int fromProcess, int toProcess) {
        List<Integer> list = waitForGraph.get(fromProcess);
        if (list != null) list.remove(Integer.valueOf(toProcess));
    }

    public boolean detectDeadlock() {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recursionStack = new HashSet<>();

        for (int process : waitForGraph.keySet()) {
            if (detectCycle(process, visited, recursionStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycle(int current, Set<Integer> visited, Set<Integer> recStack) {
        if (recStack.contains(current)) return true;
        if (visited.contains(current)) return false;

        visited.add(current);
        recStack.add(current);

        for (int neighbor : waitForGraph.getOrDefault(current, Collections.emptyList())) {
            if (detectCycle(neighbor, visited, recStack)) return true;
        }

        recStack.remove(current);
        return false;
    }

    public void printGraph() {
        System.out.println("Wait-For Graph:");
        for (Map.Entry<Integer, List<Integer>> entry : waitForGraph.entrySet()) {
            System.out.println("P" + entry.getKey() + " → " + entry.getValue());
        }
    }
}

public class WFG {
    public static void main(String[] args) {
        WFGDeadlockDetector wfg = new WFGDeadlockDetector();

        // Add wait relationships (simulate a cycle)
        wfg.addWait(1, 2);
        wfg.addWait(2, 3);
        wfg.addWait(3, 1);  // <-- cycle here

        wfg.printGraph();

        if (wfg.detectDeadlock()) {
            System.out.println("Deadlock Detected!");
        } else {
            System.out.println("No Deadlock.");
        }

        // Break the cycle
        System.out.println("\nBreaking the wait from P3 to P1...");
        wfg.removeWait(3, 1);

        wfg.printGraph();

        if (wfg.detectDeadlock()) {
            System.out.println("Deadlock Detected!");
        } else {
            System.out.println("No Deadlock.");
        }
    }
}
