import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by diaosuyi on 12/24/16.
 */
public class NGramGenerator {
    private String fileName;
    private ArrayList<String> nGramList;
    private Map<String, Integer> nGramMap;

    private String[] finalList;

    NGramGenerator(String file) throws IOException{
        fileName = file;
        finalList = new String[20];
        nGramMap = new HashMap<String, Integer>();

        nGramList = new ArrayList<String>();
        generateNgrams(1);
        generateNgrams(2);
        generateNgrams(3);
        generateFinalAnswer();
    }

    //get the final out put of the NGramGenerator
    public String[] getOutput(){
        return finalList;
    }

    //generate the Final Answer
    private void generateFinalAnswer(){
        //generateNgramMap
        for(String one : nGramList){
            if(!nGramMap.containsKey(one)){
                nGramMap.put(one, 1);
            }else if(nGramMap.containsKey(one)){
                nGramMap.put(one, nGramMap.get(one) + 1);
            }
        }

        //sort the NgramMap to get the top 10 answer.
        List<Integer> mapValues = new ArrayList<>(nGramMap.values());

        Collections.sort(mapValues, Collections.reverseOrder());
        Iterator<Integer> valueIt = mapValues.iterator();

        int i = 0;
        while(valueIt.hasNext() && i < 20){
            finalList[i] = getKeyFromValue(nGramMap, valueIt.next());
            System.out.println(valueIt.next());
            i++;
        }
    }

    private String getKeyFromValue(Map<String, Integer> hm, Integer value) {
        for (String o : hm.keySet()) {
            if (hm.get(o).equals(value)) {
                return o;
            }
        }
        return null;
    }

    //function to generateNgrams
    private void generateNgrams(int N) throws IOException{
        Scanner fileScanner = new Scanner(new File(fileName));
        while(fileScanner.hasNext()) {
            String nextln = fileScanner.nextLine();
            wordSpliter ws = new wordSpliter(nextln);

            String[] tokens = ws.output().split("\\s+"); //split sentence into tokens

            //GENERATE THE N-GRAMS
            for (int k = 0; k < (tokens.length - N + 1); k++) {
                String s = "";
                int start = k;
                int end = k + N;
                for (int j = start; j < end; j++) {
                    s = s + "" + tokens[j];
                }
                //Add n-gram to a list
                nGramList.add(s);
            }
        }
    }//End of method



}
