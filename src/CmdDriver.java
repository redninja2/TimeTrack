import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class CmdDriver {
	ArrayList<Project> projects;
	final static String serializeFile = "save.dat";

	@SuppressWarnings("unchecked")
	public CmdDriver() {
		FileInputStream fis;
		ObjectInputStream ois;
		try {
			fis = new FileInputStream(serializeFile);
			ois = new ObjectInputStream(fis);
			this.projects = (ArrayList<Project>) ois.readObject();

			ois.close();
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			this.projects = new ArrayList<Project>();
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		start();
	}

	private void start() {
		Scanner input = new Scanner(System.in);
		while (true) {
			System.out.print("$>");
			String line = input.nextLine().toLowerCase();
			String[] seg = line.split(" ");

			if (line.contains("create project") && seg.length >= 3) {
				String name = seg[2];
				this.projects.add(new Project(name));
				System.out
						.println("Added new project " + name + ". Project count: " + Integer.toString(projects.size()));
			} else if ((line.contains("start") || line.contains("stop")) && seg.length >= 2) {
				for (Project proj : projects) {
					// System.out.println(proj.getName() + ":" + seg[1] + ":" +
					// Boolean.toString(proj.getName().equals(seg[1])));
					if (proj.getName().equals(seg[1])) {
						if (seg[0].equals("start")) {
							proj.startTracking();
							System.out.println("Started time tracking " + seg[1]);
							break;
						} else {
							proj.stopTracking();
							System.out.println("Stopped time tracking " + seg[1]);
							System.out.println("\t" + Long.toString(proj.getTotalTimeAsMilliseconds()) + "ms");
							break;
						}
					}
				}
			} else if (line.contains("projects")) {
				for (Project proj : projects) {
					System.out.print("{" + proj.getName() +","+Long.toString(proj.getTotalTimeAsMilliseconds()) + "ms}");

				}
				System.out.println();
			} else if (line.contains("quit")) {
				System.out.println("saving...");
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
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;
			}
		}
		System.out.println("closing...");
		input.close();
	}

	public static void main(String[] args) throws InterruptedException {
		new CmdDriver();
	}

}
