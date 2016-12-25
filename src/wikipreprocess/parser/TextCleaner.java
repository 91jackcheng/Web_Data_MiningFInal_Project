package wikipreprocess.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.script.ScriptContext;

import wikipreprocess.pipeline.Processor;

public class TextCleaner implements Processor<Page>{
	private Pattern tag;
	private Pattern ref;
	private Pattern doubleQuote;
	private Pattern special;
	public TextCleaner() {
		ref = Pattern.compile("\\<\\s*ref\\s*\\>[^\\>]*\\<\\s*/ref\\s*\\>",Pattern.CASE_INSENSITIVE);
		tag = Pattern.compile("\\</?[^\\>]*\\>",Pattern.CASE_INSENSITIVE);
		doubleQuote = Pattern.compile("\\{\\{[^\\}]*\\}\\}",Pattern.CASE_INSENSITIVE);
		special = Pattern.compile("\\&[^\\;]*\\;");
	}
	@Override
	public Page process(Page src) {
		String text = src.text;
		Matcher useless = ref.matcher(text);
		text = useless.replaceAll("");
		useless = doubleQuote.matcher(text);
		text = useless.replaceAll("");
		useless = special.matcher(text);
		text = useless.replaceAll("");
		useless = tag.matcher(text);
		text = useless.replaceAll("");
		src.text = text.replaceAll("\\{|\\}|\\[|\\]", "");
		return src;
	}

}
