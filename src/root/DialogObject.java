package root;

import java.awt.Color;
import java.awt.Font;

public class DialogObject extends GameObject {
	public static class ContentObject {
		public boolean hidden = false;
		public float x, y;
		public int dialogIndex = 0;
		public String textFormat = "Helvetica";
		public int textStyle = Font.PLAIN, textSize = 18;
		public Color textColor = Color.WHITE;
		public java.util.List<String> dialogContent = new java.util.ArrayList<String>();

		public ContentObject(float x, float y) {
			this.x = x;
			this.y = y;
		}
	}

	public ContentObject contentObject;

	public DialogObject(String name, AnimateObject animateSource) {
		super(name, animateSource);
	}
}
