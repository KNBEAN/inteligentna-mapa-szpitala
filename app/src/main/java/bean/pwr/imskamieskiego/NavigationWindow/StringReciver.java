package bean.pwr.imskamieskiego.NavigationWindow;

public class StringReciver {

    private String hintText;

    public StringReciver() {
    }

    public void StartOrDest(boolean logical){

        if(logical)
            hintText = "Wpisz miejsce docelowe";
        else
            hintText = "Wpisz miejsce startowe";

    }


    public String getHintText() {
        return hintText;
    }


}
