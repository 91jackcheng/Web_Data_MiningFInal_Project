import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by diaosuyi on 12/24/16.
 */
public class questionAnswerer {
    private String question;

    private String output;

    private List<String> luceneList;

    questionAnswerer(String str, ArrayList<String> list){
        question = str;
        output = "";
        luceneList = list;
    }

    private void getAnswer(){
        typeDetector td = new typeDetector(question);

        //a simple type question, need to get answer in a more manual way
        if(td.getType()[0].equals("Simple")){
            //get the simple question type
            String questionType = td.getType()[1];
            switch(questionType){
                case "sentenceAfter":
                    //sentence is the question core
                    String sentence = "";
                    //find the question core and assign to sentence
                    Pattern p = Pattern.compile("\"([^\"]*)\"");
                    Matcher m = p.matcher(question);
                    while (m.find()) {
                        sentence = m.group(1);
                    }




            }
        }else{
            //put it in NGram and get the answer.
        }
    }

    //detect question type
    //if simple, switch cases
    //if complex, NGram

}
