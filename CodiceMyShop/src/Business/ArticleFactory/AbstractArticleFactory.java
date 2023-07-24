package Business.ArticleFactory;

import Model.Article;
import Model.Vendor;

public interface AbstractArticleFactory {
    Article create();
    Vendor createVendor();
}
