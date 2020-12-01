/**
 * Class for creature (in spec of assignment, non-human).  Contains general methods not relevant to Worker, which this
 * class extends.
 */
public abstract class Creature extends Worker
{
    // Strings
    public String playOutsideString;
    public String mayBePoisoned = "";
    public String fallingAsleep = " fell asleep.";

    /**
     * Constructor for creature (dwarf or minion).
     * @param c Character of creature.
     * @param i Creature ID number.
     * @param name Name of creature.
     */
    public Creature(char c, int i, String name)
    {
        super(name + "(" + c + ++i + ")");

        // create death chance
        if (random.nextInt(10) == 9)
        {
            mayBePoisoned = " a poisoned apple for";
            fallingAsleep = " died.";
        }
    }

    /**
     * Method to run the thread.
     */
    @Override
    public void run()
    {
        try
        {
            System.out.println(name + " is now awake.");
            getLunch();
            work();
            waitForFriends();
            play();
            eatDinner();
            goToBathroom();
            fallAsleep(name + fallingAsleep);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Method to make the creature to wait for the rest of their kind before attempting to enter cottage.
     */
    protected abstract void waitForFriends();

    /**
     * Method for creature to play.
     * @throws InterruptedException Threads.
     */
    protected void play() throws InterruptedException
    {
        synchronized (this)
        {
            while (!turnToGoInside)
            {
                this.wait();
            }
            System.out.println(name + " " + playOutsideString);
            this.notify();
        }
    }

    /**
     * For creature to eat dinner; may be poisoned
     * @throws InterruptedException Threads.
     */
    protected void eatDinner() throws InterruptedException
    {
        eatDinner(name + " is eating" + mayBePoisoned + " dinner.");
    }

    /**
     * To go to bathroom; left blank if not needed.
     */
    protected abstract void goToBathroom();
}
