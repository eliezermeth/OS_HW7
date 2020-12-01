import java.util.Queue;
import java.util.Stack;

/**
 * Thread for Alice.
 */
public class AliceThread extends Thread
{
    // Strings
    private final String name;

    // Booleans
    public boolean madeDinner;
    public boolean reading;

    public AliceThread()
    {
        super("Alice");
        name = "Alice";
    }

    /**
     * Method to run Alice's day.
     */
    @Override
    public void run()
    {
        try
        {
            System.out.println(name + " is now awake.");
            makeLunches();
            slapBobAwake();
            awaitCreaturesComingHome(Main.minionQueue); // minions
            awaitCreaturesComingHome(Main.dwarfQueue); // dwarfs
            notifyBob();
            makeDinner();
            waitForCreaturesToSleepAndRead();
            sleep();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to make Alice to make lunch for minions and dwarfs.
     * @throws InterruptedException Threads.
     */
    private void makeLunches() throws InterruptedException
    {
        // in order of leaving - minions then dwarfs
        for (Creature c : Main.minions)
        {
            makeLunchForCreature(c);
        }

        for (Creature c : Main.dwarfs)
        {
            makeLunchForCreature(c);
        }
    }

    /**
     * Accepts a creature and gives him lunch.
     * @param c Creature (dwarf or minion).
     * @throws InterruptedException Threads.
     */
    private void makeLunchForCreature(Creature c) throws InterruptedException
    {
        synchronized (c) // take control of minion temporarily
        {
            System.out.println(name + " is making lunch for " + c.name);
            c.receivedLunch = true;
            c.notify();
            c.wait();
        }
    }

    /**
     * Slap the lazy man awake and send him off to work.
     * @throws InterruptedException Thread.
     */
    private void slapBobAwake() throws InterruptedException
    {
        BobThread bob = Main.bob;

        synchronized (bob)
        {
            System.out.println(name + " is making lunch for " + bob.name);
            bob.receivedLunch = true;
            bob.notify();
            bob.wait();
        }
    }

    /**
     * Method to hold the minions outside and allow them inside when they all arrived.
     * TODO figure out how to merge with other awaitCreaturesComingHome
     * @param creatures Queue of creatures, since the first to arrive is the first to enter.
     * @throws InterruptedException Threads.
     */
    private void awaitCreaturesComingHome(Queue<Creature> creatures) throws InterruptedException
    {
        synchronized (creatures)
        {
            while (!Main.minionsWaitingOutsideCottage)
            {
                creatures.wait();
            }

            System.out.println("There is a knock on the door.  From the snickering and slapping sounds, Alice figures it is the minions.");
            System.out.println("Alice opens the door.");

            while (!creatures.isEmpty())
            {
                Creature nextCreature = creatures.poll();

                synchronized (nextCreature)
                {
                    nextCreature.turnToGoInside = true;
                    nextCreature.notify();
                    nextCreature.wait();
                }

                Main.minionsInsideCottage = true;
            }
        }
    }

    // for dwarfs // TODO figure out how to merge
    /**
     * Method to hold the dwarfs outside and allow them inside when they all arrived.
     * TODO figure out how to merge with other awaitCreaturesComingHome
     * @param creatures Stack of creatures, since the first to arrive is the last to enter.
     * @throws InterruptedException Threads.
     */
    private void awaitCreaturesComingHome(Stack<Creature> creatures) throws InterruptedException
    {
        synchronized (creatures)
        {
            while (!Main.dwarfsWaitingOutsideCottage)
            {
                creatures.wait();
            }

            System.out.println("There is a knock on the door.  From the sneezing, grumbling, and other noises, Alice figures it is the dwarfs.");
            System.out.println("Alice opens the door.");

            while (!creatures.isEmpty())
            {
                Creature nextCreature = creatures.pop();

                synchronized (nextCreature)
                {
                    nextCreature.turnToGoInside = true;
                    nextCreature.notify();
                    nextCreature.wait();
                }

                Main.dwarfsInsideCottage = true;
            }
        }
    }

    /**
     * Tell Bob he is allowed to enter the house now.
     */
    private void notifyBob()
    {
        synchronized (Main.bob)
        {
            Main.bob.turnToGoInside = true;
            System.out.println("Bob can now enter the cottage.");
            Main.bob.notify();
        }
    }

    /**
     * Make dinner for all members of the cottage.
     * @throws InterruptedException Threads.
     */
    private void makeDinner() throws InterruptedException
    {
        Thread.sleep(Main.TIME_TO_EAT_DINNER * 20); // time to make dinner = 1 per body + 2 (or 1)

        System.out.println(name + " made slop for dinner.");

        synchronized (this)
        {
            madeDinner = true;
            this.notifyAll(); // tell everyone that dinner is ready
        }
    }

    /**
     * Wait for creatures to sleep, then read book
     */
    private void waitForCreaturesToSleepAndRead()
    {
        while (Thread.activeCount() > 4); // creatures awake

        System.out.println("Light goes on.");
        System.out.println(name + " is going to read on couch.");
        reading = true;
        synchronized (Main.bob) // Alice stays up longer - make random
        {
            Main.bob.allowedToGoToSleep = true;
            Main.bob.notify();
        }
    }

    /**
     * Go to sleep.
     * @throws InterruptedException Threads.
     */
    private void sleep() throws InterruptedException
    {
        Thread.sleep(Main.TIME_TO_FALL_ASLEEP * 5); // extended time for human
        System.out.println(name + " has fallen asleep.");
    }

    /**
     * Returns name of thread when toString() is called.
     * @return String of name.
     */
    public String toString()
    {
        return getName();
    }
}
