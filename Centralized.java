import java.util.*;

class CentralizedDeadlockDetector {
    private Map<Integer, List<Integer>> waitForGraph = new HashMap<>();

    public void addEdge(int from, int to) {
        waitForGraph.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    public boolean detectDeadlock() {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();

        for (int node : waitForGraph.keySet()) {
            if (detectCycle(node, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    private boolean detectCycle(int node, Set<Integer> visited, Set<Integer> recStack) {
        if (recStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recStack.add(node);

        for (int neighbor : waitForGraph.getOrDefault(node, Collections.emptyList())) {
            if (detectCycle(neighbor, visited, recStack)) return true;
        }

        recStack.remove(node);
        return false;
    }
}

public class Centralized {
    public static void main(String[] args) {
        CentralizedDeadlockDetector detector = new CentralizedDeadlockDetector();
        detector.addEdge(1, 2);
        detector.addEdge(2, 3);
        detector.addEdge(3, 1); // cycle

        System.out.println("Deadlock detected? " + detector.detectDeadlock());
    }
}
