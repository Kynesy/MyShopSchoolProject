package Business.TableFactory;

import Business.ArticleBusiness;
import Business.ArticleFactory.ArticleFactoryProvider;
import Business.TableFactory.TableArticleType.*;
import Model.Item;
import Model.Service;

import javax.swing.*;
import java.util.ArrayList;

public class TableArticleFactory {
    public enum TableTypeArticles {USER_TYPE, MANAGER_TYPE, ADMIN_TYPE, WISHLIST_TYPE, GUEST_TYPE}
    public JTable create(TableTypeArticles tableTypeArticles, String[][] data){
        switch (tableTypeArticles){
            case USER_TYPE:{
                return new TableTypeUser(data);
            }

            case ADMIN_TYPE:{
                return new TableTypeAdmin(data);
            }

            case MANAGER_TYPE:{
                return new TableTypeManager(data);
            }
            case GUEST_TYPE:{
                return new TableTypeGuest(data);
            }

            case WISHLIST_TYPE:{
                return new TableTypeWishlist(data);
            }

//            case COMPOSITE_TYPE:{
//                return new TableTypeComposite(data);
//            }
        }
        return null;
    }
//
//    public String[][] getData(TableTypeArticles tableTypeArticles, ArrayList<ItemInterface> items) {
//
//    }
    
    public String[][] getData (TableTypeArticles tableTypeArticles, ArrayList<Item> items, ArrayList<Service> services){
        String [][] result;
        switch (tableTypeArticles){
            case WISHLIST_TYPE:{
                result = new String[items.size() + services.size()][6];
                for(int i=0; i<items.size(); i++){
                    result[i][0] = "" + items.get(i).getId();
                    result[i][1] = items.get(i).getName();
                    result[i][2] = "" + items.get(i).getPrice() + " Euro";
                    result[i][3] = items.get(i).getItemVendor().getName();
                    result[i][4] = "" + items.get(i).getWishlistQuantity();
                    if(ArticleBusiness.getInstance().getArticleType(items.get(i).getId())== ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){
                        result[i][5] = "Prodotto Composito";
                    } else {
                        result[i][5] = "Prodotto";
                    }
                }

                for(int i = items.size(); i< items.size()+services.size(); i++){
                    int n = i - items.size();
                    result[i][0] = "" + services.get(n).getId();
                    result[i][1] = services.get(n).getName();
                    result[i][2] = "" + services.get(n).getPrice() + " Euro";
                    result[i][3] = services.get(n).getVendorName();
                    result[i][4] = "Disponibile";
                    result[i][5] = "Servizio";
                }
                break;
            }

            case USER_TYPE:{
                result = new String[items.size() + services.size()][6];
                for(int i=0; i<items.size(); i++){
                    result[i][0] = "" + items.get(i).getId();
                    result[i][1] = items.get(i).getName();
                    result[i][2] = "" + items.get(i).getPrice() + " Euro";
                    result[i][3] = items.get(i).getItemVendor().getName();
                    int avaiability = items.get(i).getAvaiability();
                    if(avaiability > 0){
                        result[i][4] = "" + avaiability;
                    }else {
                        result[i][4] = "Esaurito";
                    }
                    if(ArticleBusiness.getInstance().getArticleType(items.get(i).getId())== ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){
                        result[i][5] = "Prodotto Composito";
                    } else {
                        result[i][5] = "Prodotto";
                    }
                }

                for(int i = items.size(); i< items.size()+services.size(); i++){
                    int n = i - items.size();
                    result[i][0] = "" + services.get(n).getId();
                    result[i][1] = services.get(n).getName();
                    result[i][2] = "" + services.get(n).getPrice() + " Euro";
                    result[i][3] = services.get(n).getVendorName();
                    result[i][4] = "Disponibile";
                    result[i][5] = "Servizio";
                }
                break;
            }

            case ADMIN_TYPE:{
                result = new String[items.size() + services.size()][6];
                for(int i=0; i<items.size(); i++){
                    result[i][0] = "" + items.get(i).getId();
                    result[i][1] = items.get(i).getName();
                    result[i][2] = "" + items.get(i).getPrice() + " Euro";
                    result[i][3] = items.get(i).getItemVendor().getName();
                    result[i][4] = items.get(i).getCityStore();
                    if(ArticleBusiness.getInstance().getArticleType(items.get(i).getId())== ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){
                        result[i][5] = "Prodotto Composito";
                    } else {
                        result[i][5] = "Prodotto";
                    }
                }

                for(int i = items.size(); i< items.size()+services.size(); i++){
                    int n = i - items.size();
                    result[i][0] = "" + services.get(n).getId();
                    result[i][1] = services.get(n).getName();
                    result[i][2] = "" + services.get(n).getPrice() + " Euro";
                    result[i][3] = services.get(n).getVendorName();
                    result[i][4] = "Tutti i punti vendita";
                    result[i][5] = "Servizio";
                }
                break;
            }

            case MANAGER_TYPE:{
                result = new String[items.size() + services.size()][5];
                for(int i=0; i<items.size(); i++){
                    result[i][0] = "" + items.get(i).getId();
                    result[i][1] = items.get(i).getName();
                    result[i][2] = "" + items.get(i).getPrice() + " Euro";
                    int avaiability = items.get(i).getAvaiability();
                    if(avaiability > 0){
                        result[i][3] = "" + avaiability;
                    }else {
                        result[i][3] = "Esaurito";
                    }
                    if(ArticleBusiness.getInstance().getArticleType(items.get(i).getId())== ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){
                        result[i][4] = "Prodotto Composito";
                    } else {
                        result[i][4] = "Prodotto";
                    }
                }

                for(int i = items.size(); i< items.size()+services.size(); i++){
                    int n = i - items.size();
                    result[i][0] = "" + services.get(n).getId();
                    result[i][1] = services.get(n).getName();
                    result[i][2] = "" + services.get(n).getPrice() + " Euro";
                    result[i][3] = "Disponibile";
                    result[i][4] = "Servizio";
                }
                break;
            }

            case GUEST_TYPE: {
                result = new String[items.size() + services.size()][5];
                for (int i = 0; i < items.size(); i++) {
                    result[i][0] = "" + items.get(i).getId();
                    result[i][1] = items.get(i).getName();
                    result[i][2] = "" + items.get(i).getPrice() + " Euro";
                    result[i][3] = items.get(i).getCityStore();
                    if(ArticleBusiness.getInstance().getArticleType(items.get(i).getId())== ArticleFactoryProvider.ArticleType.COMPOSITE_ITEM){
                        result[i][4] = "Prodotto Composito";
                    } else {
                        result[i][4] = "Prodotto";
                    }
                }

                for (int i = items.size(); i < items.size() + services.size(); i++) {
                    int n = i - items.size();
                    result[i][0] = "" + services.get(n).getId();
                    result[i][1] = services.get(n).getName();
                    result[i][2] = "" + services.get(n).getPrice() + " Euro";
                    result[i][3] = "Tutti i punti vendita";
                    result[i][4] = "Servizio";
                }
                break;
            }

            default:{
                throw new IllegalStateException("Unexpected value: " + tableTypeArticles);
            }
        }
        return result;
    }
}
