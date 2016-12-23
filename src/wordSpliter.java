/**
 * Created by diaosuyi on 12/21/16.
 */

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;

public class wordSpliter {
    //the question from input
    private String question;

    //need to translate question into term list for reading
    private List<Term> translatedQuestion;

    wordSpliter(String text){
        text = text.replaceAll(" ", "");
        question = text;
        Result result = ToAnalysis.parse(question);
        translatedQuestion = result.getTerms();
    }

    //reread question in to the wordSpliter
    public void reReadQuestionFromString(String questionIn){
        question = questionIn;
        Result result = ToAnalysis.parse(question);
        translatedQuestion = result.getTerms();
    }


    private static String termListToSpaceSpiltString(List<Term> origin) {
        String result = "";
        for (Term term : origin) {
            result = result + term.getName() + " ";
        }
        return result;
    }

    public String output(){
        return termListToSpaceSpiltString(translatedQuestion);
    }

    public static void main(String[] args){
        wordSpliter a = new wordSpliter("欢迎使用ansj_seg,(ansj中文分词)在这里如果你遇到什么问题都可以联系我.我一定尽我所能.帮助大家.ansj_seg更快,更准,更自由!");
        System.out.println(a.output());
    }
}
