package index;


import index.IndexLoader;
import crawler.Searchwords;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.store.SimpleFSDirectory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by RTCarl on 2016/12/23.
 */
public class Index {

    /**
     * 被检索文件的存放目录,可以自行修改
     */
    public static File fileDir = new File("/Users/diaosuyi/Downloads");


    /**
     * 生成的检索文件的目录，可以自行修改
     */
    public static File indexDir = new File("/Users/diaosuyi/Desktop/index");



    public static int SENTENCE = 1;
    public static int PAGE = 2;

    //建立索引
    public static void initIndex(int way) throws IOException {


        Directory dir = new SimpleFSDirectory(indexDir.toPath());
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, cfg);

        Document doc = null;
        int cnt = 0;
        for(File file : fileDir.listFiles())
        {
            System.out.println("fileName: " + file.getName());
            if(way == SENTENCE)
                IndexLoader.indexBySentence(file, writer);
            else
                IndexLoader.indexByDocument(file, writer);
            cnt ++;
        }

        System.out.println("Total documents: "+cnt);

        writer.close();
        dir.close();
    }
    //检索部分，开放测试
    public static List<String> searchOnline(String question) throws IOException, ParseException
    {
        List<String> res = new ArrayList<String>();

        Searchwords online = new Searchwords(question);
        String fbtext = online.getFbtext();
        if(fbtext != null)
        {
            res.add(fbtext);
            return res;
        }
        //无fbtext
        String[] sentences = online.get();

        Directory dir = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig cfg = new IndexWriterConfig(analyzer);
        IndexWriter writer = new IndexWriter(dir, cfg);

        Document doc = null;
        for(String sentence : sentences)
        {
            doc = new Document();
            doc.add(new TextField("contents", sentence, Field.Store.YES));
            writer.addDocument(doc);
        }
        writer.close();

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        Query q = new QueryParser("contents", analyzer).parse(question);
        TopDocs topDocs = searcher.search(q, 10);

        ScoreDoc[] hits = topDocs.scoreDocs;

        for(ScoreDoc scoreDoc : hits)
        {
            doc = searcher.doc(scoreDoc.doc);

            String contents = doc.get("contents");
            res.add(contents);
//            System.out.println(contents);
        }
        reader.close();

        return res;
    }

    //检索部分,封闭测试
    public static List<IndexResult> searchIndex(String queryString) throws IOException, ParseException {

        List<IndexResult> ress = new ArrayList<IndexResult>();



        Analyzer analyzer = new StandardAnalyzer();
        Directory dir = FSDirectory.open(indexDir.toPath());
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);


        Map<String, Float> boosts = new HashMap<String, Float>();
        boosts.put("title", 1.0f);  //设置title匹配度的权重，可调节
        boosts.put("contents", 1.0f);   //设置contents匹配度的权重，可调节

        String[] fields = new String[]{"title", "contents"};

        MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer, boosts);
        //多域联合查询

        Query query = parser.parse(queryString);
        TopDocs topDocs = searcher.search(query, 20);
        //得到匹配度最高的20个文档

        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        for(ScoreDoc scDoc : scoreDocs)
        {

            Document doc = searcher.doc(scDoc.doc);

            //String no = doc.get("No.");
            String title = doc.get("title");
            String contents = doc.get("contents");


            IndexResult res = new IndexResult();
            res.setTitle(title);
            //res.setNo(Integer.parseInt(no));
            res.setContents(contents);

            ress.add(res);

        }
        reader.close();
        dir.close();

        return ress;
        //返回按相关程度排序后的结果
    }

    //样例
    public static void main(String[] args) {


//        List<IndexResult> res = null;
//
//        try {
//
//            //选择建立索引，以句子为单位
//            //initIndex(Index.SENTENCE);
//            //进行检索
//            res = searchIndex("西晋开国皇帝司马炎的父亲叫什么名字");
//            for(IndexResult str : res)
//            {
//                System.out.println(str.getContents());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        try {
            List<String> res = null;
            res = Index.searchOnline("“床前明月光”的下一句是什么");
            for(String s : res)
            {
                System.out.println(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
