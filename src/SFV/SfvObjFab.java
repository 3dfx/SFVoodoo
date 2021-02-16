package SFV;

/*
 * Created on 02.08.2006
 */
/**
 * @author 3dfx
 */

public class SfvObjFab {
	public static ICreate get(String type) {
		ICreate result = null;
		if (type.equalsIgnoreCase("create")) {
			result = new CreateSfv();
		}
		else if (type.equalsIgnoreCase("folders")) {
			result = new CreateSfvFolders();
		}
		else if (type.equalsIgnoreCase("rename")) {
			result = new CreateRename();
		}

		return result;
	}
}
