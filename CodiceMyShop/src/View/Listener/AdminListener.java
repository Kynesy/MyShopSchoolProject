package View.Listener;

import Business.*;
import Business.ArticleFactory.AbstractArticleFactory;
import Business.ArticleFactory.ArticleFactoryProvider;
import Model.*;
import View.*;
import View.AddArticlePanel.*;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AdminListener implements ActionListener {
    private IStandardFrame frame;
    public enum CommandKeyAdmin { CREATE_ITEM, CREATE_SERVICE, CREATE_CATEGORY, //per gli articoli
         CREATE_VENDOR, CREATE_STORE, // per i produttori
         ADD_MANAGER, CREATE_COMPOSITE, MODIFY_ITEM, MODIFY_SERVICE, MODIFY_COMPOSITE, ADD_ADMIN //generici

    }

    public AdminListener(IStandardFrame frame){
        this.frame = frame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        CommandKeyAdmin cmd = CommandKeyAdmin.valueOf(e.getActionCommand());
        System.out.println("Azionato comando:" + cmd);

        switch (cmd){
            case CREATE_ITEM:{
                AddArticlePanel panel = (AddArticlePanel) frame.getMainPanel();
                AddItemSubpanel itemPanel = (AddItemSubpanel) panel.getMainPanel();

                Item item = new Item();
                item.setName(itemPanel.getTxtName());
                item.setPrice(itemPanel.getTxtPrice());
                item.setDescription(itemPanel.getTxtDescription());
                item.setItemVendor(itemPanel.getTxtItemVendor());
                item.setCityStore(itemPanel.getTxtStoreCity());
                item.setPosition(itemPanel.getTxtLine(), itemPanel.getTxtShelf());
                item.setFotos(itemPanel.getFotos());
                Category category = new Category();
                category.setId(itemPanel.getCategoryID());
                item.setCategory(category);

                UploadResponse uploadResponse = ItemBusiness.getInstance().createItem(item);

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                if(uploadResponse.isDone()){
                    itemPanel.resetFields();
                }
                break;
            }

            case MODIFY_ITEM:{
                ModifyItemPanel itemPanel = (ModifyItemPanel) frame.getMainPanel();

                Item item = new Item();
                item.setName(itemPanel.getTxtName());
                item.setPrice(itemPanel.getTxtPrice());
                item.setDescription(itemPanel.getTxtDescription());
                item.setItemVendor(itemPanel.getTxtItemVendor());
                item.setCityStore(itemPanel.getTxtStoreCity());
                item.setPosition(itemPanel.getTxtLine(), itemPanel.getTxtShelf());
                item.setFotos(itemPanel.getFotos());
                Category category = new Category();
                category.setId(itemPanel.getCategoryID());
                item.setCategory(category);
                item.setId(itemPanel.getItemID());

                UploadResponse uploadResponse = ItemBusiness.getInstance().modifyItem(item);

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case CREATE_COMPOSITE:{
                AddArticlePanel panel = (AddArticlePanel) frame.getMainPanel();
                AddCompositeSubpanel itemPanel = (AddCompositeSubpanel) panel.getMainPanel();

                CompositeItem compositeItem = new CompositeItem();
                compositeItem.setName(itemPanel.getTxtName());
                compositeItem.setDescription(itemPanel.getTxtDescription());
                compositeItem.setItemVendor(itemPanel.getTxtItemVendor());
                compositeItem.setCityStore(itemPanel.getTxtStoreCity());
                compositeItem.setPosition(itemPanel.getTxtLine(), itemPanel.getTxtShelf());
                compositeItem.setFotos(itemPanel.getFotos());
                Category category = new Category();
                category.setId(itemPanel.getCategoryID());
                compositeItem.setCategory(category);
                compositeItem.setSubItemList(itemPanel.getSubitemsList());

                UploadResponse uploadResponse = CompositeBusiness.getInstance().uploadCompositeItemlist(compositeItem);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                if(uploadResponse.isDone()){
                    itemPanel.resetFields();
                }
                break;
            }

            case MODIFY_COMPOSITE:{
                ModifyComposite modifyCompositePanel = (ModifyComposite) frame.getMainPanel();

                CompositeItem compositeItem = new CompositeItem();
                compositeItem.setName(modifyCompositePanel.getTxtName());
                compositeItem.setDescription(modifyCompositePanel.getTxtDescription());
                compositeItem.setItemVendor(modifyCompositePanel.getTxtItemVendor());
                compositeItem.setCityStore(modifyCompositePanel.getTxtStoreCity());
                compositeItem.setPosition(modifyCompositePanel.getTxtLine(), modifyCompositePanel.getTxtShelf());
                compositeItem.setFotos(modifyCompositePanel.getFotos());
                Category category = new Category();
                category.setId(modifyCompositePanel.getCategoryID());
                compositeItem.setCategory(category);
                compositeItem.setSubItemList(modifyCompositePanel.getSubitemsList());
                compositeItem.setId(modifyCompositePanel.getCompositeID());

                UploadResponse uploadResponse = CompositeBusiness.getInstance().modifyComposite(compositeItem);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case CREATE_SERVICE:{
                AddArticlePanel panel = (AddArticlePanel) frame.getMainPanel();
                AddServiceSubpanel servicePanel = (AddServiceSubpanel) panel.getMainPanel();

                Service service = new Service();
                service.setName(servicePanel.getTxtName());
                service.setDescription(servicePanel.getTxtDescription());
                service.setPrice(servicePanel.getTxtPrice());
                service.setVendorName(servicePanel.getTxtServiceVendor());
                Category category = new Category();
                category.setId(servicePanel.getCategoryID());
                service.setCategory(category);
                service.setFotos(servicePanel.getFotos());

                UploadResponse uploadResponse = ServiceBusiness.getInstance().createService(service);

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Conferma inserimento", JOptionPane.INFORMATION_MESSAGE);
                if(uploadResponse.isDone()){
                    servicePanel.resetFields();
                }
                break;
            }

            case MODIFY_SERVICE:{
                ModifyServicePanel servicePanel = (ModifyServicePanel) frame.getMainPanel();

                Service service = new Service();
                service.setName(servicePanel.getTxtName());
                service.setDescription(servicePanel.getTxtDescription());
                service.setPrice(servicePanel.getTxtPrice());
                service.setVendorName(servicePanel.getTxtServiceVendor());
                service.setFotos(servicePanel.getFotos());
                service.setId(servicePanel.getServiceID());
                Category category = new Category();
                category.setId(servicePanel.getCategoryID());
                service.setCategory(category);

                UploadResponse uploadResponse = ServiceBusiness.getInstance().modifyService(service);

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Conferma inserimento", JOptionPane.INFORMATION_MESSAGE);
                break;
            }

            case CREATE_CATEGORY:{
                AddCategoryPanel addCategoryPanel = (AddCategoryPanel) frame.getMainPanel();
                Category category = new Category();
                category.setName(addCategoryPanel.getCategoryName());
                category.setParentId(addCategoryPanel.getParentID());
                UploadResponse uploadResponse = ArticleBusiness.getInstance().addCategory(category);

                JOptionPane.showMessageDialog(null,uploadResponse.getMessage(), "Info",
                            JOptionPane.INFORMATION_MESSAGE);

                Category[] c = ArticleBusiness.getInstance().getAllCategories();
                AddCategoryPanel updatedCategoriesPanel = new AddCategoryPanel(this, c);
                frame.setMainPanel(updatedCategoriesPanel);
                break;
            }

            case CREATE_VENDOR:{
                AddVendorPanel addVendorPanel = (AddVendorPanel) frame.getMainPanel();
                AddVendorPanel.ProducerType producerType = addVendorPanel.getSelection();
                AbstractArticleFactory factory;
                Vendor vendor;
                if(AddVendorPanel.ProducerType.ITEM_PRODUCER.equals(producerType)){
                    factory = ArticleFactoryProvider.getArticleFactory(ArticleFactoryProvider.ArticleType.ITEM);
                } else {
                    factory = ArticleFactoryProvider.getArticleFactory(ArticleFactoryProvider.ArticleType.SERVICE);
                }
                vendor = factory.createVendor();

                vendor.setCity(addVendorPanel.getCity());
                vendor.setName(addVendorPanel.getName());
                vendor.setNation(addVendorPanel.getNation());
                vendor.setWebsite(addVendorPanel.getWebsite());

                UploadResponse uploadResponse;
                if(AddVendorPanel.ProducerType.ITEM_PRODUCER.equals(producerType)){
                    uploadResponse = ItemBusiness.getInstance().createItemProducer((ItemVendor) vendor);
                } else {
                    uploadResponse = ServiceBusiness.getInstance().createServiceVendor((ServiceVendor) vendor);
                }

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                addVendorPanel.resetFields();
                break;
            }

            case CREATE_STORE:{
                AddStorePanel addStorePanel = (AddStorePanel) frame.getMainPanel();
                Store store = new Store();
                store.setCity(addStorePanel.getStoreCity());
                UploadResponse uploadResponse = AdministratorBusiness.getInstance().createStore(store);
                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                addStorePanel.resetFields();
                break;
            }

            case ADD_MANAGER:{
                AddManagerSubpanel panel = (AddManagerSubpanel) frame.getMainPanel();

                if(!panel.comparePasswords()){
                    JOptionPane.showMessageDialog(null, "Le password non corrispondono", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Manager manager = new Manager();
                manager.setUsername(panel.getTxtUsername());
                manager.setPassword(panel.getTxtPassword1());
                manager.setStoreCity(panel.getTxtStore());
                manager.setName(panel.getTxtName());
                manager.setSurname(panel.getTxtSurname());

                UploadResponse uploadResponse = AdministratorBusiness.getInstance().createManager(manager);

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                panel.resetFields();
                break;
            }

            case ADD_ADMIN:{
                AddAdminSubpanel panel = (AddAdminSubpanel) frame.getMainPanel();

                if(!panel.comparePasswords()){
                    JOptionPane.showMessageDialog(null, "Le password non corrispondono", "Errore", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Administrator administrator = new Administrator();
                administrator.setUsername(panel.getTxtUsername());
                administrator.setPassword(panel.getTxtPassword1());

                UploadResponse uploadResponse = AdministratorBusiness.getInstance().createAdmin(administrator);

                JOptionPane.showMessageDialog(null, uploadResponse.getMessage(), "Informazione", JOptionPane.INFORMATION_MESSAGE);
                panel.resetFields();
                break;
            }

        }

        ((JFrame) frame).invalidate();
        ((JFrame) frame).revalidate();
        ((JFrame) frame).repaint();
    }
}
