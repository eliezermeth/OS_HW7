package program;

public class BobThread implements Runnable
{
    private String name = "Bob";
    private Cottage cottage;

    public BobThread(Cottage cottage)
    {
        this.cottage = cottage;
    }

    @Override
    public void run()
    {
        if (Driver.ALL_LOGGER || Driver.HUMAN_LOGGER) System.out.println(name + " is now awake.");

        try
        {
            morningSleepRoutine();
            goToWork();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }


    }

    private void morningSleepRoutine() throws InterruptedException
    {
        if (Driver.ALL_LOGGER || Driver.HUMAN_LOGGER) System.out.println(name + " has gone back to sleep.");

        synchronized (cottage.allCreaturesHaveLeftLock)
        {
            if (cottage.allCreaturesHaveLeft) // all creatures have left; can wake
            {
                if (Driver.ALL_LOGGER || Driver.HUMAN_LOGGER) System.out.println(name + " has reawoken.");
                cottage.allCreaturesHaveLeftLock.notify();
            }
            else
            {
                cottage.allCreaturesHaveLeftLock.wait();
            }
        }
    }

    private void goToWork()
    {

    }
}
