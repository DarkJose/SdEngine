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
		if (dialogObject.get("�˵�").animateFrame == dialogObject.get("�˵�").animateSource.act.get("stand").size() - 1)
			dialogObject.get("�˵�").stopAct = true;
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
		float x = unit("����").x - MainPage.cameraX + (CellX / 2 * 128 - 256);
		float y = unit("����").y - MainPage.cameraY + (CellY / 2 * 128 - 128);
		bg.drawImage(unit("����").animate(), (int) (x + unit("����").imageOffset.x), (int) (y + unit("����").imageOffset.y),
				null);
	}

	public static void fixedUpdate() {
		if (keyPressedConfirm(KeyEvent.VK_W) || keyPressedConfirm(KeyEvent.VK_S)) {
			ibox = ~ibox;
		}
		if (keyPressed(Implement2.keyDo) && ibox == 0 && GameState == 0) {
			GameState = 1;
			hideDialog("�˵�");
			cameraMoveTo(1920, 1080, 1, timeSpan);
		} else if (keyPressed(Implement2.keyDo) && ibox == -1 && GameState == 0) {
			System.exit(0);
		}

		if (GameState == 1 && doStart) {
			cameraAim = "����";
			cameraX = unit(cameraAim).x;
			cameraY = unit(cameraAim).y;
			if (keyPressedConfirm(Implement2.keyDo) && !talkready) {
				createPhoton(unit(cameraAim).x, unit(cameraAim).y, unit(cameraAim).dir(), "����ӵ�", the("photon"));
			}
		}
		if (time % (interval * randomInt(5, 9)) == 0)
			if (unit("�ϴ�") != null)
				if (getDistance(unit("����").pos, unit("�ϴ�").pos) <= 1000)
					for (int j = 0; j < 9; j++)
						createPhoton(unit("�ϴ�").x, unit("�ϴ�").y, unit("�ϴ�").dir() + (j - 4) * (float) Math.PI / 8, "�ӵ�",
								the("photon"));
		if (time % (interval * randomInt(5, 9)) == 0) // enemy direction
		{
			for (GameObject one : allGameObjects())
				if (one.name == "С��") {
					one.calculDir = randomInt(-4, 5);
				}
		}
		unit("����").calculDir = 0;
		if (keyPressed(Implement2.keyUp))
			unit("����").calculDir += 3;
		if (keyPressed(Implement2.keyDown))
			unit("����").calculDir -= 3;
		if (keyPressed(Implement2.keyLeft))
			unit("����").calculDir -= 1;
		if (keyPressed(Implement2.keyRight))
			unit("����").calculDir += 1;
		if (unit("����").calculDir != 0) {
			switch (unit("����").calculDir) {
			case 4:
				unit("����").thisAct = "����";
				unit("����").defaultAct = "����";
				break;
			case 1:
				unit("����").thisAct = "��";
				unit("����").defaultAct = "��";
				break;
			case -2:
				unit("����").thisAct = "����";
				unit("����").defaultAct = "����";
				break;
			case -3:
				unit("����").thisAct = "��";
				unit("����").defaultAct = "��";
				break;
			case -4:
				unit("����").thisAct = "����";
				unit("����").defaultAct = "����";
				break;
			case -1:
				unit("����").thisAct = "��";
				unit("����").defaultAct = "��";
				break;
			case 2:
				unit("����").thisAct = "����";
				unit("����").defaultAct = "����";
				break;
			case 3:
				unit("����").thisAct = "��";
				unit("����").defaultAct = "��";
				break;
			}
		}
	}

	public static void collision(GameObject a, GameObject b) {
		if (objectName(a, b, "����ӵ�", "�ϴ�")) {
			life -= 1;
			String life_s = "";
			for (int l = 0; l < life / 2; l++) {
				life_s += "��";
			}
			setDialog("�ϴ�Ѫ��", "Boss Life:" + life_s);
			if (life <= 0) {
				GameState = 2;
				if (a.name.contentEquals("�ϴ�"))
					kill(a);
				else
					kill(b);
			}
			if (a.name.contentEquals("����ӵ�"))
				kill(a);
			else
				kill(b);
		} else

		if (objectName(a, b, "����ӵ�", "С��")) {
			current_xiaodi += 1;
			setDialog("�������", "�ɵ�20ֻС��(" + current_xiaodi + "/" + xiaodi + ")\n�ɵ�Boss��(0/1)");
			kill(a);
			kill(b);
		} else

		if (objectName(a, b, "�����ӵ�", "���")) {
			GameState = 2;
		} else

		if (objectName(a, b, "�ϴ�ռ�", "���")) {
			if (!dialogObject.containsKey("�ϴ�Ѫ��")) {
				createDialog(90, 950, "�ϴ�Ѫ��", null);
				String life_s = "";
				for (int l = 0; l < life; l++) {
					life_s += "��";
				}
				addChat("�ϴ�Ѫ��", "Boss Life:" + life_s);
				textDisplay("�ϴ�Ѫ��", 1.5f);
			}
		} else

		if (objectName(a, b, "���", "NPC")) {
			if (keyPressedConfirm(Implement2.keyDo))
				if (menuoff) {
					menuoff = false;
					GameState = 2;
					unhideDialog("��poem�ĶԻ�");
					textDisplay("��poem�ĶԻ�", 1);
				} else {
					if (keyPressedConfirm(Implement2.keyDo)) {
						if (nextDialog("��poem�ĶԻ�")) {
							menuoff = true;
							Api.createDialog(90, 800, "��������", the("box"));
							Api.createChat(50, 50, "��������");
							Api.addChat("��������", "�ɵ�20ֻС��(" + current_xiaodi + "/" + xiaodi + ")\n�ɵ�Boss��(0/1)");
							GameState = 1;
							textDisplay("��������", 1.5f);
						} else {
							textDisplay("��poem�ĶԻ�", 0.5f);
						}
					}
				}
		}
		if ((GameState == 1 || GameState == 2) && doStart) {
			// ��������
		}
	}

	public static class NormalPhysics extends PhysicsObject {
		public float speed = 1f;

		public NormalPhysics(String name) {
			super(name);
		}

		public void calcul(GameObject object) {
			if (object.name.contentEquals("����ӵ�"))
				speed = 2.5f;
			else if (object.name.contentEquals("�����ӵ�"))
				speed = 1.1f;
			// else if (object.name.contentEquals("С��") || object.name.contentEquals("�ϴ�"))
			// {
			// if (time % (interval / 2) == 0)
			// if (time % (interval * randomInt(5, 12)) == 0) {
			// createPhoton(object.x, object.y, object.dir(), "�����ӵ�", the("photon"));
			// physics("��׼����").add("�����ӵ�");
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
		new NormalPhysics("��׼����");
		cloneAnimateAct("fox", "��", "1");
		cloneAnimateAct("fox", "����", "2");
		cloneAnimateAct("fox", "��", "3");
		cloneAnimateAct("fox", "����", "4");
		cloneAnimateAct("fox", "��", "5");
		cloneAnimateAct("fox", "����", "6");
		cloneAnimateAct("fox", "��", "7");
		cloneAnimateAct("fox", "����", "stand");
		create(12 * 128, 8 * 128, degree(270), "NPC", defaultAct, the("poem"));
		Api.createDialog(110, 870, "��poem�ĶԻ�", the("box"));
		Api.createChat(50, 50, "��poem�ĶԻ�");
		Api.addChat("��poem�ĶԻ�", "Fox!���²�����!���ǲ���Boss�����齫,������һæС�����ҳ�����!");
		Api.addChat("��poem�ĶԻ�", "Ŷ,����Ҍ���.");
		Api.addChat("��poem�ĶԻ�", "���ѰѶ���,����ʵ�ڲ���Ӯ��Ǯ��!");
		Api.addChat("��poem�ĶԻ�", "Ŷ,����Ҍ���.");
		Api.addChat("��poem�ĶԻ�", "��ץ������ŮƱGumi.");
		Api.addChat("��poem�ĶԻ�", "#%$*!@&%(%�����ȥ������.");
		Api.hideDialog("��poem�ĶԻ�");
		cloneAnimateAct("map", "3", "stand");
		for (int i = 0; i < w + 3; i++)
			for (int j = 0; j < w + 3; j++)
				create(i * 128, j * 128, degree(270), "�ݵ�" + i * 100 + j, Integer.toString(randomInt(1, 4)),
						the("map"));
		for (int i = 0; i < 90; i++) {
			create(randomInt(0, w) * 128, randomInt(0, w) * 128, degree(270), "��" + i, defaultAct, the("flowers"));
			create(randomInt(4, w) * 128, randomInt(4, w) * 128, degree(270), "ʯͷ" + i, defaultAct, the("stones"));
			unit("ʯͷ" + i).collision = new Collision.RoundCollision(45);
			unit("ʯͷ" + i).collision.passable = false;
		}
		for (int i = 0; i < w + 3; i++)
			for (int j = 0; j < w + 3; j++)
				create(i * 128, j * 128, degree(270), "�ݵ�" + i * 100 + j, Integer.toString(randomInt(1, 4)),
						the("map"));
		for (int i = 0; i < xiaodi; i++) {
			int a, b;
			a = randomInt(14, w - 10);
			b = randomInt(11, w - 7);
			create(a * 128, b * 128, degree(270), "С��" + i, Implement2.defaultAct, the("trex"));
			physics("��׼����").add("С��" + i);
		}
		create(25 * 128, 20 * 128, degree(270), "�ϴ�", Implement2.defaultAct, the("boss"));
		physics("��׼����").add("�ϴ�");
		Api.createDialog(900, 500, "�˵�", the("menu"));
		Api.createChat(50, 50, "�˵�");
		Api.addChat("�˵�", "");
		Api.addChat("�˵�", "");
		create(1920, 1080, degree(270), "����", Implement2.defaultAct, the("fox"));
		unit("����").collision = new Collision.RoundCollision(90);
		physics("��׼����").add("����");
		unit("����").calculDir = 0;
		MainPage.cameraX = 30 * 128;
		MainPage.cameraY = 30 * 128;
	}

	public static void main(String[] args) throws IOException {
		JFrame frame = new MainPage();
		frame.setTitle("������");
		frame.setUndecorated(false);
		frame.pack();
		frame.setVisible(true);
	}
}
