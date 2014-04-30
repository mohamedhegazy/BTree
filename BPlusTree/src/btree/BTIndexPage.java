package btree;

import global.PageId;
import global.RID;

public class BTIndexPage extends BTSortedPage {

	public BTIndexPage(PageId arg0, int arg1) throws Exception {
		super(arg0, arg1);
		// TODO Auto-generated constructor stub
	}

	public RID insertKey(KeyClass key, PageId pageNo) {
		return null;

	}

	public PageId getPageNoByKey(KeyClass key) {
		return curPage;

	}

	public KeyDataEntry getNext(RID rid) {
		return null;

	}

	public PageId getLeftLink() {
		return curPage;

	}
	 public void setLeftLink(PageId left)
	 {
		 
	 }

}
