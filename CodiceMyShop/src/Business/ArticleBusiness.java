package Business;

import Business.ArticleFactory.*;
import Business.TableFactory.TableArticleFactory;
import DAO.*;
import Model.*;

import javax.swing.*;
import java.util.ArrayList;

public class ArticleBusiness {
    private static ArticleBusiness instance = new ArticleBusiness();

    private ArticleBusiness(){ }

    public static ArticleBusiness getInstance(){
        return instance;
    }

    public JTable getArticleTable(User user){
        TableArticleFactory tableArticleFactory = new TableArticleFactory();
        ArrayList<Service> services = ServiceDAO.getInstance().getAllServices();
        ArrayList<Item> items;
        String[][] data;
        JTable jTable = null;
        UserBusiness.UserPrivilege privilege = UserBusiness.getInstance().getUserPrivilege(user);

        if(privilege == null){
            items = ItemBusiness.getInstance().getAllItemsByStore(null);
            data = tableArticleFactory.getData(TableArticleFactory.TableTypeArticles.GUEST_TYPE, items,services);
            jTable = tableArticleFactory.create(TableArticleFactory.TableTypeArticles.GUEST_TYPE, data);
        } else {
            switch (privilege){
                case USER:{
                    Customer c = CustomerDAO.getInstance().getByUsername(user.getUsername());
                    items = ItemBusiness.getInstance().getAllItemsByStore(c.getStoreCity());
                    data = tableArticleFactory.getData(TableArticleFactory.TableTypeArticles.USER_TYPE, items,services);
                    jTable = tableArticleFactory.create(TableArticleFactory.TableTypeArticles.USER_TYPE, data);
                    break;
                }

                case MANAGER:{
                    Manager m = AdministratorDAO.getInstance().getManagerByUsername(user.getUsername());
                    items = ItemBusiness.getInstance().getAllItemsByStore(m.getStoreCity());
                    data = tableArticleFactory.getData(TableArticleFactory.TableTypeArticles.MANAGER_TYPE, items,services);
                    jTable = tableArticleFactory.create(TableArticleFactory.TableTypeArticles.MANAGER_TYPE, data);
                    break;
                }

                case ADMINISTRATOR:{
                    items = ItemBusiness.getInstance().getAllItemsByStore(null);
                    data = tableArticleFactory.getData(TableArticleFactory.TableTypeArticles.ADMIN_TYPE, items,services);
                    jTable = tableArticleFactory.create(TableArticleFactory.TableTypeArticles.ADMIN_TYPE, data);
                    break;
                }
            }
        }
        return jTable;
    }

    public ArticleFactoryProvider.ArticleType getArticleType(int id){
        boolean itemExist = ItemDAO.getInstance().itemExist(id);
        if(itemExist){
            if(CompositeDAO.getIstance().isComposite(id)){
                return ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM;
            }
            return ArticleFactoryProvider.ArticleType.ITEM;
        }else {
            return ArticleFactoryProvider.ArticleType.SERVICE;
        }
    }

    public UploadResponse createFeedback(Feedback feedback, int articleId) {
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Recensione pubblicata con successo.");
        uploadResponse.setDone(true);
        if(feedback.getMessage().length() >150){
            uploadResponse.setDone(false);
            uploadResponse.setMessage("Errore: Recensione troppo lunga.");
            return uploadResponse;
        }

        int row = ArticleDAO.getInstance().createFeedback(feedback.getIndex(), feedback.getMessage(), articleId, feedback.getUsername());
        if(row!=1){
            uploadResponse.setMessage("Errore nella pubblicazione");
            uploadResponse.setDone(false);
            return uploadResponse;
        }
        return uploadResponse;
    }

    public ArrayList<Feedback> getAllFeedbacksByID(int articleID) {
        return ArticleDAO.getInstance().getAllFeedbackByArticleId(articleID);
    }

    public UploadResponse deleteArticle(int articleID){
        UploadResponse uploadResponse;
        if (articleID==-1){
            uploadResponse = new UploadResponse();
            uploadResponse.setMessage("Nessun articolo selezionato.");
            return uploadResponse;
        }
        switch (getArticleType(articleID)){
            case ITEM:{
                uploadResponse = ItemBusiness.getInstance().deleteItem(articleID);
                return uploadResponse;
            }
            case SERVICE: {
                uploadResponse = ServiceBusiness.getInstance().deleteService(articleID);
                return uploadResponse;
            }
            case COMPOSITE_ITEM:{
                CompositeDAO.getIstance().deleteComposite(articleID);
                uploadResponse = ItemBusiness.getInstance().deleteItem(articleID);
                return uploadResponse;
            }
        }
        return null;
    }


    public UploadResponse addCategory(Category category){
        UploadResponse uploadResponse = new UploadResponse();
        uploadResponse.setMessage("Categoria correttamente creata.");

        if(ArticleDAO.getInstance().categoryExist(category)){
            uploadResponse.setMessage("La categoria esiste già.");
            return uploadResponse;
        }

        int parentId = category.getParentId();
        int row;
        if(parentId == -1){ //per convenzione il -1 significa nessuna categoria padre
            row = ArticleDAO.getInstance().addCategory(category);
        }else {
            row = ArticleDAO.getInstance().addCategory(category, parentId);
        }

        if(row==0){
            uploadResponse.setMessage("Creazione categoria interrotta: errore sconosciuto");
        }

        return uploadResponse;
    }


    public Category getCategory(int itemID){
        Category category = ArticleDAO.getInstance().getCategoryByID(itemID);
        category.setSottocategorie(ArticleDAO.getInstance().getSubCategory(category.getId()));
        category.setCategoryTree(getCategoryTree(category.getId()));
        return category;
    }

    public ArrayList<Category> getCategoryTree(int categoryID){
        ArrayList<Category> categoryTree = new ArrayList<>();
        int parentID = ArticleDAO.getInstance().getParentId(categoryID);
        while (parentID!=-1 && parentID!=0){
            Category tmpCategory = ArticleDAO.getInstance().getCategory(parentID);
            tmpCategory.setSottocategorie(ArticleDAO.getInstance().getSubCategory(parentID));
            categoryTree.add(tmpCategory);
            parentID = tmpCategory.getParentId();
        }
        return categoryTree;
    }

    public int removeCategory(Category category){
        //prendo l'id e vedo le sottocategorie
        //a. non ha sottocategorie -> elimina
        //b. ha sottocategorie -> per ogni categoria richiamo la funzione

        ArticleDAO articleDAO = ArticleDAO.getInstance();
        int id = category.getId();
        ArrayList<Category> categoryArrayList = articleDAO.getSubCategory(id);
        if(categoryArrayList == null){
            return articleDAO.removeCategory(id);
        }else{
            for(Category cat : categoryArrayList){
                int i = removeCategory(cat);
                if(i != 1){
                    return 0;
                }
            }
            return articleDAO.removeCategory(id);
        }
    }

    public Category[] getAllCategories(){
        ArrayList<Category> c = ArticleDAO.getInstance().getAllCategories();
        return c.toArray(Category[]::new);
    }

}
