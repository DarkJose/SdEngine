package root;

import static root.Api.*;
import static root.MainPage.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.Clip;
import javax.swing.JFrame;

public class Implement {
	// 全局预设变量
	public static int DesignWidth = 1920;
	public static int DesignHeight = 1080;
	public static int keyUp = KeyEvent.VK_W;
	public static int keyDown = KeyEvent.VK_S;
	public static int keyLeft = KeyEvent.VK_A;
	public static int keyRight = KeyEvent.VK_D;
	public static int keyDo = KeyEvent.VK_J;
	public static float defaultAnimateSpeed = 100;
	public static String defaultAct = "stand";
	// normal speed is 100,and each frame lasts 0.03s,
	// so when speed is 20,one second will have about 7 frames

	public static void updateEarly(Graphics bg) {
		// 绘制层次控制
		if (dialogObject.get("菜单").animateFrame == dialogObject.get("菜单").animateSource.act.get("stand").size() - 1)
			dialogObject.get("菜单").stopAct = true;
		levelDraw("map1", bg);
		levelDraw("map2", bg);
		levelDraw("map3", bg);
		levelDraw("castle", bg);
		levelDraw("aim", bg);
		levelDraw("boxx", bg);
		levelDraw("old", bg);
		levelDraw("fox", bg);
		bg.drawString("回退", 1650, 890);
		bg.drawRect(1650, 900, 200, 50);
		bg.drawRect(1800, 0, 100, 900);
		bg.drawString("音乐", 50, 40);
		bg.drawRect(50, 50, 50, 50);
	}

	public static void updateLate(Graphics bg) {
		// 绘制在最上层
		if (time > 18 && GameState == 0) {
			bg.drawString("Start", 815, 613);
			bg.drawString("Exit", 819, 643);
			if (ibox == 0) {
				bg.drawImage(the("box", defaultAct, 0), 785, 595, null);
			} else {
				bg.drawImage(the("box", defaultAct, 0), 785, 625, null);
			}
		}
		// 双重碰撞计算
		GameObject object = unit("主角");
		Enumeration<String> collisionObject = gameObject.keys();
		while (collisionObject.hasMoreElements()) {
			GameObject object2 = gameObject.get(collisionObject.nextElement());
			if (!object.hidden && !object2.hidden && !object.equals(object2) && object.collision != null
					&& object2.collision != null) {
				if (Api.collision(object, object2))
					Implement.collision(object, object2);
			}
		}
	}

	public static void fixedUpdate() {
		// 初始状态逻辑
		if (keyPressedConfirm(KeyEvent.VK_W) || keyPressedConfirm(KeyEvent.VK_S)) {
			ibox = ~ibox;
		}
		if (keyPressed(Implement.keyDo) && ibox == 0 && GameState == 0) {
			GameState = 1;
			hideDialog("菜单");
			cameraMoveTo(600, 600, 1, timeSpan);
		} else if (keyPressed(Implement.keyDo) && ibox == -1 && GameState == 0) {
			System.exit(0);
		}
		// 记录变化信息
		if (noDragging)
			if (!mainPosition.equals(unit("主角").pos)) {
				mainPosition.set(unit("主角").pos);
				for (GameObject one : allGameObjects()) {
					if (one.animateSource.equals(the("boxx")) || one.animateSource.equals(the("fox"))
							|| one.animateSource.equals(the("old"))) {
						source.put(time + ":" + one.name, one.animateSource);
						position.put(time + ":" + one.name, new Vector2(one.x, one.y));
						dact.put(time + ":" + one.name, one.defaultAct);
						calculDir.put(time + ":" + one.name, one.calculDir);
						act.put(time + ":" + one.name, one.thisAct);
						frame.put(time + ":" + one.name, one.animateFrame);
					}
				}
			}
		// 将单位角度与按键和动作关联
		if (GameState == 1) {
			unit("主角").calculDir = 0;
			if (keyPressed(Implement.keyUp))
				unit("主角").calculDir += 3;
			if (keyPressed(Implement.keyDown))
				unit("主角").calculDir -= 3;
			if (keyPressed(Implement.keyLeft))
				unit("主角").calculDir -= 1;
			if (keyPressed(Implement.keyRight))
				unit("主角").calculDir += 1;
			if (unit("主角").calculDir != 0) {
				switch (unit("主角").calculDir) {
				case 4:
					unit("主角").thisAct = "右上";
					unit("主角").defaultAct = "右上";
					break;
				case 1:
					unit("主角").thisAct = "右";
					unit("主角").defaultAct = "右";
					break;
				case -2:
					unit("主角").thisAct = "右下";
					unit("主角").defaultAct = "右下";
					break;
				case -3:
					unit("主角").thisAct = "下";
					unit("主角").defaultAct = "下";
					break;
				case -4:
					unit("主角").thisAct = "左下";
					unit("主角").defaultAct = "左下";
					break;
				case -1:
					unit("主角").thisAct = "左";
					unit("主角").defaultAct = "左";
					break;
				case 2:
					unit("主角").thisAct = "左上";
					unit("主角").defaultAct = "左上";
					break;
				case 3:
					unit("主角").thisAct = "上";
					unit("主角").defaultAct = "上";
					break;
				}
			}
		}
	}

