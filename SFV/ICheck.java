package SFV;
import java.io.FileNotFoundException;
import java.io.IOException;

/*
 * Created on 21.09.2005
 */
/**
 * @author 3dfx
 */

public interface ICheck {
    public int check() throws FileNotFoundException, IOException;
}