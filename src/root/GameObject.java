package root;

import java.awt.Image;
import java.util.Hashtable;

import static root.Api.*;

public class GameObject {
	public boolean hidden = false;
	public float chatX;
	public float chatY;
	public String chatName;
	public float x;
	public float y;
	public Vector2 pos = new Vector2(0, 0);
	public Vector2 imageOffset = new Vector2(0, 0);
	public boolean imageCenterOffset = true;
	public int calculDir = 0;
	public float finalDir;
	public String name;
	public String thisAct = Implement.defaultAct;
	public String defaultAct = Implement.defaultAct;
	public boolean stopAct = false;
	public int animateFrame = -1;
	public float animateSpeed = Implement.defaultAnimateSpeed;
	public AnimateObject animateSource;

	public static Hashtable<String, AnimateObject> animateObject = new Hashtable<String, AnimateObject>();

	public Collision collision;

	public GameObject(String name, AnimateObject animateSource) {
		this.name = name;
		this.animateSource = animateSource;
	}

	public GameObject(float x, float y, float dir, String name, String defaultAct, AnimateObject animateSource) {
		this.x = x;
		this.y = y;
		this.pos.set(x, y);
		this.finalDir = dir;
		this.name = name;
		this.defaultAct = defaultAct;
		this.animateSource = animateSource;
	}

	public void pos(float x, float y) {
		this.x = x;
		this.y = y;
		this.pos.set(x, y);
	}

	public void pos(Vector2 v) {
		this.x = v.x;
		this.y = v.y;
		this.pos.set(v);
	}

	public float dir() {
		return this.finalDir;
	}

	public void dir(float dir) {
		this.finalDir = dir;
	}

	public void name(String name) {
		gameObject.remove(name);
		this.name = name;
		gameObject.put(name, this);
	}

	public void defaultAct(String defaultAct) {
		this.defaultAct = defaultAct;
	}

	public void animateSource(AnimateObject animateSource) {
		this.animateSource = animateSource;
	}

	public void set(float x, float y, float dir, String name, String defaultAct, AnimateObject animateSource) {
		this.x = x;
		this.y = y;
		this.pos.set(x, y);
		this.finalDir = dir;
		this.name = name;
		this.defaultAct = defaultAct;
		this.animateSource = animateSource;
	}

	public Image animate() {
		this.pos.set(this.x, this.y);
		if (calculDir != 0) {
			switch (calculDir) {
			case 4:
				finalDir = (float) Math.PI / 4;
				break;
			case 1:
				finalDir = (float) 0;
				break;
			case -2:
				finalDir = -(float) Math.PI / 4;
				break;
			case -3:
				finalDir = -(float) Math.PI / 2;
				break;
			case -4:
				finalDir = -3 * (float) Math.PI / 4;
				break;
			case -1:
				finalDir = (float) Math.PI;
				break;
			case 2:
				finalDir = 3 * (float) Math.PI / 4;
				break;
			case 3:
				finalDir = (float) Math.PI / 2;
				break;
			}
		}
		// animation
		if (defaultAct == null)
			if (animateSource.act.containsKey(Implement.defaultAct))
				defaultAct = Implement.defaultAct;
			else
				defaultAct = animateSource.act.keys().nextElement();
		if (thisAct == null)
			return null;
		if (animateSource.act.get(thisAct) == null)
			return null;
		if (MainPage.time % (100 / animateSpeed) < 1 && !stopAct)
			animateFrame++;
		if (animateFrame == -1 || animateSource.act.get(thisAct).size() == animateFrame) {
			thisAct = defaultAct;
			animateFrame = 0;
		}
//		if(animateSource.act.get(thisAct).size()<animateFrame)
//		System.out.println(thisAct+" "+animateSource.act.get(thisAct).size());
		Image image = animateSource.act.get(thisAct).get(animateFrame);
		if (imageCenterOffset)
			imageOffset.set(-image.getWidth(null) / 2, -image.getHeight(null) / 2);
		return image;
	}
}