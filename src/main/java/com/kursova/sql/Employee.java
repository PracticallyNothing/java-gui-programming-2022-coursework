/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.kursova.sql;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pn
 */
public class Employee extends BaseModel {
    @Override
    public String getTableName() {
        return "Employees";
    }
    public String Username;
    public String Password;
    public String Name;
    public String PhoneNumber;
    public Integer StorageLocationId;
    public Integer IsSupervisor;
}
