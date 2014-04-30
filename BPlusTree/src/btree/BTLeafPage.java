package btree;

import diskmgr.Page;
import global.PageId;
import global.RID;

public class BTLeafPage extends BTSortedPage {

	public BTLeafPage(PageId arg0, int arg1) throws ConstructPageException {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public BTLeafPage(Page page, int keyType) {
		super(page, keyType);
	}

	public BTLeafPage(int keyType) throws ConstructPageException {
		super(keyType);
	}

	public KeyDataEntry getCurrent(RID rid) {
		return null;
	}

	public KeyDataEntry getNext(RID rid) {
		return null;
	}

	public KeyDataEntry getFirst(RID rid) {
		return null;
	}

	public RID insertRecord(KeyClass key, RID dataRid) {
		return null;
	}

	public boolean delEntry(KeyDataEntry dEntry) {
		return false;
	}

}
