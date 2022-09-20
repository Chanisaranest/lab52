package com.example.lab52;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class SentenceConsumer {
    protected Sentence sentences = new Sentence();


    @RabbitListener(queues = "BadWordQueue")
    public void addBadSentence(String s){
        sentences.badSentences.add(s);
        String text ="";
        for(int i=0;i<sentences.badSentences.size();i++){

            if(text != ""){
                text = text+", "+sentences.goodSentences.get(i);
            }
            else {
                text = sentences.goodSentences.get(i);
            }
        }
        System.out.println("In addBadSentence Method [" + text +"]");
    }

    @RabbitListener(queues = "GoodWordQueue")
    public void addGoodSentence(String s){
        String text1 = "";
        sentences.goodSentences.add(s);
        for (int j=0;j<sentences.goodSentences.size();j++){
            if(text1 != ""){
                text1 = text1+", "+sentences.goodSentences.get(j);
            }
            else {
                text1 = sentences.goodSentences.get(j);
            }
        }
        System.out.println("In addGoodSentence Method ["+ text1+"]");
    }
    @RabbitListener(queues = "GetQueue")
    public Sentence getSentencs(){
        return sentences;
    }

}
