import java.util.List;

/**
 * Driving class.
 *
 * @author Eliezer Meth
 * Start Date: 2020-11-27
 * Last Modified: 2020-11-23
 */
public class Main extends Thread
{
    public static String[] dwarfs = {"Doc", "Happy", "Sneezy", "Sleepy", "Bashful", "Grumpy", "Dopey"};
    public static String[] minions = {"Bob", "Carl", "Dave", "Jerry", "John", "Kevin", "Mark", "Phil", "Stuart", "Tim"};

    protected List<Thread> wakeup;
    protected List<Thread> atWork;

    public static void main(String[] args)
    {
        Main main = new Main();
        main.start();
    }

    @Override
    public void run()
    {


    }

}
