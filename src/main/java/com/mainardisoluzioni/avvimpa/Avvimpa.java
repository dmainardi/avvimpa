/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.mainardisoluzioni.avvimpa;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

/**
 *
 * @author Davide Mainardi <davide@mainardisoluzioni.com>
 */
public class Avvimpa {

    public static void main(String[] args) {
        System.out.println("Hello World!");
        
        List<Object> tempBean = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        InputStream inputStream = Avvimpa.class.getResourceAsStream("/com/mainardisoluzioni/avvimpa/documents/etichetta.jasper");
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(tempBean));
            JasperExportManager.exportReportToPdfFile(jasperPrint, "maina");
        } catch (JRException ex) {
            Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, ex);
        }
        /*SerialPort comPort = SerialPort.getCommPorts()[0];
        comPort.openPort();
        comPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        InputStream in = comPort.getInputStream();
        try {
        for (int j = 0; j < 1000; ++j) {
        System.out.print((char) in.read());
        }
        in.close();
        } catch (Exception e) {
        e.printStackTrace();
        }
        comPort.closePort();*/
    }
}
