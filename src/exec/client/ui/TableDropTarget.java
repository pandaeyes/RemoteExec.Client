package exec.client.ui;

import java.awt.Point;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTable;

import exec.common.Command;

public class TableDropTarget implements DropTargetListener {
	
	private JTable table = null;
	
	public TableDropTarget(JTable table) {
		this.table = table;
		new DropTarget(table, this);
	}

	@Override
	public void dragEnter(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragOver(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent dtde) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragExit(DropTargetEvent dte) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drop(DropTargetDropEvent dtde) {
		Transferable transferable = dtde.getTransferable();
		java.awt.datatransfer.DataFlavor flavors[] = transferable
				.getTransferDataFlavors();
		for (int i = 0; i < flavors.length; i++) {
			if (!transferable.isDataFlavorSupported(flavors[i]))
				continue;
			Point p = dtde.getLocation();
			int row = table.rowAtPoint(p);
			if (dtde.getDropAction() == DnDConstants.ACTION_COPY) {
				try {
					Object data = transferable.getTransferData(flavors[i]);
					ArrayList list = (ArrayList) data;
					if (list != null && list.size() == 1) {
						int oldRow = (Integer)list.get(0);
						List<Command> listCmd = ((DataTableModel)table.getModel()).getList();
						Command cmd = listCmd.get(oldRow);
						listCmd.remove(oldRow);
						listCmd.add(row, cmd);
						((DataTableModel)table.getModel()).fireTableDataChanged();
					}
					return;
				} catch (Exception e) {
					e.printStackTrace();
					throw new RuntimeException("移动出错了");
				}
			}
		}
	}

}
