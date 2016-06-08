package ua.sumy.sttp.diplom;

/**
 * Created by Слава on 08.06.2016.
 */
public class CodingToWindows1251 {
    public static String coding (String in){
        if(in.contains("А")){
            in = in.replace("A","%C0");
        }
        if(in.contains("Х")){
            in = in.replace("Х","%D5");
        }
        if(in.contains("П")){
            in = in.replace("П","%CF");
        }
        if(in.contains("Ц")){
            in = in.replace("Ц","%D6");
        }
        if(in.contains("Е")){
            in = in.replace("Е","%C5");
        }
        return in;
    }
}
