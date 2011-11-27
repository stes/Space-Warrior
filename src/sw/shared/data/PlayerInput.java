/*******************************************************************************
 * Space Warrior - an open source multiplayer shooter
 *     Copyright (C) 2011 Redix stes Abbadonn
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
package sw.shared.data;

import sw.shared.Packettype;

/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerInput
{
    // Bezugsobjekte

    // Attribute
    private int _moveDirection;
    private int _turnDirection;
    private int _isShooting;
    
    // Konstruktor
    /**
     * player input  instance is created
     */
    public PlayerInput()
    {
    }
    
    /**
     * player input  instance is created
     *
     * @param moveDirection movement
     * @param turnDirection rotation
     * @param isShooting shot
     */
    public PlayerInput(
        int moveDirection,
        int turnDirection,
        int isShooting)
    {
        this();
        _moveDirection = moveDirection;
        _turnDirection = turnDirection;
        _isShooting = isShooting;
    }
    
    /**
     * creates a input-instance from a packet
     *
     * @param input input-packet
     */
    public PlayerInput(Packet input)
    {
        this(input.getNumber(), input.getNumber(),
            input.getNumber());
    }

    /**
     * creates a input-instance from the given input
     * 
     * @param input to copy the input instance
     */
    public PlayerInput(PlayerInput input)
    {
        this(input.moveDirection(), input.turnDirection(), input.schuss());
    }
    // Dienste
    
    /**
     * @return the movement
     */
    public int moveDirection()
    {
        return _moveDirection;
    }
    
    /**
     * @return the rotation
     */
    public int turnDirection()
    {
        return _turnDirection;
    }
    
    @Override
    public boolean equals(Object other)
    {
        if(!(other instanceof PlayerInput))
            return false;
        PlayerInput e = (PlayerInput) other;
        if(
            e.moveDirection() == this.moveDirection() &&
            e.turnDirection() == this.turnDirection() &&
            e.schuss() == this.schuss())
            return true;
        return false;
    }
    
    /**
     * writes the command in a packet and passes it back
     *
     * @return the packet
     */
    public Packet pack()
    {
        Packet packet = new Packet(Packettype.CL_INPUT);
        packet.addNumber(_moveDirection);
        packet.addNumber(_turnDirection);
        packet.addNumber(_isShooting);
        return packet;
    }
    
    /**
     * @return the shot
     */
    public int schuss()
    {
        return _isShooting;
    }
    
    /**
     * sets the movement
     *
     * @param value the movement
     */
    public void setzeBewegung(int wert)
    {
        if (Math.abs(wert) > 1)
            throw new IllegalArgumentException();
        _moveDirection = wert;
    }
    
    /**
     * sets the rotation
     *
     * @param value the rotation
     */
    public void setzeDrehung(int wert)
    {
        if (Math.abs(wert) > 1)
            throw new IllegalArgumentException();
        _turnDirection = wert;
    }
    
    /**
     * sets the shot
     *
     * @param value the shot
     */
    public void setzeSchuss(int wert)
    {
        _isShooting = wert;
    }
}
