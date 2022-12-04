package com.kursova.sql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import com.kursova.Java2022KursovaRabota;
import java.sql.Types;

public class SqlUtils {
    public static Connection conn;
    
    public static void init() {
        try {
            conn = DriverManager.getConnection("jdbc:sqlite:kursova.db");
            if(!conn.isValid(10)) {
                throw new SQLException("Връзката към базата не е валидна!");
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(
                null,
                ("Не успяхме да се свържем с базата данни! Уверете се, че инсталациятя е наред!\n"
                 + ex.toString()),
                "ГРЕШКА: Недостъпна база",
                JOptionPane.ERROR_MESSAGE
            );
            Logger.getLogger(Java2022KursovaRabota.class.getName()).log(
                Level.SEVERE,
                "Недостъпна база!",
                ex
            );
            System.exit(-1);
        }
    }
    
    public static BaseModel selectById(BaseModel model) {
        PreparedStatement st;
        try {
            st = conn.prepareStatement(model.constructQuery() + " where Id = ?");
            st.setInt(1, model.Id);
            ResultSet rs = st.executeQuery();
            
            if(!rs.next()) { return null; }
            model.fromResultSet(rs);
            return model;
        } catch (SQLException ex) {
            Logger.getLogger(SqlUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    public static boolean save(BaseModel model) {
        String[] columns = model.getColumnNames();
        
        PreparedStatement st;
        try {
            st = conn.prepareStatement(
                    String.format("insert or replace into %s(%s) values (%s)",
                            model.getTableName(),
                            String.join(", ", columns),
                            "?, ".repeat(columns.length-1) + " ?")
            );
        } catch (SQLException ex) {
            Logger.getLogger(SqlUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        try {
            Field[] fields = model.getClass().getFields();
            for(int i = 0; i < fields.length; i++)
            {
                Field f = fields[i];
                if(f.get(model) == null) {
                    st.setNull(i+1, Types.NULL);
                    continue;
                }

                Class<?> t = f.getType();
                if(t == Integer.class || t == int.class) {
                    st.setInt(i+1, (int) f.get(model));
                } else if(t == float.class) {
                    st.setFloat(i+1, f.getFloat(model));
                } else if(t == String.class) {
                    st.setString(i+1, (String) f.get(model));
                } else if(t == Date.class) {
                    st.setDate(i, (Date) f.get(model));
                }
            }
            st.execute();
        } catch (SQLException | IllegalArgumentException | IllegalAccessException ex) {
            Logger.getLogger(SqlUtils.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }
    public static ResultSet selectAll(BaseModel model) {
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(model.constructQuery());
        } catch (SQLException ex) {
            Logger.getLogger(SqlUtils.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
   }
}
