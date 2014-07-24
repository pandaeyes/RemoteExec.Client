package exec.client.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;

import exec.client.ClientService;
import exec.common.Command;
import exec.proto.SmsObjectC102;
import exec.proto.SmsObjectC104;
import exec.proto.SmsObjectC105;
import exec.proto.SmsObjectS100;
import exec.proto.SmsObjectS101;
import exec.proto.SmsObjectS102;
import exec.proto.SmsObjectS103;
import exec.proto.SmsObjectS104;
import exec.proto.SmsObjectS105;

public class ClientFrame extends JFrame {
	
	private Dimension dimension = new Dimension(800, 600);
	private String title = "远程命令";
	private DefaultComboBoxModel comboboxModel = null;
	private Object comboBoxVal = null;
	private JTable table = null;
	private JTable selectedTable = null;
	private JButton bathRunBut = new JButton("运行");
	private JButton reloadBut = new JButton("重载");
	private JTextField singleCmdTxt = new JTextField();
	private JTextField singleParamTxt = new JTextField();
	private JButton singleRunBut = new JButton("运行");
	private Command selectedCommand = null; 
	private JTextArea runInfo = null;
	private JComboBox srvListCombo = null;
	private boolean showDialog = true;
	
	public static void main(String [] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		new ClientFrame();
	}
	
