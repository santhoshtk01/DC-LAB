import java.util.*;

class Probe {
    int initiator, sender, receiver;
    Probe(int i, int s, int r) {
        initiator = i; sender = s; receiver = r;
    }
}

class ProbeProcess {
    int id;
    Integer waitingOn = null;
    boolean isBlocked = false;

    ProbeProcess(int id) {
        this.id = id;
    }

    void receiveProbe(Probe probe, Map<Integer, ProbeProcess> processes) {
        System.out.println("P" + id + " received probe: " + probe.initiator + " -> " + probe.sender + " -> " + probe.receiver);

        if (probe.initiator == id) {
            System.out.println("Deadlock detected at P" + id + "!");
            return;
        }

        if (isBlocked && waitingOn != null) {
            Probe newProbe = new Probe(probe.initiator, id, waitingOn);
            processes.get(waitingOn).receiveProbe(newProbe, processes);
        }
    }

    void startProbe(Map<Integer, ProbeProcess> processes) {
        if (isBlocked && waitingOn != null) {
            Probe probe = new Probe(id, id, waitingOn);
            processes.get(waitingOn).receiveProbe(probe, processes);
        }
    }
}

public class ProbeBased {
    public static void main(String[] args) {
        Map<Integer, ProbeProcess> processes = new HashMap<>();
        for (int i = 1; i <= 3; i++) processes.put(i, new ProbeProcess(i));

        // Simulate wait cycle: 1 → 2 → 3 → 1
        processes.get(1).isBlocked = true; processes.get(1).waitingOn = 2;
        processes.get(2).isBlocked = true; processes.get(2).waitingOn = 3;
        processes.get(3).isBlocked = true; processes.get(3).waitingOn = 1;

        processes.get(1).startProbe(processes);
    }
}
