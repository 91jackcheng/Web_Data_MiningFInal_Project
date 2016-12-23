/**
 * Created by diaosuyi on 12/21/16.
 */

import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.ToAnalysis;

import java.util.List;
import java.util.Scanner;

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

}
