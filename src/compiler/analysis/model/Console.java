package compiler.analysis.model;

public class Console {

	private static boolean ativo = false;
	private static boolean ativoNT = false;
	private final static String DEBUG_TAG = "log$ ";

	public static void log(String message) {
		if (ativo) {
			System.out.println(DEBUG_TAG + message);
		}
	}
	
	public static void logNT(String message){
		if (ativoNT) {
			System.out.println(DEBUG_TAG + "- NT$ " + message);
		}
	}

}
