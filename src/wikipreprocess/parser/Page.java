package wikipreprocess.parser;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;

public class Page {
	public String title;
	public String text;
	public Vector<String> sentences;
	Page(){
		sentences = new Vector<String>();
	}

	Document toXML() throws ParserConfigurationException{
		Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
		Element page = document.createElement("page");
		Element pagetitle = document.createElement("title");
		pagetitle.setTextContent(title);
		page.appendChild(pagetitle);
		for(String s : sentences){
			Element st = document.createElement("sentence");
			st.setTextContent(s);
			page.appendChild(st);
		}
		document.appendChild(page);
		return document;
	}
}
