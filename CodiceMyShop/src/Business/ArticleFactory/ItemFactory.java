package Business.ArticleFactory;

import Business.ArticleFactory.AbstractArticleFactory;
import Model.Article;
import Model.Item;
import Model.ItemVendor;
import Model.Vendor;

public class ItemFactory implements AbstractArticleFactory {
    @Override
    public Article create() {
        return new Item();
    }

    @Override
    public Vendor createVendor() {
        return new ItemVendor();
    }
}
