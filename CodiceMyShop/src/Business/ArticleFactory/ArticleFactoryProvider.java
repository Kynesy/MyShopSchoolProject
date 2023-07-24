package Business.ArticleFactory;

public class ArticleFactoryProvider {
    public enum ArticleType {ITEM, SERVICE, COMPOSITE_ITEM}

    public static AbstractArticleFactory getArticleFactory(ArticleType articleType){
        switch (articleType){
            case ITEM :{
                return new ItemFactory();
            }

            case SERVICE:{
                return new ServiceFactory();
            }

            case COMPOSITE_ITEM:{
                return new CompositeItemFactory();
            }
        }
        return null;
    }
}
