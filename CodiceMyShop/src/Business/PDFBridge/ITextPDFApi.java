package Business.PDFBridge;

import Business.CompositeBusiness;
import Business.ItemBusiness;
import Business.OrderBusiness;
import DAO.CompositeDAO;
import Model.*;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;

public class ITextPDFApi implements PDFApi {
    @Override
    public File createPDForder(User user, WishList wishList, String orderID) throws FileNotFoundException, DocumentException{
        String name = "ordine_Lista_"+ wishList.getName() + "_User_" + user.getUsername() + ".pdf";
        String file_name = System.getProperty("user.dir") + "\\" + name;

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file_name));

        document.open();

        Paragraph space = new Paragraph(" ");
        Font font = new Font(Font.FontFamily.HELVETICA,14.0f, Font.BOLD);
        Paragraph titolo = new Paragraph("Nome lista: " + wishList.getName() + " (STATO: NON PAGATO) \n CODICE ORDINE: "+orderID, font);
        Font font1 = new Font(Font.FontFamily.HELVETICA,13.5f);
        Paragraph utente = new Paragraph("Dati utente\n Nome: "+ user.getName() + "\n Username: " + user.getUsername() +
                "\n Email:  "+ user.getEmail() + "\n", font1);
        String timeStamp = new SimpleDateFormat("dd/MM/yyyy ").format(Calendar.getInstance().getTime());
        Paragraph data = new Paragraph("Data: " + timeStamp);


        PdfPTable table = new PdfPTable(5);
        PdfPCell c1 = new PdfPCell(new Paragraph("ID", font));
        PdfPCell c2 = new PdfPCell(new Paragraph("Nome articolo", font));
        PdfPCell c3 = new PdfPCell(new Paragraph("Prezzo", font));
        PdfPCell c4 = new PdfPCell(new Paragraph("Quantità", font));
        PdfPCell c5 = new PdfPCell(new Paragraph("Tipo", font));
        table.addCell(c1);
        table.addCell(c2);
        table.addCell(c3);
        table.addCell(c4);
        table.addCell(c5);

        Iterator<Item> itemIterator = wishList.getItems().iterator();
        while (itemIterator.hasNext()){
            Item item =itemIterator.next();
            c1 = new PdfPCell(new Paragraph("" + item.getId(), font1));
            c2 = new PdfPCell(new Paragraph("" + item.getName(), font1));
            c3 = new PdfPCell(new Paragraph("" + item.getPrice() + "€", font1));
            c4 = new PdfPCell(new Paragraph("" + item.getWishlistQuantity(), font1));
            if(CompositeDAO.getIstance().isComposite(item.getID())){
                c5 = new PdfPCell(new Paragraph("Prodotto Composito", font1));
            }else {
                c5 = new PdfPCell(new Paragraph("Prodotto", font1));
            }
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
        }

        Iterator<Service> serviceIterator = wishList.getServices().iterator();
        while (serviceIterator.hasNext()){
            Service service = serviceIterator.next();
            c1 = new PdfPCell(new Paragraph("" + service.getId(), font1));
            c2 = new PdfPCell(new Paragraph("" + service.getName(), font1));
            c3 = new PdfPCell(new Paragraph("" + service.getPrice() + "€", font1));
            c4 = new PdfPCell(new Paragraph("1"));
            c5 = new PdfPCell(new Paragraph("Servizio" , font1));
            table.addCell(c1);
            table.addCell(c2);
            table.addCell(c3);
            table.addCell(c4);
            table.addCell(c5);
        }

        double subtotal = OrderBusiness.getInstance().calculateSubtotal(wishList);
        Paragraph totale = new Paragraph("TOTALE: "+ subtotal + "€", font1);

        document.add(titolo);
        document.add(utente);
        document.add(space);
        document.add(table);
        document.add(space);
        document.add(totale);
        document.add(space);
        document.add(data);

        document.close();
        File file = new File(file_name);
        return file;
    }

    @Override
    public File createPDFreceipt(String username, String orderID) throws FileNotFoundException, DocumentException {
        String name = "RicevutaOrdine_"+ orderID + "_User_" + username + ".pdf";
        String file_name = System.getProperty("user.dir") + "\\" + name;

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(file_name));

        document.open();

        Font font = new Font(Font.FontFamily.HELVETICA,14.0f, Font.BOLD);
        Font font1 = new Font(Font.FontFamily.HELVETICA,13.5f);
        Paragraph space = new Paragraph(" ");

        Paragraph titolo = new Paragraph("ORDINE ["+orderID+"] APPROVATO",font);
        Paragraph contenuto = new Paragraph("Gentile utente ["+username+"],\n la informiamo che il suo" +
                "ordine numero ["+orderID+"] è dato approvato dalla dirigenza di MyShop e risulta correttamente concluso.", font1);
        Paragraph fine = new Paragraph("Questo file ha validità di ricevuta per MyShop (myshop1702@gmail.com)", font1);

        document.add(titolo);
        document.add(space);
        document.add(contenuto);
        document.add(space);
        document.add(fine);

        document.close();
        File file = new File(file_name);
        return file;
    }
}
