package program;

import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import java.util.Random;

public class MinionThread extends Creature
{
    private boolean enteredHouse = false;

    public MinionThread(Cottage cottage, char identifier, int id, String name)
    {
        super(cottage, identifier, id, name);
    }

    @Override
    protected void goToWork() // can go directly to work without waiting
    {
        synchronized (cottage.minionsLeftToWorkLock)
        {
            cottage.minionsLeftToWork++;
        }
        leaveCottage();
    }

    private void leaveCottage()
    {
        if (Driver.ALL_LOGGER || Driver.MINION_LOGGER) System.out.println(name + " is going to work in the deli.");

        try
        {
            Random random = new Random();
            Thread.sleep(Cottage.DELI_WORKDAY + random.nextInt(Cottage.DELI_ALLOWED_OVERTIME));
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        if (Driver.ALL_LOGGER || Driver.MINION_LOGGER) System.out.println(name + " is going home from deli.");
    }

    @Override
    protected void arriveAtHome()
    {
        if (Driver.ALL_LOGGER || Driver.MINION_LOGGER) System.out.println(name + " has arrived and is entering queue.");

        cottage.getInLine(this);
    }

    @Override
    protected void enterHomeRoutine() throws InterruptedException
    {
        while (!cottage.minionsReadyToEnterHouse) { } // infinite loop

        while (!enteredHouse)
        {
            synchronized (cottage.minionsWaitingToEnterQueueLock)
            {
                System.out.println(name + " checking " + cottage.minionsWaitingToEnterQueue.peek().toString());
                if (cottage.minionsWaitingToEnterQueue.peek().toString().equals(name)) // at front of queue
                {
                    if (Driver.ALL_LOGGER || Driver.MINION_LOGGER) System.out.println(name + " has entered the house.");
                    cottage.minionsWaitingToEnterQueue.poll();
                    enteredHouse = true;
                    cottage.minionsWaitingToEnterQueueLock.notify();

                    if (cottage.minionsWaitingToEnterQueue.isEmpty())
                    {
                        cottage.allMinionsHaveEnteredHouse = true;
                    }
                }
                else
                {
                    cottage.minionsWaitingToEnterQueueLock.wait();
                }
            }
        }
    }
}