import java.util.*;
class HierarchicalDetector {
    int id;
    List<HierarchicalDetector> children;
    Map<Integer, List<Integer>> localWaitFor;

    HierarchicalDetector(int id) {
        this.id = id;
        this.children = new ArrayList<>();
        this.localWaitFor = new HashMap<>();
    }

    void addEdge(int from, int to) {
        localWaitFor.computeIfAbsent(from, k -> new ArrayList<>()).add(to);
    }

    boolean detectDeadlock() {
        boolean local = hasLocalCycle();
        for (HierarchicalDetector child : children) {
            local |= child.detectDeadlock();
        }
        return local;
    }

    boolean hasLocalCycle() {
        Set<Integer> visited = new HashSet<>();
        Set<Integer> recStack = new HashSet<>();
        for (int node : localWaitFor.keySet()) {
            if (detectCycle(node, visited, recStack)) return true;
        }
        return false;
    }

    private boolean detectCycle(int node, Set<Integer> visited, Set<Integer> recStack) {
        if (recStack.contains(node)) return true;
        if (visited.contains(node)) return false;

        visited.add(node);
        recStack.add(node);

        for (int neighbor : localWaitFor.getOrDefault(node, Collections.emptyList())) {
            if (detectCycle(neighbor, visited, recStack)) return true;
        }

        recStack.remove(node);
        return false;
    }
}

public class Heirarchical {
    public static void main(String[] args) {
        HierarchicalDetector root = new HierarchicalDetector(0);
        HierarchicalDetector child1 = new HierarchicalDetector(1);
        HierarchicalDetector child2 = new HierarchicalDetector(2);

        root.children.add(child1);
        root.children.add(child2);

        child1.addEdge(1, 2);
        child1.addEdge(2, 3);
        child1.addEdge(3, 1); // cycle in subtree

        System.out.println("Deadlock in hierarchy? " + root.detectDeadlock());
    }
}