	// 鼠标事件
	public static void mouseDragged(MouseEvent e) {
		if (!noDragging) {
			List<Map.Entry<String, Vector2>> list = new ArrayList<Map.Entry<String, Vector2>>(position.entrySet());
			Collections.sort(list, new Comparator<Map.Entry<String, Vector2>>() {
				public int compare(Entry<String, Vector2> arg0, Entry<String, Vector2> arg1) {
					return Integer.parseInt(arg0.getKey().substring(0, arg0.getKey().indexOf(':')))
							- Integer.parseInt(arg1.getKey().substring(0, arg1.getKey().indexOf(':')));
				}
			});

			int i = 0;
			while (i < list.size()) {
				String x = list.get(i).getKey();
				if (x.substring(x.indexOf(':') + 1).contentEquals("主角")) {
					if (((float) e.getX() - 1650) / (1850 - 1650) * position.size() <= i) {
						for (GameObject one : allGameObjects()) {
							one.stopAct = true;
							String k = x.substring(0, x.indexOf(':') + 1) + one.name;
							if (position.containsKey(k)) {
								one.animateSource = source.get(k);
								one.pos(position.get(k));
								one.defaultAct = dact.get(k);
								one.calculDir = calculDir.get(k);
								one.thisAct = act.get(k);
								one.animateFrame = frame.get(k);
							}
						}
						break;
					}
				}
				i++;
			}
		}
		if (!mapNoDragging) {
			Enumeration<String> objects = dialogObject.keys();
			while (objects.hasMoreElements()) {
				DialogObject object = dialogObject.get(objects.nextElement());
				if (object.animateSource.equals(the("show")))
					object.y += (e.getY() - showPos.y) / 10;
			}
		}

	}

	public static void mouseMoved(MouseEvent e) {

	}

	public static void mousePressed(MouseEvent e) {
		if (clickIn(e, new Vector2(50, 50), new Vector2(100, 100))) {
			if (musicStart) {
				musicStart = !musicStart;
				music.stop();
			} else if (!musicStart) {
				musicStart = !musicStart;
				music.start();
			} else {
				musicStart = true;
				music.start();
			}
		}
		if (clickIn(e, new Vector2(1650, 900), new Vector2(1850, 950))) {
			noDragging = false;
		}
		if (clickIn(e, new Vector2(1850, 0), new Vector2(1950, 900))) {
			mapNoDragging = false;
			showPos.set(e.getX(), e.getY());
		}
		int i = 0;
		Enumeration<String> objects = dialogObject.keys();
		while (objects.hasMoreElements()) {
			DialogObject object = dialogObject.get(objects.nextElement());
			if (object.animateSource.equals(the("show"))) {
				if (clickIn(e, new Vector2(1850, object.y - 50), new Vector2(1950, object.y + 50))) {
					cameraMoveTo(600 + 1920 * i, 600, 0.5f, timeSpan);
					List<Map.Entry<String, Vector2>> list = new ArrayList<Map.Entry<String, Vector2>>(
							position.entrySet());
					Collections.sort(list, new Comparator<Map.Entry<String, Vector2>>() {
						public int compare(Entry<String, Vector2> arg0, Entry<String, Vector2> arg1) {
							return Integer.parseInt(arg0.getKey().substring(0, arg0.getKey().indexOf(':')))
									- Integer.parseInt(arg1.getKey().substring(0, arg1.getKey().indexOf(':')));
						}
					});
					for (GameObject one : allGameObjects()) {
						String k = position.keys().nextElement().substring(0,
								position.keys().nextElement().indexOf(":") + 1) + one.name;
						if (position.containsKey(k)) {
							one.animateSource = source.get(k);
							one.pos(position.get(k));
							one.defaultAct = dact.get(k);
							one.calculDir = calculDir.get(k);
							one.thisAct = act.get(k);
							one.animateFrame = frame.get(k);
						}
					}
					unit("主角").pos(mainPos[i]);
					source.clear();
					position.clear();
					dact.clear();
					calculDir.clear();
					act.clear();
					frame.clear();
				}
				i++;
			}
		}
	}

