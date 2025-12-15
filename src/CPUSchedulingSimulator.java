import java.util.*;


public class CPUSchedulingSimulator {
    private static final Scanner sc = new Scanner(System.in);
    private static final List<MyProcess> processes = new ArrayList<>();

    public static void main(String[] args) {
        simulateScheduling();
    }

    public static void simulateScheduling() {
        System.out.println("\t\tCPU SCHEDULING");
        System.out.println("\t\t=============");

        System.out.println();

        System.out.print("Input the number of processes <max 15>: ");
        int n = sc.nextInt();

        System.out.println();

        System.out.print("Enter R to generate random Burst time and Priority or Press any key: ");
        char choice = sc.next().charAt(0);
        if (choice == 'R' || choice == 'r') {
            generateRandomProcesses(n);
            selectSchedulingAlgorithm();
        } else {
            inputProcesses(n);
            selectSchedulingAlgorithm();
        }


    }

    public static void inputProcesses(int n) {
        Random rand = new Random();
        List<Integer> priorities = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            priorities.add(i);
        }
        Collections.shuffle(priorities);

        for (int i = 0; i < n; i++) {
            System.out.println("For P " + (i + 1) + ": ");
            System.out.print("Input Arrival Time: ");
            int arrivalTime = sc.nextInt();

            System.out.print("Input Burst Time: ");
            int burstTime = sc.nextInt();
            System.out.println();
            MyProcess process = new MyProcess(i+1, burstTime, priorities.get(i), arrivalTime);
            processes.add(process);
        }
        displayProcessesWithPriority();
    }

    public static void generateRandomProcesses(int n) {
        Random rand = new Random();
        List<Integer> priorities = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            priorities.add(i);
        }
        Collections.shuffle(priorities);

        for (int i = 0; i < n; i++) {
            int burstTime = rand.nextInt(1, 30);
            int priority = priorities.get(i);
            MyProcess process = new MyProcess(i + 1, burstTime, priority, i);
            processes.add(process);
        }
        displayProcessesWithPriority();
    }

    public static void displayProcessesWithPriority() {
        System.out.println("""
                
                Process\tCreation_time\tBurst_time\tPriority
                -------\t-------------\t----------\t--------""");
        for (MyProcess process : processes) {
            System.out.printf("p%d\t\t\t%d\t\t\t\t%d\t\t\t%d\n", process.getProcessID(), process.getArrivalTime(),
                    process.getBurstTime(), process.getPriority());
        }
    }

    public static void displayProcessesWithoutPriority() {
        System.out.println("""
                
                Process\tCreation_time\tBurst_time
                -------\t-------------\t----------""");
        for (MyProcess process : processes) {
            System.out.printf("p%d\t\t\t%d\t\t\t\t%d\n", process.getProcessID(), process.getArrivalTime(),
                    process.getBurstTime());
        }
    }

    public static void selectSchedulingAlgorithm() {
        System.out.println("==========================================");
        System.out.println("\tCPU Scheduling Algorithms");
        System.out.println("\t=========================");
        System.out.println("""
                \t1. FCFS\t\t\t2.SJF
                \t3. Priority\t\t4. Round Robin
                """);
        System.out.println();
        System.out.print("Select one of the CPU scheduling algorithms by entering its choice number: ");
        int algorithmChoice = sc.nextInt();

        switch (algorithmChoice) {
            case 1:
                fcfsScheduling();
                break;
            case 2:
                System.out.println();
                sjfScheduling();
                break;
            case 3:
                System.out.println();
                priorityScheduling();
                break;
            case 4:
                System.out.println();
                roundRobinScheduling();
                break;
            default:
                System.out.println("Invalid choice. Please select a valid algorithm.");
                break;
        }
        continueOrExit();
    }

    public static void continueOrExit() {
        System.out.print("Press Y to continue or any key to EXIT from the simulation: ");
        char continueChoice = sc.next().charAt(0);
        if (continueChoice == 'Y' || continueChoice == 'y') {
            System.out.print("""
                    
                    Your Choice:
                    1. Run another CPU Scheduling Simulation with same processes
                    2. Run another CPU Scheduling Simulation with new processes
                    3. EXIT
                    """);
            int choice = sc.nextInt();
            switch (choice) {
                case 1:
                    selectSchedulingAlgorithm();
                    break;
                case 2:
                    processes.clear();
                    simulateScheduling();
                    break;
                case 3:
                    System.out.println("Exiting the simulation. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Exiting the simulation. Goodbye!");
                    break;
            }
        } else {
            System.out.println("Exiting the simulation. Goodbye!");
        }
    }

    public static void fcfsScheduling() {
        printTitle("FCFS CPU Scheduling Algorithm");
        displayProcessesWithoutPriority();
        System.out.println("Gantt Chart <with starting time is zero>:\n");
        int currentTime = 0;
        float totalWaitingTime = 0;

        for (MyProcess process : processes) {
            int start = currentTime;
            int end = currentTime + process.getBurstTime() - 1;
            System.out.print("| p" + process.getProcessID() + "(" + start + "-" + end + ")");
            process.setWaitingTime(currentTime - process.getArrivalTime());
            currentTime += process.getBurstTime();
            totalWaitingTime += process.getWaitingTime();
        }
        System.out.println("\n");
        totalWaitingTime /= processes.size();
        for (MyProcess process : processes) {
            System.out.println("Waiting time of process, P" + process.getProcessID() + " = " + process.getWaitingTime());
        }
        System.out.println();
        System.out.println("The Average Process Waiting Time = " + totalWaitingTime + " ms.\n");
    }

    public static void sjfScheduling() {
        printTitle("SJF CPU Scheduling Algorithm");
        displayProcessesWithoutPriority();
        System.out.println("Gantt Chart <with starting time is zero>:\n");
        int n = processes.size();
        int[] remaining = new int[n];
        int[] finish = new int[n];
        for (int i = 0; i < n; i++) {
            remaining[i] = processes.get(i).getBurstTime();
        }

        int currentTime = 0;
        int completed = 0;
        int lastPid = -1;
        int segmentStart = 0;
        List<int[]> segments = new ArrayList<>(); // {pid, start, end}

        while (completed < n) {
            int bestIndex = -1;
            int bestRemain = Integer.MAX_VALUE;

            // choose process with smallest remaining among arrived
            for (int i = 0; i < n; i++) {
                MyProcess p = processes.get(i);
                if (p.getArrivalTime() <= currentTime && remaining[i] > 0) {
                    if (remaining[i] < bestRemain) {
                        bestRemain = remaining[i];
                        bestIndex = i;
                    } else if (remaining[i] == bestRemain) {
                        // tie-breaker: earlier arrival
                        if (p.getArrivalTime() < processes.get(bestIndex).getArrivalTime()) {
                            bestIndex = i;
                        }
                    }
                }
            }

            // CPU idle if nothing arrived yet
            if (bestIndex == -1) {
                currentTime++;
                continue;
            }

            int pid = processes.get(bestIndex).getProcessID();

            // if switching to a new process, close previous segment
            if (lastPid != -1 && pid != lastPid) {
                int segEnd = currentTime - 1; // inclusive
                segments.add(new int[]{lastPid, segmentStart, segEnd});
                segmentStart = currentTime;
            } else if (lastPid == -1) {
                segmentStart = currentTime;
            }

            // run 1 time unit
            lastPid = pid;
            remaining[bestIndex]--;
            currentTime++;

            // if finished
            if (remaining[bestIndex] == 0) {
                finish[bestIndex] = currentTime; // finish time (exclusive end)
                completed++;
            }
        }

        // close last segment
        if (lastPid != -1) {
            int segEnd = currentTime - 1;
            segments.add(new int[]{lastPid, segmentStart, segEnd});
        }

        // Print Gantt chart in requested format: p{pid} ({duration}) | ...
        StringBuilder gantt = new StringBuilder();
        for (int[] s : segments) {
            int pid = s[0];
            int start = s[1];
            int end = s[2];
            int duration = end - start + 1;
            gantt.append("p").append(pid).append(" (").append(duration).append(") | ");
        }
        System.out.println(gantt.toString().trim() + "\n");

        // waiting time = finish - arrival - burst
        float totalWaitingTime = 0;
        for (int i = 0; i < n; i++) {
            MyProcess p = processes.get(i);
            int wt = finish[i] - p.getArrivalTime() - p.getBurstTime();
            p.setWaitingTime(wt);
            totalWaitingTime += wt;
            System.out.println("Waiting time of process, P" + p.getProcessID() + " = " + wt);
        }

        System.out.println();
        System.out.println("The Average Process Waiting Time = " + (totalWaitingTime / n) + " ms.\n");
    }

    public static void priorityScheduling() {
        printTitle("Priority CPU Scheduling Algorithm (Preemptive)");
        displayProcessesWithPriority();

        int n = processes.size();
        int[] remaining = new int[n];
        int[] finish = new int[n];

        for (int i = 0; i < n; i++) {
            remaining[i] = processes.get(i).getBurstTime();
        }

        int currentTime = 0;
        int completed = 0;

        System.out.println("\nGantt Chart <with starting time is zero>:\n");
        System.out.print("| ");

        int lastIndex = -1;
        int segmentStart = 0;

        while (completed < n) {
            int bestIndex = -1;

            // pick highest priority among arrived processes (lowest priority number)
            for (int i = 0; i < n; i++) {
                MyProcess p = processes.get(i);

                if (p.getArrivalTime() <= currentTime && remaining[i] > 0) {
                    if (bestIndex == -1) {
                        bestIndex = i;
                    } else {
                        MyProcess best = processes.get(bestIndex);

                        // smaller priority value = higher priority
                        if (p.getPriority() < best.getPriority()) {
                            bestIndex = i;
                        } else if (p.getPriority() == best.getPriority()) {
                            // tie-breaker 1: earlier arrival
                            if (p.getArrivalTime() < best.getArrivalTime()) {
                                bestIndex = i;
                            } else if (p.getArrivalTime() == best.getArrivalTime()) {
                                // tie-breaker 2: smaller PID
                                if (p.getProcessID() < best.getProcessID()) {
                                    bestIndex = i;
                                }
                            }
                        }
                    }
                }
            }

            // CPU idle if nothing available
            if (bestIndex == -1) {
                // close previous running segment if any
                if (lastIndex != -1) {
                    MyProcess last = processes.get(lastIndex);
                    System.out.print("P" + last.getProcessID() + "(" + segmentStart + "-" + currentTime + ") | ");
                    lastIndex = -1;
                }
                currentTime++;
                continue;
            }

            // if switching process, close previous segment
            if (lastIndex != -1 && bestIndex != lastIndex) {
                MyProcess last = processes.get(lastIndex);
                System.out.print("P" + last.getProcessID() + "(" + segmentStart + "-" + currentTime + ") | ");
                segmentStart = currentTime;
            }

            // if starting first segment
            if (lastIndex == -1) {
                segmentStart = currentTime;
            }

            // run chosen process for 1 time unit
            remaining[bestIndex]--;
            currentTime++;
            lastIndex = bestIndex;

            // if finished now
            if (remaining[bestIndex] == 0) {
                finish[bestIndex] = currentTime; // end time (exclusive)
                completed++;
            }
        }

        // close last segment
        if (lastIndex != -1) {
            MyProcess last = processes.get(lastIndex);
            System.out.print("P" + last.getProcessID() + "(" + segmentStart + "-" + currentTime + ") | ");
        }

        System.out.println("\n");

        // Waiting time: WT = Finish - Arrival - Burst
        float totalWT = 0;
        for (int i = 0; i < n; i++) {
            MyProcess p = processes.get(i);
            int wt = finish[i] - p.getArrivalTime() - p.getBurstTime();
            p.setWaitingTime(wt);
            totalWT += wt;
        }

        // Print waiting times (same style as your screenshot)
        for (MyProcess p : processes) {
            System.out.println("Waiting time of process, P" + p.getProcessID() + " = " + p.getWaitingTime() + " ms.");
        }

        System.out.println();
        System.out.println("The Average Process Waiting Time = " + (totalWT / n) + " ms.\n");
    }

    public static void roundRobinScheduling() {
        printTitle("Round Robin CPU Scheduling Algorithm");
        displayProcessesWithoutPriority();

        System.out.print("\nInput Time Quantum: ");
        int tq = sc.nextInt();
        if (tq <= 0) {
            System.out.println("Time Quantum must be > 0");
            return;
        }

        System.out.println("\nTime Quantum = " + tq);

        int n = processes.size();
        int[] remaining = new int[n];
        int[] finish = new int[n];

        for (int i = 0; i < n; i++) {
            remaining[i] = processes.get(i).getBurstTime();
        }

        // indices sorted by arrival time (then PID)
        List<Integer> order = new ArrayList<>();
        for (int i = 0; i < n; i++) order.add(i);
        order.sort(Comparator
                .comparingInt((Integer idx) -> processes.get(idx).getArrivalTime())
                .thenComparingInt(idx -> processes.get(idx).getProcessID()));

        Queue<Integer> q = new ArrayDeque<>();
        List<Segment> segments = new ArrayList<>();

        int currentTime = 0;
        int completed = 0;
        int nextArr = 0;

        // start by adding all processes that arrive at time 0 (or earliest time)
        if (n > 0) {
            currentTime = Math.min(0, processes.get(order.get(0)).getArrivalTime());
        }

        while (completed < n) {

            // enqueue all arrived processes
            while (nextArr < n && processes.get(order.get(nextArr)).getArrivalTime() <= currentTime) {
                q.add(order.get(nextArr));
                nextArr++;
            }

            // if queue is empty, jump to next arrival (CPU idle)
            if (q.isEmpty()) {
                if (nextArr < n) {
                    currentTime = processes.get(order.get(nextArr)).getArrivalTime();
                    continue;
                }
                break;
            }

            int idx = q.poll();
            MyProcess p = processes.get(idx);

            int run = Math.min(tq, remaining[idx]);

            // record gantt segment in (duration) format
            addSegment(segments, p.getProcessID(), run);

            // run it
            remaining[idx] -= run;
            currentTime += run;

            // enqueue processes that arrived during this time slice
            while (nextArr < n && processes.get(order.get(nextArr)).getArrivalTime() <= currentTime) {
                q.add(order.get(nextArr));
                nextArr++;
            }

            // if not finished, put back to queue
            if (remaining[idx] > 0) {
                q.add(idx);
            } else {
                finish[idx] = currentTime; // finish time (exclusive)
                completed++;
            }
        }

        // Print gantt chart like your screenshot
        System.out.println("\nThe Gantt Chart:");
        System.out.print("|");
        for (Segment s : segments) {
            System.out.print(" P" + s.pid + "(" + s.duration + ") |");
        }
        System.out.println("\n");

        // Waiting Time = Finish - Arrival - Burst
        float totalWT = 0;
        System.out.println("Process Waiting Time:");
        for (int i = 0; i < n; i++) {
            MyProcess p = processes.get(i);
            int wt = finish[i] - p.getArrivalTime() - p.getBurstTime();
            p.setWaitingTime(wt);
            totalWT += wt;
            System.out.println("P" + p.getProcessID() + " waiting time = " + wt + " ms.");
        }

        System.out.println();
        System.out.println("The Average Process Waiting Time = " + (totalWT / n) + " ms.\n");
    }

    // Put this inside CPUSchedulingSimulator (outside methods)
    static class Segment {
        int pid;
        int duration;
        Segment(int pid, int duration) {
            this.pid = pid;
            this.duration = duration;
        }

    }

    private static void addSegment(List<Segment> segs, int pid, int dur) {
        if (dur <= 0) return;
        if (!segs.isEmpty() && segs.get(segs.size() - 1).pid == pid) {
            segs.get(segs.size() - 1).duration += dur; // merge consecutive same PID
        } else {
            segs.add(new Segment(pid, dur));
        }
    }



    public static void printTitle(String title) {
        System.out.println("=====================================");
        System.out.println("\t" + title);
        System.out.println("\t===========================");
    }

}
