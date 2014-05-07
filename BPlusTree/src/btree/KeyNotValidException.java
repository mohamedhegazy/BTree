package btree;

import chainexception.ChainException;

@SuppressWarnings("serial")
public class KeyNotValidException extends ChainException {
	public KeyNotValidException(Exception e,String name) {
		super(e, name);
	}
}
