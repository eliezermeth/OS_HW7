package program;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

public class Cottage
{
    public final int NUM_SEATS_AT_TABLE = 5;
    public final int NUM_DWARFS = 7;
    public final int NUM_MINIONS = 10;

    public List<Runnable> waitingForLunch = new LinkedList<>();

    // for lunch
    public boolean availableLunch = false; // if Alice has made a lunch and it is not yet taken
    public final Object availableLunchLock = new Object(); // lock for lunch
    public int creaturesReceivedLunch = 0; // number of creatures received lunch
    public final Object creaturesReceivedLunchLock = new Object();

    // for creatures who have left to work
    public int minionsLeftToWork = 0;
    public final Object minionsLeftToWorkLock = new Object();
    public int dwarfsLeftToWork = 0;
    public Queue<Runnable> dwarfsReadyToLeaveToWork = new LinkedList<>();
    public final Object dwarfsLeftToWorkLock = new Object();

    // for reawakening Bob
    public boolean allCreaturesHaveLeft = false;
    public final Object allCreaturesHaveLeftLock = new Object();

    public boolean everyoneLeftForWork = false; // if dwarfs, minions, and Bob have left for work for the day


    public Queue<Runnable> minionsWaitingToEnterQueue = new ArrayBlockingQueue<>(10); // where minions wait outside when return from work
    public final Object minionsWaitingToEnterQueueLock = new Object();
    public boolean minionsReadyToEnterHouse = false;
    public boolean allMinionsHaveEnteredHouse = false;
    public String lastMinionToHoldLock = "";
    public Stack<Runnable> dwarfsWaitingToEnterStack = new Stack<>(); // where dwarfs wait outside when return from work
    public final Object dwarfsWaitingToEnterStackLock = new Object();
    public boolean dwarfsReadyToEnterHouse = false;
    public boolean allDwarfsHaveEnteredHouse = false;
    public boolean bobReadyToEnterHouse = false;

    public static final int TIME_TO_EAT_DINNER = 500; // milliseconds to eat dinner
    public static final int MINE_WORKDAY = 2000; // milliseconds for the length of workday in mine
    public static  final int MINE_ALLOWED_OVERTIME = 1000; // milliseconds for the maximum allowed overtime to work in mine
    public static final int DELI_WORKDAY = 3000; // milliseconds for the length of workday in deli
    public static  final int DELI_ALLOWED_OVERTIME = 200; // milliseconds for the maximum allowed overtime to work in deli

    public synchronized void getInLine(Runnable r)
    {
        if (r instanceof DwarfThread)
        {
            dwarfsWaitingToEnterStack.add(r);

            if (dwarfsWaitingToEnterStack.size() == 7)
            {
                System.out.println("*********************** ALL DWARFS HAVE ARRIVED");
                dwarfsReadyToEnterHouse = true;
            }
        }
        else if (r instanceof MinionThread)
        {
            minionsWaitingToEnterQueue.add(r);

            if (minionsWaitingToEnterQueue.size() == 10)
            {
                System.out.println("*********************** ALL MINIONS HAVE ARRIVED");
                minionsReadyToEnterHouse = true;
            }
        }
        else
            System.out.println("ERROR *** ERROR *** ERROR *** ERROR *** ERROR *** ERROR *** ERROR *** ERROR");
    }
}
