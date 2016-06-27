package org.t_robop.y_ogawara.tancolle;

public class ListItem {

    String name;
    String kana;
    String image;

    int  itemId;

    ListItem(String name, String kana) {
        this.name = name;
        this.kana = kana;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }
    public int getItemId(){
        return itemId;
    }

    public void setSmallImage(String image){
        this.image = image;
    }
    public String getImage(){
        return image;
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


