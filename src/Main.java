import index.Index;
import index.IndexResult;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.*;
import java.util.List;
import java.util.Scanner;

import static index.Index.initIndex;
import static index.Index.searchIndex;

/**
 * Created by 91jackcheng on 12/25/16.
 */
public class Main {

    public static void main(String[] args) throws IOException{
        //initIndex(Index.SENTENCE);
        Scanner s = new Scanner(new File("questions.txt"));
        FileWriter output = new FileWriter("answer.txt");

        while(s.hasNext()){
            int id = s.nextInt();
            String question = s.nextLine().trim();

            //开放测试开始
            List<String> res = null;
            try {
                res = Index.searchOnline(question);


                questionAnswerer qa = new questionAnswerer(question, res.toArray(new String[0]));
                output.write("" + id +"\t"+qa.getOutput() + "\n");

            } catch (IOException e) {
                e.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            封闭测试，注释掉以下代码，注释掉上面代码一直到List<String> res = null
//            List<IndexResult> res1 = null;
//
//        try {
//            //进行检索
//            res1 = searchIndex(question);
//            questionAnswerer qa = new questionAnswerer(question, res1.toArray(new String[0]));
//            output.write("" + id +"\t"+qa.getOutput() + "\n");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        }
        output.close();
    }
}
