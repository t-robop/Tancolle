package org.t_robop.y_ogawara.tancolle;

public class ListItem {

    private String name;
    private String kana;

    private int itemId;


    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public int getId(){
        return itemId;
    }


    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setKana(String kana) {
        this.kana = kana;
    }

    public String getKana() {
        return kana;
    }
}
