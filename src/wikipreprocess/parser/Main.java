package wikipreprocess.parser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.TransformerConfigurationException;

import org.xml.sax.SAXException;

import wikipreprocess.pipeline.Pipeline;

public class Main {
	public static void main(String[] args) throws IOException, TransformerConfigurationException, SAXException{
		Pipeline<Page> xmlPipeline = new Pipeline<Page>(3, 8);
		FileParser begin = new FileParser(new FileInputStream(new File("/Users/diaosuyi/Downloads/zhwiki-20161101-pages-articles-multistream.xml")));
		FileStorer end = new FileStorer("/Users/diaosuyi/Desktop/wikidata/wiki.clean.part");
		xmlPipeline.registerProducer(begin);
		xmlPipeline.registerConsumer(end);
		xmlPipeline.registerProcessor(new CategoryParser());
		xmlPipeline.registerProcessor(new TextCleaner());
		xmlPipeline.registerProcessor(new SentenceGener());
		
		if(xmlPipeline.start()) xmlPipeline.waitStop();
	}
}
