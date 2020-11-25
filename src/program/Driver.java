package program;

public class Driver
{
    public static boolean ALL_LOGGER = true;
    public static boolean HUMAN_LOGGER = true;
    public static boolean CREATURE_LOGGER = false;
    public static boolean DWARF_LOGGER = true;
    public static boolean MINION_LOGGER = false;

    public static String[] dwarfs = {"Doc", "Happy", "Sneezy", "Sleepy", "Bashful", "Grumpy", "Dopey"};
    public static String[] minions = {"Bob", "Carl", "Dave", "Jerry", "John", "Kevin", "Mark", "Phil", "Stuart", "Tim"};

    public static void main(String[] args)
    {
        Cottage cottage = new Cottage();

        Thread alice = new Thread(new AliceThread(cottage));
        alice.start();
        Thread bob = new Thread(new BobThread(cottage));
        //bob.start();

        for (int i = 0; i < minions.length; i++)
        {
            Thread m = new Thread(new MinionThread(cottage, 'm', i, minions[i]));
            m.start();
        }

        for (int i = 0; i < dwarfs.length; i++)
        {
            Thread d = new Thread(new DwarfThread(cottage, 'd', i, dwarfs[i]));
            d.start();
        }

    }
}
