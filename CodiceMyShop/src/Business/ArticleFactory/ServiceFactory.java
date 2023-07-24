package Business.ArticleFactory;

import Business.ArticleFactory.AbstractArticleFactory;
import Model.*;

public class ServiceFactory implements AbstractArticleFactory {
    @Override
    public Article create() {
        return new Service();
    }

    @Override
    public Vendor createVendor() {
        return new ServiceVendor();
    }
}
