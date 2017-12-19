package root;

import static root.GameObject.*;
import static root.MainPage.CellX;
import static root.MainPage.CellY;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Api {
	public static java.util.Hashtable<String, DialogObject> dialogObject = new java.util.Hashtable<String, DialogObject>();
	public static java.util.Hashtable<String, GameObject> gameObject = new java.util.Hashtable<String, GameObject>();

	public static boolean clickIn(MouseEvent e, Vector2 a, Vector2 b) {
		return (e.getX() > a.x && e.getX() < b.x && e.getY() > a.y && e.getY() < b.y);
	}

	public static Clip loadMusic(String name) {
		URL url = null;
		Clip clip = null;
		try {
			url = new URL("file:" + name);
			AudioInputStream audioIn = null;
			audioIn = AudioSystem.getAudioInputStream(url);
			clip = AudioSystem.getClip();
			clip.open(audioIn);
		} catch (Exception e) {
		}
		return clip;
	}

	public static void levelDraw(String name, Graphics bg) {
		Enumeration<String> keys = gameObject.keys();
		while (keys.hasMoreElements()) {
			GameObject object = gameObject.get(keys.nextElement());
			if (object.animateSource == the(name))
				if (!object.hidden) {
					float x = object.x - MainPage.cameraX + (CellX / 2 * 128 - 256);
					float y = object.y - MainPage.cameraY + (CellY / 2 * 128 - 128);
					if (x >= -128 && x <= Implement.DesignWidth + 128 && y >= -128 && y <= Implement.DesignHeight + 128)
						bg.drawImage(object.animate(), (int) (x + object.imageOffset.x),
								(int) (y + object.imageOffset.y), null);
				}
		}
	}

	public static ArrayList<GameObject> allGameObjects() {
		return Collections.list(gameObject.elements());
	}

	public static ArrayList<PhysicsObject> allPhysicsObjects() {
		return Collections.list(PhysicsObject.physicsObject.elements());
	}

	public static int randomInt(int min, int max) {
		java.util.Random random = new java.util.Random();
		return random.nextInt(max - min) + min;
	}

	public static boolean objectName(GameObject a, GameObject b, String n1, String n2) {
		return (a.name.contentEquals(n1) && b.name.contentEquals(n2))
				|| (b.name.contentEquals(n1) && a.name.contentEquals(n2));
	}

	public static boolean objectAnimate(GameObject a, GameObject b, String n1, String n2) {
		return (a.animateSource.equals(animateObject.get(n1)) && b.animateSource.equals(animateObject.get(n2)))
				|| (b.animateSource.equals(animateObject.get(n1)) && a.animateSource.equals(animateObject.get(n2)));
	}

	public static PhysicsObject physics(String phyAim) {
		return PhysicsObject.physicsObject.get(phyAim);
	}

	public static GameObject unit(String gameAim) {
		return gameObject.get(gameAim);
	}

	public static void cloneAnimateAct(String aniAim, String actName, String source) {
		the(aniAim).act.put(actName, the(aniAim, source));
	}

	public static AnimateObject the(String aniAim) {
		return animateObject.get(aniAim);
	}

	public static List<Image> the(String aniAim, String actAim) {
		return animateObject.get(aniAim).act.get(actAim);
	}

	public static Image the(String aniAim, String actAim, Integer aim) {
		return animateObject.get(aniAim).act.get(actAim).get(aim);
	}

	public static void setAnimateAct(String animateSource, String act) {
		if (animateObject.get(animateSource).act.get(act) == null)
			animateObject.get(animateSource).act.put(act, new ArrayList<Image>());
	}

	public static void setAnimateAct(String animateSource, String act, Image source) {
		if (animateObject.get(animateSource).act.get(act) == null)
			animateObject.get(animateSource).act.put(act, new ArrayList<Image>());
		animateObject.get(animateSource).act.get(act).add(source);
	}

	public static int fileToInt(File file) {
		return Integer.parseInt(deleteSuffix(file.getName()));
	}

	public static File intToFile(String parentName, Integer i) {
		return new File("src/image/" + parentName + "/" + Integer.toString(i) + ".png");
	}

	// public static void animateFilling(String fileName, int toBe) {
	// for (int n = toBe - animateObject.get(fileName).size(); n-- > 0;)
	// animateObject.get(fileName).add(null);
	// }

	public Image getAbsoluteImage(String AbsolutePath) {
		return new ImageIcon(getClass()
				.getResource("../" + AbsolutePath.substring(AbsolutePath.lastIndexOf("image")).replaceAll("\\\\", "/")))
						.getImage();
	}

	public static String deleteSuffix(String name) {
		if (name.lastIndexOf(".") == -1)
			return name;
		return name.substring(0, name.lastIndexOf("."));
	}

	public static String getParentName(File file) {
		return new File(file.getParent()).getName();
	}

	public static boolean keyPressedConfirm(int key) {
		if (MainPage.KeyPressed.getOrDefault(key, false) && MainPage.KeyPressedConfirm.getOrDefault(key, false))
			return true;
		return false;
	}

	public static boolean keyPressed(int key) {
		if (MainPage.KeyPressed.getOrDefault(key, false))
			return true;
		return false;
	}

	public static void killDialog(String name) {
		dialogObject.remove(name);
	}

	public static void createChat(float x, float y, String name) {
		dialogObject.get(name).contentObject = new DialogObject.ContentObject(x, y);
	}

	public static void addChat(String name, String content) {
		dialogObject.get(name).contentObject.dialogContent.add(content);
	}

	public static void kill(String name) {
		gameObject.remove(name);
	}

	public static void kill(GameObject o) {
		gameObject.remove(o.name);
	}

	public static void hideDialog(String name) {
		dialogObject.get(name).hidden = true;
	}

	public static void unhideDialog(String name) {
		dialogObject.get(name).hidden = false;
	}

	public static void hideDialog(DialogObject o) {
		o.hidden = true;
	}

	public static void unhideDialog(DialogObject o) {
		o.hidden = false;
	}

	public static void hide(String name) {
		unit(name).hidden = true;
	}

	public static void unhide(String name) {
		unit(name).hidden = false;
	}

	public static void hide(GameObject o) {
		o.hidden = true;
	}

	public static void unhide(GameObject o) {
		o.hidden = false;
	}

	public static void create(float x, float y, float dir, String name, String defaultAct,
			AnimateObject animateSource) {
		gameObject.put(name, new GameObject(x, y, dir, name, defaultAct, animateSource));
	}

	public static void createDialog(float x, float y, String name, AnimateObject animateSource) {
		dialogObject.put(name, new DialogObject(name, animateSource));
		dialogObject.get(name).pos(x, y);
	}

	public static void createPhoton(float x, float y, float dir, String name, AnimateObject animateSource) {
		gameObject.put(name, new GameObject(x, y, dir, name, Implement.defaultAct, animateSource));
	}

	public static float degree(float degree) {
		return (float) (2 * Math.PI * degree / 360);
	}

	// translate from screen coordinates to macro coordinates
	public static float relative(char xy, float n) {
		return xy == 'x' ? n + MainPage.cameraX - (MainPage.CellX / 2 * 128 - 256)
				: n + MainPage.cameraY - (MainPage.CellY / 2 * 128 - 128);
	}

	public static boolean collision(GameObject a, GameObject b) {
		a.collision.parentVector = (a.pos.add(a.collision.offset));
		b.collision.parentVector = (b.pos.add(b.collision.offset));
		return Collision.chooseType(a.collision.collisionType, b.collision.collisionType, a.collision, b.collision);
	}

	public static double getDistance(Vector2 a, Vector2 b) {
		return b.sub(a).len();
	}

	public static double getDirection(Vector2 a, Vector2 b) {
		float x = b.x - a.x;
		float y = b.y - a.y;
		double d = getDistance(a, b);
		double dir = Math.acos(x / d);
		if (x * y * dir == 0) {
			if (y < 0) {
				return dir + Math.PI;
			} else {
				return dir;
			}
		} else if (x * y * dir > 0) {
			return dir;
		} else {
			return dir + Math.PI;
		}
	}

	public static void checkMove(GameObject one, float radius, String type, float originX, float originY) {
		Collision.RoundCollision aim = new Collision.RoundCollision(radius);
		aim.parentVector = new Vector2(one.x, one.y);
		Enumeration<String> keys = gameObject.keys();
		while (keys.hasMoreElements()) {
			GameObject object = unit(keys.nextElement());
			if (object.animateSource.equals(the(type)) || type.contentEquals(""))
				if (object.collision != null && !object.hidden) {
					object.collision.parentVector = object.pos.add(object.collision.offset);
					if (Collision.chooseType(aim.collisionType, object.collision.collisionType, aim,
							object.collision)) {
						if (!object.collision.passable) {
							one.x = originX;
							one.y = originY;
						}
						Implement.collision(object, one);
					}
				}
		}
	}

	public static boolean checkNoCollision(GameObject one, String type) {
		one.collision.parentVector = (one.pos.add(one.collision.offset));
		Enumeration<String> keys = gameObject.keys();
		while (keys.hasMoreElements()) {
			GameObject object = unit(keys.nextElement());
			if (object.animateSource.equals(the(type)) || type.contentEquals(""))
				if (object.collision != null && !object.hidden) {
					object.collision.parentVector = object.pos.add(object.collision.offset);
					return (!Collision.chooseType(one.collision.collisionType, object.collision.collisionType,
							one.collision, object.collision));
				}
		}
		return true;
	}

	public static void move(String object, double distance, int timeSpan) {
		unit(object).x += distance * Math.cos(unit(object).finalDir) * timeSpan / 1000;
		unit(object).y -= distance * Math.sin(unit(object).finalDir) * timeSpan / 1000;
	}

	public static void move(GameObject object, double distance, int timeSpan) {
		object.x += distance * Math.cos(object.finalDir) * timeSpan / 1000;
		object.y -= distance * Math.sin(object.finalDir) * timeSpan / 1000;
	}

	public static void moveBack(String object, double distance, int timeSpan) {
		unit(object).x -= distance * Math.cos(unit(object).finalDir) * timeSpan / 1000;
		unit(object).y += distance * Math.sin(unit(object).finalDir) * timeSpan / 1000;
	}

	public static void moveByDir(String object, double distance, double dir, int timeSpan) {
		unit(object).x += distance * Math.cos(dir) * timeSpan / 1000;
		unit(object).y -= distance * Math.sin(dir) * timeSpan / 1000;
	}

	public static void moveBackByDir(String object, double distance, double dir, int timeSpan) {
		unit(object).x -= distance * Math.cos(dir) * timeSpan / 1000;
		unit(object).y += distance * Math.sin(dir) * timeSpan / 1000;
	}

	public static void cameraMove(double s, double dir, int timeSpan) {
		MainPage.cameraX += (int) (s * Math.cos(dir) * timeSpan / 1000);
		MainPage.cameraY -= (int) (s * Math.sin(dir) * timeSpan / 1000);
	}

	public static void cameraMoveBack(double s, double dir, int timeSpan) {
		MainPage.cameraX -= (int) (s * Math.cos(dir) * timeSpan / 1000);
		MainPage.cameraY += (int) (s * Math.sin(dir) * timeSpan / 1000);
	}

	public static class CameraTimer extends Timer {
		private int timeSpan;

		private float startTime;

		private float endTime;

		private double x;

		private double dir;

		public int getTimeSpan() {
			return timeSpan;
		}

		public void setTimeSpan(int timeSpan) {
			this.timeSpan = timeSpan;
		}

		public float getStartTime() {
			return startTime;
		}

		public void setStartTime(float startTime) {
			this.startTime = startTime;
		}

		public float getEndTime() {
			return endTime;
		}

		public void setEndTime(float endTime) {
			this.endTime = endTime;
		}

		public double getX() {
			return x;
		}

		public void setX(double x) {
			this.x = x;
		}

		public double getDir() {
			return dir;
		}

		public void setDir(double dir) {
			this.dir = dir;
		}
	}

	public static void cameraMoveTo(int x, int y, float time, int timeSpan) {
		Vector2 a = new Vector2(MainPage.cameraX, MainPage.cameraY);
		Vector2 b = new Vector2(x, y);
		double d = getDistance(a, b);
		double dir = getDirection(a, b);
		CameraTimer cameraTimer = new CameraTimer();
		cameraTimer.setTimeSpan(timeSpan);
		cameraTimer.setStartTime(0);
		cameraTimer.setEndTime(time / timeSpan * 1000);
		cameraTimer.setX(d / time);
		cameraTimer.setDir(dir);
		cameraTimer.schedule(new TimerTask() {
			public void run() {
				cameraTick(cameraTimer);
			}
		}, 0, timeSpan);
	}

	private static void cameraTick(Object sender) {
		CameraTimer cameraTimer = (CameraTimer) sender;
		cameraTimer.setStartTime(cameraTimer.getStartTime() + 1);
		if (cameraTimer.getStartTime() >= cameraTimer.getEndTime()) {
			cameraTimer.cancel();
		} else {
			cameraMove(cameraTimer.getX(), cameraTimer.getDir(), cameraTimer.getTimeSpan());
		}
	}

	public static class TextTimer extends Timer {
		private String privateText;

		public final String getText() {
			return privateText;
		}

		public final void setText(String value) {
			privateText = value;
		}

		private String privateName;

		public final String getName() {
			return privateName;
		}

		public final void setName(String value) {
			privateName = value;
		}

		private int privateIndex;

		public final int getIndex() {
			return privateIndex;
		}

		public final void setIndex(int value) {
			privateIndex = value;
		}

		private float privateStarttime;

		public final float getStarttime() {
			return privateStarttime;
		}

		public final void setStarttime(float value) {
			privateStarttime = value;
		}

		private float privateEndtime;

		public final float getEndtime() {
			return privateEndtime;
		}

		public final void setEndtime(float value) {
			privateEndtime = value;
		}
	}

	public static DialogObject.ContentObject dialog(DialogObject object) {
		return object.contentObject;
	}

	public static DialogObject.ContentObject dialog(String name) {
		return dialogObject.get(name).contentObject;
	}

	public static void setDialog(String name, String content) {
		dialog(name).dialogContent.set(dialog(name).dialogIndex, content);
	}

	public static boolean nextDialog(String name) {
		if (dialog(name).dialogContent.size() <= dialog(name).dialogIndex + 1) {
			dialog(name).hidden = true;
			return true;
		} else
			dialog(name).dialogIndex += 1;
		return false;
	}

	public static void textDisplay(String name, float time) {
		TextTimer textTimer = new TextTimer();
		textTimer.setText(dialog(name).dialogContent.get(dialog(name).dialogIndex));
		textTimer.setName(name);
		textTimer.setIndex(dialog(name).dialogIndex);
		textTimer.setStarttime(0);
		textTimer.setEndtime(time / MainPage.timeSpan * 1000);
		textTimer.schedule(new TimerTask() {
			public void run() {
				textTick(textTimer);
			}
		}, 0, MainPage.timeSpan);
	}

	private static void textTick(Object sender) {
		TextTimer textTimer = (TextTimer) sender;
		String name = textTimer.getName();
		textTimer.setStarttime(textTimer.getStarttime() + 1);
		if (textTimer.getStarttime() >= textTimer.getEndtime()) {
			dialog(name).dialogContent.set(textTimer.getIndex(), textTimer.getText());
			textTimer.cancel();
		} else
			dialog(name).dialogContent.set(textTimer.getIndex(), textTimer.getText().substring(0,
					(int) (textTimer.getStarttime() / textTimer.getEndtime() * (textTimer.getText().length() - 1))));
	}
}