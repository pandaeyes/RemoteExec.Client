package exec.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

public class ClientFrame extends JFrame {
	
	private Dimension dimension = new Dimension(750, 600);
	private String title = "远程命令";
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new ClientFrame();
	}
	
	public ClientFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPane = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPane, BorderLayout.CENTER);
		mainPane.setLayout(new BorderLayout());
		mainPane.add(getNorthPane(), BorderLayout.NORTH);
		mainPane.add(getRunPane(), BorderLayout.CENTER);
		setSize(dimension);
		setTitle(title);
		GUIUtils.centerWindow(this);
		setVisible(true);
	}
	
	public JPanel getNorthPane() {
		JPanel mainPane = new JPanel();
		JPanel cmdPane = new JPanel();
		JPanel execPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		cmdPane.setLayout(new BorderLayout());
		execPane.setLayout(new BorderLayout());
		mainPane.add(cmdPane, BorderLayout.CENTER);
		mainPane.add(execPane, BorderLayout.EAST);
		mainPane.setBorder(BorderFactory.createTitledBorder("命令栏"));
		
		JComboBox srvListCombo = new JComboBox();
		cmdPane.add(srvListCombo, BorderLayout.NORTH);
		JTable table = new JTable(new DataTableModel(new ArrayList<CmdObject>()));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(50, 30));
		cmdPane.add(new JScrollPane(table), BorderLayout.CENTER);
		
		JPanel nPane = new JPanel();
		JPanel sPane = new JPanel();
		nPane.setLayout(new BorderLayout());
		sPane.setLayout(new BorderLayout());
		nPane.add(new JLabel("已选命令"), BorderLayout.WEST);
		nPane.add(new JLabel("清空"), BorderLayout.EAST);
		execPane.add(nPane, BorderLayout.NORTH);
		JTable selectedTable = new JTable(new DataTableModel(new ArrayList<CmdObject>()));
		execPane.add(new JScrollPane(selectedTable), BorderLayout.CENTER);
		sPane.add();
		return mainPane;
	}
	
	public JPanel getRunPane() {
		JPanel runPane = new JPanel();
		return runPane;
	}
}
