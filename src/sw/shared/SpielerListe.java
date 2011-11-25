package sw.shared;

import javax.management.AttributeNotFoundException;
/**
 * @author Alex Belke, Dennis Sternberg, Steffen Schneider
 * @version 15.11.11
 */
public class SpielerListe
{
    // Bezugsobjekte
    private SpielerDaten[] _liste;
    private SpielerEingabe[] _eingaben;
    // Attribute

    // Konstruktor
    /**
     * Spielerliste Eine neue Spielerliste wird erstellt
     * 
     * @param groesse Die groesse der Liste 
     */
    public SpielerListe(int groesse)
    {
        _liste = new SpielerDaten[groesse];
        _eingaben = new SpielerEingabe[groesse];
    }

    // Dienste
    /**
     * Die Spielerdaten werden aus der Liste geloescht
     */
    public void leere()
    {
        _liste = new SpielerDaten[_liste.length];
        _eingaben = new SpielerEingabe[_eingaben.length];
    }
    /**
     * F�gt der Liste einen neuen Datensatz hinzu
     * 
     * @param spieler Der Datensatz
     * @param eingabe Die aktuelle Spielereingabe, null fuer Standardwert
     */
    public void fuegeEin(SpielerDaten spieler, SpielerEingabe eingabe)
    {
        int index = leererPlatz();
        if (index == -1)
        {
            throw new ArrayIndexOutOfBoundsException("Kein Platz in der Liste");
        }
        _liste[index] = spieler;
        _eingaben[index] = (eingabe == null ? new SpielerEingabe() : eingabe);
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
     * Gibt die Groesse der Liste zurueck
     * 
     * @return Die Listengroesse
     */
    public int laenge()
    {
        return _liste.length;
    }
    
    /**
     * @return liste
     */
    public SpielerDaten elementAn(int index)
    {
        return _liste[index];
    }
    
    /**
     * @return eingabe
     */
    public SpielerEingabe eingabeAn(int index)
    {
        return _eingaben[index];
    }
    
    /**
     * Sucht den Spieler mit dem angegebenen Namen in der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler geh�rt
     */
    public SpielerDaten suche(String name) throws AttributeNotFoundException
    {
        SpielerDaten s = versucheSuche(name);
        if (s != null)
            return s;
        throw new AttributeNotFoundException();
    }
    
    /**
     * Sucht den Spieler mit dem angegebenen Namen in der
     * Liste
     * 
     * @param name Der Spielername
     * @return Der Datensatz der zum Spieler geh�rt oder null wenn kein Spieler gefunden wurde
     */
    public SpielerDaten versucheSuche(String name)
    {
        for (SpielerDaten s : _liste)
        {
            if (s != null && s.name().equals(name))
                return s;
        }
        return null;
    }
    
    /**
     * Setzt die Eingabe des Spielers
     * 
     * @param name Der Spielername
     * @param eingabe Eingabe des Spielers
     * @return Der Datensatz der zum Spieler geh�rt oder null wenn kein Spieler gefunden wurde
     */
    public boolean versucheSetzeEingabe(String name, SpielerEingabe eingabe)
    {
        for (int i = 0; i < _liste.length; i++)
        {
            SpielerDaten s = _liste[i];
            if (s != null && s.name().equals(name))
            {
                _eingaben[i] = eingabe;
                return true;
            }
        }
        return false;
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
    
    
    /**
     * Schreibt die Spielerliste in ein Snapshot und gibt dieses zurueck
     * 
     * @return Das Paket
     */
    public Paket erstelleSnapshot(String lokalerName)
    {
        Paket p = new Paket(Nachrichtentyp.SV_SNAPSHOT);
        p.fuegeZahlAn(this.laenge());
        p.fuegeZahlAn(this.zaehle());
        for (SpielerDaten s : _liste)
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
     * Updated das Paket
     * 
     * @param p Das Paket
     */
    public void update(Paket p)
    {
        SpielerListe liste = SpielerListe.ausSnapshot(p);
        _liste = liste._liste;
        _eingaben = liste._eingaben;
    }
    
    /**
     * Erstellt einen neuen Datensatz aus dem �bergebenen Paket
     * 
     * @param p Das Paket
     * @return Eine neue Spielerlisten-Instanz
     */
    private static SpielerListe ausSnapshot(Paket p)
    {
        SpielerListe liste = new SpielerListe(p.holeZahl());
        int n = p.holeZahl();
        for (int i = 0; i < n; i++)
        {
           liste.fuegeEin(SpielerDaten.hole(p.holePaket()), null);
        }
        return liste;
    }
}
