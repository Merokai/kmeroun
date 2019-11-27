package udev.jsp.kmeroun.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import javax.persistence.*;
import java.io.Serializable;

@Table(name = "dishes")
@Entity
public class Dish extends Product implements Serializable {

    public Dish(){
        super();
    }

    @Override
    public String toString() {
        String json = "{}";
        try{
            json = JacksonObjectMapper.getInstance().writeValueAsString(this);
        } catch(JsonProcessingException e){
            e.printStackTrace();
        }
        return json;
    }

    public static Dish fromString(String json) throws JsonProcessingException {
        return JacksonObjectMapper.getInstance().readValue(json, Dish.class);
    }
}
