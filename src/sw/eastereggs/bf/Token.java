/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
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
