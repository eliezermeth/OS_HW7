import java.util.Queue;
import java.util.Stack;

public class DwarfThread extends Creature
{

    public DwarfThread(char c, int i, String name)
    {
        super(c, i, name);

        workPlace = "mine";
        workTime = Main.MINE_WORKDAY;
        workAllowedOvertime = Main.MINE_ALLOWED_OVERTIME;

        receivedLunchString = "Have a good day!";
        playOutsideString = "is going to play outside and scrub his hands in the tub for dinner.";
    }

    public DwarfThread(int i, String name)
    {
        this('d', i, name);
    }

    @Override
    protected void waitForFriends()
    {
        Stack<Creature> dwarfsWaitingOutside = Main.dwarfQueue;

        synchronized (dwarfsWaitingOutside)
        {
            dwarfsWaitingOutside.add(this); // add to queue

            // check if all of friends are ready to go in
            if (dwarfsWaitingOutside.size() == Main.NUM_DWARFS)
            {
                Main.dwarfsWaitingOutsideCottage = true;
                System.out.println("All dwarfs are now waiting outside the cottage.");
                dwarfsWaitingOutside.notify(); // ready to go in
            }
        }
    }

    @Override
    protected void goToBathroom()
    {
        try
        {
            Main.bathroom.acquire(); // enter bathroom and lock door
            System.out.println(name + " entered the bathroom.");
            Thread.sleep(random.nextInt(Main.MAX_FOR_RANDOM_TIME_IN_BATHROOM)); // in bathroom up to max
            System.out.println(name + " finished with the bathroom.");
            Main.bathroom.release(); // exit bathroom and allow someone else to enter
        }
        catch (InterruptedException e)
        {
            Main.bathroom.release(); // leave bathroom so someone else can enter
            e.printStackTrace(); // show error
        }
    }
}
