package index;

import org.apache.lucene.document.*;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * 从文件读入语料库，完成预处理，建立索引
 * Created by RTCarl on 2016/12/23.
 */
public class IndexLoader {


    //以文档为单位建立索引
    public static void indexByDocument(File file, IndexWriter writer){
        Document doc = new Document();
        SAXReader reader = new SAXReader();
        try{
            org.dom4j.Document d = reader.read(file);   //读取XML格式的文档

            Element rootElm = d.getRootElement();

            List<Element> pages = rootElm.elements("page"); //得到所有的Page元素

            Integer no = 1;

            for (Element page : pages) {
                Element title = page.element("title ");  //同一page中的title都一样
                doc.add(new StringField("title", title.getStringValue(), Field.Store.YES));
                //标题不做分词处理，可以使用String的方式储存
                //doc.add(new IntField("No.", no, Field.Store.YES));
                //存储编号
                no++;

                StringBuilder sb = new StringBuilder();

                List<Element> sentences = page.elements("sentence");
                for (Element sentence : sentences) {
                    sb.append(sentence.getStringValue()).append(" ");
                }

                String contents = sb.toString();
                System.out.println(contents);

                doc.add(new TextField("contents", contents, Field.Store.YES));
                writer.addDocument(doc);    //文档写入索引库中
            }

        }catch(DocumentException e){
            e.printStackTrace();
        }catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //以句子为单位建立索引
    public static void indexBySentence(File file, IndexWriter writer) {
        SAXReader reader = new SAXReader();

        try {

            org.dom4j.Document d = reader.read(file);

            Element rootElm = d.getRootElement();

            List<Element> pages = rootElm.elements("page"); //得到所有的Page元素

            Integer no = 1;

            for (Element page : pages) {
                String title = page.elementText("title ");  //同一page中的title都一样
                //逐句建立文档
                List<Element> sentences = page.elements("sentence");
                for (Element sentence : sentences) {
                    Document doc = new Document(); //每一个句子建立一个文档

                    //doc.add(new StringField("title", title, Field.Store.YES));
                    //标题不做分词处理，可以使用String的方式储存
                    //doc.add(new IntField("No.", no, Field.Store.YES));
                    //储存编号
                    doc.add(new TextField("contents", sentence.getStringValue(), Field.Store.YES));
                    //句子内容需要按文本的方式存储，Lucene的分析器会进行分词处理。
                    writer.addDocument(doc);
                }
                no++;
            }

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
