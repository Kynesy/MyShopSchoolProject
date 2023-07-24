package Business.PDFBridge;

import Model.User;
import Model.WishList;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;

public interface PDFApi {
    File createPDForder(User user, WishList wishList, String orderID) throws FileNotFoundException, DocumentException;
    File createPDFreceipt(String username, String orderID) throws FileNotFoundException, DocumentException;
}
