package root;

import static root.Api.*;

import java.util.ArrayList;
import java.util.List;

public abstract class PhysicsObject {
	public List<GameObject> aims = new ArrayList<GameObject>();
	public String name;
	public static java.util.Hashtable<String, PhysicsObject> physicsObject = new java.util.Hashtable<String, PhysicsObject>();

	public PhysicsObject(String name) {
		this.name = name;
		physicsObject.put(name, this);
	}

	public void add(GameObject aim) {
		aims.add(aim);
	}

	public void add(String name) {
		aims.add(unit(name));
	}

	public void run() {
		for (GameObject one : aims) {
			if (one != null)
				calcul(one);
		}
	}

	public abstract void calcul(GameObject object);
}
