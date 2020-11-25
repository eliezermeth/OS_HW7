package program;

public class AliceThread implements Runnable
{
    private final String name = "Alice";
    private final Cottage cottage;

    public AliceThread(Cottage cottage)
    {
        this.cottage = cottage;
    }

    @Override
    public void run()
    {
        if (Driver.ALL_LOGGER || Driver.HUMAN_LOGGER) System.out.println(name + " is now awake.");
        try
        {
            produceLunch();
            //wakeBob();
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }


    }

    public void produceLunch() throws InterruptedException
    {
        while (cottage.creaturesReceivedLunch != 17) // total creatures
        {
            synchronized (cottage.availableLunchLock)
            {
                if (cottage.availableLunch) // a lunch has been made but not yet taken
                {
                    cottage.availableLunchLock.wait();
                }
                else // no lunch available
                {
                    if (Driver.ALL_LOGGER || Driver.HUMAN_LOGGER) System.out.println(name + " has made a lunch.");
                    cottage.availableLunch = true;

                    cottage.availableLunchLock.notify();
                }
            }
        }
    }

    public void wakeBob() throws InterruptedException
    {
        synchronized (cottage.allCreaturesHaveLeftLock)
        {
            if (cottage.dwarfsLeftToWork == 7) // Bob ready to wake up
            {
                cottage.allCreaturesHaveLeft = true;
                if (Driver.ALL_LOGGER || Driver.ALL_LOGGER) System.out.println(name + " is slapping Bob awake.");
                cottage.allCreaturesHaveLeftLock.notify();
            }
            else // not all creatures have left
            {
                cottage.allCreaturesHaveLeftLock.wait();
            }
        }
    }
}
