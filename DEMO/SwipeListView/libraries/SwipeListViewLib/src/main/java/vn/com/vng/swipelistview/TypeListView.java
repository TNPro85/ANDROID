package vn.com.vng.swipelistview;

public enum TypeListView {
    EMAIL(0),
    SMS(1);

    private int value ;

    TypeListView(int value) {
        this.value = value ;
    }

    public int getValue(){
        return value;
    }
    
}
