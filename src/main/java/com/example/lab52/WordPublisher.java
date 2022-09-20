package com.example.lab52;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
public class WordPublisher {
    @Autowired
    private RabbitTemplate rabbitTemplate;
    protected Word words = new Word();

    @RequestMapping(value = "/addBad" , method = RequestMethod.POST)
    public ArrayList<String> addBadWord(@RequestParam("n1") String s) {
        words.badWords.add(s);
        return words.badWords;
    }
    @RequestMapping(value = "/delBad/{word}" , method = RequestMethod.GET)
    public ArrayList<String> deleteBadWord(@PathVariable("word") String s){
        words.badWords.remove(s);
        return words.badWords;
    }
    @RequestMapping(value = "/addGood" , method = RequestMethod.POST)
    public ArrayList<String> addGoodWord(@RequestParam("n1") String s){
        words.goodWords.add(s);
        return words.goodWords;
    }
    @RequestMapping(value = "/delGood/{word}" , method = RequestMethod.GET)
    public ArrayList<String>  deleteGoodWord(@PathVariable("word") String s){
        words.goodWords.remove(s);
        return words.goodWords;
    }
    @RequestMapping(value = "/proofSentence" , method = RequestMethod.POST)
    public String proofSentence(@RequestParam("n2") String s){
        boolean isFoundBad = false;
        boolean isFoundGood = false;

        for(int i=0;i< words.badWords.size(); i++) {
            isFoundBad = s.contains(words.badWords.get(i));
            if (isFoundBad){
                break;
            }

        }
        for(int j=0;j< words.goodWords.size(); j++) {
            isFoundGood = s.contains(words.goodWords.get(j));
            if (isFoundGood){
                break;
            }
        }
        if(isFoundBad && isFoundGood){
            rabbitTemplate.convertAndSend("Fanout","",s );
            return "Found Bad & Good word";
        }
        else if(isFoundBad){
            rabbitTemplate.convertAndSend("Direct","bad",s );
            return "Found Bad Words";
        }
        else if(isFoundGood){
            rabbitTemplate.convertAndSend("Direct","good",s );
            return "Found Good Words";
        }

        return "Not Found";
    }

    @RequestMapping(value = "/getSentence" , method = RequestMethod.GET)
    public Sentence getSentence(){

        Object show = rabbitTemplate.convertSendAndReceive("Direct" , "get","");
//        System.out.println((Sentence) show);
        return ((Sentence)show);
    }
}
