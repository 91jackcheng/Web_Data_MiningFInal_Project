package wikipreprocess.parser;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wikipreprocess.pipeline.Processor;

public class CategoryParser implements Processor<Page>{
	private Pattern category;
	
	public CategoryParser() {
		category = Pattern.compile("\\[\\[category\\:([^\\]]*)\\]\\]",Pattern.CASE_INSENSITIVE);
	}
	
	@Override
	public Page process(Page src) {
//		if(true)return null;
		String text = src.text;
		Matcher categories = category.matcher(text);
		while(categories.find()){
			String ctg = categories.group(1);
			src.sentences.add(src.title+"是"+ctg);
		}
		src.text = categories.replaceAll("");
		return src;
	}

}
