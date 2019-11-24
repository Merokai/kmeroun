package udev.jsp.kmeroun.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import udev.jsp.kmeroun.models.Dish;
import udev.jsp.kmeroun.utils.JacksonObjectMapper;

import java.io.Serializable;
import java.util.ArrayList;

public class SerializableArrayList<T> extends ArrayList<T> implements Serializable {

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

    public static SerializableArrayList fromString(String json) throws JsonProcessingException {
        return JacksonObjectMapper.getInstance().readValue(json, SerializableArrayList.class);
    }
}
