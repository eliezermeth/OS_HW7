package program;

import java.util.Random;

public class DwarfThread extends Creature
{
    private boolean inLineToLeave = false;
    private boolean leftToWork = false;
    private boolean enteredHouse = false;

    public DwarfThread(Cottage cottage, char identifier, int id, String name)
    {
        super(cottage, identifier, id, name);
    }

    @Override
    protected void goToWork() throws InterruptedException
    {
        while (!leftToWork)
        {
            synchronized (cottage.dwarfsLeftToWorkLock)
            {
                if (cottage.minionsLeftToWork == cottage.NUM_MINIONS &&
                        !cottage.dwarfsReadyToLeaveToWork.isEmpty() && cottage.dwarfsReadyToLeaveToWork.peek().toString().equals(name))
                {
                    cottage.dwarfsLeftToWork++;
                    cottage.dwarfsReadyToLeaveToWork.poll();
                    leaveCottage();
                    leftToWork = true;
                    cottage.dwarfsLeftToWorkLock.notify();
                }
                else // not all minions have left yet
                {
                    if (!inLineToLeave)
                    {
                        if (Driver.ALL_LOGGER || Driver.DWARF_LOGGER) System.out.println(name + " is ready to leave to work and is entering queue.");
                        cottage.dwarfsReadyToLeaveToWork.add(this);
                        inLineToLeave = true;
                    }
                    else
                    {
                        cottage.dwarfsLeftToWorkLock.wait();
                    }
                }
            }
        }
    }

    private void leaveCottage()
    {
        if (Driver.ALL_LOGGER || Driver.DWARF_LOGGER) System.out.println(name + " is going to work in the mine.");

        try
        {
            Random random = new Random();
            Thread.sleep(Cottage.MINE_WORKDAY + random.nextInt(Cottage.MINE_ALLOWED_OVERTIME));
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        if (Driver.ALL_LOGGER || Driver.DWARF_LOGGER) System.out.println(name + " is going home from mine.");
    }

    @Override
    protected void arriveAtHome()
    {
        if (Driver.ALL_LOGGER || Driver.DWARF_LOGGER) System.out.println(name + " has arrived and is entering stack.");

        cottage.getInLine(this);
    }

    @Override
    protected void enterHomeRoutine() throws InterruptedException
    {
        while (!cottage.dwarfsReadyToEnterHouse || !cottage.allMinionsHaveEnteredHouse) { } // infinite loop

        while (!enteredHouse)
        {
            synchronized (cottage.dwarfsWaitingToEnterStackLock)
            {
                if (cottage.dwarfsWaitingToEnterStack.peek().toString().equals(name)) // at top of stack
                {
                    if (Driver.ALL_LOGGER || Driver.DWARF_LOGGER) System.out.println(name + " has entered the house.");
                    cottage.dwarfsWaitingToEnterStack.pop();
                    enteredHouse = true;
                    cottage.dwarfsWaitingToEnterStackLock.notify();

                    if (cottage.dwarfsWaitingToEnterStack.isEmpty())
                    {
                        cottage.allDwarfsHaveEnteredHouse = true;
                    }
                }
                else
                {
                    cottage.dwarfsWaitingToEnterStackLock.wait();
                }
            }
        }
    }
}
