import java.util.Queue;
import java.util.Random;
import java.util.Stack;

public class AliceThread extends Thread
{
    // Strings
    private String name;

    // Booleans
    public boolean madeDinner;
    public boolean reading;

    // Random
    Random random = new Random();

    public AliceThread()
    {
        super("Alice");
        name = "Alice";
    }

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
            waitForCreaturesToSleep();
            sleep();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

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

    // for minions // TODO figure out how to merge
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

    private void notifyBob()
    {
        synchronized (Main.bob)
        {
            Main.bob.turnToGoInside = true;
            Main.bob.notify();
        }
    }

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

    private void waitForCreaturesToSleep()
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

    private void sleep() throws InterruptedException
    {
        Thread.sleep(Main.TIME_TO_FALL_ASLEEP * 5); // extended time for human
        System.out.println(name + " has fallen asleep.");
    }
}
