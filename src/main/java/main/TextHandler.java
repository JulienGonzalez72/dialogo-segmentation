package main;

public class TextHandler {
	
	private String ctxt;
	private String txt;
	
	
	public TextHandler(String texteOriginal, String texteAvecCesures) {
		this.txt = texteOriginal;
		this.ctxt = texteAvecCesures;
	}
	
	/**
	 * Indique si la césure est placée au bon endroit.
	 */
	public boolean correctPause(int offset) {
		StringBuilder b = new StringBuilder(ctxt);
		for (int i = 0; i < b.length(); i++) {
			if (i >= offset) {
				break;
			}
			/// supprime le slash et l'espace avant ///
			if (b.charAt(i) == '/') {
				b.deleteCharAt(i);
				b.deleteCharAt(i - 1);
			}
		}
		return b.charAt(++offset) == '/';
	}
	
	/**
	 * L'utilisateur marque la pause à cet endroit là
	 */
	public void markPause(int offset) {
		
	} 
	
}
