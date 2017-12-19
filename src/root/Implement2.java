package root;

import static root.Api.*;
import static root.MainPage.*;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;

public class Implement2 {
	public static int DesignWidth = 1920;
	public static int DesignHeight = 1080;
	public static int keyUp = KeyEvent.VK_W;
	public static int keyDown = KeyEvent.VK_S;
	public static int keyLeft = KeyEvent.VK_A;
	public static int keyRight = KeyEvent.VK_D;
	public static int keyDo = KeyEvent.VK_J;
	public static float defaultAnimateSpeed = 20;
	public static String defaultAct = "stand";
	// normal speed is 100,and each frame lasts 0.03s,
	// so when speed is 20,one second will have about 7 frames

	public static void updateEarly(Graphics bg) {
		if (dialogObject.get("菜单").animateFrame == dialogObject.get("菜单").animateSource.act.get("stand").size() - 1)
			dialogObject.get("菜单").stopAct = true;
	}

	public static void updateLate(Graphics bg) {
		if (time > 18 && GameState == 0) {
			bg.drawString("Start", 900, 520);
			bg.drawString("Exit", 904, 580);
			if (ibox == 0) {
				bg.drawImage(the("box", defaultAct, 0), 865, 505, null);
			} else {
				bg.drawImage(the("box", defaultAct, 0), 865, 565, null);
			}
		}
		float x = unit("主角").x - MainPage.cameraX + (CellX / 2 * 128 - 256);
		float y = unit("主角").y - MainPage.cameraY + (CellY / 2 * 128 - 128);
		bg.drawImage(unit("主角").animate(), (int) (x + unit("主角").imageOffset.x), (int) (y + unit("主角").imageOffset.y),
				null);
	}

