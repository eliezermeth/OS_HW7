public abstract class Creature extends Worker
{
    // Strings
    public String playOutsideString;
    public String mayBePoisoned = "";
    public String fallingAsleep = " fell asleep.";

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

    protected abstract void waitForFriends();

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

    protected void eatDinner() throws InterruptedException
    {
        eatDinner(name + " is eating" + mayBePoisoned + " dinner.");
    }

    protected abstract void goToBathroom();
}
