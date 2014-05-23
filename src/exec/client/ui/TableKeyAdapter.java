package exec.client.ui;

import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTable;

public class TableKeyAdapter extends KeyAdapter {

	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if (key >= KeyEvent.VK_A && key <= KeyEvent.VK_Z) {
			selectedKey((JTable)e.getSource(), key + 32);
		}
	}
	
	private void selectedKey(JTable table, int key) {
		int count = table.getRowCount();
		int selected = table.getSelectedRow();
		String name = "";
		int selectindex = -1;
		for (int i = 0; i < count; i++) {
			name = "" + table.getValueAt(i, 1);
			if (name.trim().length() > 0) {
				char c = name.trim().charAt(0);
				if (c == key) {
					if ((selected + 1) == i) {
						table.getSelectionModel().setSelectionInterval(i, i);
						Rectangle rect = table.getCellRect(i + 1, 0, true);
						table.scrollRectToVisible(rect);
						return;
					} else {
						if (selectindex == -1) {
							selectindex = i;
						}
					}
				}
			}
		}
		if (selectindex != -1) {
			table.getSelectionModel().setSelectionInterval(selectindex, selectindex);
			Rectangle rect = table.getCellRect(selectindex, 0, true);
			table.scrollRectToVisible(rect);
		}
	}
}
