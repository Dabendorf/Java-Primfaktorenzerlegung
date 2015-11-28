package primfaktoren;

import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

/**
 * Diese Klasse nimmt eine Zahl entgegen und gibt dem Nutzer alle Primfaktoren dieser Zahl aus.
 * @author Lukas Schramm
 * @version 1.0
 */
public class Primfaktoren {

	private NumberFormat format = NumberFormat.getInstance(); 
	private NumberFormatter formatter = new NumberFormatter(format);
	private String umbruch = System.getProperty("line.separator");
	private int zahl;
	private int faktor;
	private int restwert;
	private int tempPruefzahl = 1;
	private ArrayList<Integer> primzahlen = new ArrayList<Integer>();
	private long zeit;
	
	public Primfaktoren() {
		format.setGroupingUsed(false); 
		formatter.setAllowsInvalid(false);
	}
	
	/**
	 * Diese Methode nimmt die Zahl in einem Eingabefeld an, die der Nutzer eingibt.<br>
	 * Sie fragt die Zahl ab, die der Nutzer auf ihre Primfaktoren ueberpruefen moechte.<br>
	 * Wenn der Nutzer nichts eingibt, wird eine Fehlermeldung ausgegeben.<br>
	 * Bei extrem grossen Werten wird der Nutzer gewarnt, dass die Rechenzeit laenger sein koennte.<br>
	 * Die Methode leitet direkt zu faktorenBestimmen() weiter, wo die Primfaktoren berechnet werden.
	 */
	private void eingabe() {
		long zahltemp;
		JFormattedTextField nummernfeld = new JFormattedTextField(formatter);
		Object[] zahlenfrage = {"Bitte gib eine zu prüfende Zahl ein.", nummernfeld};
		JOptionPane pane = new JOptionPane(zahlenfrage, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		pane.createDialog(null, "Primfaktoren").setVisible(true);
		String zahlStr = nummernfeld.getText();
		try {
			if(zahlStr.equals("")) {
				JOptionPane.showMessageDialog(null, "Bitte gib eine natürliche Zahl ein!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
				eingabe();
			} else {
				zahltemp = Long.parseLong(zahlStr);
				if(zahltemp < 1) {
					JOptionPane.showMessageDialog(null, "Bitte gib eine natürliche Zahl ein!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
					eingabe();
				} else if(zahltemp > Integer.MAX_VALUE) {
					JOptionPane.showMessageDialog(null, "Bitte gib eine Zahl ein, die kleiner als 2.147.483.647 ist!"+umbruch+"Dein Computer wird es Dir danken.", "Zu große Zahl", JOptionPane.ERROR_MESSAGE);
					eingabe();
				} else if(zahltemp > 50000000) {
					JOptionPane.showMessageDialog(null, "Bitte beachte, dass die Berechnungszeit\nbei größeren Zahlen etwas länger sein kann.", "Warnung", JOptionPane.WARNING_MESSAGE);
					zahl = (int)zahltemp;
					faktorenBestimmen();
				} else {
					zahl = (int)zahltemp;
					faktorenBestimmen();
				}
			}
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Bitte gib eine natürliche Zahl ein!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
			eingabe();
		}
	}
	
	/**
	 * Diese Methode nimmt sich die naechste Primzahl und ueberprueft solange die Teilbarkeit selbiger in der angegebenen Zahl, bis alle Primfaktoren gefunden wurden.
	 */
	public void faktorenBestimmen() {
		zeit = - System.currentTimeMillis();
		faktor = naechstePruefzahl();
		restwert = zahl;
		while(2*faktor <= restwert) {
			if(restwert%faktor==0) {
				primzahlen.add(faktor);
				restwert /= faktor;
			} else {
				faktor = naechstePruefzahl();
			}
		}
		primzahlen.add(restwert);
	}
	
	/**
	 * Diese Methode gibt die Primzahlen, beginnend bei 2 in Reihenfolge aus, solange bis das Programm keine weiteren Faktoren mehr benoetigt.
	 * @return Gibt eine Primzahl aus
	 */
	private int naechstePruefzahl() {
		boolean prim = false;
		while(prim==false) {
			if(tempPruefzahl==1||tempPruefzahl==2) {
				tempPruefzahl++;
				prim = true;
			} else {
				tempPruefzahl+=2;
				boolean primtemp = true;
				for(int n=3;n<=Math.sqrt(tempPruefzahl);n+=2) {
					if(tempPruefzahl%n == 0) {
						primtemp = false;
						break;
					}
				}
				if(primtemp==true) {
					prim=true;
				}
			}
		}
		return tempPruefzahl;
	}
	
	/**
	 * Diese Methode gibt dem Nutzer eine Liste aller Primfaktoren aus. Falls es sich um eine Primzahl handelt, wird dies signalisiert.
	 * Anschliessend leitet er das Programm in die neustart()-Methode weiter.
	 */
	private void ausgabe() {
		System.out.println(primzahlen);
		System.out.print(zeit+=System.currentTimeMillis());
		System.out.println("ms");
		String ausgabestr = "";
		for(int i=0;i<primzahlen.size();i++) {
			if(i<primzahlen.size()-1) {
				ausgabestr += primzahlen.get(i)+"x";
			} else {
				ausgabestr += primzahlen.get(i);
			}
		}
		if(primzahlen.get(0)==zahl) {
			JOptionPane.showMessageDialog(null, "Die "+zahl+" ist eine Primzahl."+umbruch+"Ihr einziger Primfaktor ist demzufolge sie selbst.", "Ergebnis", JOptionPane.PLAIN_MESSAGE);
		} else {
			JOptionPane.showMessageDialog(null, "Die "+zahl+" hat folgende Primfaktoren:"+umbruch+ausgabestr, "Ergebnis", JOptionPane.PLAIN_MESSAGE);
		}
		neustart();
	}
	
	/**
	 * Diese Methode fragt den Nutzer, ob er eine weitere Zahl ueberpruefen moechte und startet je nach Nutzereingabe entweder eine neue Abfrage oder beendet das Programm.
	 */
	private void neustart() {
		int dialogneustart = JOptionPane.showConfirmDialog(null, "Möchtest Du eine neue Zahl prüfen?", "Neue Abfrage?", JOptionPane.YES_NO_OPTION);
        if(dialogneustart == 0) {
        	Primfaktoren pf = new Primfaktoren();
    		pf.eingabe();
    		pf.ausgabe();
        } else {
     	   System.exit(0);
        }
	}
	
	public ArrayList<Integer> getPrimzahlen() {
		return primzahlen;
	}

	public void setZahl(int zahl) {
		this.zahl = zahl;
	}

	public static void main(String[] args) {
		Primfaktoren pf = new Primfaktoren();
		pf.eingabe();
		pf.ausgabe();
	}
}