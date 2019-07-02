package wvustat.data;

/**
* updatable is an interface very similar to Observer except that its
* update method needs a string parameter
*/

public interface updatable{

	/**
	*  update
	*
	* @param identifier tells what what changes have been made
	*/
	public void update(String identifier);
	
}
