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
        displayProcesses();
    }

    public static void displayProcesses() {
        System.out.println("""
        
        Process\tCreation_time\tBurst_time\tPriority
        -------\t-------------\t----------\t--------""");
        for (MyProcess process : processes) {
            System.out.printf("p%d\t\t\t%d\t\t\t\t%d\t\t\t%d\n", process.getProcessID(), process.getArrivalTime(),
                    process.getBurstTime(), process.getPriority());
        }
        System.out.println("==========================================");
        selectSchedulingAlgorithm();
    }

    public static void selectSchedulingAlgorithm() {
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
    }

    public static void fcfsScheduling() {
        printTitle("FCFS CPU Scheduling Algorithm");




    }

    public static void printTitle(String title) {
        System.out.println("=====================================");
        System.out.println("\t" + title);
        System.out.println("\t===========================");
    }

}
