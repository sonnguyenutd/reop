package v8.gen.parser;

import java.util.List;

public class GroundingExecutor {
	public static void execute(List<GroundingThread> ths) throws InterruptedException {
		for (GroundingThread th : ths) 
			th.start();
		for (GroundingThread th : ths) 
			th.join();
	}
}
