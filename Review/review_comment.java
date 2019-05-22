public class review_comment{

    private String commentName;
    private String iconName;
    private String comment;

    public review_comment(String commentName, String iconName, String comment) {
        this.commentName= commentName;
        this.iconName= iconName;
        this.comment= comment;
    }

    public String getComment() {
        return comment;
    }

    public void setPopulation(String comment) {
        this.comment = comment;
    }

    public String getCommentName() {
        return commentName;
    }

    public void setCountryName(String commentName) {
        this.commentName = commentName;
    }

    public String getIconName() {
        return iconName;
    }

    public void setFlagName(String iconName) {
        this.iconName = iconName;
    }
}