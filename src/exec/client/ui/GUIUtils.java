package exec.client.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

import javax.swing.JTextArea;

public class GUIUtils {

	public GUIUtils() {
	}

	public static void centerWindow(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension winSize = win.getSize();
		if (winSize.height > screenSize.height)
			winSize.height = screenSize.height;
		if (winSize.width > screenSize.width)
			winSize.width = screenSize.width;
		win.setLocation((screenSize.width - winSize.width) / 2,
				(screenSize.height - winSize.height) / 2 - 20);
	}
	
	public static void areaAppend(JTextArea area, String text) {
		if (area != null) {
			if (area.getText().trim().length() != 0) {
				area.append("\r\n");
			}
			area.append(text);
			int l = area.getDocument().getLength();
			if (text.length() < l)
				l = l - text.length();
			area.setCaretPosition(l);
		}
	}
	
	public static String convertToLinux(String path) {
		if (path == null) {
			return path;
		} else {
			String relust = path.replaceAll("\\\\", "/");
			relust = relust.replaceAll("%PHP%", "\\${PHP}");
			relust = relust.replaceAll("%TOOLPATH%", "\\${TOOLPATH}");
			relust = relust.replaceAll("%SRVPATH%", "\\${SRVPATH}");
			relust = relust.replaceAll("%CLIENTPATH%", "\\${CLIENTPATH}");
			relust = relust.replaceAll("%DATAPATH%", "\\${DATAPATH}");
			relust = relust.replaceAll("copy ", "cp ");
			return relust;
		}
	}
	
	public static boolean isNull(String str) {
		if (str == null) return true;
		if (str.trim().length() == 0)
			return true;
		else
			return false;
	}
}
