package crawler;

/**
 * Created by Dang on 2016/12/23.
 */


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Searchwords{
    private String question;
    private String content;
    //百度搜索
    private static final String urlRoot = "http://www.baidu.com/s?wd=";

    public Searchwords(String question)
    {
        this.question = question;
        this.content = getUrl(urlRoot + question);
    }

    public String getFbtext()
    {
        Pattern pattern = Pattern.compile("fbtext: '([^']*)'");
        Matcher matcher = pattern.matcher(content);
        if(matcher.find())
        {
            return matcher.group(1);
        }
        return null;
    }

    //查询格式函数，包括各个参数集合
    public String[] get(){
        ArrayList<String> sentences = new ArrayList<String>();
        //String content = getContentFromUrl("http://www.baidu.com/s?wd="+question);
        Document document = Jsoup.parse(content);
        Elements elements = document.select("div#content_left div[id~=^[1-9]$] div.c-abstract");
        for(Element element: elements){
            Element span = element.getElementsByTag("span").first();
            if(span != null) span.text("");
            String[] ss = element.text().split("。");
            for(String s:ss) sentences.add(s);
        }
        return sentences.toArray(new String[0]);
    }

    //url格式函数
    private String getUrl( String strUrl )
    {
        try {
            URL url = new URL(strUrl);
            InputStream stream = url.openStream();
            String content = readAllContent( stream,"UTF-8" ); //常见的编码包括 GB2312, UTF-8
            return content;
        }catch ( MalformedURLException e) {
            System.out.println("URL格式有错 : " + strUrl );
        }catch (IOException ioe) {
            System.out.println("IO异常 : " + strUrl );
        }
        return "";
    }


    private String readAllContent( InputStream stream, String charcode ) throws IOException{
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(stream, charcode));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line+"\n");
        }
        return sb.toString();
    }
}
