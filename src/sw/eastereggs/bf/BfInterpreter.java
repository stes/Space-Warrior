package sw.eastereggs.bf;

import java.lang.StringBuilder;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class BfInterpreter
{
    public static void main(String[] args)
    {
        if (args != null && args[0] != null)
        {
            BfInterpreter bf = new BfInterpreter();
            System.out.println("Reading code");
            bf.readCode(args[0]);
            System.out.println("Executing");
            bf.execute();
        }
        else
        {
            System.out.println("Invalid arguments");
        }
    }
    
    // Bezugsobjekte
    private BfInstance _lastBuild;
    
    // Attribute

    // Konstruktor
    public BfInterpreter()
    {

    }

    // Dienste
    public void readCode(String code)
    {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < code.length(); i++)
        {
            char current = code.charAt(i);
            if (Token.isValid(current))
            {
                sb.append(current);
            }
        }
        _lastBuild = new BfInstance(sb.toString());
    }
    
    public void execute()
    {
        if (_lastBuild != null)
        {
            _lastBuild.execute();
        }
    }
}
