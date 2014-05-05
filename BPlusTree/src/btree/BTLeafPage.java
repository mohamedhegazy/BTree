package btree;

import java.io.IOException;

import diskmgr.Page;
import global.PageId;
import global.RID;

public class BTLeafPage extends BTSortedPage {

	public BTLeafPage(PageId arg0, int arg1) throws ConstructPageException,
			IOException {
		super(arg0, arg1);
		setType(NodeType.LEAF);// set in the HFPage
		// TODO Auto-generated constructor stub
	}

	public BTLeafPage(Page page, int keyType) throws IOException {
		super(page, keyType);
		setType(NodeType.LEAF);// set in the HFPage
	}
	public BTLeafPage(int keyType) throws ConstructPageException, IOException {
		super(keyType);
		setType(NodeType.LEAF);// set in the HFPage
	}
	public KeyDataEntry getCurrent(RID rid) throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException {
		rid.slotNo--;
		return getNext(rid);
	}

	public KeyDataEntry getNext(RID rid) throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException {
		rid.pageNo = getCurPage();
		rid.slotNo++;
		if (rid.slotNo < getSlotCnt()) {
			return BT.getEntryFromBytes(getpage(), getSlotOffset(rid.slotNo),
					getSlotLength(rid.slotNo), keyType, NodeType.LEAF);
		}
		return null;
	}

	public KeyDataEntry getFirst(RID rid) throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException {
		rid.pageNo = getCurPage();
		rid.slotNo = 0;
		if (getSlotCnt() != 0) {
			return BT.getEntryFromBytes(getpage(), getSlotOffset(0),
					getSlotLength(0), keyType, NodeType.LEAF);
		}

		return null;
	}

	public RID insertRecord(KeyClass key, RID dataRid)
			throws InsertRecException {
		return insertRecord(new KeyDataEntry(key, dataRid));
	}

	public boolean delEntry(KeyDataEntry dEntry) throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException, DeleteRecException {
		RID rid = new RID();
		KeyDataEntry entry = getFirst(rid);
		while (entry != null) {
			if (entry.equals(dEntry)) {
				deleteSortedRecord(rid);
				return true;
			}
			entry=getNext(rid);
		}
		return false;
	}

}
