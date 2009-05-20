package edu.thu.thss.twe.console;

import edu.thu.thss.twe.TweContext;
import edu.thu.thss.twe.config.Configuration;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;

public class TweConsole {
	public static void main(String[] args) {

		TweContext context = Configuration.getConfiguration().getTweContext();
		TweConsoleFrame frame = new TweConsoleFrame(context);
		frame.start();
	}
}
