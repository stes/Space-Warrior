package sw.eastereggs.fortytwo;


/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class FortyTwo
{
    public static boolean answer(String question)
    {
        String q = question.toLowerCase();
        if (q.contains("what") &&
            q.contains("answer") &&
            q.contains("life") &&
            q.contains("universe") &&
            q.contains("everything"))
            {
                System.out.println("42");
                return true;
            }
        return false;
    }
}
