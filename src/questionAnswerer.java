import index.Index;
import sun.nio.ch.sctp.SctpNet;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import index.Index;

/**
 * Created by diaosuyi on 12/24/16.
 */
public class questionAnswerer {
    private String question;

    private String output;

    private String[] luceneData;


    questionAnswerer(String str,String[] luceneInput) throws IOException{
        question = str;
        output = "";
        luceneData = luceneInput;
        getAnswer();
    }

    public String getOutput()
    {
        return output;
    }

    private void getAnswer() throws IOException{
        typeDetector td = new typeDetector(question);

        //a simple type question, need to get answer in a more manual way
        if(td.getType()[0].equals("Simple")){
            //get the simple question type
            String questionType = td.getType()[1];
            switch(questionType){
                case "sentenceafter":
                    String[] result = luceneData[0].split(",");
                    output = result[0];
                    break;

                case "sentencebefore":
                    String[] result1 = luceneData[0].split(",");
                    output = result1[0];
                    break;

                case "dynasty":
                    String combinedStr = Arrays.toString(luceneData);
                    wordSpliter ws = new wordSpliter(combinedStr);
                    String nothing = ws.output();
                    String[] useable = nothing.split("\\s+");
                    boolean find = false;
                    for(int i = 0; i < useable.length; i++) {
                        Pattern p = Pattern.compile(".*朝|.*代");
                        Matcher m = p.matcher(useable[i]);
                        while (m.find()) {
                            find = true;
                            output = m.group(0);
                        }
                    }
                    if(!find){
                        output = "清朝";
                    }
                    break;

                case "city":
                    String str = Arrays.toString(luceneData);
                    wordSpliter ws1 = new wordSpliter(str);
                    String a = ws1.output();
                    String[] useable1 = a.split("\\s+");
                    boolean find1 = false;
                    for(int i = 0; i < useable1.length; i++) {
                        Pattern p = Pattern.compile(".*市|.*省|.*区|.*县");
                        Matcher m = p.matcher(useable1[i]);
                        while (m.find()) {
                            find1 = true;
                            output = m.group(0);
                        }
                    }
                    if(!find1){
                        //Ngram
                        NGramGeneratorString nggs = new NGramGeneratorString(str);
                        output = nggs.getOutput();

                    }
                    break;

                case "country":
                    boolean find2 = false;
                    Scanner file = new Scanner(new File("country.txt"));
                    Map<String, Integer> map = new HashMap<>();
                    while(file.hasNext()){
                        map.put(file.next(), 0);
                    }
                    String aaa = Arrays.toString(luceneData);
                    wordSpliter ws2 = new wordSpliter(aaa);
                    String aa = ws2.output();
                    String[] useable2 = aa.split(" ");

                    for(int i = 0; i < useable2.length; i++){
                        if(map.containsKey(useable2[i])){
                            output = useable2[i];
                            find2 = true;
                        }
                    }

                    if(!find2){
                        //Ngram
                        NGramGeneratorString nggs = new NGramGeneratorString(aaa);
                        output = nggs.getOutput();
                    }
                    break;

                case "continent":
                    String haha = Arrays.toString(luceneData);
                    wordSpliter ws3 = new wordSpliter(haha);
                    String b = ws3.output();
                    String[] useable3 = b.split("\\s+");
                    boolean find3 = false;
                    for(int i = 0; i < useable3.length; i++) {
                        Pattern p = Pattern.compile(".*洲");
                        Matcher m = p.matcher(useable3[i]);
                        while (m.find()) {
                            find3 = true;
                            output = m.group(0);
                        }
                    }
                    if(!find3){
                        output = "非洲";
                    }
                    break;

                case "date":
                    String xixi = Arrays.toString(luceneData);
                    wordSpliter ws4 = new wordSpliter(xixi);
                    String c = ws4.output();
                    String[] useable4 = c.split("\\s+");
                    boolean find4 = false;
                    for(int i = 0; i < useable4.length; i++) {
                        Pattern p = Pattern.compile(".*月|.*日|.*号|.*点|.*时|.*天|^[0-9]*$");
                        Matcher m = p.matcher(useable4[i]);
                        while (m.find()) {
                            find4 = true;
                            output = m.group(0);
                        }
                    }
                    if(!find4){
                        //NGram
                        NGramGeneratorString nggs = new NGramGeneratorString(xixi);
                        output = nggs.getOutput();
                    }
                    break;

                case "years":
                    String xixixi = Arrays.toString(luceneData);
                    wordSpliter ws5 = new wordSpliter(xixixi);
                    String d = ws5.output();
                    String[] useable5 = d.split("\\s+");
                    boolean find5 = false;
                    for(int i = 0; i < useable5.length; i++) {
                        Pattern p = Pattern.compile(".*年|^[0-9]*$");
                        Matcher m = p.matcher(useable5[i]);
                        while (m.find()) {
                            find5 = true;
                            output = m.group(0);
                        }
                    }
                    if(!find5){
                        //NGram
                        NGramGeneratorString nggs = new NGramGeneratorString(xixixi);
                        output = nggs.getOutput();
                    }
                    break;

                case "number":
                    String xixixih = Arrays.toString(luceneData);
                    wordSpliter ws6 = new wordSpliter(xixixih);
                    String e = ws6.output();
                    String[] useable6 = e.split("\\s+");
                    boolean find6 = false;
                    for(int i = 0; i < useable6.length; i++) {
                        Pattern p = Pattern.compile("^[0-9]*$|[\\d一二三四五六七八九十]+");
                        Matcher m = p.matcher(useable6[i]);
                        while (m.find()) {
                            find6 = true;
                            output = m.group(0);
                        }
                    }
                    if(!find6){
                        //NGram
                        NGramGeneratorString nggs = new NGramGeneratorString(xixixih);
                        output = nggs.getOutput();
                    }
                    break;

            }
        }else{
            //put it in NGram and get the answer.
            String xixixih = Arrays.toString(luceneData);
            NGramGeneratorString nggs = new NGramGeneratorString(xixixih);
            output = nggs.getOutput();
        }
    }


}
