package exec.client.ui;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import exec.common.Command;

public class DataTableModel extends AbstractTableModel {
	
	private List<Command> list = null;
	
	public DataTableModel(List<Command> list) {
		MyComparator comparator = new MyComparator();
		Collections.sort(list, comparator);
		this.list = list;
	}
	
	public String getColumnName(int col){
		switch(col) {
			case 0:
				return "";
			case 1:
				return "索引";
			case 2:
				return "描述";
			default:
				return "命令";
		}
	}

	@Override
	public int getRowCount() {
		return list.size();
	}

	@Override
	public int getColumnCount() {
		return 4;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if ((rowIndex + 1) > list.size())
			return null;
		switch(columnIndex) {
			case 0:
				return rowIndex + 1;
			case 1:
				return list.get(rowIndex).getKey();
			case 2:
				return list.get(rowIndex).getDesc();
			default:
				return list.get(rowIndex).getCmd();
		}
	}
	
	public List<Command> getList() {
		return list;
	}
	
	public void replaceList(List<Command> list) {
		MyComparator comparator = new MyComparator();
		Collections.sort(list, comparator);
		this.list = list;
	}
}

class MyComparator implements Comparator {
	public int compare(Object arg0, Object arg1) {
		if (arg0 instanceof Command && arg1 instanceof Command) {
			return ((Command)arg0).getKey().compareTo(((Command)arg1).getKey());
		} else {
			return 0;
		}
	}
}
