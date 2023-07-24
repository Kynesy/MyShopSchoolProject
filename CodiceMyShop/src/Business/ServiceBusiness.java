package Business;


import DAO.ArticleDAO;
import DAO.OrderDAO;
import DAO.ServiceDAO;
import Model.*;

import java.io.File;
import java.util.ArrayList;

public class ServiceBusiness {
    private static ServiceBusiness instance = new ServiceBusiness();
    private ServiceDAO serviceDAO;
    private ArticleDAO articleDAO;

    private ServiceBusiness(){
        serviceDAO = ServiceDAO.getInstance();
        articleDAO = ArticleDAO.getInstance();
    }

    public static ServiceBusiness getInstance(){
        return instance;
    }

    public UploadResponse createService(Service service){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Servizio creato con successo");
        uploadResponse.setDone(true);

        if( service.getVendorName().isBlank() || service.getDescription().isBlank() || service.getVendorName()==null){
            uploadResponse.setMessage("Creazione interrotta: alcuni campi sono vuoti.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }


        if( service.getPrice()<=0){
            uploadResponse.setMessage("Creazione interrotta: dati inconsistenti nel campo prezzo.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        int id = serviceDAO.createService(service);
        if(id == -1){
            uploadResponse.setMessage("Creazione interrotta: errore nel cariamento dei dati.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        service.setId(id);

        int fotoRow = 0;
        for (File foto : service.getFotos()){
            int r = articleDAO.addFoto(service.getId(), foto);
            fotoRow = fotoRow + r;
        }
        if(fotoRow != service.getFotos().size()){
            uploadResponse.setMessage("Servizio creato con successo, ma c'è un errore nel caricamento delle foto.");
            uploadResponse.setDone(true);
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse createServiceVendor(ServiceVendor serviceVendor) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Fornitore di servizi correttamente creato.");


        if(serviceVendor.getName().isBlank() || serviceVendor.getWebsite().isBlank() || serviceVendor.getCity().isBlank() || serviceVendor.getNation().isBlank()){
            uploadResponse.setMessage("Creazione interrotta: I dati inseriti non sono validi.");
            return uploadResponse;
        }

        if(serviceDAO.serviceVendorExist(serviceVendor.getName())){
            uploadResponse.setMessage("Creazione interrotta: Esiste già un fornitore con lo stesso nome.");
            return uploadResponse;
        }

        int row = serviceDAO.addServiceVendor(serviceVendor);
        if(row!=1){
            uploadResponse.setMessage("Errore sconosciuto.");
            return uploadResponse;
        }

        return uploadResponse;
    }

    public Service getServiceById(int articleID) {
        Service service = serviceDAO.getService(articleID);
        Category category = ArticleBusiness.getInstance().getCategory(articleID);
        service.setCategory(category);
        ArrayList<File> fotos = articleDAO.getFotosFromDB(articleID);
        service.setFotos(fotos);
        return service;
    }

    public Vendor[] getAllServiceVendors() {
        ArrayList<Vendor> arrayList = serviceDAO.getAllServiceVendors();
        return arrayList.toArray(Vendor[]::new);
    }

    public UploadResponse modifyService(Service service) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Servizio aggiornato con successo");
        uploadResponse.setDone(true);

        if( service.getVendorName().isBlank() || service.getDescription().isBlank() || service.getVendorName()==null){
            uploadResponse.setMessage("Aggiornamento interrotto: alcuni campi sono vuoti.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }


        if( service.getPrice()<=0){
            uploadResponse.setMessage("Aggiornamento interrotto: dati inconsistenti nel campo prezzo.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        int row = serviceDAO.modifyService(service);
        if(row == -1){
            uploadResponse.setMessage("Aggiornamento interrotto: errore nel cariamento dei dati.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        //elimino le vecchie foto e ricarico le nuove
        articleDAO.deleteAllFotos(service.getId());
        int fotoRow = 0;
        for (File foto : service.getFotos()){
            int r = articleDAO.addFoto(service.getId(), foto);
            fotoRow = fotoRow + r;
        }
        if(fotoRow != service.getFotos().size()){
            uploadResponse.setMessage("Servizio aggiornato con successo, ma c'è un errore nel caricamento delle foto.");
            uploadResponse.setDone(true);
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse deleteService(int serviceID){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Servizio eliminato con successo");

        articleDAO.deleteServiceFromAllWishlist(serviceID);
        articleDAO.deleteAllFotos(serviceID);
        articleDAO.deleteServiceFromFeedbacks(serviceID);
        OrderDAO.getInstance().deleteArticleFromOrderHistory(serviceID);
        serviceDAO.deleteService(serviceID);
        int row = articleDAO.deleteArticle(serviceID);
        if(row!=1){
            uploadResponse.setMessage("Errore nell'eliminazione del servizio.");
            return uploadResponse;
        }
        return uploadResponse;
    }
}
