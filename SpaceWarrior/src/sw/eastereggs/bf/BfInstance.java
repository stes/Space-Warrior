package sw.eastereggs.bf;

import java.io.IOException;

/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class BfInstance
{
    // Bezugsobjekte
    private String _sourceCode;
    
    // Attribute
    private int _programPointer;
    private int _pointer;
    private char[] _memory;
    
    // Konstruktor
    public BfInstance(String code)
    {
        _sourceCode = code;
        _programPointer = 0;
        _pointer = 0;
        _memory = new char[65536];
    }

    // Dienste
    public void execute()
    {
        _programPointer = 0;
        int _breakPoint = 0;
        while (_programPointer < _sourceCode.length())
        {
            switch (_sourceCode.charAt(_programPointer))
            {
                case Token.INC_POINTER:
                {
                    _pointer++;
                    if (_pointer >= _memory.length)
                        _pointer = 0;
                    _programPointer++;
                    break;
                }
                case Token.DEC_POINTER:
                {
                    _pointer--;
                    if (_pointer < 0)
                        _pointer = _memory.length - 1;
                    _programPointer++;
                    break;
                }
                case Token.INC_VALUE:
                {
                    _memory[_pointer]++;
                    _programPointer++;
                    break;
                }
                case Token.DEC_VALUE:
                {
                    _memory[_pointer]--;
                    _programPointer++;
                    break;
                }
                case Token.OUTP:
                {
                    System.out.print(_memory[_pointer]);
                    _programPointer++;
                    break;
                }
                case Token.READ:
                {
                    try
                    {
                        _memory[_pointer] = (char)System.in.read();
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                    _programPointer++;
                    break;
                }
                case Token.WHILE_GE_NULL:
                {
                    _breakPoint = _programPointer;
                    if (_memory[_pointer] <= 0)
                    {
                        while (_sourceCode.charAt(_programPointer) != Token.END_WHILE)
                        {
                            _programPointer++;
                        }
                    }
                    _programPointer++;
                    break;
                }
                case Token.END_WHILE:
                {
                    _programPointer = _breakPoint;
                    break;
                }
            }
        }
    }
}