	public static void mouseReleased(MouseEvent e) {
		noDragging = true;
		mapNoDragging = true;
		for (GameObject one : allGameObjects())
			one.stopAct = false;
	}

	public static void mouseClicked(MouseEvent e) {

	}

	// 碰撞事件
	public static void collision(GameObject a, GameObject b) {
		if (objectAnimate(a, b, "fox", "boxx")) {
			if (a.animateSource.equals(the("boxx"))) {
				if (Math.abs(b.calculDir % 2) != 0)
					a.calculDir = b.calculDir;
			} else {
				if (Math.abs(a.calculDir % 2) != 0)
					b.calculDir = a.calculDir;
			}
		}
		if (objectAnimate(a, b, "aim", "boxx")) {
			if (a.animateSource.equals(the("boxx"))) {
				a.animateSource(the("old"));
				move(a, 256 * 10, timeSpan);
			} else {
				b.animateSource(the("old"));
				move(b, 256 * 10, timeSpan);
			}
		}
	}

	// 物理运算内容
	public static class NormalPhysics extends PhysicsObject {
		public float speed = 1f;

		public NormalPhysics(String name) {
			super(name);
		}

		public void calcul(GameObject object) {
			if (GameState == 1 && object.calculDir != 0) {
				float x = object.x, y = object.y;
				if (object.animateSource.equals(the("boxx"))) {
					move(object, 256 * 5, timeSpan);
					checkMove(object, 0, "", x, y);
					object.calculDir = 0;
				} else if (object.animateSource.equals(the("fox"))) {
					move(object, 256 * speed, timeSpan);
					checkMove(object, 0, "", x, y);
				}
			}
		}
	}

	// 内容逻辑变量
	public static Vector2 showPos = new Vector2();
	public static Vector2[] mainPos = new Vector2[100];
	public static Clip music;
	public static boolean noDragging = true, musicStart;
	public static boolean mapNoDragging = true;
	public static int GameState = 0;
	public static int ibox = 0;
	public static Vector2 mainPosition = new Vector2();
	public static Hashtable<String, AnimateObject> source = new Hashtable<String, AnimateObject>();
	public static Hashtable<String, Vector2> position = new Hashtable<String, Vector2>();
	public static Hashtable<String, Integer> calculDir = new Hashtable<String, Integer>();
	public static Hashtable<String, String> dact = new Hashtable<String, String>();
	public static Hashtable<String, String> act = new Hashtable<String, String>();
	public static Hashtable<String, Integer> frame = new Hashtable<String, Integer>();
	public static Hashtable<String, String> maps = new Hashtable<String, String>();

