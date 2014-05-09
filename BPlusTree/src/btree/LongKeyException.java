package btree;

import chainexception.ChainException;

@SuppressWarnings("serial")
public class LongKeyException extends ChainException {
	public LongKeyException(Exception e,String name) {
		// TODO Auto-generated constructor stub
		super(e, name);
	}
}
