package SFV;

/*
 * Created on 02.08.2006
 */
/**
 * @author 3dfx
 */

public class SfvObjFab {
    public static ICreate get(String type) {
        if (type.equalsIgnoreCase("create")) {
	        return new CreateSfv();
	    }
	    else if (type.equalsIgnoreCase("folders")) {
	        return new CreateSfvFolders();
	    }
	    else if (type.equalsIgnoreCase("rename")) {
	        return new CreateRename();
	    }
	    
        return null;
    }
}