	public ClientFrame() {
		ClientService.getInstance().setFrame(this);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JPanel mainPane = new JPanel();
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(mainPane, BorderLayout.CENTER);
		mainPane.setLayout(new BorderLayout());
		JPanel upPane = getNorthPane();
		JPanel downPane = getRunPane();
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, upPane, downPane);
		split.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
//		mainPane.add(upPane, BorderLayout.NORTH);
//		mainPane.add(downPane, BorderLayout.CENTER);
		mainPane.add(split, BorderLayout.CENTER);
		setSize(dimension);
		setTitle(title);
		GUIUtils.centerWindow(this);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				showDialog = false;
				ClientService.getInstance().closeConnect();
			}
		});
		setVisible(true);
	}
	
	public JPanel getNorthPane() {
		JPanel mainPane2 = new JPanel();
		JPanel mainPane = new JPanel();
		JPanel cmdPane = new JPanel();
		JPanel execPane = new JPanel();
		mainPane.setLayout(new BorderLayout());
		mainPane2.setLayout(new BorderLayout());
		cmdPane.setLayout(new BorderLayout());
		execPane.setLayout(new BorderLayout());
		mainPane2.add(mainPane, BorderLayout.CENTER);
		mainPane.add(cmdPane, BorderLayout.CENTER);
		mainPane.add(execPane, BorderLayout.EAST);
		mainPane2.setBorder(BorderFactory.createTitledBorder("命令栏"));
		
		JPanel comboPane = new JPanel();
		comboPane.setLayout(new BorderLayout());
		List<ServerObject> serverList = ClientService.getInstance().getServerList();
		comboboxModel = new DefaultComboBoxModel();
		comboboxModel.addElement("请选择服务器...");
		for (ServerObject obj : serverList) {
			comboboxModel.addElement(obj);
		}
		srvListCombo = new JComboBox(comboboxModel);
		srvListCombo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Object select = comboboxModel.getSelectedItem();
				if (!select.equals(comboBoxVal)) {
					setButEditable(false);
					selectedCommand = null;
					singleCmdTxt.setText("");
					singleParamTxt.setText("");
					((DataTableModel)selectedTable.getModel()).getList().clear();
					((DataTableModel)selectedTable.getModel()).fireTableDataChanged();
					
					comboBoxVal = select;
					showDialog = false;
					comboBoxActionPerformed(select);
				}
			}
		});
		comboPane.add(srvListCombo, BorderLayout.CENTER);
		comboPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		cmdPane.add(comboPane, BorderLayout.NORTH);
		table = new TipJTable(new DataTableModel(new ArrayList<Command>()));
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.getTableHeader().setReorderingAllowed(false);
		table.setPreferredScrollableViewportSize(new Dimension(50, 150));
		table.getColumnModel().getColumn(0).setMaxWidth(22);
		table.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				int row =((JTable)e.getSource()).getSelectedRow();
				Command cmd = ((DataTableModel)table.getModel()).getList().get(row);
				if (e.getClickCount() == 2) {
					((DataTableModel)selectedTable.getModel()).getList().add(cmd);
					((DataTableModel)selectedTable.getModel()).fireTableDataChanged();
				}
				selectedCommand = cmd;
				singleParamTxt.setText("");
				singleCmdTxt.setText(cmd.getCmd());
			}
		});
		table.addKeyListener(new TableKeyAdapter());
		JScrollPane scrollPane = new JScrollPane(table);
		cmdPane.add(scrollPane, BorderLayout.CENTER);
		
		JPanel nPane = new JPanel();
		nPane.setLayout(new BorderLayout());
		JPanel butPane = new JPanel();
		butPane.setLayout(new BoxLayout(butPane, BoxLayout.X_AXIS));
		butPane.add(new JLabel(" 已选命令"));
		JLabel eLabel = new JLabel("  清空");
		eLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		eLabel.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				((DataTableModel)selectedTable.getModel()).getList().clear();
				((DataTableModel)selectedTable.getModel()).fireTableDataChanged();
			}
		});
		butPane.add(eLabel);
		butPane.add(Box.createHorizontalStrut(8));
		butPane.add(reloadBut);
		reloadBut.setVisible(false);
		eLabel.setForeground(Color.BLUE);
		nPane.add(butPane, BorderLayout.WEST);
		bathRunBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doRunBatch();
			}
		});
		
		reloadBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doReloadBatch();
			}
		});
		setButEditable(false);
		nPane.add(bathRunBut, BorderLayout.EAST);
		execPane.add(nPane, BorderLayout.NORTH);
		selectedTable = new TipJTable(new DataTableModel(new ArrayList<Command>()));
		selectedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		selectedTable.getTableHeader().setReorderingAllowed(false);
		selectedTable.setPreferredScrollableViewportSize(new Dimension(370, 100));
		selectedTable.getColumnModel().getColumn(0).setMaxWidth(22);
		selectedTable.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e){
				if (e.getClickCount() == 2) {
					int row =((JTable)e.getSource()).getSelectedRow();
					((DataTableModel)selectedTable.getModel()).getList().remove(row);
					((DataTableModel)selectedTable.getModel()).fireTableDataChanged();
				}
			}
		});
		selectedTable.addKeyListener(new TableKeyAdapter());
		new TableDragSource(selectedTable);
		new TableDropTarget(selectedTable);
		execPane.add(new JScrollPane(selectedTable), BorderLayout.CENTER);
		execPane.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));
		
		JPanel singlePane = new JPanel();
		singlePane.setBorder(BorderFactory.createEmptyBorder(2, 0, 0, 0));
		singlePane.setLayout(new BorderLayout(5, 5));
		singlePane.add(singleCmdTxt, BorderLayout.CENTER);
		singleCmdTxt.setBackground(new Color(236, 233, 216));
		singleCmdTxt.setEditable(false);
		JPanel singleEastPane = new JPanel();
		singleEastPane.setLayout(new BoxLayout(singleEastPane, BoxLayout.X_AXIS));
		singleParamTxt.setColumns(50);
		singleEastPane.add(singleParamTxt);
		singleEastPane.add(Box.createHorizontalStrut(9)); 
		singleEastPane.add(singleRunBut);
		singleRunBut.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				doRunSingle();
			}
		});
		singlePane.add(new JLabel("CMD:"), BorderLayout.WEST);
		singlePane.add(singleCmdTxt, BorderLayout.CENTER);
		singlePane.add(singleEastPane, BorderLayout.EAST);
		mainPane2.add(singlePane, BorderLayout.SOUTH);
		return mainPane2;
	}
	
	public JPanel getRunPane() {
		JPanel runPane = new JPanel();
		runPane.setLayout(new BorderLayout());
		runInfo = new JTextArea();
		runInfo.setEditable(false);
		runPane.add(new JScrollPane(runInfo), BorderLayout.CENTER);
		runPane.setBorder(BorderFactory.createTitledBorder("运行结果"));
		return runPane;
	}
	
	public void handle(SmsObjectS100 s100) {
		if (s100.getSucc() == 0) {
			JOptionPane.showMessageDialog(this, s100.getMsg(), "校验结果",1);
			srvListCombo.setSelectedIndex(0);
		} else {
			showDialog = true;
			setButEditable(true);
			if (s100.getGroup() == 9) {
				reloadBut.setVisible(true);
			} else {
				reloadBut.setVisible(false);
			}
		}
	}
	
	public void handle(SmsObjectS101 s101) {
		List<Command> cmdList = s101.getCmdList();
		DataTableModel model = (DataTableModel)table.getModel();
		model.replaceList(cmdList);
		model.fireTableDataChanged();
	}
	
	public void handle(SmsObjectS102 s102) {
		GUIUtils.areaAppend(runInfo, s102.getLine());
	}
	
	public void handle(SmsObjectS103 sms103) {
		JOptionPane.showMessageDialog(this, sms103.getLine(), "提示信息",1);
	}
	
	public void handle(SmsObjectS104 sms104) {
		if (sms104.getResult() == 0) {
			JOptionPane.showMessageDialog(this, "执行失败", "提示信息",1);
		}
		setButEditable(true);
	}
	
	public void handle(SmsObjectS105 sms105) {
		if (sms105.getSucc() == 1) {
			JOptionPane.showMessageDialog(this, "重载成功!", "提示信息",1);
		}
	}
	
	public void linkError() {
		JOptionPane.showMessageDialog(this, "连接失败", "提示信息",1);
		srvListCombo.setSelectedIndex(0);
	}
	
	public void closeConnect() {
		if (showDialog) {
			JOptionPane.showMessageDialog(this, "与服务链接已经断开", "提示信息",1);
			srvListCombo.setSelectedIndex(0);
		}
	}
	
	private void comboBoxActionPerformed(Object values) {
		if (values instanceof String) {
			ClientService.getInstance().closeConnect();
			DataTableModel model = (DataTableModel)table.getModel();
			model.getList().clear();
			model.fireTableDataChanged();
		} else if (values instanceof ServerObject) {
			ClientService.getInstance().switchConnect((ServerObject)values);
		}
	}
	
	private void doRunBatch() {
		setButEditable(false);
		List<Command> cmdList = new ArrayList<Command>();
		cmdList = ((DataTableModel)selectedTable.getModel()).getList();
		if (cmdList.size() < 1) {
			JOptionPane.showMessageDialog(this, "请选择命令！", "提示信息",1);
			setButEditable(true);
			return;
		} else {
			runInfo.setText("");
			SmsObjectC102 sms102 = new SmsObjectC102();
			for (Command cmd : cmdList) {
				if (cmd.getCmd() != null
						&& cmd.getCmd().indexOf("${param}") != -1) {
					JOptionPane.showMessageDialog(this, "批量执行不可以有带参数命令！", "提示信息",1);
					setButEditable(true);
					return;
				}
				sms102.getCmdList().add(cmd.getKey());
			}
			ClientService.getInstance().send(sms102);
		}
	}
	
	private void doReloadBatch() {
		SmsObjectC105 sms105 = new SmsObjectC105();
		ClientService.getInstance().send(sms105);
	}
	
	private void doRunSingle() {
		setButEditable(false);
		String param = singleParamTxt.getText().trim();
		if (selectedCommand == null) {
			JOptionPane.showMessageDialog(this, "请选择执行命令！", "提示信息",1);
			setButEditable(true);
			return;
		}
		if (selectedCommand.getCmd() != null
				&& selectedCommand.getCmd().indexOf("${param}") != -1
				&& param.length() == 0) {
			JOptionPane.showMessageDialog(this, "命令包含参数，请输入！", "提示信息",1);
			setButEditable(true);
			return;
		}
		if (param.matches("(\\w*_*)*")) {
			runInfo.setText("");
			SmsObjectC104 sms104 = new SmsObjectC104();
			sms104.setParam(param);
			sms104.setCmdkey(selectedCommand.getKey());
			ClientService.getInstance().send(sms104);
		} else {
			JOptionPane.showMessageDialog(this, "参数非法，只能输入英文字母、数字和下划线！", "提示信息",1);
			setButEditable(true);
		}
	}
	
	private void setButEditable(boolean editable) {
		reloadBut.setEnabled(editable);
		bathRunBut.setEnabled(editable);
		singleRunBut.setEnabled(editable);
	}
}
