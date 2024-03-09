/*
 * Copyright (C) 2024 Davide Mainardi <davide@mainardisoluzioni.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
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
        tempBean.add(new Object());
        Map<String, Object> params = new HashMap<>();
        Avvimpa instance = new Avvimpa();
        InputStream inputStream = instance.getFileAsIOStream("documents/etichetta.jasper");
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(tempBean));
            JasperExportManager.exportReportToPdfFile(jasperPrint, "maina.pdf");
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
    
    private InputStream getFileAsIOStream(final String fileName) 
    {
        InputStream ioStream = this.getClass().getClassLoader().getResourceAsStream(fileName);
        
        if (ioStream == null) {
            throw new IllegalArgumentException(fileName + " is not found");
        }
        return ioStream;
    }
}
