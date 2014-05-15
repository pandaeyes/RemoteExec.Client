package exec.client.ui;

import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.util.ArrayList;

import javax.swing.JTable;

public class TableDragSource implements DragSourceListener, DragGestureListener {
	
	private DragSource source;
	
	public TableDragSource(JTable table) {
		source = new DragSource();
		source.createDefaultDragGestureRecognizer(table, 1, this);
	}

	public void dragGestureRecognized(DragGestureEvent dge) {
		java.awt.Component comp = dge.getComponent();
		if (comp instanceof JTable) {
			ArrayList list = new ArrayList();
			int row =((JTable)comp).getSelectedRow();
			list.add(row);
			source.startDrag(dge, DragSource.DefaultLinkDrop, new ArrayListTransferable(list), this);
		}
	}
	
	public void dragEnter(DragSourceDragEvent dragsourcedragevent) {
	}

	public void dragExit(DragSourceEvent dragsourceevent) {
	}

	public void dragOver(DragSourceDragEvent dragsourcedragevent) {
	}

	public void dropActionChanged(DragSourceDragEvent dragsourcedragevent) {
	}

	public void dragDropEnd(DragSourceDropEvent dragsourcedropevent) {
	}
}
