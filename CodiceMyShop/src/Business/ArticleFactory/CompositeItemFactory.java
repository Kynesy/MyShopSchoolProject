package Business.ArticleFactory;

import Model.*;

public class CompositeItemFactory implements AbstractArticleFactory {
    @Override
    public Article create() {
        return new CompositeItem();
    }

    @Override
    public Vendor createVendor() {
        return new ItemVendor();
    }
}
