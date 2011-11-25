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
 * Eine Sammlung von Konstanten, die fuer den Spielverlauf von
 * Bedeutung sind
 * 
 * @author Redix, stes, Abbadonn
 * @version 25.11.11
 */
public final class Spielkonstanten
{
    // Netzwerk
    public final static int STANDARD_PORT = 2489;
    public final static int SNAPSHOTS_PRO_SEKUNDE = 20;
    public final static int SNAPSHOT_INTERVALL = 1000 / SNAPSHOTS_PRO_SEKUNDE;
    public final static String SERVER_INFO_ANFRAGE = "swinfo?";
    public final static String SERVER_INFO_ANTWORT = "swinfo!";
    
    // Gameplay
    public final static int SPIELER_AKTUALISIERUNGS_INTERVALL = SNAPSHOT_INTERVALL;
    public final static int MAX_LEBEN = 100;
    public final static int MAX_MUNITION = 200;
    public final static int MAX_GESCHWINDIGKEIT = 16;
    public final static int MAX_BEWEGUNG_PRO_SNAP = MAX_GESCHWINDIGKEIT * SPIELER_AKTUALISIERUNGS_INTERVALL;
    public final static int MAX_REICHWEITE = 200;
    public final static int MAX_MASTER_REICHWEITE = 500;
    public final static int MAX_SPIELERZAHL = 6;
    public final static int MAX_SCHADEN = 10;
    public final static int MAX_MASTER_SCHADEN = 25;
    public final static int MUNITION_PRO_SCHUSS = 20;
    public final static int MUNITION_PRO_MSCHUSS = 100;

    
    // Steuerung
    public final static double BESCHLEUNIGUNG = 0.01 * SPIELER_AKTUALISIERUNGS_INTERVALL;
    public final static double VERZOEGERUNG = -0.01 * SPIELER_AKTUALISIERUNGS_INTERVALL;
    public final static double DREHWINKEL = 0.3 * SPIELER_AKTUALISIERUNGS_INTERVALL;
    
    // Darstellung
    public final static int SPIELERGROESSE = 64;
    public final static int SCHUSS_LEBENSDAUER = 400;
    public final static int BALKEN_LAENGE = 400;
    public final static int BALKEN_X = 10;
    
    // Spielfeld
    public final static int SPIELFELD_BREITE = 800;
    public final static int SPIELFELD_HOEHE = 600;
    public final static int BEZUGSPUNKT_X = 10;
    public final static int BEZUGSPUNKT_Y = 10;
}
