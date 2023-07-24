package Business.TableFactory;

import Business.TableFactory.TableArticleType.*;
import Business.TableFactory.TableUsersType.TableManager;
import Business.TableFactory.TableUsersType.TableUser;
import Model.Manager;
import Model.User;

import javax.swing.*;
import java.util.ArrayList;

public class TableUsersFactory {
    public enum TableTypeUser {USER_TYPE, MANAGER_TYPE}
    public JTable create(TableTypeUser tableTypeUser, String[][] data){
        switch (tableTypeUser){
            case USER_TYPE:{
                return new TableUser(data);
            }

            case MANAGER_TYPE:{
                return new TableManager(data);
            }
        }
        return null;
    }

    public String[][] getData (TableTypeUser tableTypeUser, ArrayList<User> users){
        String [][] result;
        switch (tableTypeUser){
            case USER_TYPE:{
                result = new String[users.size()][4];
                for(int i=0; i<users.size(); i++){
                    result[i][0] = users.get(i).getUsername();
                    result[i][1] = users.get(i).getName();
                    result[i][2] = users.get(i).getEmail();
                    int disabled = users.get(i).isDisabled();
                    if(disabled==1){
                        result[i][3] = "NO";
                    }else
                    {
                        result[i][3] = "SI";
                    }
                }
                break;
            }

            case MANAGER_TYPE:{
                result = new String[users.size()][4];
                for(int i=0; i<users.size(); i++){
                    Manager manager = (Manager) users.get(i);
                    result[i][0] = manager.getUsername();
                    result[i][1] = manager.getName();
                    int disabled = manager.isDisabled();
                    if(disabled==1){
                        result[i][2] = "NO";
                    }else {
                        result[i][2] = "SI";
                    }
                   result[i][3] = manager.getStoreCity();
                }
                break;
            }

            default:{
                throw new IllegalStateException("Unexpected value: " + tableTypeUser);
            }
        }
        return result;
    }
}
