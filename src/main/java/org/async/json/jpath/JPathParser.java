package org.async.json.jpath;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.LinkedList;
import java.util.List;

import org.async.json.jpath.conditions.SimpleCondition;
import org.async.json.jpath.points.ArrayPoint;
import org.async.json.jpath.points.ObjectPoint;
import org.async.json.jpath.points.WildCardPoint;

public class JPathParser {
	public static JPath parse(Reader reader) throws IOException, ParseException {
		JPathReader r = new JPathReader(reader);
		char[] tail = new char[1];
		r.read(tail);
		return parse(r, tail);
	}

	private static JPath parse(JPathReader r, char[] tail) throws IOException,
			ParseException {
		JPath jpath = new JPath();
		List<JPathPoint> points = new LinkedList<JPathPoint>();
		if (tail[0] == '$') {
			jpath.setRoot(true);
		} else if (tail[0] == '@') {
			jpath.setRoot(false);
		} else {
			error(r, tail);
		}
		r.read(tail);
		while (true) {
			if (tail[0] == '[') {
				points.add(readArrayPoint(r, tail));
			} else if (tail[0] == '.') {
				points.add(readObjectPoint(r, tail));
			} else {
				break;
			}
			if (tail[0] == 0)
				break;
		}
		jpath.setPoints(points.toArray(new JPathPoint[points.size()]));
		return jpath;
	}

	private static ArrayPoint readArrayPoint(JPathReader r, char[] tail)
			throws IOException, ParseException {
		ArrayPoint arrayPoint = new ArrayPoint();
		r.read(tail);
		if (tail[0] != ']') {
			if (tail[0] != '?' && tail[0] != ':') {
				arrayPoint.setI(readValue(r, tail));
				if (tail[0] == ',') {
					r.read(tail);
					arrayPoint.setTo(readValue(r, tail));
				}
			}

			if (tail[0] == ':') {
				r.read(tail);
				arrayPoint.setStep(readValue(r, tail));
			}
			if (tail[0] == '?') {
				arrayPoint.setCondition(readCondition(r, tail));
			}
		}
		if (tail[0] != ']')
			error(r, tail);
		r.read(tail);
		return arrayPoint;
	}

	private static JPathPoint readObjectPoint(JPathReader r, char[] tail)
			throws IOException, ParseException {
		JPathPoint point = null;
		r.read(tail);
		String name = r.readName(tail);
		if (name.length() > 0) {
			point = (name.equals("*")) ? new WildCardPoint() : new ObjectPoint(
					name);
			if (tail[0] == '?') {
				point.setCondition(readCondition(r, tail));
			}
		}
		return point;
	}

	private static SimpleCondition readCondition(JPathReader r, char[] tail)
			throws IOException, ParseException {
		r.read(tail);
		if (tail[0] != '(')
			error(r, tail);
		r.read(tail);
		// TODO parse all conditions
		SimpleCondition condition = new SimpleCondition(readValue(r, tail),
				SimpleCondition.UNDEFINED, null);
		if (tail[0] != ')')
			error(r, tail);
		r.read(tail);
		return condition;
	}

	private static Object readValue(JPathReader r, char[] tail)
			throws IOException, ParseException {
		if (tail[0] == '$' || tail[0] == '@') {
			String jp = r.readJPath(tail);
			return parse(new JPathReader(new StringReader(jp)));
		} else if (tail[0] == '"') {
			return r.readString(tail);
		} if (tail[0] == 'n'||tail[0] == 't'||tail[0] == 'f') {
			return r.readBoolean(tail);
		} else {
			return r.readNumber(tail).intValue();
		}

	}

	private static void error(JPathReader r, char[] tail) throws ParseException {
		throw new ParseException("Illegal symbol "
				+ (tail[0] < ' ' ? "''" : tail[0]) + " code: " + (int) tail[0]
				+ " at " + r.getPos(), r.getPos());
	}

}
