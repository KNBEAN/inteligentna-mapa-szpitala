package bean.pwr.imskamieskiego.NavigationWindow;

public class StringReciver {

    public StringReciver() {
    }

   
    private String hintText;



    public String getServicePlaceName (boolean servicesButton){
        //true - toalety
        //false - restauracje
        String ServicePlaceName = null;

        if(servicesButton)     //servicesButton == true
            ServicePlaceName = "Toaleta (najbliżej)";
        else
            ServicePlaceName = "Restauracja (najbliżej)";


        return ServicePlaceName;
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
