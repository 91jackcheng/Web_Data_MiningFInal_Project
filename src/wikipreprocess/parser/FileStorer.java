package wikipreprocess.parser;

import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import wikipreprocess.pipeline.Consumer;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileStorer implements Consumer<Page>{
	OutputStreamWriter fileWriter;
	FileOutputStream fileOutputStream;
	TransformerHandler th;
    AttributesImpl rootattr;
	String filename;
	int count;
	
	private void createOutputFile(int num) throws FileNotFoundException, TransformerConfigurationException, SAXException{
		fileOutputStream = new FileOutputStream(filename+num+".xml");
	    Result resultXml = new StreamResult(fileOutputStream); 
	    SAXTransformerFactory sff = (SAXTransformerFactory)SAXTransformerFactory.newInstance();  
	    th = sff.newTransformerHandler();  
	    th.setResult(resultXml);  
	          
	    Transformer transformer = th.getTransformer();  
	    transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8"); //编码格式是UTF-8  
	    transformer.setOutputProperty(OutputKeys.INDENT, "yes"); //换行
	    
	    th.startDocument(); //开始xml文档 
        rootattr = new AttributesImpl();  
        th.startElement("", "", "root", rootattr); 
	}
	private void closeOutputFile() throws SAXException, IOException{
		th.endElement("", "", "root");
		th.endDocument();
		fileOutputStream.close();
	}
	public FileStorer(String name) throws FileNotFoundException, TransformerConfigurationException, SAXException { 	    
		filename = name;
		count = 0;
		createOutputFile(count/10000);
	}
	@Override
	public void consume(Page src) {
		if (src.sentences.size() == 0)return;
		count++;
		try{
		if(count%10000 == 0){
			closeOutputFile();
			createOutputFile(count/10000);
		}}catch(Exception exception){
			exception.printStackTrace();
		}
		try{
	        AttributesImpl attr = new AttributesImpl();
	        th.startElement("", "", "page", attr);
	        th.startElement("", "", "title", attr);
	        th.characters(src.title.toCharArray(), 0, src.title.length());
	        th.endElement("", "", "title");
	        for(String sentence : src.sentences){
		        th.startElement("", "", "sentence", attr); //定义age节点
		        th.characters(sentence.toCharArray(), 0, sentence.length());
		        th.endElement("", "", "sentence"); //结束age节点
	        }
	        th.endElement("", "", "page"); //结束person节点
		}catch(SAXException e){
			e.printStackTrace();
		}
	}
	@Override
	public void end() {
		try {
			closeOutputFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
