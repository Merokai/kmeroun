package udev.jsp.kmeroun.model;

import java.util.Collection;
import java.util.HashMap;

public class Database {
    //private DishList dishModelList;
    private HashMap<String, UserModel> userModelList;
    private HashMap<Integer, OrderModel> orderModelList;
    private HashMap<Integer, DishModel> dishModelList;

    private static Database db;

    public static Database getInstance(){
        if(db == null){
            db = new Database();
        }
        return db;
    }

    private Database(){
        dishModelList = new HashMap<>();

            DishModel dish = new DishModel();
            dish.setIdentifiant(5);
            dish.setNom("test");
            dish.setPrix(6.75);
            dishModelList.put(dish.getIdentifiant(), dish);
            dish = new DishModel();
            dish.setIdentifiant(6);
            dish.setNom("test+1");
            dish.setPrix(7.75);
            dishModelList.put(dish.getIdentifiant(), dish);

        userModelList = new HashMap<>();
        orderModelList = new HashMap<>();
    }

    public Collection<DishModel> getDishModelList() {
        return dishModelList.values();
    }

    public Collection<OrderModel> getOrderModelList() {
        return orderModelList.values();
    }

    public Collection<UserModel> getUserModelList() {
        return userModelList.values();
    }
}
