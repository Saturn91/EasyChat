package main;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Debug mode output:
 * -1: nothing
 * 0: Normal Runtime Status
 * 1: Errors that are critical and can stop the Program from running
 * 2: "Errors" that can happen at runtime
 * 3: Status informations Detail 0
 * 4. Status informations Detail 1
 * 5. Status informations Detail 2
 * @author M.Geissbberger
 */
public class Log {
	private static int debugMode;
	private static int allClassesDebugMode = -1;		//-1 means use normal debug mode
	private final static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.YYYY-hh:mm:ss");
	private final static String info = " [INFO]";
	private final static String error ="[ERROR]";

	private static StringBuilder sb = new StringBuilder();

	private static boolean printOnlyExact_DebugMode = false;

	/**
	 * Print the msg message on the debugMod Layer
	 * @param msg	Status Message
	 * @param debugMode
	 */
	public static void printLn(String msg, String className, int _debugMode){
		String msgLine = "[" + getDate() + "]: " + info + " " + className + ": " + msg;
		if(allClassesDebugMode == -1){
			if(printOnlyExact_DebugMode){
				if(debugMode == _debugMode){
					System.out.println(_debugMode + "| "+ msgLine);
					sb.append(_debugMode + "| "+msgLine + "\n");
				}
			}else{
				if(debugMode >= _debugMode){
					System.out.println(_debugMode + "| "+msgLine);
					sb.append(_debugMode + "| "+msgLine + "\n");
				}
			}
		}else{
			if(allClassesDebugMode >= _debugMode){
				System.out.println(_debugMode + "| "+msgLine);
				sb.append(_debugMode + "| "+msgLine + "\n");
			}
		}
	}

	/**
	 * Print the msg error message on the debugMode Layer
	 * @param msg	Status Message
	 * @param debugMode
	 */
	public static void printErrorLn(String msg, String className, int _debugMod){
		String msgLine = "[" + getDate() + "]: " + error + " " + className + ": " + msg;
		if(allClassesDebugMode == -1){
			if(printOnlyExact_DebugMode){
				if(debugMode == _debugMod){
					System.err.println(_debugMod + "| "+msgLine);
					sb.append(_debugMod + "| "+msgLine + "\n");
				}
			}else{
				if(debugMode >= _debugMod){
					System.err.println(_debugMod + "| "+msgLine);
					sb.append(_debugMod + "| "+msgLine + "\n");
				}
			}
		}else{
			if(allClassesDebugMode >= _debugMod){
				System.err.println(_debugMod + "| "+msgLine);
				sb.append(_debugMod + "| "+msgLine + "\n");
			}
		}
	}

	/**
	 * Change the debug Mod so that other deeper or less deep msg gets trough
	 * @param debugMode
	 */
	public static void setDebugMode(int _debugMod){
		debugMode = _debugMod;
	}

	/**
	 * If active, only the one set Layer will be printed
	 * @param printOnlyExact_Debugmode
	 */
	public void setOnlyThisLayer(boolean _printOnlyExact_Debugmode){
		printOnlyExact_DebugMode = _printOnlyExact_Debugmode;
	}

	/**
	 * This Method provides every text output with the last printed msg could be used for a log File
	 * @return
	 */
	public static StringBuilder getDebugStrings(){
		StringBuilder sb2 = new StringBuilder();
		sb2.append(sb.toString());
		sb.setLength(0);
		return sb2;
	}

	/**
	 * This Method changes the debug mode for all classes
	 * @param debugmode
	 */
	public static void setDebugModeAllClasses(int debugmode){
		allClassesDebugMode = debugmode;
	}

	public static String getDate(){
		Date date = new Date(System.currentTimeMillis());
		return dateFormat.format(date).toString();
	}

	public static int getDebugMode(){
		if(allClassesDebugMode == -1){
			return debugMode;
		}else{
			return allClassesDebugMode;
		}
	}
}
