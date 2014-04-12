package exec.client.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

public class ClientFrame extends JFrame {
	
	private Dimension dimension = new Dimension(800, 600);
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
		
		JPanel comboPane = new JPanel();
		comboPane.setLayout(new BorderLayout());
		JComboBox srvListCombo = new JComboBox();
		comboPane.add(srvListCombo, BorderLayout.CENTER);
		comboPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		cmdPane.add(comboPane, BorderLayout.NORTH);
		JTable table = new JTable(new DataTableModel(new ArrayList<CmdObject>()));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(50, 200));
		JScrollPane scrollPane = new JScrollPane(table);
		cmdPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel nPane = new JPanel();
		nPane.setLayout(new BorderLayout());
		JPanel butPane = new JPanel();
		butPane.setLayout(new BoxLayout(butPane, BoxLayout.X_AXIS));
		butPane.add(new JLabel(" 已选命令"));
		butPane.add(new JLabel("  清空"));
		nPane.add(butPane, BorderLayout.WEST);
		
		nPane.add(new JButton("运行"), BorderLayout.EAST);
		execPane.add(nPane, BorderLayout.NORTH);
		JTable selectedTable = new JTable(new DataTableModel(new ArrayList<CmdObject>()));
		selectedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedTable.getTableHeader().setReorderingAllowed(false);
		selectedTable.setPreferredScrollableViewportSize(new Dimension(370, 100));
		execPane.add(new JScrollPane(selectedTable), BorderLayout.CENTER);
		execPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		return mainPane;
	}
	
	public JPanel getRunPane() {
		JPanel runPane = new JPanel();
		runPane.setLayout(new BorderLayout());
		JTextArea runInfo = new JTextArea();
		runInfo.setEditable(false);
		runPane.add(new JScrollPane(runInfo), BorderLayout.CENTER);
		runPane.setBorder(BorderFactory.createTitledBorder("运行结果"));
		return runPane;
	}
}
