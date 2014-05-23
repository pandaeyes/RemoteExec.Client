package exec.client.ui;

import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.JTable;

import exec.common.Command;

public class TipJTable extends JTable {

	public TipJTable() {
		super();
	}
	
	public TipJTable(DataTableModel model) {
		super(model);
	}
	
	public String getToolTipText(MouseEvent e) {
		String tip = null;
        java.awt.Point p = e.getPoint();  
        int rowIndex = rowAtPoint(p);  
        int colIndex = columnAtPoint(p); 
        int realColumnIndex = convertColumnIndexToModel(colIndex);
        List<Command> list = ((DataTableModel)getModel()).getList();
        Command cmd = null;
        if (rowIndex > (list.size() + 1)) {
        	tip = super.getToolTipText(e); 
        } else {
        	cmd = list.get(rowIndex);
        	switch(realColumnIndex) {
	        	case 1:
	        		tip = cmd.getKey();
	        		break;
	        	case 2:
	        		tip = cmd.getDesc();
	        		break;
	        	case 3:
	        		tip = cmd.getCmd();
	        		break;
	        	default:
	        		tip = null;
        	}
        }
        if (tip != null) {
        	StringBuffer sb = new StringBuffer();
        	tip = replaceHTML(tip);
        	for(int i =0; i<tip.length(); i++){
        		if(tip.charAt(i) == '\n'){
        			sb.append("<br>");
        		} else {
        			sb.append(tip.charAt(i));
        		}
        	}
        	if (realColumnIndex == 3) {
        		tip = "<html>" + "<font color=\"#0000ff\">[" + replaceHTML(cmd.getDir()) + "]</font><br>"  + sb.toString() + "</html>";
        	} else {
        		tip = "<html>" + sb.toString() + "</html>";
        	}
        }
		return tip;
	}
	
	public String replaceHTML(String str) {
		if (str == null)
			return null;
		
		StringBuilder buff = new StringBuilder();
		int start = 0;
		
		for(int i = 0; i < str.length(); i++) {
			switch(str.charAt(i)) {
				case '&':
					buff.append(str.substring(start, i)).append("&amp;");
					start = i + 1;
					break;
				case '<':
					buff.append(str.substring(start, i)).append("&lt;");
					start = i + 1;
					break;
				case '>':
					buff.append(str.substring(start, i)).append("&gt;");
					start = i + 1;
					break;
				case '\'':
					buff.append(str.substring(start, i)).append("&apos;");
					start = i + 1;
					break;
				case '"':
					buff.append(str.substring(start, i)).append("&quot;");
					start = i + 1;
					break;
			}
		}
		buff.append(str.substring(start));
		return buff.toString();
	}
}
