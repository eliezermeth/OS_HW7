import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.Semaphore;

public class Main
{
    // constants
    public final static int NUM_MINIONS = 10;
    public final static String[] minionNames = {"Bob", "Carl", "Dave", "Jerry", "John", "Kevin", "Mark", "Phil", "Stuart", "Tim"};
    public final static int NUM_DWARFS = 7;
    public final static String[] dwarfNames = {"Doc", "Happy", "Sneezy", "Sleepy", "Bashful", "Grumpy", "Dopey"};

    public final static int NUM_CHAIRS_AT_TABLE = 5;
    public final static int NUM_BATHROOMS = 1;

    // Threads
    public static AliceThread alice;
    public static BobThread bob;
    public static MinionThread[] minions = new MinionThread[NUM_MINIONS];
    public static DwarfThread[] dwarfs = new DwarfThread[NUM_DWARFS];

    // Stacks/Queues for creatures waiting to enter the house
    public static Queue<Creature> minionQueue = new LinkedList<>();
    public static Stack<Creature> dwarfQueue = new Stack<>();

    // Booleans
    // if done work
    public static boolean minionsWaitingOutsideCottage;
    public static boolean dwarfsWaitingOutsideCottage;
    // if inside
    public static boolean minionsInsideCottage;
    public static boolean dwarfsInsideCottage;
    // if humans reading
    public static boolean aliceReading;
    public static boolean bobReading;

    // Semaphores (for and after dinner)
    public static Semaphore chairAtDinnerTable = new Semaphore(NUM_CHAIRS_AT_TABLE); // for dinner
    public static Semaphore bathroom = new Semaphore(NUM_BATHROOMS);

    // Times
    public static final int MINE_WORKDAY = 2000; // milliseconds for the length of workday in mine
    public static  final int MINE_ALLOWED_OVERTIME = 1000; // milliseconds for the maximum allowed overtime to work in mine
    public static final int DELI_WORKDAY = 3000; // milliseconds for the length of workday in deli
    public static  final int DELI_ALLOWED_OVERTIME = 200; // milliseconds for the maximum allowed overtime to work in deli
    public static final int ACCOUNTANT_WORKDAY = 2500; // milliseconds for the length of workday in accountant office
    public static  final int ACCOUNTANT_ALLOWED_OVERTIME = 1300; // milliseconds for the maximum allowed overtime to work in accountant office

    public static final int TIME_TO_EAT_DINNER = 500; // milliseconds to eat dinner
    public static final int MAX_FOR_RANDOM_TIME_IN_BATHROOM = 700;
    public static final int TIME_TO_FALL_ASLEEP = 300; // milliseconds to fall asleep

    public static void main(String[] args)
    {
        wakeupCreatures(); // create and start creatures
        wakeupHumans();// create and start humans

        while (Thread.activeCount() > 2)
        {
            if (alice.reading || bob.reading)
            {
                if (!(aliceReading || bobReading))
                {
                    aliceReading = alice.reading;
                    bobReading = bob.reading;
                }
            }
        }
        System.out.println("Lights go out.");
    }

    private static void wakeupCreatures()
    {
        // create and start minions
        for (int i = 0; i < NUM_MINIONS; i++)
        {
            minions[i] = new MinionThread(i, minionNames[i]);
            minions[i].start();
        }

        // create and start dwarfs
        for (int i = 0; i < NUM_DWARFS; i++)
        {
            dwarfs[i] = new DwarfThread(i, dwarfNames[i]);
            dwarfs[i].start();
        }
    }

    private static void wakeupHumans()
    {
        // create and start Alice
        alice = new AliceThread();
        alice.start();

        // create and start Bob
        bob = new BobThread();
        bob.start();
    }
}