	public static void fixedUpdate() {
		if (keyPressedConfirm(KeyEvent.VK_W) || keyPressedConfirm(KeyEvent.VK_S)) {
			ibox = ~ibox;
		}
		if (keyPressed(Implement2.keyDo) && ibox == 0 && GameState == 0) {
			GameState = 1;
			hideDialog("菜单");
			cameraMoveTo(1920, 1080, 1, timeSpan);
		} else if (keyPressed(Implement2.keyDo) && ibox == -1 && GameState == 0) {
			System.exit(0);
		}

		if (GameState == 1 && doStart) {
			cameraAim = "主角";
			cameraX = unit(cameraAim).x;
			cameraY = unit(cameraAim).y;
			if (keyPressedConfirm(Implement2.keyDo) && !talkready) {
				createPhoton(unit(cameraAim).x, unit(cameraAim).y, unit(cameraAim).dir(), "玩家子弹", the("photon"));
			}
		}
		if (time % (interval * randomInt(5, 9)) == 0)
			if (unit("老大") != null)
				if (getDistance(unit("主角").pos, unit("老大").pos) <= 1000)
					for (int j = 0; j < 9; j++)
						createPhoton(unit("老大").x, unit("老大").y, unit("老大").dir() + (j - 4) * (float) Math.PI / 8, "子弹",
								the("photon"));
		if (time % (interval * randomInt(5, 9)) == 0) // enemy direction
		{
			for (GameObject one : allGameObjects())
				if (one.name == "小怪") {
					one.calculDir = randomInt(-4, 5);
				}
		}
		unit("主角").calculDir = 0;
		if (keyPressed(Implement2.keyUp))
			unit("主角").calculDir += 3;
		if (keyPressed(Implement2.keyDown))
			unit("主角").calculDir -= 3;
		if (keyPressed(Implement2.keyLeft))
			unit("主角").calculDir -= 1;
		if (keyPressed(Implement2.keyRight))
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

	public static void collision(GameObject a, GameObject b) {
		if (objectName(a, b, "玩家子弹", "老大")) {
			life -= 1;
			String life_s = "";
			for (int l = 0; l < life / 2; l++) {
				life_s += "■";
			}
			setDialog("老大血条", "Boss Life:" + life_s);
			if (life <= 0) {
				GameState = 2;
				if (a.name.contentEquals("老大"))
					kill(a);
				else
					kill(b);
			}
			if (a.name.contentEquals("玩家子弹"))
				kill(a);
			else
				kill(b);
		} else

		if (objectName(a, b, "玩家子弹", "小怪")) {
			current_xiaodi += 1;
			setDialog("打怪任务", "干掉20只小弟(" + current_xiaodi + "/" + xiaodi + ")\n干掉Boss酱(0/1)");
			kill(a);
			kill(b);
		} else

		if (objectName(a, b, "怪物子弹", "玩家")) {
			GameState = 2;
		} else

		if (objectName(a, b, "老大空间", "玩家")) {
			if (!dialogObject.containsKey("老大血条")) {
				createDialog(90, 950, "老大血条", null);
				String life_s = "";
				for (int l = 0; l < life; l++) {
					life_s += "■";
				}
				addChat("老大血条", "Boss Life:" + life_s);
				textDisplay("老大血条", 1.5f);
			}
		} else

		if (objectName(a, b, "玩家", "NPC")) {
			if (keyPressedConfirm(Implement2.keyDo))
				if (menuoff) {
					menuoff = false;
					GameState = 2;
					unhideDialog("和poem的对话");
					textDisplay("和poem的对话", 1);
				} else {
					if (keyPressedConfirm(Implement2.keyDo)) {
						if (nextDialog("和poem的对话")) {
							menuoff = true;
							Api.createDialog(90, 800, "任务内容", the("box"));
							Api.createChat(50, 50, "任务内容");
							Api.addChat("任务内容", "干掉20只小弟(" + current_xiaodi + "/" + xiaodi + ")\n干掉Boss酱(0/1)");
							GameState = 1;
							textDisplay("任务内容", 1.5f);
						} else {
							textDisplay("和poem的对话", 0.5f);
						}
					}
				}
		}
		if ((GameState == 1 || GameState == 2) && doStart) {
			// 绘制条件
		}
	}

	public static class NormalPhysics extends PhysicsObject {
		public float speed = 1f;

		public NormalPhysics(String name) {
			super(name);
		}

		public void calcul(GameObject object) {
			if (object.name.contentEquals("玩家子弹"))
				speed = 2.5f;
			else if (object.name.contentEquals("怪物子弹"))
				speed = 1.1f;
			// else if (object.name.contentEquals("小怪") || object.name.contentEquals("老大"))
			// {
			// if (time % (interval / 2) == 0)
			// if (time % (interval * randomInt(5, 12)) == 0) {
			// createPhoton(object.x, object.y, object.dir(), "怪物子弹", the("photon"));
			// physics("标准物理").add("怪物子弹");
			// }
			// }
			if (GameState == 1 && object.calculDir != 0)
				move(object, 256 * speed, timeSpan);
		}
	}

	public static String cameraAim;
	public static int GameState = 0;
	public static final int w = 40;
	public static boolean menuoff = true;
	public static int xiaodi = 20;
	public static int current_xiaodi = 0;
	public static int ibox = 0;
	public static boolean doStart = false;
	public static boolean talkready = false;
	public static Color chatcolor = Color.white;
	public static int life = 50;

	public static void setMap() {
		new NormalPhysics("标准物理");
		cloneAnimateAct("fox", "下", "1");
		cloneAnimateAct("fox", "左下", "2");
		cloneAnimateAct("fox", "左", "3");
		cloneAnimateAct("fox", "左上", "4");
		cloneAnimateAct("fox", "上", "5");
		cloneAnimateAct("fox", "右上", "6");
		cloneAnimateAct("fox", "右", "7");
		cloneAnimateAct("fox", "右下", "stand");
		create(12 * 128, 8 * 128, degree(270), "NPC", defaultAct, the("poem"));
		Api.createDialog(110, 870, "和poem的对话", the("box"));
		Api.createChat(50, 50, "和poem的对话");
		Api.addChat("和poem的对话", "Fox!大事不好啦!我们不让Boss来打麻将,她带着一忙小弟来砸场子啦!");
		Api.addChat("和poem的对话", "哦,这关我屌事.");
		Api.addChat("和poem的对话", "她把把都输,我们实在不想赢她钱啦!");
		Api.addChat("和poem的对话", "哦,这关我屌事.");
		Api.addChat("和poem的对话", "她抓走了你女票Gumi.");
		Api.addChat("和poem的对话", "#%$*!@&%(%我这就去干死她.");
		Api.hideDialog("和poem的对话");
		cloneAnimateAct("map", "3", "stand");
		for (int i = 0; i < w + 3; i++)
			for (int j = 0; j < w + 3; j++)
				create(i * 128, j * 128, degree(270), "草地" + i * 100 + j, Integer.toString(randomInt(1, 4)),
						the("map"));
		for (int i = 0; i < 90; i++) {
			create(randomInt(0, w) * 128, randomInt(0, w) * 128, degree(270), "花" + i, defaultAct, the("flowers"));
			create(randomInt(4, w) * 128, randomInt(4, w) * 128, degree(270), "石头" + i, defaultAct, the("stones"));
			unit("石头" + i).collision = new Collision.RoundCollision(45);
			unit("石头" + i).collision.passable = false;
		}
		for (int i = 0; i < w + 3; i++)
			for (int j = 0; j < w + 3; j++)
				create(i * 128, j * 128, degree(270), "草地" + i * 100 + j, Integer.toString(randomInt(1, 4)),
						the("map"));
		for (int i = 0; i < xiaodi; i++) {
			int a, b;
			a = randomInt(14, w - 10);
			b = randomInt(11, w - 7);
			create(a * 128, b * 128, degree(270), "小怪" + i, Implement2.defaultAct, the("trex"));
			physics("标准物理").add("小怪" + i);
		}
		create(25 * 128, 20 * 128, degree(270), "老大", Implement2.defaultAct, the("boss"));
		physics("标准物理").add("老大");
		Api.createDialog(900, 500, "菜单", the("menu"));
		Api.createChat(50, 50, "菜单");
		Api.addChat("菜单", "");
		Api.addChat("菜单", "");
		create(1920, 1080, degree(270), "主角", Implement2.defaultAct, the("fox"));
		unit("主角").collision = new Collision.RoundCollision(90);
		physics("标准物理").add("主角");
		unit("主角").calculDir = 0;
		MainPage.cameraX = 30 * 128;
		MainPage.cameraY = 30 * 128;
	}

	public static void main(String[] args) throws IOException {
		JFrame frame = new MainPage();
		frame.setTitle("推箱子");
		frame.setUndecorated(false);
		frame.pack();
		frame.setVisible(true);
	}
}
