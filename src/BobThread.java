public class BobThread extends Worker
{
    // Booleans
    public boolean allowedToGoToSleep;
    public boolean reading;

    public BobThread()
    {
        super("Bob");

        workPlace = "accountant office";
        workTime = Main.ACCOUNTANT_WORKDAY;
        workAllowedOvertime = Main.ACCOUNTANT_ALLOWED_OVERTIME;

        receivedLunchString = "Looks good.";
    }

    @Override
    public void run()
    {
        try
        {
            System.out.println(name + " is now awake.");
            getLunch();
            work();
            enterCottage();
            eatDinner(name + " is now eating dinner.");
            waitForSleepAlert();
            fallAsleep(name + " has fallen asleep.");
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    private void enterCottage() throws InterruptedException
    {
        while (!Main.dwarfsInsideCottage)
        {
            this.wait();
        }
        System.out.println(name + " lets himself into the cottage.");
    }

    private void waitForSleepAlert() throws InterruptedException
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
