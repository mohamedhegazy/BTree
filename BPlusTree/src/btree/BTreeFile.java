package btree;

import global.RID;

public class BTreeFile extends IndexFile {
	
	public BTreeFile(String string, int keyType, int i, int j) {
		// TODO Auto-generated constructor stub
	}

	public BTreeFile(String string) {
		// TODO Auto-generated constructor stub
	}

	public BTreeHeaderPage getHeaderPage() throws ConstructPageException {
		// TODO Auto-generated method stub
		return new BTreeHeaderPage(0);
	}

	public void insert(IntegerKey integerKey, RID rid) {
		// TODO Auto-generated method stub
		
	}

	public void Delete(IntegerKey integerKey, RID rid) {
		// TODO Auto-generated method stub
		
	}

	public void close() {
		// TODO Auto-generated method stub
		
	}

	public BTFileScan new_scan(KeyClass lowkey, KeyClass hikey) {
		// TODO Auto-generated method stub
		return null;
	}

	public void destroyFile() {
		// TODO Auto-generated method stub
		
	}

	public void traceFilename(String string) {
		// TODO Auto-generated method stub
		
	}

	public void insert(KeyClass key, RID rid) {
		// TODO Auto-generated method stub
		
	}

	public boolean Delete(KeyClass key, RID rid) {
		// TODO Auto-generated method stub
		return false;
	}
}
