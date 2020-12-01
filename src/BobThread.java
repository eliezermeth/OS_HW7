/**
 * Thread for Bob.  Extends Worker.
 */
public class BobThread extends Worker
{
    // Booleans
    public boolean allowedToGoToSleep;
    public boolean reading;

    /**
     * Constructor for Bob.  Sets name, work info, and lunch info.
     */
    public BobThread()
    {
        super("Bob");

        workPlace = "accountant office";
        workTime = Main.ACCOUNTANT_WORKDAY;
        workAllowedOvertime = Main.ACCOUNTANT_ALLOWED_OVERTIME;

        receivedLunchString = "Looks good.";
    }

    /**
     * Run Bob's day.
     */
    @Override
    public void run()
    {
        try
        {
            System.out.println(name + " is now awake.");
            System.out.println(name + " went back to sleep.");
            getLunch();
            work();
            enterCottage();
            eatDinner(name + " is now eating dinner.");
            waitToGoToSleep();
            fallAsleep(name + " has fallen asleep.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Print Bob entering cottage.
     */
    private void enterCottage()
    {
        System.out.println(name + " lets himself into the cottage.");
    }

    /**
     * Wait to go to sleep and read in the meantime.
     * @throws InterruptedException Threads.
     */
    private void waitToGoToSleep() throws InterruptedException
    {
        synchronized (this)
        {
            while (!allowedToGoToSleep)
            {
                this.wait();
            }
        }

        System.out.println(name + " is reading on the couch.");
        reading = true;
    }
}
