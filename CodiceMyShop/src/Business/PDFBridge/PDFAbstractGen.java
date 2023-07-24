package Business.PDFBridge;

import com.itextpdf.text.DocumentException;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class PDFAbstractGen {
    protected PDFApi pdfApi;

    protected PDFAbstractGen(PDFApi pdfApi){
        this.pdfApi = pdfApi;
    }

    public abstract File createOrder() throws DocumentException, FileNotFoundException;
    public abstract File createReceipt()throws DocumentException, FileNotFoundException;
}
