package sw.eastereggs.bf;


/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class Token
{
    public static final char INC_POINTER = '>';
    public static final char DEC_POINTER = '<';
    public static final char INC_VALUE = '+';
    public static final char DEC_VALUE = '-';
    public static final char OUTP = '.';
    public static final char READ = ',';
    public static final char WHILE_GE_NULL = '[';
    public static final char END_WHILE = ']';
    
    public static boolean isValid(char token)
    {
        return token == INC_POINTER ||
               token == DEC_POINTER ||
               token == INC_VALUE ||
               token == DEC_VALUE ||
               token == OUTP ||
               token == READ ||
               token == WHILE_GE_NULL ||
               token == END_WHILE;
    }
}
