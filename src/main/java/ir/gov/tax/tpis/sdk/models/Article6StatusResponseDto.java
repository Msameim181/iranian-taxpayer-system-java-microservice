package ir.gov.tax.tpis.sdk.models;

public class Article6StatusResponseDto {
    private Boolean article6RemainStatus;

    public Boolean getArticle6RemainStatus() {
        return article6RemainStatus;
    }

    public void setArticle6RemainStatus(final Boolean article6RemainStatus) {
        this.article6RemainStatus = article6RemainStatus;
    }

    @Override
    public String toString() {
        return "Article6StatusResponseDto{" +
               "article6RemainStatus=" + article6RemainStatus +
               '}';
    }
}

