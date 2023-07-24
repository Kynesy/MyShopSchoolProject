package Business;

import DAO.*;
import Model.Customer;
import Model.UploadResponse;
import Model.User;
import Model.WishList;

import java.util.ArrayList;

public class CustomerBusiness {
    private static CustomerBusiness instance = new CustomerBusiness();
    private CustomerDAO updatedUser;

    private CustomerBusiness(){
        updatedUser = CustomerDAO.getInstance();
    }

    public static CustomerBusiness getInstance(){
        return instance;
    }

    public UploadResponse createWishlist(String wishlishName, User user){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Hai creato la lista ["+ wishlishName +"] con successo.");
        uploadResponse.setDone(true);

        if(wishlishName.isEmpty()){
            uploadResponse.setDone(false);
            uploadResponse.setMessage("Creazione lista interrotta: dati non validi.");
            return uploadResponse;
        }
        int row = updatedUser.createWishList(user.getUsername(), wishlishName);
        if(row == 0){
            uploadResponse.setMessage("Lista non creata per via di un errore sconosciuto.");
            uploadResponse.setDone(true);
            return uploadResponse;
        }

        return uploadResponse;
    }

    public WishList[] getAllCustomerWishlists(User user){
        ArrayList<WishList> wishListArrayList = updatedUser.getAllCustomerWishLists(user.getUsername());
        if(wishListArrayList == null){
            return null;
        }
        return wishListArrayList.toArray(WishList[]::new);
    }

