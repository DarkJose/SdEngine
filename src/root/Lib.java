package root;

import root.Collision.RoundCollision;

public class Lib {
	public static float square(float x) {
		return x * x;
	}

	public static float getDistancePointLine(Vector2 aim, Vector2 lineA, Vector2 lineB) {
		float a = lineA.sub(lineB).y;
		float b = lineA.sub(lineB).x;
		return Math.abs(a * aim.x + b * aim.y + determinant(lineA.y, lineA.x, lineB.x, lineB.y))
				/ new Vector2(a, b).len();
	}

	public static float determinant(float v1, float v2, float v3, float v4) // 行列式
	{
		return (v1 * v3 - v2 * v4);
	}

	public static boolean intersect(Vector2 a1, Vector2 a2, Vector2 b1, Vector2 b2) {
		double delta = determinant(a2.x - a1.x, b1.x - b2.x, a2.y - a1.y, b1.y - b2.y);
		if (delta <= (1e-6) && delta >= -(1e-6)) // delta=0，表示两线段重合或平行
			return false;
		double namenda = determinant(b1.x - a1.x, b1.x - b2.x, b1.y - a1.y, b1.y - b2.y) / delta;
		if (namenda > 1 || namenda < 0)
			return false;
		double miu = determinant(a2.x - a1.x, b1.x - a1.x, a2.y - a1.y, b1.y - a1.y) / delta;
		if (miu > 1 || miu < 0)
			return false;
		return true;
	}

	public static boolean polyInPoly(Vector2[] polygonA, Vector2[] polygonB) {
		int leftCount = 0;
		int rightCount = 0;
		for (int i = 0; i < polygonA.length; i++) {
			float x = polygonA[i].x;
			float y = polygonA[i].y;
			int preIndex = polygonB.length - 1;
			// test polygonB contains vertex
			for (int j = 0; j < polygonB.length; j++) {
				float vertexY = polygonB[j].y;
				float preY = polygonB[preIndex].y;
				if ((vertexY < y && preY >= y) || (preY < y && vertexY >= y)) {
					float vertexX = polygonB[j].x;
					if (vertexX + (y - vertexY) / (preY - vertexY) * (polygonB[preIndex].x - vertexX) <= x)
						leftCount++;
					else
						rightCount++;
				}
				preIndex = j;
			}
			if (leftCount % 2 != 0)
				return true;
		}
		return leftCount != 0 && leftCount == rightCount;
	}

	public static boolean pointInPoly(Vector2 aim, Vector2[] points) {
		int preIndex = points.length - 1;
		boolean inside = false;
		for (int i = 0; i < points.length; i++) {
			float vertexY = points[i].y;
			float preY = points[preIndex].y;
			if ((vertexY < aim.y && preY >= aim.y) || (preY < aim.y && vertexY >= aim.y)) {
				float vertexX = points[i].x;
				if (vertexX + (aim.y - vertexY) / (preY - vertexY) * (points[preIndex].x - vertexX) <= aim.x)
					inside = !inside;
			}
			preIndex = i;
		}
		return inside;
	}

	public static int pointInPoly(Vector2[] points, Vector2 aim) {
		int i, j, wn = 0;
		float k;
		int n = points.length;
		for (i = n - 1, j = 0; j < n; i = j, j++) {
			k = (aim.x - points[i].x) * (points[j].y - points[i].y)
					- (points[j].x - points[i].x) * (aim.y - points[i].y);
			if ((aim.y >= points[i].y && aim.y <= points[j].y) || (aim.y <= points[i].y && aim.y >= points[j].y)) {
				if (k < 0)
					wn++;
				else if (k > 0)
					wn--;
				else {
					if ((aim.y <= points[i].y && aim.y >= points[j].y && aim.x <= points[i].x && aim.x >= points[j].x)
							|| (aim.y <= points[i].y && aim.y >= points[j].y && aim.x >= points[i].x
									&& aim.x <= points[j].x)
							|| (aim.y >= points[i].y && aim.y <= points[j].y && aim.x <= points[i].x
									&& aim.x >= points[j].x)
							|| (aim.y >= points[i].y && aim.y <= points[j].y && aim.x >= points[i].x
									&& aim.x <= points[j].x))
						return 0; // 点在多边形边界上
				}
			}
		}
		if (wn == 0)
			return 1; // 点在多边形外部
		else
			return -1; // 点在多边形内部
	}

	public static boolean pointInRound(Vector2 aim, RoundCollision round) {
		return aim.sub(round.parentVector).lenSquare() <= Lib.square(round.radius);
	}
}
