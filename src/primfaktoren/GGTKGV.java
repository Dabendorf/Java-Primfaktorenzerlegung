package primfaktoren;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;

import javax.swing.JFormattedTextField;
import javax.swing.JOptionPane;
import javax.swing.text.NumberFormatter;

/**
 * Diese Klasse gibt ausgehend von den Ergebnissen aus Objekten der Klasse Primfaktoren den ggT und den kgV zweier Zahlen aus.
 * @author Lukas Schramm
 * @version 1.0
 */
public class GGTKGV {

	private NumberFormat format = NumberFormat.getInstance(); 
	private NumberFormatter formatter = new NumberFormatter(format);
	private String umbruch = System.getProperty("line.separator");
	private int zahl1;
	private int zahl2;
	
	public GGTKGV() {
		format.setGroupingUsed(false); 
		formatter.setAllowsInvalid(false);
		eingabe();
	}
	
	/**
	 * Diese Methode nimmt die Zahlen in einem Eingabefeld an, die der Nutzer eingibt.<br>
	 * Sie fragt die Zahlen ab, die der Nutzer auf Basis der Primfaktorenzerlegung nach ihrem ggT und ihrem kgV befragen moechte.<br>
	 * Wenn der Nutzer nichts eingibt, wird eine Fehlermeldung ausgegeben.<br>
	 * Bei extrem grossen Werten wird der Nutzer gewarnt, dass die Rechenzeit laenger sein koennte.<br>
	 * Die Methode leitet direkt zu berechnen() weiter, wo die Primfaktoren berechnet werden.
	 */
	private void eingabe() {
		long zahltemp1;
		long zahltemp2;
		JFormattedTextField nummernfeld1 = new JFormattedTextField(formatter);
		JFormattedTextField nummernfeld2 = new JFormattedTextField(formatter);
		Object[] nummernfelder = {nummernfeld1,nummernfeld2};
		Object[] zahlenfrage = {"Bitte gib die beiden Zahlen ein.",nummernfelder};
		JOptionPane pane = new JOptionPane(zahlenfrage, JOptionPane.PLAIN_MESSAGE, JOptionPane.DEFAULT_OPTION);
		pane.createDialog(null, "ggT und kgV").setVisible(true);
		String zahlStr1 = nummernfeld1.getText();
		String zahlStr2 = nummernfeld2.getText();
		try {
			if(zahlStr1.equals("") || zahlStr2.equals("")) {
				JOptionPane.showMessageDialog(null, "Bitte gib zwei natürliche Zahlen ein!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
				eingabe();
			} else {
				zahltemp1 = Long.parseLong(zahlStr1);
				zahltemp2 = Long.parseLong(zahlStr2);
				if(zahltemp1 < 1 || zahltemp2 < 1) {
					JOptionPane.showMessageDialog(null, "Bitte gib zwei natürliche Zahlen ein!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
					eingabe();
				} else if(zahltemp1 > Integer.MAX_VALUE || zahltemp2 > Integer.MAX_VALUE) {
					JOptionPane.showMessageDialog(null, "Bitte gib zwei Zahlen ein, die kleiner als 2.147.483.647 sind!"+umbruch+"Dein Computer wird es Dir danken.", "Zu große Zahlen", JOptionPane.ERROR_MESSAGE);
					eingabe();
				} else if(zahltemp1 > 50000000 || zahltemp2 > 50000000) {
					JOptionPane.showMessageDialog(null, "Bitte beachte, dass die Berechnungszeit\nbei größeren Zahlen etwas länger sein kann.", "Warnung", JOptionPane.WARNING_MESSAGE);
					zahl1 = (int)zahltemp1;
					zahl2 = (int)zahltemp2;
					berechnen();
				} else {
					zahl1 = (int)zahltemp1;
					zahl2 = (int)zahltemp2;
					berechnen();
				}
			}
		} catch(NumberFormatException e) {
			JOptionPane.showMessageDialog(null, "Bitte gib zwei natürliche Zahlen ein!", "Ungültige Eingabe", JOptionPane.ERROR_MESSAGE);
			eingabe();
		}
	}
	
	/**
	 * Diese Methode berechnet anhand der Regeln der Primfaktorenzerlegung den GGT und das KGV.<br>
	 * Sollte bei der Berechnung des KGVs der Wertebereich ueberschritten werden, wird die Exception gecatcht und mit BigInteger neu berechnet.
	 */
	private void berechnen() {
		Primfaktoren pf1 = new Primfaktoren();
		Primfaktoren pf2 = new Primfaktoren();
		pf1.setZahl(zahl1);
		pf2.setZahl(zahl2);
		pf1.faktorenBestimmen();
		pf2.faktorenBestimmen();
		ArrayList<Integer> faktoren1 = new ArrayList<Integer>(pf1.getPrimzahlen());
		ArrayList<Integer> faktoren2 = new ArrayList<Integer>(pf2.getPrimzahlen());
		
		int ggt = 1;
		b:for(Integer f1:faktoren1) {
			for(Integer f2:faktoren2) {
				if(f1==f2 && f1!=-1) {
					ggt *= f1;
					faktoren1.set(faktoren1.indexOf(f1),-1);
					faktoren2.set(faktoren2.indexOf(f2),-1);
					continue b;
				}
			}
		}
		try {
			long kgv = StrictMath.multiplyExact(zahl1, zahl2)/ggt;
			ausgabe(ggt,String.valueOf(kgv));
		} catch(ArithmeticException e) {
			BigInteger bi = (BigInteger.valueOf(zahl1).multiply(BigInteger.valueOf(zahl2)).divide(BigInteger.valueOf(ggt)));
			ausgabe(ggt,bi.toString());
		}
	}
	
	/**
	 * Diese Methode gibt dem Nutzer die Werte fuer den ggT und den kgV beider Werte aus.<br>
	 * Sie werden in der Methode berechnen() bestimmt und dieser Methode als Parameter uebergeben.<br>
	 * Anschliessend leitet er das Programm in die neustart()-Methode weiter.
	 * @param ggt Nimmt den ggT entgegen.
	 * @param kgv Nimmt den kgV entgegen.
	 */
	private void ausgabe(int ggt, String kgv) {
		JOptionPane.showMessageDialog(null, "Der größte gemeinsame Teiler von "+zahl1+" und "+zahl2+" ist "+ggt+"."+umbruch+"Das kleinste gemeinsame Vielfache ist "+kgv+".", "Ergebnis", JOptionPane.PLAIN_MESSAGE);
		neustart();
	}
	
	/**
	 * Diese Methode fragt den Nutzer, ob er zwei weitere Zahlen ueberpruefen moechte und startet je nach Nutzereingabe entweder eine neue Abfrage oder beendet das Programm.
	 */
	private void neustart() {
		int dialogneustart = JOptionPane.showConfirmDialog(null, "Möchtest Du eine weitere Berechnung starten?", "Neue Abfrage?", JOptionPane.YES_NO_OPTION);
        if(dialogneustart == 0) {
     	   new GGTKGV();
        } else {
     	   System.exit(0);
        }
	}
	
	public static void main(String[] args) {
		new GGTKGV();
	}

}