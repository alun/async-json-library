package org.async.json.in.ex;

import java.util.HashMap;
import java.util.Map;

import org.async.json.in.Callback;
import org.async.json.in.ParseException;
import org.async.json.in.RootParser;
import org.async.json.in.StringState;

/*
 *  include DIRECTIVE supporting
 */
public class XStringState extends StringState {

	private static final char DIRECTIVE_ARGUMENT_AFTER = ')';
	private static final char DIRECTIVE_ARGUMENT_BEFORE = '(';
	protected Map<String, Directive> directives = new HashMap<String, Directive>();

	@Override
	protected int finish(char[] tail, Callback callback) throws ParseException {
		String directiveName;
		String directiveArg;
		String rs = builder.toString();
		if (rs.startsWith("#")) {
			builder.setLength(0);
			int i = rs.indexOf(DIRECTIVE_ARGUMENT_BEFORE);
			int j = rs.indexOf(DIRECTIVE_ARGUMENT_AFTER);
			if (i * j > 0) {
				directiveName = rs.substring(1, i);
				Directive directive = directives.get(directiveName);
				if (directive == null) {
					throw new ParseException("Unsupported directive: "
							+ directiveName);
				}
				directiveArg = rs.substring(i + 1, j);
				directive.run(directiveArg);
			} else {
				throw new ParseException("Unmatched input:");
			}
			return RootParser.RESUME;
		} else {
			return super.finish(tail, callback);
		}
	}

	public Map<String, Directive> getDirectives() {
		return directives;
	}

	public void setDirectives(Map<String, Directive> directives) {
		this.directives = directives;
	}

}
