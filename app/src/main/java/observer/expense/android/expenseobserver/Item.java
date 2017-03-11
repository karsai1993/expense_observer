package observer.expense.android.expenseobserver;

public class Item {
 
    private String title;
    private String description;
    private String expense;
    private String datum;
    private String comment;

    public Item(String title, String description) {
        super();
        this.title = title;
        this.description = description;
    }

    public Item(String expense, String datum, String comment) {
        super();
        this.expense = expense;
        this.datum = datum;
        this.comment = comment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getExpense() {
        return expense;
    }

    public void setExpense(String expense) {
        this.expense = expense;
    }

    public String getDatum() {
        return datum;
    }

    public void setDatum(String datum) {
        this.datum = datum;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}