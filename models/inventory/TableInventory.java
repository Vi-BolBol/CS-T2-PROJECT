package models.inventory;
import models.Table;
import java.util.ArrayList;


public class TableInventory {
    private ArrayList<Table> tables;
    TableInventory(){
        tables = new ArrayList<>();
    }

    public void addTable(Table table){
        tables.add(table);
    }
    
}