    public UploadResponse deleteUserWishlist(WishList wishList){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Lista ["+ wishList.getName() +"] cancellata con successo.");
        int r = CustomerDAO.getInstance().resetWishlist(wishList.getId());
        if(r != wishList.getNumerOfArticles()){
            uploadResponse.setMessage("Errore cancellazione: problema durante lo svuotamento della lista. ");
            return uploadResponse;
        }
        int r1 = CustomerDAO.getInstance().deleteUserWishlist(wishList.getId());
        if(r1 != 1){
            uploadResponse.setMessage("Errore: la lista ["+ wishList.getName() +"] è stata svuotata, ma non eliminata.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse emptyUserWishlist(WishList wishList)
    {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Lista ["+ wishList.getName() +"] correttamente svuotata.");
        int r = CustomerDAO.getInstance().resetWishlist(wishList.getId());
        if(r != wishList.getNumerOfArticles()){
            uploadResponse.setMessage("Errore: lista non svuotata.");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public UploadResponse addArticleToWishlist(WishList wishList, int articleID, int quantity){
        UploadResponse uploadResponse = new UploadResponse();
        int oldQuantity = CustomerDAO.getInstance().wishlistContainsItem(wishList.getId(), articleID);
        if(articleID==-1){
            uploadResponse.setMessage("Articolo selezionato non valido.");
            return uploadResponse;
        }


        if(ItemDAO.getInstance().itemExist(articleID)) { //branca del prodotto
            uploadResponse. setMessage("Sono state aggiunti ["+ quantity +" pezzi] dell'articolo [ID: "+ articleID +"]" +
                    " alla lista ["+ wishList.getName() +"].");
            if(quantity<1){//controllo la validità della quantità
                uploadResponse.setMessage("La quantità selezionata deve essere maggiore di 1.");
                return uploadResponse;
            }
            if (oldQuantity != -1) { //se è diversa da -1 vuol dire che il prodotto c'è già in lista
                int row = CustomerDAO.getInstance().updateItemQuantity(wishList.getId(), articleID, oldQuantity + quantity);
                if(row!=1){
                    uploadResponse.setMessage("La quantià richiesta non è stata aggiunta per via di un errore.");
                    return uploadResponse;
                }
            } else { //se è -1, va prima inserito poi settata la quantità
                int row = CustomerDAO.getInstance().addArticleToWishList(articleID, wishList);
                if(row != 1){
                    uploadResponse.setMessage("Errore sconosciuto nell'aggiunta del prodotto alla lista.");
                    return uploadResponse;
                }

                int row1 = CustomerDAO.getInstance().updateItemQuantity(wishList.getId(), articleID, quantity);
                if(row1!=1){
                    uploadResponse.setMessage("Il prodotto è stato aggiunto alla lista, ma c'è un errore relativo alla quantità.");
                    return uploadResponse;
                }
            }
            return uploadResponse;
        }else {//branca del servizio
            uploadResponse.setMessage("Il servizio [ID: "+ articleID +"] è stato aggiunto alla lista ["+ wishList +"]");
            if (oldQuantity != -1) { //i servizi non hanno quantità, vanno solo aggiunti in lista
                uploadResponse.setMessage("Il servizio è già presente nella lista.");
                return uploadResponse;
            }
            int row = CustomerDAO.getInstance().addArticleToWishList(articleID, wishList);
            if(row != 1){
                uploadResponse.setMessage("Errore sconosciuto nell'aggiunta del servizio.");
                return uploadResponse;
            }
            return uploadResponse;
        }
    }

    public UploadResponse removeArticleFromWishlist(WishList wishlist, int articleID, int quantity) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Articolo rimosso con successo dalla lista.");
        if(articleID == -1){
            uploadResponse.setMessage("Nessun articolo valido selezionato.");
            return uploadResponse;
        }

        if(quantity<1){
            uploadResponse.setMessage("Errore: la quantità inserita deve essere maggiore di 0.");
            return uploadResponse;
        }

        if(!ItemDAO.getInstance().itemExist(articleID)){ //non avendo quantità, i servizi vanno rimossi e basta
            int row = CustomerDAO.getInstance().removeItemFromWishList(articleID, wishlist);
            if(row != 1){
                uploadResponse.setMessage("Errore sconosciuto nella rimozione del servizio.");
                return uploadResponse;
            }
        } //se sei qui, è un prodotto
        int oldQuantity = CustomerDAO.getInstance().wishlistContainsItem(wishlist.getId(), articleID);
        if(oldQuantity>quantity){ //verifica che la quantità desiderata non diventi negativa. in tal caso rimuove il prodotto dalla lista
            int row = CustomerDAO.getInstance().updateItemQuantity(wishlist.getId(), articleID, oldQuantity-quantity);
            if(row!=1){
                uploadResponse.setMessage("Errore: quantità non aggiornata");
                return uploadResponse;
            }
            uploadResponse.setMessage("Il prodotto ["+ articleID +"] ha subito una variazione dei pezzi presenti in lista.");
            return uploadResponse;
        }else {
            int row = CustomerDAO.getInstance().removeItemFromWishList(articleID, wishlist);
            if(row != 1){
                uploadResponse.setMessage("Errore nella rimozione del prodotto dalla lista.");
                return uploadResponse;
            }
        }
        return uploadResponse;
    }

    public UploadResponse removeCustomer(String selectedUsername) {

        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Utente ["+selectedUsername+"] eliminato con successo.");

        OrderDAO.getInstance().removeAllUserOrders(selectedUsername);
        ArticleDAO.getInstance().removeAllUserFeedbacks(selectedUsername);
        OrderDAO.getInstance().removeAllUserBookings(selectedUsername);
        OrderDAO.getInstance().removeUserHistory(selectedUsername);
        ManagerDAO.getInstance().activateByUsername(selectedUsername);
        //svuoto le liste utente
        //elimino le liste utente
        ArrayList<WishList> wishListArrayList = CustomerDAO.getInstance().getAllCustomerWishLists(selectedUsername);
        for(WishList wishList : wishListArrayList){
            CustomerDAO.getInstance().resetWishlist(wishList.getId());
        }
        CustomerDAO.getInstance().removeAllUserWishlist(selectedUsername);
        CustomerDAO.getInstance().removeCustomer(selectedUsername);
        int row = UserDAO.getInstance().removeByUsername(selectedUsername);
        if(row!=1){
            uploadResponse.setMessage("Errore: utente non eliminato");
            return uploadResponse;
        }
        return uploadResponse;
    }

    public User getByUsername(String username) {
        return updatedUser.getByUsername(username);
    }

    public UploadResponse updateCustomer(Customer updatedUser) {

        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Dati utente aggiornati con successo.");
        uploadResponse.setDone(true);

        if(updatedUser.getName().isBlank() || updatedUser.getSurname().isBlank() || updatedUser.getEmail().isBlank()) {
            uploadResponse.setMessage("Dati non validi");
            uploadResponse.setDone(false);
        }

        if(updatedUser.getBirthYear()==0){
            uploadResponse.setMessage("Anno di nascita non valido.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        if(updatedUser.getPhoneNumber() != 0){
            if(Long.toString(updatedUser.getPhoneNumber()).length() != 10){
                uploadResponse.setMessage("Il numero di telefono non contiene abbastanza caratteri.");
                uploadResponse.setDone(false);
                return uploadResponse;
            }
        }else{
            uploadResponse.setMessage("Il numero di telefono contiene simboli proibiti.");
            uploadResponse.setDone(false);
            return uploadResponse;
        }

        UserDAO.getInstance().updateUserData(updatedUser);
        int row = this.updatedUser.updateCustomerData(updatedUser);
        if(row != 1){
            uploadResponse.setMessage("Errore nel db");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        return uploadResponse;
    }
}
