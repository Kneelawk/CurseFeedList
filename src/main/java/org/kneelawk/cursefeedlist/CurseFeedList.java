package org.kneelawk.cursefeedlist;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

public class CurseFeedList {

	private static CloseableHttpClient client;
	
	public static HttpClient client() {
		return client;
	}

	public static void main(String[] args) {
		client = HttpClients.createDefault();

		// set default UI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("Curse Feed");
		frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				try {
					client.close();
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		frame.setSize(1000, 600);

		JPanel root = new JPanel();
		root.setLayout(new BoxLayout(root, BoxLayout.X_AXIS));
		frame.add(root);

		JPanel listPanel = new JPanel();
		listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
		root.add(listPanel);

		listPanel.add(new JLabel("Curse Projects"));

		Vector<String> str = new Vector<String>();
		str.add("HELLO WORLD!!!!!!");

		JList<String> feedList = new JList<String>(str);
		listPanel.add(new JScrollPane(feedList));

		frame.setVisible(true);
	}

}
