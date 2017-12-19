package root;

public class Collision {
	public Vector2 offset = new Vector2(0, 0);
	public String collisionType;
	public Vector2 parentVector;
	public boolean passable = true;

	public static class enumType {
		public static String rect = "rect";
		public static String round = "round";
	}

	public static boolean chooseType(String type1, String type2, Collision a, Collision b) {
		if (type1.contentEquals(enumType.rect) && type2.contentEquals(enumType.rect))
			return rectRect((RectCollision) a, (RectCollision) b);
		else if (type1.contentEquals(enumType.round) && type2.contentEquals(enumType.round))
			return roundRound((RoundCollision) a, (RoundCollision) b);
		else if (type1.contentEquals(enumType.rect) && type2.contentEquals(enumType.round))
			return rectRound((RectCollision) a, (RoundCollision) b);
		else if (type1.contentEquals(enumType.round) && type2.contentEquals(enumType.rect))
			return rectRound((RectCollision) b, (RoundCollision) a);
		return false;
	}

	public static boolean rectRect(RectCollision a, RectCollision b) {
		a.calculPara();
		b.calculPara();
		if (a.a.x > b.b.x)
			return false;
		if (a.b.x < b.a.x)
			return false;
		if (a.c.y > b.a.y)
			return false;
		if (a.a.y < b.c.y)
			return false;
		return true;
	}

	public static boolean roundRound(RoundCollision a, RoundCollision b) {
		return a.parentVector.sub(b.parentVector).lenSquare() <= Lib.square(a.radius + b.radius);
	}

	public static boolean rectRound(RectCollision a, RoundCollision b) {
		Vector2 topCenter = a.parentVector.add(new Vector2(0, a.height));
		Vector2 rightCenter = a.parentVector.add(new Vector2(a.width, 0));
		float w1 = a.parentVector.sub(rightCenter).len();
		float h1 = a.parentVector.sub(topCenter).len();
		float w2 = Lib.getDistancePointLine(b.parentVector, a.parentVector, topCenter);
		float h2 = Lib.getDistancePointLine(b.parentVector, a.parentVector, rightCenter);
		if (w2 > w1 + b.radius)
			return false;
		if (h2 > h1 + b.radius)
			return false;
		if (w2 <= w1)
			return true;
		if (h2 <= h1)
			return true;
		return (w2 - w1) * (w2 - w1) + (h2 - h1) * (h2 - h1) <= Lib.square(b.radius);
	}

	public static class RectCollision extends Collision {
		public float height, width;
		public Vector2 vector = new Vector2(0, 0);
		public Vector2 a = new Vector2(0, 0), b = new Vector2(0, 0), c = new Vector2(0, 0), d = new Vector2(0, 0);

		public RectCollision(float height, float width) {
			this.height = height;
			this.width = width;
			this.vector.set(height, width);
			this.collisionType = enumType.rect;
		}

		public void calculPara() {
			this.a = this.parentVector.sub(this.vector);
			this.d = this.parentVector.add(this.vector);
			this.b.x = this.parentVector.x + this.width;
			this.b.y = this.parentVector.y - this.height;
			this.c.x = this.parentVector.x - this.width;
			this.c.y = this.parentVector.y + this.height;
		}
	}

	public static class RoundCollision extends Collision {
		public float radius;

		public RoundCollision(float radius) {
			this.radius = radius;
			this.collisionType = enumType.round;
		}
	}
}