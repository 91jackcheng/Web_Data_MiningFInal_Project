package wikipreprocess.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ArrayBlockingQueue;

import wikipreprocess.pipeline.Producer;

import javax.xml.parsers.SAXParser;  
import javax.xml.parsers.SAXParserFactory;  

import org.xml.sax.Attributes;  
import org.xml.sax.SAXException;  
import org.xml.sax.helpers.DefaultHandler;

public class FileParser extends DefaultHandler implements Producer<Page>{
	private ArrayBlockingQueue<Page> pageQueue;
	private boolean stopped;
	private InputStream xmlStream;
	private String preTag;
	private Page tmpPage;
	private StringBuffer titleBuffer;
	private StringBuffer textBuffer;
	
	private int count;
	private static int maxcount = Integer.MAX_VALUE-1;
	
	public FileParser(InputStream input) {
		super();
		xmlStream = input;
		stopped = false;
		pageQueue = new ArrayBlockingQueue<Page>(500);
		preTag = null;
		tmpPage = null;
		count = 0;
	}
	
    @Override  
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {  
        if("page".equals(qName)){  
            tmpPage = new Page();  
        }else if("title".equals(qName)){
        	titleBuffer = new StringBuffer();
        }else if("text".equals(qName)){
        	textBuffer = new StringBuffer();
        }
        preTag = qName;//将正在解析的节点名称赋给preTag  
    }  
  
    @Override  
    public void endElement(String uri, String localName, String qName) throws SAXException {  
        if("page".equals(qName)){  
            try {
				pageQueue.put(tmpPage);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}  
            count++;
            if(count % 10000 == 0)System.err.println(count);
            tmpPage = null; 
        }else if("title".equals(qName)){
        	tmpPage.title = titleBuffer.toString();
        }else if("text".equals(qName)){
        	tmpPage.text = textBuffer.toString();
        } 
        preTag = null;  
        if(count > maxcount) throw new SAXException();
        
    }  
      
    @Override  
    public void characters(char[] ch, int start, int length) throws SAXException {  
        if(preTag!=null){    
            if("title".equals(preTag)){  
                titleBuffer.append(new String(ch,start,length)); 
            }else if("text".equals(preTag)){  
            	textBuffer.append(new String(ch,start,length));
            }  
        }  
    }
	
	@Override
	public Page getProduction() throws InterruptedException {
		return pageQueue.take();
	}

	@Override
	public boolean isStop() {
		return stopped && pageQueue.isEmpty();
	}

	@Override
	public void start() {
		new Thread(()->{
			stopped = false;
	        SAXParserFactory factory = SAXParserFactory.newInstance();  
	        SAXParser parser;    
	        try {
	        	parser = factory.newSAXParser(); 
				parser.parse(xmlStream, this);
			} catch(SAXException e){
				e.printStackTrace();
				System.out.println("parse over");
			} catch (Exception e) {
				e.printStackTrace();
			}finally{
		    	try {
					xmlStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				stopped = true;
			}
		}).start();
	}
}
