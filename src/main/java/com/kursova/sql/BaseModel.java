/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kursova.sql;

import java.lang.reflect.Field;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pn
 */
public abstract class BaseModel{
    public Integer Id;

    public String getTableName() {
        return this.getClass().getName();
    }
    
    public String[] getColumnNames() {
        Field[] fields = this.getClass().getFields(); 
        String[] columns = new String[fields.length];
        
        for(int i = 0; i < fields.length; i++)
            columns[i] = fields[i].getName();
        
        return columns;
    }
    
    @Override
    public String toString() {
        Field[] fields = this.getClass().getFields();
        String result = "";
        
        for(Field f : fields) {
            try {
                Object value = f.get(this);
                result += "    " + f.getName() + ": " + (value == null ? "NULL" : value.toString()) + "\n";
            } catch (IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(BaseModel.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return "{\n" + result + "}\n";
    }

    public String constructQuery() {
        String[] names = getColumnNames();
        return String.format("select %s from %s", String.join(", ", names), this.getTableName());
    }
    
    public BaseModel fromResultSet(ResultSet rs) {
        Field[] fields = this.getClass().getFields();
        for(Field f : fields) {
            String name = f.getName();
            Class<?> type = f.getType();
            
            try {
                if(type == Integer.class || type == int.class) {
                    f.set(this, rs.getInt(name));
                } else if (type == String.class) {
                    f.set(this, rs.getString(name));
                } else if (type == Date.class) {
                    f.set(this, rs.getDate(name));
                }
            } catch (SQLException | IllegalArgumentException | IllegalAccessException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
            }
        }
        return this;
    }
}
