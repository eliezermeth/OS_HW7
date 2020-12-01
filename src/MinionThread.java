import java.util.Queue;

public class MinionThread extends Creature
{
    public MinionThread(char c, int i, String name)
    {
        super(c, i, name);

        workPlace = "deli";
        workTime = Main.DELI_WORKDAY;
        workAllowedOvertime = Main.DELI_ALLOWED_OVERTIME;

        receivedLunchString = "Thank you, Alice!";
        playOutsideString = "is going inside to play in the evil lair.";
    }

    public MinionThread(int i, String name)
    {
        this('m', i, name);
    }

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

    @Override
    protected void goToBathroom()
    {
        // does not go to bathroom -- holds it in forever
    }
}
