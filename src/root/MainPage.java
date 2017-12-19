package root;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;

import static root.Api.*;

public final class MainPage extends JFrame {
	private static final long serialVersionUID = 1L;
	public static float cameraX, cameraY;
	public static int CellX = (int) Implement.DesignWidth / 128 + 3;
	public static int CellY = (int) Implement.DesignHeight / 128 + 3;
	public static int timeSpan = 30, time = 0, interval = 5; // each interval is 0.03s(timeSpan/1000)
	public static Timer GlobalTimer = new Timer();
	public static Hashtable<Object, Timer> KeyPressedConfirmTimer = new Hashtable<Object, Timer>();

	public MainPage() throws IOException {
		// String filepath = getClass().getResource("../Fonts/Tandysoft.ttf").getFile();
		// File file = new File(filepath);
		// try {
		// FileInputStream fi = new FileInputStream(file);
		// BufferedInputStream fb = new BufferedInputStream(fi);
		// TextFormat1 = Font.createFont(Font.TRUETYPE_FONT, fb);
		// } catch (FontFormatException e) {
		// // this exception will never happen
		// }
		DrawImage panel = new DrawImage();
		this.add(panel);
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		Implement.setMap();
		GlobalTimer.schedule(new TimerTask() {
			public void run() {
				globalTimerTick();
			}
		}, 0, timeSpan);
	}

	public static Hashtable<Object, Boolean> KeyPressedFirst = new Hashtable<Object, Boolean>();
	public static Hashtable<Object, Boolean> KeyPressed = new Hashtable<Object, Boolean>();
	public static Hashtable<Object, Boolean> KeyPressedConfirm = new Hashtable<Object, Boolean>();

	private void globalTimerTick() {
		time++;
		Implement.fixedUpdate();

		Enumeration<String> PKeys = PhysicsObject.physicsObject.keys();
		while (PKeys.hasMoreElements()) {
			PKeys.nextElement();
			for (PhysicsObject one : allPhysicsObjects())
				one.run();
		}

		Enumeration<Object> keys = KeyPressedConfirm.keys();
		while (keys.hasMoreElements()) {
			int thisKey = (int) keys.nextElement();
			if (keyPressedConfirm(thisKey)) {
				KeyPressedConfirm.put(thisKey, false);
				KeyPressedConfirmTimer.put(thisKey, new Timer());
				KeyPressedConfirmTimer.get(thisKey).schedule(new TimerTask() {
					public void run() {
						keyPressedConfirmTick(thisKey);
					}
				}, MainPage.timeSpan * MainPage.interval);
			}
		}
	}

	private void keyPressedConfirmTick(int sender) {
		if (keyPressed(sender))
			MainPage.KeyPressedConfirm.put(sender, true);
		KeyPressedConfirmTimer.get(sender).cancel();
	}
}