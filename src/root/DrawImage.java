package root;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Panel;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Enumeration;

import static root.MainPage.*;
import static root.GameObject.*;
import static root.Api.*;

public class DrawImage extends Panel {
	private static final long serialVersionUID = 1L;

	public DrawImage() {
		String filePath = "src/image";
		createResources(filePath);
		setFocusable(true);
		setPreferredSize(new Dimension(Implement.DesignWidth, Implement.DesignHeight));
		addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				KeyPressed.put(e.getKeyCode(), true);
				if (KeyPressedFirst.getOrDefault(e.getKeyCode(), true)) {
					KeyPressedConfirm.put(e.getKeyCode(), true);
					KeyPressedFirst.put(e.getKeyCode(), false);
				}
			}

			public void keyReleased(KeyEvent e) {
				KeyPressed.put(e.getKeyCode(), false);
				KeyPressedFirst.put(e.getKeyCode(), true);
				KeyPressedConfirm.put(e.getKeyCode(), false);
			}
		});
		addMouseListener(new MouseAdapter()// Êó±ê¼àÌý
		{
			public void mouseClicked(MouseEvent e) {
				Implement.mouseClicked(e);
			}

			public void mousePressed(MouseEvent e) {
				Implement.mousePressed(e);
			}

			public void mouseReleased(MouseEvent e) {
				Implement.mouseReleased(e);
			}
		});
		addMouseMotionListener(new MouseAdapter()// Êó±ê¼àÌý
		{
			public void mouseMoved(MouseEvent e) {
				Implement.mouseMoved(e);
			}

			public void mouseDragged(MouseEvent e) {
				Implement.mouseDragged(e);
			}
		});

	}

	public void paint(Graphics g) {
		BufferedImage bf = new BufferedImage(Implement.DesignWidth, Implement.DesignHeight, 2); // »º³åÍ¼Æ¬
		Graphics bg = bf.createGraphics(); // »º³åÍ¼Æ¬µÄgraphics¶ÔÏó
		Implement.updateEarly(bg);
		Enumeration<String> dKeys = dialogObject.keys();
		while (dKeys.hasMoreElements()) {
			DialogObject object = dialogObject.get(dKeys.nextElement());
			if (!object.hidden) {
				bg.drawImage(object.animate(), (int) (object.x + object.imageOffset.x),
						(int) (object.y + object.imageOffset.y), null);
				if (!object.contentObject.hidden) {
					bg.setFont(new Font(dialog(object).textFormat, dialog(object).textStyle, dialog(object).textSize));
					bg.setColor(dialog(object).textColor);
					bg.drawString(dialog(object).dialogContent.get(dialog(object).dialogIndex),
							(int) (object.x + dialog(object).x), (int) (object.y + dialog(object).y));
				}
			}
		}
		// Enumeration<String> keys = gameObject.keys();
		// while (keys.hasMoreElements()) {
		// GameObject object = gameObject.get(keys.nextElement());

		// }
		Implement.updateLate(bg);
		g.drawImage(bf, 0, 0, null);
		repaint();
	}

	public void update(Graphics g) {
		paint(g);
	}

	private void createResources(String filePath) {
		File root = new File(filePath);
		File[] files = root.listFiles();
		for (File file : files)
			if (file.isDirectory()) {
				animateObject.put(file.getName(), new AnimateObject());
				createResources(file.getAbsolutePath());
			} else if (getParentName(file).contentEquals("image")) {
				animateObject.put(deleteSuffix(file.getName()), new AnimateObject());
				setAnimateAct(deleteSuffix(file.getName()), Implement.defaultAct,
						new Api().getAbsoluteImage(file.getAbsolutePath()));
			} else {
				if (the(getParentName(file)).act.size() == 0 || the(getParentName(file)).act.size() == 1) {
					setAnimateAct(getParentName(file), Implement.defaultAct,
							new Api().getAbsoluteImage(file.getAbsolutePath()));
				} else
					setAnimateAct(getParentName(file), Integer.toString(the(getParentName(file)).act.size() - 1),
							new Api().getAbsoluteImage(file.getAbsolutePath()));
				if (!intToFile(getParentName(file), fileToInt(file) + 1).exists())
					setAnimateAct(getParentName(file), Integer.toString(the(getParentName(file)).act.size()));
			}
	}
}