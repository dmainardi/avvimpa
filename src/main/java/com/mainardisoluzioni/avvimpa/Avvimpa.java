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
import javax.print.PrintService;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaPrintableArea;
import javax.print.attribute.standard.MediaSize;
import javax.print.attribute.standard.OrientationRequested;
import javax.print.attribute.standard.PrinterName;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
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

    public static void main(String[] args) {

        Avvimpa instance = new Avvimpa();
        instance.stampaSuEtichettatrice("mainami");

        /*List<Object> tempBean = new ArrayList<>();
        tempBean.add(new Object());
        Map<String, Object> params = new HashMap<>();
        Avvimpa instance = new Avvimpa();
        InputStream inputStream = instance.getFileAsIOStream("documents/etichetta.jasper");
        try {
            JasperPrint jasperPrint = JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(tempBean));
            JasperExportManager.exportReportToPdfFile(jasperPrint, "maina.pdf");
        } catch (JRException ex) {
            Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, ex);
        }*/


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

    private void stampaSuEtichettatrice(String nomeEtichettatrice) {
        if (nomeEtichettatrice != null && !nomeEtichettatrice.isBlank()) {
            PrintService printService = PrintUtility.findPrintService(nomeEtichettatrice);
            if (printService != null) {
                try {
                    JasperPrint jasperPrint = creaReport();

                    PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
                    printRequestAttributeSet.add(new MediaPrintableArea(0, 0, 1, 0.5f, MediaPrintableArea.INCH));
                    printRequestAttributeSet.add(MediaSize.findMedia(1, 0.5f, MediaSize.INCH));
                    printRequestAttributeSet.add(OrientationRequested.PORTRAIT);

                    PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
                    printServiceAttributeSet.add(printService.getAttribute(PrinterName.class));

                    JRPrintServiceExporter exporter = new JRPrintServiceExporter();
                    exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                    SimplePrintServiceExporterConfiguration configuration = new SimplePrintServiceExporterConfiguration();

                    //configuration.setPrintRequestAttributeSet(printRequestAttributeSet);
                    configuration.setPrintServiceAttributeSet(printServiceAttributeSet);
                    configuration.setDisplayPageDialog(false);
                    configuration.setDisplayPrintDialog(false);
                    exporter.setConfiguration(configuration);
                    exporter.exportReport();
                    
                    JasperExportManager.exportReportToPdfFile(jasperPrint, "maina.pdf");
                } catch (JRException ex) {
                    Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, ex);
                    //Messages.create("error").error().detail(ex.getMessage()).add();
                }
            }
            else
                Logger.getLogger(Avvimpa.class.getName()).log(Level.SEVERE, null, "vendita.etichetta.stampa.errore.etichettatriceMancante");
                //Messages.create("error").error().detail("vendita.etichetta.stampa.errore.etichettatriceMancante").add();
        }
    }

    private JasperPrint creaReport() throws JRException {
        List<Object> tempBean = new ArrayList<>();
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Etichetta");
        /*params.put("subReportPath", externalContext.getRealPath("/resources/documents/vendita/") + "/");
        params.put("reportImagePath", externalContext.getRealPath("/resources/documents/images/") + "/");*/

        tempBean.add(new Object());

        Avvimpa instance = new Avvimpa();
        InputStream inputStream = instance.getFileAsIOStream("documents/etichetta.jasper");
        return JasperFillManager.fillReport(inputStream, params, new JRBeanCollectionDataSource(tempBean));
    }


}
