package diskrete;

import java.math.BigInteger;
import java.util.Arrays;

public class Primzahltest {

	private static final int SICHERHEIT = 10;
	private static final boolean DEBUG = false;

	public static void main(String[] args) {
		istWahrscheinlichPrim(3, SICHERHEIT);
		istWahrscheinlichPrim(5, SICHERHEIT);
		istWahrscheinlichPrim(7, SICHERHEIT);
		istWahrscheinlichPrim(11, SICHERHEIT);
		istWahrscheinlichPrim(19, SICHERHEIT);
		// ---
		istWahrscheinlichPrim(2019, SICHERHEIT);
		istWahrscheinlichPrim(7919, SICHERHEIT);
		istWahrscheinlichPrim(123456789, SICHERHEIT);
		istWahrscheinlichPrim(15051973, SICHERHEIT);
	}

	/**
	 * Fermatscher Primzahltest.
	 * @param kandidat p
	 * @param sicherheit s
	 * @return Ob p wahrscheinlich prim ist.
	 */
	private static boolean istWahrscheinlichPrim(long kandidat, long sicherheit) {
		long i = 0;
		
		while (i < sicherheit) {
			long zufallszahl = 0L;
			
			// 1 <= a <= p-2
			while (zufallszahl == 0L) {
				zufallszahl = Math.round(Math.random() * (kandidat - 2));
			}
			
			// a^(p-1)
			BigInteger power = power(zufallszahl, kandidat - 1, kandidat);
			// a^(p-1) mod p
			BigInteger rest = power.mod(BigInteger.valueOf(kandidat));
			
			// a^(p-1) mod p != 1
			if (!rest.equals(BigInteger.ONE)) {
				System.out.print("p=" + kandidat + " ist zusammengesetzt, denn ");
				System.out.println(power + " % " + kandidat + " = " + rest + " != 1\n");
				return false;
			}
			i++;
		}
		
		System.out.println("p=" + kandidat + " ist wahrscheinlich prim\n");
		return true;
	}

	/**
	 * Berechnet Modul einer großen Zahl der Form b^e.
	 * @param basis b
	 * @param exponent e
	 * @param modul m
	 * @return b^e mod m
	 */
	private static BigInteger power(long basis, long exponent, long modul) {
		if (DEBUG) System.out.print("Square and Multiply: ");
		
		// Bildung der Binärdarstellung des Exponenten
		char[] exponentBinaer = Long.toBinaryString(exponent).toCharArray();
		if (DEBUG) System.out.print("Exponent: " + exponent);
		if (DEBUG) System.out.print("; Exponent binär: " + String.copyValueOf(exponentBinaer));
		if (DEBUG) System.out.print("; Basis: " + basis);
		
		if (DEBUG) System.out.print("; Produkt(");
		
		BigInteger[] zwischenergebnisse = new BigInteger[exponentBinaer.length];
		BigInteger ergebnis = BigInteger.valueOf(1L); // Neutrales Element Multiplikation
		
		// Zwischenergebnisse werden gebildet, indem rückwärts über das Array des Exponenten iteriert wird,
		// denn am Ende steht das Least Significant Bit:
		// ... b^6 b^5 b^4 b^3 b^2 b^1
		zwischenergebnisse[exponentBinaer.length - 1] = BigInteger.valueOf(basis); // basis^1
		
		// Multiplikation des ersten Zwischenergebnisses, wenn der Exponent in der Binärdarstellung an der Stelle des Index eine '1' hat
		if (exponentBinaer[exponentBinaer.length - 1] == '1') {
			if (DEBUG) System.out.print(zwischenergebnisse[exponentBinaer.length - 1] + " ");
			ergebnis = ergebnis.multiply(zwischenergebnisse[exponentBinaer.length - 1]);
		}
		
		for (int indexExponentBinaer = exponentBinaer.length - 2; indexExponentBinaer >= 0; indexExponentBinaer--) {
			// Square des vorherigen Ergebnisses und dann Modulo des neuen Zwischenergebnisses
			zwischenergebnisse[indexExponentBinaer] = zwischenergebnisse[indexExponentBinaer + 1].modPow(BigInteger.TWO, BigInteger.valueOf(modul));
			
			// Multiplikation der Zwischenergebnisse, wenn der Exponent in der Binärdarstellung an der Stelle des Index eine '1' hat
			if (exponentBinaer[indexExponentBinaer] == '1') {
				if (DEBUG) System.out.print(zwischenergebnisse[indexExponentBinaer] + " ");
				ergebnis = ergebnis.multiply(zwischenergebnisse[indexExponentBinaer]);
			}
		}

		// Modulo des Produktes der Zwischenergebnisse ergibt das Endergebnis
		ergebnis = ergebnis.mod(BigInteger.valueOf(modul));
		
		if (DEBUG) System.out.println(") == " + ergebnis + " (mod " + modul + ")");
		
		if (DEBUG) System.out.println("Exponent binär:     " + Arrays.toString(exponentBinaer));
		if (DEBUG) System.out.println("Zwischenergebnisse: " + Arrays.toString(zwischenergebnisse));
		
		if (DEBUG) System.out.println("Square and Multiply: " + basis + "^" + exponent + " mod " + modul + " = " + ergebnis);
		
		return ergebnis;
	}
}
