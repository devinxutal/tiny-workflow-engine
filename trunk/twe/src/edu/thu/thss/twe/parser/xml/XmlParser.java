package edu.thu.thss.twe.parser.xml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import edu.thu.thss.twe.exception.TweException;
import edu.thu.thss.twe.model.graph.Activity;
import edu.thu.thss.twe.model.graph.Condition;
import edu.thu.thss.twe.model.graph.DataField;
import edu.thu.thss.twe.model.graph.Join;
import edu.thu.thss.twe.model.graph.Participant;
import edu.thu.thss.twe.model.graph.Split;
import edu.thu.thss.twe.model.graph.Transition;
import edu.thu.thss.twe.model.graph.TransitionRestriction;
import edu.thu.thss.twe.model.graph.WorkflowElement;
import edu.thu.thss.twe.model.graph.WorkflowProcess;
import edu.thu.thss.twe.parser.Parser;
import edu.thu.thss.twe.util.DataTypeUtil;
import edu.thu.thss.twe.util.TypeUtil;

public class XmlParser implements Parser {
	private static String PACKAGE = "Package";
	private static String WORKFLOW_PROCESS = "WorkflowProcess";
	private static String WORKFLOW_PROCESSES = "WorkflowProcesses";
	private static String DATA_FIELDS = "DataFields";
	private static String DATA_FIELD = "DataField";
	private static String PARTICIPANTS = "Participants";
	private static String PARTICIPANT = "Participant";
	private static String ACTIVITIES = "Activities";
	private static String ACTIVITY = "Activity";
	private static String TRANSITIONS = "Transitions";
	private static String TRANSITION = "Transition";
	private static String PERFORMER = "Performer";
	private static String TRANSITION_RESTRICTIONS = "TransitionRestrictions";
	private static String TRANSITION_RESTRICTION = "TransitionRestriction";
	private static String JOIN = "Join";
	private static String SPLIT = "Split";
	private static String CONDITION = "Condition";

	private static String ID = "Id";
	private static String NAME = "Name";
	private static String DESCRIPTION = "Description";
	private static String FROM = "From";
	private static String TO = "To";
	private static String TYPE = "Type";

	private Map<String, Activity> activities = new HashMap<String, Activity>();
	private Map<String, DataField> datafields = new HashMap<String, DataField>();
	private Map<String, Transition> transitions = new HashMap<String, Transition>();
	private Map<String, Participant> participants = new HashMap<String, Participant>();

	private Map<String, Element> activityElements = new HashMap<String, Element>();
	private Map<String, Element> datafieldElements = new HashMap<String, Element>();
	private Map<String, Element> transitionElements = new HashMap<String, Element>();
	private Map<String, Element> participantElements = new HashMap<String, Element>();

	public WorkflowProcess parse(InputStream input) {
		try {
			Document doc = read(input);
			return parse(doc);
		} catch (Exception e) {
			throw new TweException("cannot parse the document", e);
		}
	}