	public static void setMap() {
		// 创建物理运算
		new NormalPhysics("标准物理");
		// 命名动画
		cloneAnimateAct("fox", "下", "1");
		cloneAnimateAct("fox", "左下", "2");
		cloneAnimateAct("fox", "左", "3");
		cloneAnimateAct("fox", "左上", "4");
		cloneAnimateAct("fox", "上", "5");
		cloneAnimateAct("fox", "右上", "6");
		cloneAnimateAct("fox", "右", "7");
		cloneAnimateAct("fox", "右下", "stand");
		// 读取地图
		File root = new File("map");
		File[] files = root.listFiles();
		for (File file : files) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				String tempString = null;
				while ((tempString = reader.readLine()) != null)
					if (maps.get(file.getName()) == null)
						maps.put(file.getName(), tempString);
					else
						maps.put(file.getName(), maps.get(file.getName()) + ":" + tempString);
				reader.close();
			} catch (IOException e) {
			}
		}
		// 生成地图
		int mapNum = -1;
		Enumeration<String> mapsKey = maps.keys();
		while (mapsKey.hasMoreElements()) {
			String key = mapsKey.nextElement();
			String map = maps.get(key);
			String last = "", aim = "";
			int line = 0;
			mapNum++;
			Api.createDialog(1850, 200 + -150 + mapNum * 150, mapNum + "", the("show"));
			Api.createChat(-5, 5, mapNum + "");
			Api.addChat(mapNum + "", mapNum + 1 + "");
			dialog(mapNum + "").textColor = Color.DARK_GRAY;
			while (!last.contentEquals(map)) {
				if (line == 0) {
					aim = map.substring(0, 1);
					last = map.substring(map.indexOf(":") + 1);
				} else {
					if (last.indexOf(":") != -1) {
						aim = last.substring(0, last.indexOf(":"));
						last = last.substring(last.indexOf(":") + 1);
					} else {
						aim = last;
						last = map;
					}
				}
				if (aim.length() == 1)
					create(mapNum * 1920 + 664, 605, degree(270), aim + key, defaultAct, the("map" + aim));
				else {
					for (int i = 0; i < aim.length(); i++) {
						if (aim.substring(i, i + 1).contentEquals("1")) {
							create(mapNum * 1920 + i * 128, line * 128, degree(270),
									"石头" + i * 100 + line + mapNum * 10000, defaultAct, the("castle"));
							unit("石头" + i * 100 + line + mapNum * 10000).collision = new Collision.RoundCollision(80);
							unit("石头" + i * 100 + line + mapNum * 10000).collision.passable = false;
						}
						if (aim.substring(i, i + 1).contentEquals("2")) {
							create(mapNum * 1920 + i * 128, line * 128, degree(270),
									"箱子" + i * 100 + line + mapNum * 10000, defaultAct, the("boxx"));
							unit("箱子" + i * 100 + line + mapNum * 10000).collision = new Collision.RoundCollision(30);
							unit("箱子" + i * 100 + line + mapNum * 10000).collision.passable = false;
							unit("箱子" + i * 100 + line + mapNum * 10000).animateSpeed = 20;
							physics("标准物理").add("箱子" + i * 100 + line + mapNum * 10000);
						}
						if (aim.substring(i, i + 1).contentEquals("3")) {
							create(mapNum * 1920 + i * 128, line * 128, degree(270),
									"目标" + i * 100 + line + mapNum * 10000, defaultAct, the("aim"));
							unit("目标" + i * 100 + line + mapNum * 10000).collision = new Collision.RoundCollision(80);
							unit("目标" + i * 100 + line + mapNum * 10000).collision.passable = false;
						}
						if (aim.substring(i, i + 1).contentEquals("4")) {
							mainPos[mapNum] = new Vector2();
							mainPos[mapNum].x = mapNum * 1920 + i * 128;
							mainPos[mapNum].y = line * 128;
							if (mapNum == 0) {
								create(i * 128, line * 128, degree(270), "主角", Implement.defaultAct, the("fox"));
								unit("主角").collision = new Collision.RoundCollision(30);
								physics("标准物理").add("主角");
							}
						}
					}
				}
				line++;
			}
		}
		// 创建对话框
		Api.createDialog(860, 540, "菜单", the("show2"));
		Api.createChat(0, 0, "菜单");
		Api.addChat("菜单", "");
		// 设定镜头位置
		MainPage.cameraX = 600;
		MainPage.cameraY = 600;
	}

	public static void main(String[] args) throws IOException {
		// 窗口创建与设定
		JFrame frame = new MainPage();
		music = loadMusic("src/music/a.wav");
		frame.setTitle("推箱子");
		frame.setUndecorated(false);
		frame.pack();
		frame.setVisible(true);
	}
}
