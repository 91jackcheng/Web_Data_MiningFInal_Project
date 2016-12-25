package wikipreprocess.parser;

import wikipreprocess.pipeline.Processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SentenceGener implements Processor<Page>{

	@Override
	public Page process(Page src) {
		String[] strings = src.text.split("。");
		int count = 0;
		Pattern p = Pattern.compile("[\n\\|]");
		for (String string : strings){
			Matcher m = p.matcher(string);
			while (m.find()) {
				count++;
			}
			if (count > 3)continue;
//			if(string.split("[\n\\|]").length > 3)continue;
			if(string.indexOf("#") != -1)continue;
			src.sentences.add(string);
		}
		return src;
	}

}
