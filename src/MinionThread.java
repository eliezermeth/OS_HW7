import java.util.Queue;

/**
 * Class for minion.  Extends Creature and Worker.
 */
public class MinionThread extends Creature
{
    /**
     * Constructor for minion.  Takes identifying character, ID, name.  Sets name, work info, and lunch and play info.
     * @param c Identifying character.
     * @param i ID.
     * @param name Name.
     */
    public MinionThread(char c, int i, String name)
    {
        super(c, i, name);

        workPlace = "deli";
        workTime = Main.DELI_WORKDAY;
        workAllowedOvertime = Main.DELI_ALLOWED_OVERTIME;

        receivedLunchString = "Thank you, Alice!";
        playOutsideString = "is going inside to play in the evil lair.";
    }

    /**
     * Easy constructor.  Calls other constructor.
     * @param i ID.
     * @param name Name.
     */
    public MinionThread(int i, String name)
    {
        this('m', i, name);
    }

    /**
     * Wait for all other minions to arrive at cottage before going in.
     */
    @Override
    protected void waitForFriends()
    {
        Queue<Creature> minionsWaitingOutside = Main.minionQueue;

        synchronized (minionsWaitingOutside)
        {
            minionsWaitingOutside.add(this); // add to queue

            // check if all of friends are ready to go in
            if (minionsWaitingOutside.size() == Main.NUM_MINIONS)
            {
                Main.minionsWaitingOutsideCottage = true;
                System.out.println("All minions are now waiting outside the cottage.");
                minionsWaitingOutside.notify(); // ready to go in
            }
        }
    }

    /**
     * Required from Creatures.  Since minion does not go to bathroom, it is left empty.
     */
    @Override
    protected void goToBathroom()
    {
        // does not go to bathroom -- holds it in forever
    }
}