	public Document read(InputStream input) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(input);
		return document;

	}

	private WorkflowProcess parse(Document doc) {
		WorkflowProcess process = new WorkflowProcess();

		Element workflowElement = getWorkflowProcessElement(doc);
		if (workflowElement == null) {
			return null;
		}
		setElementAttributes(process, workflowElement);
		parseWorkflowElements(process, workflowElement);
		reparseWorkflowElements(process);
		return process;
	}

	private Element getWorkflowProcessElement(Document doc) {
		System.out.println(PACKAGE + "/" + WORKFLOW_PROCESSES + "/"
				+ WORKFLOW_PROCESS);
		Element packageEle = doc.getRootElement();
		return packageEle.element(WORKFLOW_PROCESSES).element(WORKFLOW_PROCESS);
	}

	private boolean parseWorkflowElements(WorkflowProcess process,
			Element workflowElement) {
		for (Iterator<?> it = workflowElement.elementIterator(); it.hasNext();) {
			Object o = it.next();
			if (!(o instanceof Element)) {
				continue;
			}
			Element ele = (Element) o;
			System.out.println(ele.getName());
			if (ele.getName().equals(DATA_FIELDS)) {
				process.setDataFileds(parseDataFields(ele));
			} else if (ele.getName().equals(PARTICIPANTS)) {
				process.setParticipants(parseParticipants(ele));
			} else if (ele.getName().equals(ACTIVITIES)) {
				process.setActivities(parseActivities(ele));
			} else if (ele.getName().equals(TRANSITIONS)) {
				process.setTransitions(parseTransitions(ele));
			}
		}
		System.out.println(participants.size());
		return true;
	}

	private List<DataField> parseDataFields(Element parentElement) {
		LinkedList<DataField> datafields = new LinkedList<DataField>();
		for (Iterator<?> it = parentElement.elementIterator(DATA_FIELD); it
				.hasNext();) {
			Element ele = (Element) it.next();
			DataField datafield = new DataField();
			setElementAttributes(datafield, ele);
			datafields.add(datafield);
			this.datafieldElements.put(datafield.getElementId(), ele);
			this.datafields.put(datafield.getElementId(), datafield);
		}
		return datafields;
	}

	private List<Participant> parseParticipants(Element parentElement) {
		LinkedList<Participant> participants = new LinkedList<Participant>();
		for (Iterator<?> it = parentElement.elementIterator(PARTICIPANT); it
				.hasNext();) {
			Element ele = (Element) it.next();
			Participant participant = new Participant();
			setElementAttributes(participant, ele);
			participants.add(participant);
			this.participantElements.put(participant.getElementId(), ele);
			this.participants.put(participant.getElementId(), participant);
		}
		return participants;
	}

	private List<Activity> parseActivities(Element parentElement) {
		LinkedList<Activity> activities = new LinkedList<Activity>();
		for (Iterator<?> it = parentElement.elementIterator(ACTIVITY); it
				.hasNext();) {
			Element ele = (Element) it.next();
			Activity activity = new Activity();
			setElementAttributes(activity, ele);
			activities.add(activity);
			this.activityElements.put(activity.getElementId(), ele);
			this.activities.put(activity.getElementId(), activity);
		}
		return activities;
	}

	private List<Transition> parseTransitions(Element parentElement) {
		LinkedList<Transition> transitions = new LinkedList<Transition>();
		for (Iterator<?> it = parentElement.elementIterator(TRANSITION); it
				.hasNext();) {
			Element ele = (Element) it.next();
			Transition transition = new Transition();
			setElementAttributes(transition, ele);
			transitions.add(transition);
			this.transitionElements.put(transition.getElementId(), ele);
			this.transitions.put(transition.getElementId(), transition);
		}
		return transitions;
	}

	private boolean reparseWorkflowElements(WorkflowProcess process) {
		reparseDataFields(process);
		reparseParticipants(process);
		reparseActivities(process);
		reparseTransitions(process);

		return true;
	}

	private boolean reparseDataFields(WorkflowProcess process) {
		for (DataField datafield : process.getDataFileds()) {
			Element ele = datafieldElements.get(datafield.getElementId());
			datafield.setWorkflowProcess(process);
			Element datatype = ele.element("DataType");
			if (datatype == null) {
				throw new TweException("cannot parse datafield "
						+ datafield.getName() + ": unknown type");
			}
			Element basictype = datatype.element("BasicType");
			if (basictype == null) {
				throw new TweException("cannot parse datafield "
						+ datafield.getName() + ": unknown type");
			}
			int dataType = DataTypeUtil.determineDataType(basictype
					.attributeValue(TYPE));
			if (dataType < 0) {
				throw new TweException("cannot parse datafield "
						+ datafield.getName() + ": unknown type");
			}
			datafield.setDataType(dataType);
			Element initialValue = ele.element("InitialValue");
			if (initialValue != null) {
				datafield.setInitialValue(initialValue.getTextTrim());
			}
		}
		return true;
	}

	public boolean reparseParticipants(WorkflowProcess process) {
		for (Participant participant : process.getParticipants()) {
			Element ele = participantElements.get(participant.getElementId());
			participant.setWorkflowProcess(process);
			Element participantType = ele.element("ParticipantType");
			if (participantType == null) {
				throw new TweException("cannot parse participant "
						+ participant.getName() + ": unknown type");
			}
			int type = TypeUtil.parseParticipantType(participantType
					.attributeValue(TYPE));
			if (type < 0) {
				throw new TweException("cannot parse participant "
						+ participant.getName() + ": unknown type");
			}
			participant.setParticipantType(type);
		}
		return true;
	}

	public boolean reparseActivities(WorkflowProcess process) {
		for (Activity activity : process.getActivities()) {
			Element ele = activityElements.get(activity.getElementId());
			activity.setWorkflowProcess(process);
			// performer
			Element performer = ele.element(PERFORMER);
			if (performer != null) {
				String performerId = performer.getTextTrim();
				Participant p = this.participants.get(performerId);
				if (p == null) {
					throw new TweException("cannot parse activity "
							+ activity.getName() + ": unknown participant");
				}
				activity.setPerformer(p);
			}
			// TransitionRestrictions
			Element transitionRestrictions = ele
					.element(TRANSITION_RESTRICTIONS);
			if (transitionRestrictions != null) {
				Element transitionRestriction = transitionRestrictions
						.element(TRANSITION_RESTRICTION);
				if (transitionRestriction != null) {
					parseTransitionRestriction(activity, transitionRestriction);
				}
			}
		}
		return true;
	}

	public boolean reparseTransitions(WorkflowProcess process) {
		for (Transition transition : process.getTransitions()) {
			Element ele = transitionElements.get(transition.getElementId());
			transition.setWorkflowProcess(process);
			// target and source
			String fromId = ele.attributeValue(FROM);
			String toId = ele.attributeValue(TO);
			Activity from = activities.get(fromId);
			Activity to = activities.get(toId);
			if (from == null || to == null) {
				throw new TweException("cannot parse transition "
						+ transition.getName() + ": unknown source or target");
			}
			from.addLeavingTransition(transition);
			to.addArrivingTransition(transition);
			// condition
			Element conditionEle = ele.element(CONDITION);
			if (conditionEle != null) {
				Condition condition = new Condition();
				condition.setExpression(conditionEle.getTextTrim());
				int type = TypeUtil.parseConditionType(conditionEle
						.attributeValue(TYPE));
				if (type < 0) {
					throw new TweException("cannot parse transition "
							+ transition.getName() + ": unknown condition type");
				}
				condition.setConditionType(type);
				transition.setCondition(condition);
			}
		}
		return true;
	}

	private boolean parseTransitionRestriction(Activity activity,
			Element trElement) {
		TransitionRestriction tr = new TransitionRestriction();
		// join
		Element joinEle = trElement.element(JOIN);
		if (joinEle != null) {
			Join join = new Join();
			tr.setJoin(join);
			int type = TypeUtil.parseJoinType(joinEle.attributeValue(TYPE));
			if (type < 0) {
				throw new TweException("cannot parse activity "
						+ activity.getName() + ": unknown join type");
			}
			join.setJoinType(type);
		}
		// split
		Element splitEle = trElement.element(SPLIT);
		if (splitEle != null) {
			Split split = new Split();
			tr.setSplit(split);
			int type = TypeUtil.parseSplitType(splitEle.attributeValue(TYPE));
			if (type < 0) {
				throw new TweException("cannot parse activity "
						+ activity.getName() + ": unknown split type");
			}
			split.setSplitType(type);
		}
		activity.setTransitionRestriction(tr);
		return true;
	}

	private boolean setElementAttributes(WorkflowElement wfe, Element e) {
		// wfe.setDescrioption(e.attributeValue(DESCRIPTION));
		wfe.setElementId(e.attributeValue(ID));
		wfe.setName(e.attributeValue(NAME));
		return true;
	}
}
