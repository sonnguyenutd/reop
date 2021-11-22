package v8.gen.parser;

import java.util.List;

public abstract class Distributor {
	public abstract List<GroundingThread> distribute(int numOfThreads);
}
