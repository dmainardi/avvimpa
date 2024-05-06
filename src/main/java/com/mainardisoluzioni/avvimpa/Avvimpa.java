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

import com.fazecast.jSerialComm.SerialPort;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.Copies;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimplePrintServiceExporterConfiguration;

/**
 *
 * @author Davide Mainardi <davide@mainardisoluzioni.com>
 */
public class Avvimpa {
    
    private final String nomeEtichettatrice;

    public Avvimpa(String nomeEtichettatrice) {
        this.nomeEtichettatrice = nomeEtichettatrice;
    }
    
    public void ascoltaSullaSerialeEStampaEtichetta() {
        boolean isBarcode = false;       
        SerialPort comPortBar = null;
        SerialPort comPortAvv = null;
        for (SerialPort commPortTemp : SerialPort.getCommPorts()) {
            String portName = commPortTemp.getDescriptivePortName().toLowerCase();
            if (portName.contains("barcode")) {
                comPortBar = commPortTemp;
                isBarcode = true;
            }
            if(portName.contains("usb")){
                comPortAvv = commPortTemp;
            }
        }
                

        if (comPortAvv == null) {
            comPortBar.disableExclusiveLock();
            comPortBar.openPort();
            comPortBar.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            int qtaEtichette = 1;
            try (InputStream inBar = comPortBar.getInputStream()) {
                BufferedReader readerBar = new BufferedReader(new InputStreamReader(inBar));
                String lineBar;
                while ((lineBar = readerBar.readLine()) != null) {
                    
                        
                        try {
                        String subLine = lineBar.substring(0);  
                        qtaEtichette = Integer.parseInt(subLine.substring(0, 1));                       
                        stampaSuEtichettatrice(nomeEtichettatrice, subLine.substring(1, 6) + " OK", DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss").format(LocalDateTime.now()), qtaEtichette);
                        }catch (IndexOutOfBoundsException e) {
                        }
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            comPortBar.closePort();
            
        }
        
    if (comPortAvv != null) {
        
         System.out.println("presente avvitatore");
         
            comPortBar.disableExclusiveLock();
            comPortBar.openPort();
            comPortBar.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
            int qtaEtichette = 1;
            try (InputStream inBar = comPortBar.getInputStream()) {
                BufferedReader readerBar = new BufferedReader(new InputStreamReader(inBar));
                String lineBar;
                while ((lineBar = readerBar.readLine()) != null) {
                    
                        
                        try {
                        String subLine = lineBar.substring(0);  
                        
                        //System.out.println(subLine.substring(0, 1));
                        //qtaEtichette = Integer.parseInt(subLine.substring(0, 1));
                        
                        //System.out.println(qtaEtichette + "/"+ subLine.substring(1, 6));
                        
                        //stampaSuEtichettatrice(nomeEtichettatrice, subLine.substring(1, 6) + " OK", DateTimeFormatter.ofPattern("dd/MM/yy HH:mm:ss").format(LocalDateTime.now()), qtaEtichette);
                        

                        comPortAvv.disableExclusiveLock();
                        comPortAvv.openPort();
                        comPortAvv.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

                        try (InputStream inAvv = comPortAvv.getInputStream()) {
                            BufferedReader readerAvv = new BufferedReader(new InputStreamReader(inAvv));
                            String lineAvv;
                            while ((lineAvv = readerAvv.readLine()) != null) {
                               
                            try {
                                String subLineAvv = lineAvv.substring(76);
                                if (subLineAvv.toLowerCase().contains("program end")) {
                                  stampaSuEtichettatrice(nomeEtichettatrice, subLine.substring(1, 6), subLineAvv.substring(0, 17), qtaEtichette);
                                 }
                             } catch (IndexOutOfBoundsException e) {
                              // niente da fare
                            }                        
                        
                            }
                        
                            }catch (IndexOutOfBoundsException e) {
                        }
                            
                        }catch (IndexOutOfBoundsException e) {
                        }
                    
                }
            } catch (IOException ex) {
                Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            comPortBar.closePort();
            comPortAvv.closePort();
                        
        }    
        
    }


  

    private void stampaSuEtichettatrice(String nomeEtichettatrice, String operazione, String identificativo, int qtaEtichette) {
        if (nomeEtichettatrice != null && !nomeEtichettatrice.isBlank()) {
            PrintService printService = PrintUtility.findPrintService(nomeEtichettatrice);
            if (printService != null) {
                try {
                    JasperPrint jasperPrint = creaReport(operazione, identificativo);

                    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                    printRequestAttributeSet.add(new Copies(qtaEtichette));
                    
                    PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                    printServiceAttributeSet.add(printService.getAttribute(PrinterName.class));

                    JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();

                    configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
                    configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
                    configuration.setDisplayPageDialog(false);
                    configuration.setDisplayPrintDialog(false);
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();

                    //JasperExportManager.exportReportToPdfFile(jasperPrint, "maina.pdf");
                } catch (JRException ex) {
                    Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, "vendita.etichetta.stampa.errore.etichettatriceMancante");
            }
        }
    }

    private JasperPrint creaReport(String operazione, String identificativo) throws JRException {
        List<Object> tempBean = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Etichetta");
        params.put("identificativo", identificativo);
        params.put("operazione", operazione);

        tempBean.add(new Object());

        ResourceFileHelper resourceFileHelper = new ResourceFileHelper();
        InputStream inputStream = resourceFileHelper.getFileAsIOStream("documents/etichetta.jasper");
        return JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(tempBean));
    }

}
