package connect;

import java.awt.Container;
import java.awt.EventQueue;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JTextField;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class main {

	private JFrame frame;
	private JTextField txtQuery;
	private JTextField txtDataName;
	private Connection conn;
	private JPanel panelResult;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					main window = new main();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public main() {
		initialize();
	}

//	Connect
	private boolean Connect(String nameDB) {

		try {
			String userName = "admin";
			String password = "toan_1991";
			String url = "jdbc:mysql://127.0.0.1:3306/" + nameDB;
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = (Connection) DriverManager.getConnection(url, userName, password);
			JOptionPane.showMessageDialog(frame, "Success !");
			;
			System.out.println("Connected");
		} catch (Exception e) {
			System.err.println("Can't connect !");
			JOptionPane.showMessageDialog(frame, "Failed to connect !");
		}
		return true;
	}
	// Executes query string
	
	public void selectData(String sql) {
		Statement stmt;
		ResultSet rs = null;
		try {
			stmt = (Statement) conn.createStatement();
			rs = stmt.executeQuery(sql);
		} catch (Exception e) {
			
		}
		creatTable(rs);
	}

	// Create table of results
	
	private void creatTable(ResultSet rs) {
		panelResult.removeAll();
		Vector<Vector<String>> vtData = new Vector<Vector<String>>();
		Vector<String> vtColumn = null;
		ResultSetMetaData rsm = null;
		try {
			rsm = rs.getMetaData();
			int col = rsm.getColumnCount();
			vtColumn = new Vector<String>(col, col);
			for (int i = 1; i <= col; i++) {
				vtColumn.add(rsm.getColumnName(i));
			}

			vtData = new Vector<Vector<String>>(10, 10);
			Vector<String> temp;
			while (rs.next()) {
				temp = new Vector<String>(col);
				for (int i = 1; i <= col; i++) {
					temp.add(rs.getString(i));
				}
				vtData.add(temp);
			}

			JTable table = new JTable(vtData, vtColumn);
			panelResult.add(new JScrollPane(table));

		} catch (SQLException e) {
		}
		panelResult.setVisible(false);
		panelResult.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 586, 481);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		txtQuery = new JTextField();
		txtQuery.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER) {
					selectData(txtQuery.getText());
				}
			}
		});
		txtQuery.setBounds(10, 411, 379, 20);
		frame.getContentPane().add(txtQuery);
		txtQuery.setColumns(10);
		txtQuery.setVisible(false);
		
		JButton btnQuery = new JButton("Query");
		btnQuery.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				selectData(txtQuery.getText());
			}
		});
		btnQuery.setBounds(399, 410, 75, 23);
		frame.getContentPane().add(btnQuery);
		btnQuery.setVisible(false);
		
		JButton btnClear = new JButton("Clear");
		btnClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtQuery.setText("");
				panelResult.removeAll();
				panelResult.setVisible(false);
				panelResult.setVisible(true);
			}
		});
		btnClear.setBounds(485, 410, 75, 23);
		frame.getContentPane().add(btnClear);
		btnClear.setVisible(false);
		
		panelResult = new JPanel();
		panelResult.setBounds(10, 42, 550, 358);
		frame.getContentPane().add(panelResult);
		
		txtDataName = new JTextField();
		txtDataName.setBounds(121, 11, 314, 20);
		frame.getContentPane().add(txtDataName);
		txtDataName.setColumns(10);
		
		JButton btnConnect = new JButton("Connect");
		btnConnect.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (Connect(txtDataName.getText())) {
					btnClear.setVisible(true);
					btnQuery.setVisible(true);
					txtQuery.setVisible(true);
				}

			}
		});
		btnConnect.setBounds(447, 10, 89, 23);
		frame.getContentPane().add(btnConnect);
		
		JLabel lblDatabaseName = new JLabel("Database name:");
		lblDatabaseName.setBounds(21, 14, 105, 14);
		frame.getContentPane().add(lblDatabaseName);
	}

}
