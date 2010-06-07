package org.async.json.in;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RootParser {
	public static int GO_TO_PARENT_STATE = 0xFFFF;
	public static int RESUME = -1;
	public static int COMPLETED = 0xFFFE;
	public static int VALUE_STATE = 0;
	public static int STRING_STATE = 1;
	public static int NUMBER_STATE = 2;
	public static int ARRAY_STATE = 3;
	public static int OBJECT_STATE = 4;
	public static int READ_NEXT_STATE = 5;
	public static int BOOLEAN_STATE = 6;

	protected State[] states = { new ValueState(), new StringState(),
			new NumberState(), new ArrayState(), new ObjectState(),
			new ReadNextState(), new BooleanState() };

	protected List<Integer> stack = new ArrayList<Integer>(128);

	protected char[] tail = new char[1];

	public boolean parse(JSONReader reader, Callback callback) {
		try {

			if (stack.isEmpty()) {
				stack.add(VALUE_STATE << 16);
				reader.read(tail);
			} else if ((stack.get(stack.size() - 1) >> 16) != READ_NEXT_STATE) {
				reader.read(tail);
			}
			State state;
			do {
				int stateIdx = stack.size() > 0 ? stack
						.remove(stack.size() - 1) : -1;
				int innerState = stateIdx & 0xFFFF;
				stateIdx = stateIdx >> 16;
				state = states[stateIdx];
				int next = state.run(reader, tail, innerState, callback);
				if (next != RESUME) {
					if (next == RootParser.GO_TO_PARENT_STATE) {
						if (stack.isEmpty()) {
							return true;
						} else if (state.isReadNext()) {
							stack.add(READ_NEXT_STATE << 16);
						}
					} else {
						if (COMPLETED != state.getState()) {
							stack.add(state.getState() + (stateIdx << 16));
						}
						stack.add(next << 16);
					}
				} else {
					stack.add(state.getState() + (stateIdx << 16));
					return false;
				}
			} while (true);
		} catch (ParseException e) {
			callback.parseError(e);
		} catch (Exception e) {
			callback.unknownError(e);
		}
		return false;
	}

	public void close(JSONReader reader, Callback callback) throws IOException,
			ParseException {
		int stateIdx = stack.size() > 0 ? stack.remove(stack.size() - 1) : -1;
		int innerState = stateIdx & 0xFFFF;
		stateIdx = stateIdx >> 16;
		if (stateIdx > -1) {
			State state = states[stateIdx];
			state.run(reader, tail, innerState, callback); // some states like
			innerState = state.getState();
			tail[0] = 0;
			state.run(reader, tail, innerState, callback);
		}
	}

	public List<Integer> getStack() {
		return stack;
	}

	public void setStack(List<Integer> stack) {
		this.stack = stack;
	}

	public State[] getStates() {
		return states;
	}

	public void setStates(State[] states) {
		this.states = states;
	}

}
