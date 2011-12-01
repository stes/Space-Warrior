/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 * Copyright (C) 2011 Redix stes Abbadonn
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package sw.eastereggs.bf;

import java.lang.StringBuilder;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
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
