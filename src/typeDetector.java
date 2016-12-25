import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TypedDependency;
import edu.stanford.nlp.trees.international.pennchinese.ChineseGrammaticalStructure;
import index.Index;
import org.apache.lucene.queryparser.classic.ParseException;

import java.io.IOException;
import java.util.Collection;
import java.util.EmptyStackException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by diaosuyi on 12/23/16.
 */


public class typeDetector {
    //the question
    private String question;
    //the question type;
    private TYPE type;

    private questionParser QP;

    //if the question can be solved using simple type, then store the simple
    //type in here.
    private String typeIfSimpleType;

    typeDetector(String input)
    {
        question = input;
        type = TYPE.COMPLEX_DISCORED;
        typeIfSimpleType = "";
        answerTypeDetect();
    }


    private String[][] typeDetectora = new String[][]{
            {"sentencebefore", "上一句","前一句","上句","前半句"},
            {"sentenceafter","下一句","后一句","下句","后半句"},
            {"dynasty", "哪个朝", "朝代是", "王朝是", "哪个王朝"},
            {"city","哪个省","哪省","哪个城市","区是","哪市","那个城市","哪座城市","什么州","什么市","哪个州","哪个市","市是","省份是","哪一座城市","省份是","县份是","哪一个省份","哪一省份","哪一省","那一省份","那一省","哪一区","哪一个区","哪一市","什么区","什么省"},
            {"country", "哪个国家", "哪国", "国籍是", "的国籍", "国家是"},
            {"continent", "哪块大陆", "哪个大陆", "哪一个大陆", "哪一块大陆", "大陆是", "大洲是", "哪个大洲", "哪一个大洲","哪个大洋","哪一个大洋"},
            {"date", "哪个时", "什么时候", "哪一月", "哪一天", "几月", "几日", "几号", "几点", "几时", "何时", "何日", "哪月", "哪天"},
            {"years", "公元多少", "公元前多少", "哪一年", "第几年", "年份是", "哪年", "何年"},
            {"number", "第几", "几", "多少", "有多少", "多远", "多重", "多大", "多小", "多宽", "多热", "多快", "多少","多久","多长", "海拔是", "高度是", "长度是", "距离是", "的海拔", "的长度", "的距离"},
    };

    void answerTypeDetect() {
        for (String[] one : typeDetectora)
            if (match(question, one)) {
                //decided the question is simple
                typeIfSimpleType = one[0];
                type = TYPE.SIMPLE;

                return;
            }
        //if does not fit into simple type, then must be complex type
        //must construct questionParser for further parsing.
        QP = new questionParser(question);
        //get the type of this comple question.
        type = QP.getType();

    }

    public String[] getType()
    {
        //first element in answer is type,
        //second element in answer is the attribute
        String[] answer = {"",""};
        switch (type){
            case SIMPLE:
                answer[0] = "Simple";
                answer[1] = typeIfSimpleType;
                break;

            case COMPLEX_CORED:
                answer[0] = "Cored";
                answer[1] = QP.getParsedQuestion();
                break;

            case COMPLEX_DISCORED:
                answer[0] = "Discored";
                answer[1] = QP.getParsedQuestion();
                break;

        }
        return answer;
    }

    boolean match(String word, String[] matchWords) {
        for (String w : matchWords)
            if (word.contains(w))
                return true;
        return false;
    }

    public enum TYPE{
        SIMPLE, COMPLEX_CORED, COMPLEX_DISCORED
    }

    public static void main(String[] args) throws IOException{
//        String a = "王莽建立的朝代叫什么？ \t新朝";
//
//        typeDetector haha = new typeDetector(a);
//
//        System.out.println(haha.getType()[0]);
//        System.out.println(haha.getType()[1]);

//
//        String modelpath="lib/models/chinesePCFG.ser.gz";
//        LexicalizedParser lp = LexicalizedParser.loadModel(modelpath);
//        String s = wss.output();
//
//        Tree t1 = lp.parse(s);
//        ChineseGrammaticalStructure gs1 = new ChineseGrammaticalStructure(t1);
//        Collection<TypedDependency> anstdl = gs1.typedDependenciesCollapsedTree();
//
//        System.out.println(anstdl);
//        NGramGeneratorString a = new NGramGeneratorString("用五条等距离的平行横线来记录音符的形式叫什么谱 《相约九八》是王菲和哪位内地著名女歌手合唱的 歌曲《达坂城的姑娘》中“你要是嫁人，不要嫁给别人”，下一句是");
//        System.out.println(a.getOutput()[0]);

        List<String> res = null;
        try {
            res = Index.searchOnline("长白山上的湖叫什么");

            questionAnswerer qa = new questionAnswerer("长白山上的湖叫什么", res.toArray(new String[0]));
            System.out.print(qa.getOutput());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }
}
