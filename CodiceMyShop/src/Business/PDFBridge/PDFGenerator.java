package Business.PDFBridge;

import Model.User;
import Model.WishList;
import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;

public class PDFGenerator extends PDFAbstractGen {
    private User user;
    private WishList wishList;
    private String orderID;
    private String username;

    public PDFGenerator(PDFApi pdfApi, User user, WishList wishList, String orderID) {
        super(pdfApi);
        this.user = user;
        this.wishList = wishList;
        this.orderID = orderID;
    }

    public PDFGenerator(PDFApi pdfApi, String username,String orderID) {
        super(pdfApi);
        this.username = username;
        this.orderID = orderID;
    }

    @Override
    public File createOrder() throws DocumentException, FileNotFoundException {
        File file = pdfApi.createPDForder(user, wishList, orderID);
        return file;
    }

    @Override
    public File createReceipt() throws DocumentException, FileNotFoundException {
        File file = pdfApi.createPDFreceipt(username, orderID);
        return file;
    }
}
