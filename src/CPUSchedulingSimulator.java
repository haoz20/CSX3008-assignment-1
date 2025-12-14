import java.util.*;

public class CPUSchedulingSimulator {
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        simulateScheduling();
    }

    public static void simulateScheduling() {
        System.out.println("\t\tCPU SCHEDULING");
        System.out.println("\t\t=============");

        System.out.println();

        System.out.println("Input the number of processes <max 15>: ");
        int n = sc.nextInt();

        System.out.println();

        System.out.println("Enter R to generate random Burst time and Priority or Press any key: ");
        char choice = sc.next().charAt(0);
    }

    public static void generateRandomProcesses(int n) {

    }

}
