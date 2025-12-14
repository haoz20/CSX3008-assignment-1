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

        generateRandomProcesses(n);
        selectSchedulingAlgorithm();
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
            MyProcess process = new MyProcess(i+1, burstTime, priority, i);
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

                break;
            case 3:
                System.out.println();

                break;
            case 4:
                System.out.println();

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
            processes.clear();
            simulateScheduling();
        } else {
            System.out.println("Exiting the CPU Scheduling Simulator. Goodbye!");
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

    public static void printTitle(String title) {
        System.out.println("=====================================");
        System.out.println("\t" + title);
        System.out.println("\t===========================");
    }

}
