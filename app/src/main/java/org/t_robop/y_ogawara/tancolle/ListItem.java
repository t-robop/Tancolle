package org.t_robop.y_ogawara.tancolle;

public class ListItem {

    //TODO privateにしないと意味が無い
    private String name;
    private String kana;

    private String smallImage;

   private int  itemId;

    //TODO これが要らない、こいつのせいで型がって言われてた
//    ListItem(String name, String kana) {
//        this.name = name;
//        this.kana = kana;
//    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId(){
        return itemId;
    }


    public String getSmallImage() {
        return smallImage;
    }

    public void setSmallImage(String smallImage) {
        this.smallImage = smallImage;
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


