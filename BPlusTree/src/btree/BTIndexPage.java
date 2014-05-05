package btree;

import java.io.IOException;

import diskmgr.Page;
import global.PageId;
import global.RID;

public class BTIndexPage extends BTSortedPage {

	public BTIndexPage(PageId arg0, int arg1) throws Exception {
		super(arg0, arg1);
		setType(NodeType.INDEX);// set in the HFPage
		// TODO Auto-generated constructor stub
	}

	public BTIndexPage(Page page, int keyType) throws IOException {
		super(page, keyType);
		setType(NodeType.INDEX);// set in the HFPage
	}

	public BTIndexPage(int keyType) throws ConstructPageException, IOException {
		super(keyType);
		setType(NodeType.INDEX);// set in the HFPage
	}

	public RID insertKey(KeyClass key, PageId pageNo) throws InsertRecException {
		return insertRecord(new KeyDataEntry(key, pageNo));
	}

	public PageId getPageNoByKey(KeyClass key) throws IOException,
			KeyNotMatchException, NodeNotMatchException, ConvertException {
		KeyDataEntry entry;
		for (int i = getSlotCnt() - 1; i >= 0; i++) { // start from last slot in
														// byte array and check
														// the keys
			entry = BT.getEntryFromBytes(getpage(), getSlotOffset(i),
					getSlotLength(i), keyType, NodeType.INDEX);
			if (BT.keyCompare(key, entry.key) >= 0) {
				return ((IndexData) entry.data).getData();
			}
		}
		return getPrevPage();
	}

	public KeyDataEntry getNext(RID rid) throws IOException,
			KeyNotMatchException, NodeNotMatchException, ConvertException {
		rid.pageNo = getCurPage();
		rid.slotNo++;
		if (rid.slotNo < getSlotCnt()) {
			return BT.getEntryFromBytes(getpage(), getSlotOffset(rid.slotNo),
					getSlotLength(rid.slotNo), keyType, NodeType.INDEX);
		}
		return null;

	}

	public KeyDataEntry getFirst(RID rid) throws IOException,
			KeyNotMatchException, NodeNotMatchException, ConvertException {
		rid.pageNo = getCurPage();
		rid.slotNo = 0;
		if (getSlotCnt() != 0) {
			return BT.getEntryFromBytes(getpage(), getSlotOffset(0),
					getSlotLength(0), keyType, NodeType.INDEX);
		}
		return null;
	}
	public PageId getLeftLink() throws IOException {
		return getPrevPage();

	}
	public void setLeftLink(PageId left) throws IOException {
		setPrevPage(left);
	}
}
