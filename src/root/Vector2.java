package root;

public class Vector2 {
	public float x = 0;
	public float y = 0;

	public Vector2() {
	}

	public Vector2(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public Vector2(Vector2 v) {
		set(v);
	}

	public boolean equals(Vector2 v) {
		return (x == v.x && y == v.y);
	}

	public float len() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public float lenSquare() {
		return x * x + y * y;
	}

	public Vector2 unit()// 计算两点方向
	{
		return new Vector2(this.x / this.len(), this.y / this.len());
	}

	public Vector2 set(Vector2 v) {
		x = v.x;
		y = v.y;
		return this;
	}

	public Vector2 set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public Vector2 sub(Vector2 v) {
		return new Vector2(this.x - v.x, this.y - v.y);
	}

	public Vector2 add(Vector2 v) {
		return new Vector2(this.x + v.x, this.y + v.y);
	}
}