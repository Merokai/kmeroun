package udev.jsp.kmeroun.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import udev.jsp.kmeroun.models.Product;

import java.util.HashMap;
import java.util.Map;

public class JacksonObjectMapper {

    private Map<Class, MapType> mapTypes = new HashMap<>();
    private JacksonObjectMapper(){
        om.getTypeFactory().constructMapType(HashMap.class, Product.class, Integer.class);
    }

    private static ObjectMapper om = null;
    public static ObjectMapper getInstance(){
        if(om == null){
            om = new ObjectMapper();
        }
        return om;
    }

}
