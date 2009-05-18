package edu.thu.thss.twe.parser;

import java.io.InputStream;

import edu.thu.thss.twe.model.graph.WorkflowProcess;

public interface Parser {
	WorkflowProcess parse(InputStream input);
}
