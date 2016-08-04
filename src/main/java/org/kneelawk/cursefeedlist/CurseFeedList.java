package org.kneelawk.cursefeedlist;

import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class CurseFeedList {

	public static void main(String[] args) {
		// set default UI
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		JFrame frame = new JFrame("Curse Feed");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
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
