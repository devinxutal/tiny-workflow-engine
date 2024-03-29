package edu.thu.thss.twe.util;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Condition;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.Transition;
import edu.thu.thss.twe.model.runtime.Token;

public class DrawUtil {

	private int height;
	private int width;
	private int itemHeight = 45;
	private int itemWidth = 60;
	private int intervalY;
	private int shiftY = 15;
	private int transitionLength = 100;
	int[] posYs;
	List<String> segments;
	Map<Activity, Point> activityPostionMap;
	private Graphics2D graphic;

	public void setHeight(int height) {
		this.height = height;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setGraphic(Graphics2D graphic) {
		this.graphic = graphic;
		this.graphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
	}

	public void setActivityPostionMap(Map<Activity, Point> activityPostionMap) {
		this.activityPostionMap = activityPostionMap;
	}

	public void DrawInit(List<Participant> participants) {
		graphic.setColor(Color.WHITE);
		graphic.fillRect(0, 0, width, height);

		int size = participants.size();
		intervalY = height / (size + 1);
		posYs = new int[size + 1];
		segments = new LinkedList<String>();

		int i = 0;
		for (; i < size; i++)
			segments.add(participants.get(i).getName());
		segments.add("Arbitray expression");

		graphic.setColor(Color.BLACK);
		i = 0;
		for (; i < size; i++) {
			posYs[i] = intervalY * i;
			graphic.drawLine(0, posYs[i], width, posYs[i]);
			graphic.drawString(segments.get(i), 1, posYs[i] + shiftY);
		}
		posYs[i] = height - intervalY;
		graphic.drawLine(0, height - intervalY, width, height - intervalY);
		graphic.drawString(segments.get(i), 1, height - intervalY + shiftY);

	}

	public void DrawStartNode(Activity startActivity) {
		String segment = startActivity.getPerformer().getName();
		int i = segments.indexOf(segment);
		int posX = 100;
		int posY = posYs[i] + shiftY;
		graphic.setColor(Color.BLACK);
		graphic.drawOval(posX, posY, itemHeight, itemHeight);
		graphic.setColor(Color.YELLOW);
		graphic.fillOval(posX + 1, posY + 1, itemHeight - 1, itemHeight - 1);
		graphic.setColor(Color.BLACK);
		graphic.drawString("Start", posX + 10, posY + itemHeight / 2);
		posX += itemHeight;
		graphic.setColor(Color.BLUE);
		DrawArrow(posX, posY + (itemHeight / 2), posX + transitionLength, posY
				+ (itemHeight / 2));
		posX += transitionLength;
		Point p = new Point(posX, posY);
		activityPostionMap.put(startActivity, p);
	}

	public void DrawEndNode(Activity endActivity) {
		Point p = activityPostionMap.get(endActivity);
		int posX = p.x + itemWidth;
		int posY = p.y + (itemHeight / 2);
		graphic.setColor(Color.BLUE);
		DrawArrow(posX, posY, posX + transitionLength, posY);
		posX += transitionLength;
		posY = p.y;
		graphic.setColor(Color.BLACK);
		graphic.drawOval(posX, posY, itemHeight, itemHeight);
		graphic.setColor(Color.YELLOW);
		graphic.fillOval(posX + 1, posY + 1, itemHeight - 1, itemHeight - 1);
		graphic.setColor(Color.BLACK);
		graphic.drawString("End", posX + 10, posY + itemHeight / 2);
	}

	public void DrawActivity(Activity activity) {
		Point p = activityPostionMap.get(activity);
		int posX = p.x;
		int posY = p.y;
		graphic.setColor(Color.BLACK);
		graphic.drawRoundRect(posX, posY, itemWidth, itemHeight, 10, 10);
		if (activity.isRoute())
			graphic.setColor(Color.PINK);
		else
			graphic.setColor(Color.CYAN);
		graphic.fillRoundRect(posX + 1, posY + 1, itemWidth - 1,
				itemHeight - 1, 10, 10);
		graphic.setColor(Color.BLACK);
		graphic.drawString(activity.getName(), posX + 5, posY + itemHeight / 2);

	}

	public void DrawTransition(Transition transition) {
		Activity sourceActivity = transition.getSourceActivity();
		Point p = activityPostionMap.get(sourceActivity);
		int startX = p.x;
		int startY = p.y;

		int endX, endY;
		Activity targetActivity = transition.getTargetActivity();
		p = activityPostionMap.get(targetActivity);
		if (p != null) {
			endX = p.x;
			endY = p.y;
		} else {
			endX = startX + transitionLength;
			if (targetActivity.isTaskActivity() == true) {
				String segment = targetActivity.getPerformer().getName();
				int i = segments.indexOf(segment);
				endY = posYs[i] + shiftY;
			} else {
				endY = posYs[posYs.length - 1] + shiftY;
			}
			Point targetPosition = new Point(endX, endY);
			activityPostionMap.put(targetActivity, targetPosition);
		}

		if (startY == endY) {
			startY += (itemHeight / 2);
			endY = startY;
			if (startX < endX)
				startX += itemWidth;
			else
				endX += itemWidth;
		} else {
			startX += itemWidth / 2;
			endX += itemWidth / 2;
			if (startY < endY)
				startY += itemHeight;
			else
				endY += itemHeight;
		}

		DrawArrow(startX, startY, endX, endY);
		Condition condition = transition.getCondition();
		if (condition != null) {
			String expression = transition.getCondition().getExpression();
			if (expression != null) {
				graphic.setColor(Color.BLACK);
				graphic.drawString(expression, (startX + endX) / 2,
						(startY + endY) / 2 + shiftY);
			}
		}

	}

	public void DrawArrow(int x1, int y1, int x2, int y2) {

		double H = 7; // 箭头高度
		double L = 5; // 底边的一半
		int x3 = 0;
		int y3 = 0;
		int x4 = 0;
		int y4 = 0;
		double awrad = Math.atan(L / H); // 箭头角度
		double arraow_len = Math.sqrt(L * L + H * H); // 箭头的长度
		double[] arrXY_1 = rotateVec(x2 - x1, y2 - y1, awrad, true, arraow_len);
		double[] arrXY_2 = rotateVec(x2 - x1, y2 - y1, -awrad, true, arraow_len);
		double x_3 = x2 - arrXY_1[0]; // (x3,y3)是第一端点
		double y_3 = y2 - arrXY_1[1];
		double x_4 = x2 - arrXY_2[0]; // (x4,y4)是第二端点
		double y_4 = y2 - arrXY_2[1];
		Double X3 = new Double(x_3);
		x3 = X3.intValue();
		Double Y3 = new Double(y_3);
		y3 = Y3.intValue();
		Double X4 = new Double(x_4);
		x4 = X4.intValue();
		Double Y4 = new Double(y_4);
		y4 = Y4.intValue();

		graphic.setColor(Color.BLUE);
		// 画线
		graphic.drawLine(x1, y1, x2, y2);
		/*
		 * // 画箭头的一半 graphic.drawLine(x2, y2, x3, y3); // 画箭头的另一半
		 * graphic.drawLine(x2, y2, x4, y4);
		 */

		int nPoint = 3;
		int[] xPoints = new int[nPoint];
		int[] yPoints = new int[nPoint];
		xPoints[0] = x2;
		xPoints[1] = x3;
		xPoints[2] = x4;
		yPoints[0] = y2;
		yPoints[1] = y3;
		yPoints[2] = y4;
		graphic.fillPolygon(xPoints, yPoints, nPoint);

	}

	public double[] rotateVec(int px, int py, double ang, boolean isChLen,
			double newLen) {

		double mathstr[] = new double[2];
		// 矢量旋转函数，参数含义分别是x分量、y分量、旋转角、是否改变长度、新长度
		double vx = px * Math.cos(ang) - py * Math.sin(ang);
		double vy = px * Math.sin(ang) + py * Math.cos(ang);
		if (isChLen) {
			double d = Math.sqrt(vx * vx + vy * vy);
			vx = vx / d * newLen;
			vy = vy / d * newLen;
			mathstr[0] = vx;
			mathstr[1] = vy;
		}
		return mathstr;

	}

	public void DrawToken(Token token) {
		int h = 10;
		if (token.getState() == Token.TokenState.Active) {
			Activity currentActivity = token.getCurrentActivity();
			Point p = activityPostionMap.get(currentActivity);
			graphic.setColor(Color.DARK_GRAY);
			int posX = p.x + itemWidth / 2;
			int posY = p.y + itemHeight / 2 + 5;
			graphic.fillOval(posX, posY, h, h);
			return;
		}

		List<Token> children = token.getChildren();
		if (children != null) {
			for (int i = 0; i < children.size(); i++) {
				Token childToken = children.get(i);
				DrawToken(childToken);
			}
		}
	}

}
