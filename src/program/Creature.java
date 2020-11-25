package program;

public abstract class Creature implements Runnable
{
    protected String name;
    protected Cottage cottage;
    protected boolean hasLunch = false;

    public Creature(Cottage cottage, char identifier, int id, String name)
    {
        this.cottage = cottage;
        this.name = name + "(" + identifier + ++id + ")";
    }

    @Override
    public void run()
    {
        if (Driver.ALL_LOGGER || Driver.CREATURE_LOGGER) System.out.println(name + " is now awake.");
        try
        {
            consumeLunch();
            goToWork();
            arriveAtHome();
            enterHomeRoutine();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        eatDinner();
    }

    protected void consumeLunch() throws InterruptedException
    {
        if (Driver.ALL_LOGGER || Driver.CREATURE_LOGGER) System.out.println(name + " is waiting for lunch.");

        // process to wait for lunch
        while (!hasLunch)
        {
            synchronized (cottage.availableLunchLock)
            {
                if (cottage.availableLunch)
                {
                    hasLunch = true;
                    if (Driver.ALL_LOGGER || Driver.CREATURE_LOGGER) System.out.println(name + " has received lunch.");
                    cottage.availableLunch = false; // remove lunch from cottage

                    synchronized (cottage.creaturesReceivedLunchLock)
                    {
                        cottage.creaturesReceivedLunch++;
                    }

                    cottage.availableLunchLock.notify();
                }
                else // no lunch available
                {
                    cottage.availableLunchLock.wait();
                }
            }
        }
    }

    protected abstract void goToWork() throws InterruptedException;
    protected abstract void arriveAtHome();
    protected abstract void enterHomeRoutine() throws InterruptedException;

    protected void eatDinner()
    {
        if (Driver.ALL_LOGGER || Driver.CREATURE_LOGGER) System.out.println(name + " is eating dinner.");
        try
        {
            Thread.sleep(Cottage.TIME_TO_EAT_DINNER);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        if (Driver.ALL_LOGGER || Driver.CREATURE_LOGGER) System.out.println(name + " is finished dinner.");
    }

    public String toString()
    {
        return name;
    }
}
