package edu.thu.thss.twe;

import edu.thu.thss.twe.config.Configuration;
import edu.thu.thss.twe.console.ui.TweConsoleFrame;

/**
 * main entry
 * 
 * @author Devin
 * 
 */
public class Twe {
	public static void main(String[] args) {
		if (args.length == 1 && args[0].equals("-console")) {
			TweContext context = Configuration.getConfiguration()
					.getTweContext();
			TweConsoleFrame frame = new TweConsoleFrame(context);
			frame.start();
		} else {
			System.out.println("Useage:");

			System.out.println("java -jar twe-1.x.x.jar -console");
			System.exit(0);
		}
	}
}
