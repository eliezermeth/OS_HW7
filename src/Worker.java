import java.util.Random;

/**
 * Extends Thread.  Used by human or creatures that goes to work.
 */
public abstract class Worker extends Thread
{
    // Strings
    public String name;
    public String receivedLunchString;
    public String workPlace;

    // Booleans
    public boolean receivedLunch;
    public boolean turnToGoInside;

    // Times
    public int workTime;
    public int workAllowedOvertime;

    // Random
    Random random = new Random();

    /**
     * Constructor with name.
     * @param name Name.
     */
    public Worker(String name)
    {
        super(name);
        this.name = name;
    }

    /**
     * Gets lunch for worker.
     * @throws InterruptedException Thread.
     */
    protected void getLunch() throws InterruptedException
    {
        synchronized (this)
        {
            while (!receivedLunch)
            {
                this.wait(); // wait until received lunch
            }
            // after got lunch
            System.out.println(name + ": " + receivedLunchString);
            System.out.println(name + " is going to work in the " + workPlace + ".");
            this.notify(); // allow thread to progress
        }
    }

    /**
     * Go to work.
     * @throws InterruptedException Thread.
     */
    protected void work() throws InterruptedException
    {
        Thread.sleep(workTime + random.nextInt(workAllowedOvertime));
        System.out.println(name + " is finished working at the " + workPlace + " and is now heading home.");
    }

    /**
     * Eat dinner.
     * @param eatingText Text displayed when eating.
     * @throws InterruptedException Thread.
     */
    protected void eatDinner(String eatingText) throws InterruptedException
    {
        synchronized (Main.alice) // depends on Alice
        {
            while (!Main.alice.madeDinner) // while Alice does not have dinner ready
            {
                Main.alice.wait();
            }
        }
        Thread.sleep(1000); // sleep for 1 second

        try
        {
            Main.chairAtDinnerTable.acquire(); // try to get a chair at the dinner table
            System.out.println(eatingText);
            Thread.sleep(Main.TIME_TO_EAT_DINNER);
            System.out.println(name + " finished his dinner.");
            Main.chairAtDinnerTable.release(); // leave chair and allow another to take it
        }
        catch (InterruptedException e)
        {
            Main.chairAtDinnerTable.release(); // leave chair for another player before throw error
            throw e; // still throw so see in output
        }
    }

    /**
     * Method for worker to go to sleep.
     * @param sleepingText Text displayed when falls asleep.
     * @throws InterruptedException Thread.
     */
    protected void fallAsleep(String sleepingText) throws InterruptedException
    {
        Thread.sleep(Main.TIME_TO_FALL_ASLEEP);
        System.out.println(sleepingText);
    }

    /**
     * Returns name of thread when toString() is called.
     * @return String of name of thread.
     */
    public String toString()
    {
        return getName();
    }
}
