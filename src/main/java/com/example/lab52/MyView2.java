package com.example.lab52;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;

@Route(value = "index2")
public class MyView2 extends FormLayout {
    private TextField n1,n2;
    private Button btn1,btn2,btn3,btn4;
    private TextArea goodSen,badSen;
    private Word words = new Word();
    public MyView2(){

        n1 = new TextField("Add Word");
        n1.setSizeFull();
        n2 = new TextField("Add Sentence");
        n2.setSizeFull();
        goodSen = new TextArea("Good Sentence");
        goodSen.setSizeFull();
        badSen = new TextArea("Bad Sentence");
        badSen.setSizeFull();
        btn1 = new Button("Add Good Word");
        btn1.setSizeFull();
        btn2 = new Button("Add Bad Word");
        btn2.setSizeFull();
        btn3 = new Button("Add sentence");
        btn3.setSizeFull();
        btn4 = new Button("Show sentence");
        btn4.setSizeFull();
        ComboBox<String> goodWordBox = new ComboBox<>();
        goodWordBox.setSizeFull();
        goodWordBox.setLabel("Good words");
        goodWordBox.setItems(words.goodWords);

        ComboBox<String> badWordBox = new ComboBox<>();
        badWordBox.setSizeFull();
        badWordBox.setLabel("Bad words");
        badWordBox.setItems(words.badWords);

        HorizontalLayout h1 = new HorizontalLayout();
        HorizontalLayout h2 = new HorizontalLayout();
        VerticalLayout v1 = new VerticalLayout();
        VerticalLayout v2 = new VerticalLayout();
        v1.add(n1,btn1,btn2,goodWordBox,badWordBox);
        v2.add(n2,btn3,goodSen,badSen,btn4);
        h1.add(v1);
        h2.add(v2);
        this.add(h1,h2);

        btn1.addClickListener(event ->{
            MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
            formData.add("n1", n1.getValue());


            ArrayList out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addGood")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            Notification text = Notification.show("Insert " +n1.getValue()+" good words list complete");
            goodWordBox.setItems(out);

        });

        btn2.addClickListener(event ->{
            MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
            formData.add("n1", n1.getValue());


            ArrayList out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/addBad")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(ArrayList.class)
                    .block();
            Notification text = Notification.show("Insert " +n1.getValue()+" bad words list complete");
            badWordBox.setItems(out);

        });

        btn3.addClickListener(event ->{
            MultiValueMap<String,String> formData = new LinkedMultiValueMap<>();
            formData.add("n2", n2.getValue());


            String out = WebClient.create()
                    .post()
                    .uri("http://localhost:8080/proofSentence")
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body(BodyInserters.fromFormData(formData))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            Notification text = Notification.show(out);
        });
        btn4.addClickListener(event ->{

            Sentence out = WebClient.create()
                    .get()
                    .uri("http://localhost:8080/getSentence")
                    .retrieve()
                    .bodyToMono(Sentence.class)
                    .block();
            System.out.println(out);
           goodSen.setValue(String.valueOf(out.goodSentences));
            badSen.setValue(String.valueOf(out.badSentences));
        });

    }


}
