package crawler;

/**
 * Created by Dang on 2016/12/22.
 */
public class Main {
    public static void main(String[] args){
        String question = "“过江千尺浪”的下一句是什么";
        Searchwords sentence = new Searchwords(question);
        String[] results = sentence.get();
        for(String string:results) {
            System.out.println(string);
            System.out.println("*********************************");
        }
        System.out.println(sentence.getFbtext());
    }
}