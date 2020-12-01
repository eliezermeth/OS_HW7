import java.util.Stack;

/**
 * Class for dwarf.  Extends Creature, which extends Worker.
 */
public class DwarfThread extends Creature
{

    /**
     * Constructor for dwarf.  Sets name, work info, and lunch and play info.
     * @param c Identifying character.
     * @param i ID.
     * @param name Name.
     */
    public DwarfThread(char c, int i, String name)
    {
        super(c, i, name);

        workPlace = "mine";
        workTime = Main.MINE_WORKDAY;
        workAllowedOvertime = Main.MINE_ALLOWED_OVERTIME;

        receivedLunchString = "Have a good day!";
        playOutsideString = "is going to play outside and scrub his hands in the tub for dinner.";
    }

    /**
     * Simple constructor for dwarf.  Only needs ID and name.
     * @param i ID.
     * @param name Name.
     */
    public DwarfThread(int i, String name)
    {
        this('d', i, name);
    }

    /**
     * Wait outside cottage for all dwarfs to arrive home from work.
     */
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

    /**
     * Go to bathroom before bed.
     */
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
