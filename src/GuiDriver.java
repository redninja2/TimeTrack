import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GuiDriver {
	private ArrayList<Project> projects;
	private Project activeProject;
	private JButton activeButton;
	private boolean isActive;
	private final static String serializeFile = "save.dat";
	private JFrame frame;
	private JPanel buttonPanel;
	private JScrollPane listScroller;
	private ArrayList<JButton> buttons;

	public GuiDriver() {
		super();
		this.isActive = false;
		this.loadProjects();
		this.createGUI();
	}

	private void createGUI() {
		frame = new JFrame("Time Tracker");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
		buttons = new ArrayList<JButton>();
		for (Project proj : projects) {
			JButton btn = new JButton("<html>" + proj.getName() + "<br />" + proj.getFormattedTime() + "</html>");
			btn.addActionListener(new ProjectButton(proj.getName()));
			buttons.add(btn);
			buttonPanel.add(btn);
		}

		listScroller = new JScrollPane(buttonPanel);
		listScroller.setPreferredSize(new Dimension(350, 350));
		listScroller.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

		Container c = frame.getContentPane();
		c.setLayout(new GridLayout(1, 1));
		c.add(listScroller);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(400, 400);
		frame.setLocationRelativeTo(null);
		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent windowEvent) {
				saveProjects();
				System.exit(0);
			}
		});
		frame.setVisible(true);
	}

	@SuppressWarnings("unchecked")
	private void loadProjects() {
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = new FileInputStream(serializeFile);
			ois = new ObjectInputStream(fis);
			this.projects = (ArrayList<Project>) ois.readObject();

			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found. Starting fresh.");
			this.projects = new ArrayList<Project>();
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	private void saveProjects() {
		for (Project proj : projects) {
			// ensure all projects are stopped
			proj.stopTracking();
		}
		FileOutputStream fos;
		ObjectOutputStream oos;
		try {
			fos = new FileOutputStream(serializeFile);
			oos = new ObjectOutputStream(fos);
			oos.writeObject(this.projects);
			oos.close();
			fos.close();
			System.out.println("save complete... " + serializeFile);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
			System.exit(1);
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	public static void main(String[] args) throws InterruptedException {
		new GuiDriver();
	}

	private class ProjectButton implements ActionListener, Runnable {
		private String projectName;

		public ProjectButton(String projectName) {
			this.projectName = projectName;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (Project proj : projects) {
				if (proj.getName().equals(this.projectName)) {
					if (proj.isTracking()) {
						proj.stopTracking();
						isActive = false;
					} else {
						proj.startTracking();
						activeProject = proj;
						activeButton = (JButton) e.getSource();
						isActive = true;
						Thread t = new Thread(this);
						t.start();
					}
				} else {
					// every other project should stop tracking; only track 1 at a time.
					proj.stopTracking();
				}
			}
		}

		@Override
		public void run() {
			for (;;) {
				if (!isActive) {
					break;
				}
				activeButton.setText(
						"<html>" + activeProject.getName() + "<br />" + activeProject.getFormattedTime() + "</html>");
				try {
					Thread.sleep(10);
				} catch (Exception e) {
				}
			}
		}

	}

}
