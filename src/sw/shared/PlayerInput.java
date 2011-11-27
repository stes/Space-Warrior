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
package sw.shared;

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
     * SpielerEingabe Instanz wird erzeugt
     */
    public PlayerInput()
    {
    }
    
    /**
     * SpielerEingabe Instanz wird erzeugt
     *
     * @param moveDirection Bewegung
     * @param turnDirection Drehung
     * @param isShooting Schuss
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
     * Erstellt eine Eingabe-Instanz aus einem Paket
     *
     * @param input Eingabe-Paket
     */
    public PlayerInput(Packet input)
    {
        this(input.holeZahl(), input.holeZahl(),
            input.holeZahl());
    }

    /**
     * ERzeugt eine Eingabe-Instanz aus der übergebenen Eingabe
     * 
     * @param input zu kopierende Eingabe-Instanz
     */
    public PlayerInput(PlayerInput input)
    {
        this(input.moveDirection(), input.turnDirection(), input.schuss());
    }
    // Dienste
    
    /**
     * @return die Bewegung
     */
    public int moveDirection()
    {
        return _moveDirection;
    }
    
    /**
     * @return die Drehung
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
     * Schreibt die Eingabe in ein Paket und gibt dieses zurueck
     *
     * @return das Paket
     */
    public Packet pack()
    {
        Packet packet = new Packet(Packettype.CL_INPUT);
        packet.fuegeZahlAn(_moveDirection);
        packet.fuegeZahlAn(_turnDirection);
        packet.fuegeZahlAn(_isShooting);
        return packet;
    }
    
    /**
     * @return der Schuss
     */
    public int schuss()
    {
        return _isShooting;
    }
    
    /**
     * setzt die Bewegung
     *
     * @param wert die Bewegung
     */
    public void setzeBewegung(int wert)
    {
        if (Math.abs(wert) > 1)
            throw new IllegalArgumentException();
        _moveDirection = wert;
    }
    
    /**
     * setzt die Drehung
     *
     * @param wert die Drehung
     */
    public void setzeDrehung(int wert)
    {
        if (Math.abs(wert) > 1)
            throw new IllegalArgumentException();
        _turnDirection = wert;
    }
    
    /**
     * setzt den Schuss
     *
     * @param wert der Schuss
     */
    public void setzeSchuss(int wert)
    {
        _isShooting = wert;
    }
}
