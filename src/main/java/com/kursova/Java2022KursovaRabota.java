/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Project/Maven2/JavaApp/src/main/java/${packagePath}/${mainClassName}.java to edit this template
 */

package com.kursova;

import com.kursova.sql.Employee;
import com.kursova.sql.SqlUtils;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

/**
 *
 * @author pn
 */
public class Java2022KursovaRabota {
    public static Connection conn;
    
    public static void main(String[] args) {
        SqlUtils.init();
        Employee e = new Employee();
        e.IsSupervisor = 1;
        e.Name = "Administrator";
        e.Username = "Admin";
        e.Password = "Password";
        e.PhoneNumber = null;
        e.StorageLocationId = null;
        SqlUtils.save(e);
        
        LoginFrame frame = new LoginFrame();
        frame.show();
    }
}
