package nz.net.thoms.HttpClient;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JList;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.BoxLayout;
import java.awt.GridLayout;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;

public class MainWindow {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
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
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 701, 577);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		GridBagLayout gridBagLayout = new GridBagLayout();
		gridBagLayout.columnWidths = new int[] {346, 346};
		gridBagLayout.rowHeights = new int[] {200, 20};
		gridBagLayout.columnWeights = new double[]{1.0, 1.0};
		gridBagLayout.rowWeights = new double[]{1.0, 0.0};
		frame.getContentPane().setLayout(gridBagLayout);
		
		final JList list = new JList();
		GridBagConstraints gbc_list = new GridBagConstraints();
		gbc_list.insets = new Insets(0, 0, 5, 5);
		gbc_list.fill = GridBagConstraints.BOTH;
		gbc_list.gridx = 0;
		gbc_list.gridy = 0;
		frame.getContentPane().add(list, gbc_list);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.insets = new Insets(0, 0, 5, 0);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 1;
		gbc_scrollPane.gridy = 0;
		frame.getContentPane().add(scrollPane, gbc_scrollPane);
		
		final JTextArea fileTextArea = new JTextArea();
		scrollPane.setViewportView(fileTextArea);
		
		JButton btnFetchListFrom = new JButton("Fetch List From Server");
		btnFetchListFrom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				list.setListData(HttpClient.getFileList().toArray());
			}
		});
		GridBagConstraints gbc_btnFetchListFrom = new GridBagConstraints();
		gbc_btnFetchListFrom.insets = new Insets(0, 0, 5, 5);
		gbc_btnFetchListFrom.gridx = 0;
		gbc_btnFetchListFrom.gridy = 1;
		frame.getContentPane().add(btnFetchListFrom, gbc_btnFetchListFrom);
		
		JButton btnFetchSelectedItem = new JButton("Fetch Selected Item");
		btnFetchSelectedItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String path = (String) list.getSelectedValue();
				fileTextArea.setText(HttpClient.getFile(path));
			}
		});
		GridBagConstraints gbc_btnFetchSelectedItem = new GridBagConstraints();
		gbc_btnFetchSelectedItem.insets = new Insets(0, 0, 5, 0);
		gbc_btnFetchSelectedItem.gridx = 1;
		gbc_btnFetchSelectedItem.gridy = 1;
		frame.getContentPane().add(btnFetchSelectedItem, gbc_btnFetchSelectedItem);
	}

}
