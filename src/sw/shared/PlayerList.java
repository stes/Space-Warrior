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

import javax.management.AttributeNotFoundException;
/**
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public class PlayerList
{
    /**
     * Erstellt einen neuen Datensatz aus dem �bergebenen Paket
     * 
     * @param p Das Paket
     * @return Eine neue Spielerlisten-Instanz
     */
    private static PlayerList ausSnapshot(Paket p)
    {
        PlayerList liste = new PlayerList(p.holeZahl());
        int n = p.holeZahl();
        for (int i = 0; i < n; i++)
        {
           liste.fuegeEin(PlayerDataSet.hole(p.holePaket()), null);
        }
        return liste;
    }
    // Bezugsobjekte
    private PlayerDataSet[] _liste;

    private PlayerInput[] _eingaben;
    // Attribute

    // Konstruktor
    /**
     * Spielerliste Eine neue Spielerliste wird erstellt
     * 
     * @param groesse Die groesse der Liste 
     */
    public PlayerList(int groesse)
    {
        _liste = new PlayerDataSet[groesse];
        _eingaben = new PlayerInput[groesse];
    }
    /**
     * @return eingabe
     */
    public PlayerInput eingabeAn(int index)
    {
        return _eingaben[index];
    }

    /**
     * @return liste
     */
    public PlayerDataSet elementAn(int index)
    {
        return _liste[index];
    }
    
    /**
     * Entfernt den Spieler mit dem angegebenen Namen aus der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler geh�rt
     */
    public void entferne(String name) throws AttributeNotFoundException
    {
        if (versucheEntfernen(name))
            return;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Schreibt die Spielerliste in ein Snapshot und gibt dieses zurueck
     * 
     * @return Das Paket
     */
    public Paket erstelleSnapshot(String lokalerName)
    {
        Paket p = new Paket(Pakettype.SV_SNAPSHOT);
        p.fuegeZahlAn(this.laenge());
        p.fuegeZahlAn(this.zaehle());
        for (PlayerDataSet s : _liste)
        {
            if (s != null)
            {
                boolean lokal = s.name().equals(lokalerName);
                p.fuegePaketAn(s.pack(lokal));
            }
        }
        return p;
    }
    
    /**
     * F�gt der Liste einen neuen Datensatz hinzu
     * 
     * @param spieler Der Datensatz
     * @param eingabe Die aktuelle Spielereingabe, null fuer Standardwert
     */
    public void fuegeEin(PlayerDataSet spieler, PlayerInput eingabe)
    {
        int index = leererPlatz();
        if (index == -1)
        {
            throw new ArrayIndexOutOfBoundsException("Kein Platz in der Liste");
        }
        _liste[index] = spieler;
        _eingaben[index] = (eingabe == null ? new PlayerInput() : eingabe);
    }
    
    /**
     * Gibt die Groesse der Liste zurueck
     * 
     * @return Die Listengroesse
     */
    public int laenge()
    {
        return _liste.length;
    }
    
    // Dienste
    /**
     * Die Spielerdaten werden aus der Liste geloescht
     */
    public void leere()
    {
        _liste = new PlayerDataSet[_liste.length];
        _eingaben = new PlayerInput[_eingaben.length];
    }
    
    /**
     * Sucht den Spieler mit dem angegebenen Namen in der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler geh�rt
     */
    public PlayerDataSet suche(String name) throws AttributeNotFoundException
    {
        PlayerDataSet s = versucheSuche(name);
        if (s != null)
            return s;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Updated das Paket
     * 
     * @param p Das Paket
     */
    public void update(Paket p)
    {
        PlayerList liste = PlayerList.ausSnapshot(p);
        _liste = liste._liste;
        _eingaben = liste._eingaben;
    }
    
    /**
     * Entfernt den Spieler mit dem angegebenen Namen aus der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler geh�rt oder null wenn kein Spieler gefunden wird
     */
    public boolean versucheEntfernen(String name)
    {
        for (int i = 0; i < _liste.length; i++)
        {
            if (_liste[i] != null && _liste[i].name().equals(name))
            {
                _liste[i] = null;
                return true;
            }
        }
        return false;
    }
    
    /**
     * Setzt die Eingabe des Spielers
     * 
     * @param name Der Spielername
     * @param eingabe Eingabe des Spielers
     * @return Der Datensatz der zum Spieler geh�rt oder null wenn kein Spieler gefunden wurde
     */
    public boolean versucheSetzeEingabe(String name, PlayerInput eingabe)
    {
        for (int i = 0; i < _liste.length; i++)
        {
            PlayerDataSet s = _liste[i];
            if (s != null && s.name().equals(name))
            {
                _eingaben[i] = eingabe;
                return true;
            }
        }
        return false;
    }
    
    
    /**
     * Sucht den Spieler mit dem angegebenen Namen in der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler geh�rt oder null wenn kein Spieler gefunden wurde
     */
    public PlayerDataSet versucheSuche(String name)
    {
        for (PlayerDataSet s : _liste)
        {
            if (s != null && s.name().equals(name))
                return s;
        }
        return null;
    }
    
    /**
     * Zaehlt die Anzahl der belegten Elemente in der Liste
     * 
     * @return Die Anzahl der belegten Elemente
     */
    public int zaehle()
    {
        int n = 0;
        for (int i = 0; i < _liste.length; i++)
        {
            if (_liste[i] != null)
            {
                n++;
            }
        }
        return n;
    }
    
    /**
     * @return n�chster freier Platz in der Liste
     * @return -1 , wenn kein freier Platz mehr verf�gbar ist
     */
    private int leererPlatz()
    {
        for (int i = 0; i < _liste.length; i++)
        {
            if (_liste[i] == null)
            {
                return i;
            }
        }
        return -1;
    }
}
