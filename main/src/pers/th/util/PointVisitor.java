package pers.th.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文本观察
 * @author 天浩
 */
public class PointVisitor {

	private volatile int point = 0;

	private volatile int lastIndex = 0;

	private volatile String visitorText;

	private volatile String searchChar;

	private volatile String item;

	private boolean has;
	
	private boolean alreadyEnd = false;

	public PointVisitor(String source, int index) {
		visitorText = source;
		point = index;
	}

	public PointVisitor(String source) {
		this(source, 0);
	}

	public PointVisitor(String source, String searchChar) {
		this(source, 0);
		this.searchChar = searchChar;
	}

	public static void main(String[] args) {
		PointVisitor pv = new PointVisitor("a1 is After:b1 is Before", ":");
//		PointVisitor pv = new PointVisitor("a1 is After,b1 is Before", "[a-zA-Z]+([0-9]+)?");
		while (pv.next()) {
			pv.changeSearch("[a-zA-Z]+([0-9]+)?");
			System.out.println(pv.point() + ":" + pv.item());
		}
	}

	public String search(String text) {
		move(visitorText.indexOf(searchChar = text));
		return (has = point > -1) ? after() : null;
	}

	public boolean is(String regex) {
		return item.matches(regex);
	}

	public boolean next() {
		regex();
		if (!has) {
			return false;
		}
		return true;
	}

	public String regex() {
		return regex(searchChar);
	}

	public String regex(String regex) {
		Matcher matcher = Pattern.compile(regex).matcher(visitorText);
		if (has = matcher.find(point)) {
			move(matcher.start());
			return setItem(matcher.group());
		}
		return null;
	}

	public void move(int point) {
		lastIndex = this.point;
		setPoint(point);
	}

	public void end() {
		if (!alreadyEnd && item != null) {
			alreadyEnd = true;
			setPoint(point + item.length());
		}
	}

	public String item() {
		end();
		return item;
	}

	public String before() {
		return visitorText.substring(0, point);
	}

	public String after() {
		return visitorText.substring(point);
	}
	
	public void changeSearch(String searchChar) {
		this.searchChar = searchChar;
	}

	public String getVisitorText() {
		return visitorText;
	}

	public void setVisitorText(String visitorText) {
		this.visitorText = visitorText;
	}

	public void setSearchChar(String searchChar) {
		this.searchChar = searchChar;
	}

	public int getLastIndex() {
		return lastIndex;
	}

	public int point() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public boolean isHas() {
		return has;
	}
	
	public String setItem(String item) {
		alreadyEnd = false;
		return this.item = item;
	}

}
