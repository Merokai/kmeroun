package udev.jsp.kmeroun.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonObjectMapper {

    private static ObjectMapper om = null;
    public static ObjectMapper getInstance(){
        if(om == null){
            om = new ObjectMapper();
        }
        return om;
    }
    private JacksonObjectMapper(){}

}
