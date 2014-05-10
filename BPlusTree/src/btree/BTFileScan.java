package btree;

import java.io.IOException;

import bufmgr.BufMgrException;
import bufmgr.BufferPoolExceededException;
import bufmgr.HashEntryNotFoundException;
import bufmgr.HashOperationException;
import bufmgr.InvalidFrameNumberException;
import bufmgr.PageNotReadException;
import bufmgr.PagePinnedException;
import bufmgr.PageUnpinnedException;
import bufmgr.ReplacerException;

import global.RID;
import global.SystemDefs;

public class BTFileScan extends IndexFileScan {

	private BTLeafPage leafPage;
	private KeyDataEntry currKey;
	private KeyClass highKey;
	private RID currRID;
	private int keyType;
	private int keySize;
	private boolean deleted;

	public BTFileScan(BTLeafPage leafPage, KeyClass lo_key, KeyClass hi_key,
			int size, RID rid, int keyType) throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException,
			ConstructPageException, ReplacerException, PageUnpinnedException,
			HashEntryNotFoundException, InvalidFrameNumberException {
		this.leafPage = leafPage;
		// currKey = lo_key;
		highKey = hi_key;
		keySize = size;
		currRID = rid;
		currKey = leafPage.getCurrent(currRID);
		this.keyType = keyType;
		deleted = true;

		while (lo_key != null && BT.keyCompare(lo_key, currKey.key) != 0) {

			currKey = this.leafPage.getNext(currRID);
			while (currKey == null) {
				SystemDefs.JavabaseBM.unpinPage(this.leafPage.getCurPage(),
						false);
				this.leafPage = new BTLeafPage(this.leafPage.getNextPage(),
						this.keyType);
				if (this.leafPage.getCurPage().pid == -1) {
					currKey = null;
					break;
				}
				currKey = leafPage.getNext(currRID);
			}
		}
	}

	@Override
	public KeyDataEntry get_next() throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException,
			ReplacerException, PageUnpinnedException,
			HashEntryNotFoundException, InvalidFrameNumberException,
			ConstructPageException {
		/* if current deleted or first called */
		if (deleted) {
			if (currKey == null || BT.keyCompare(currKey.key, highKey) > 0) {
				return null;
			}
			deleted = false;
			return currKey;
		} else {
			if (highKey != null && BT.keyCompare(currKey.key, highKey) > 0) {
				SystemDefs.JavabaseBM.unpinPage(leafPage.getCurPage(), false);
				return null;
			} else {
				currKey = leafPage.getNext(currRID);
				// if currkey reached to null entry in node (last index)
				while (currKey == null) {
					SystemDefs.JavabaseBM.unpinPage(this.leafPage.getCurPage(),
							false);
					this.leafPage = new BTLeafPage(this.leafPage.getNextPage(),
							this.keyType);
					// reached last leaf node at B+ Tree
					if (this.leafPage.getCurPage().pid == -1) {
						return null;
					}
					// first entry at next node
					currKey = leafPage.getFirst(currRID);
				}
			}
		}
		return currKey;
	}

	@Override
	public void delete_current() throws KeyNotMatchException,
			NodeNotMatchException, ConvertException, IOException,
			ConstructPageException, ReplacerException, PageUnpinnedException,
			HashEntryNotFoundException, InvalidFrameNumberException,
			DeleteRecException, HashOperationException, PageNotReadException,
			BufferPoolExceededException, PagePinnedException, BufMgrException {
		BTLeafPage currLeaf = leafPage;
		KeyDataEntry currEntry = currKey;
		KeyDataEntry nextEntry = get_next();
		SystemDefs.JavabaseBM.pinPage(currLeaf.getCurPage(), currLeaf, false);
		currLeaf.delEntry(currEntry);
		SystemDefs.JavabaseBM.unpinPage(currLeaf.getCurPage(), true);
		currKey = nextEntry;
		deleted = true;

		// BTLeafPage nextLeaf = leafPage;
		// RID nextRid = currRID;
		// nextEntry = leafPage.getNext(currRID);
		// while (nextEntry == null) {
		// SystemDefs.JavabaseBM.unpinPage(this.leafPage.getCurPage(), false);
		// this.leafPage = new BTLeafPage(this.leafPage.getNextPage(),
		// this.keyType);
		// if (this.leafPage.getCurPage().pid == -1) {
		// nextEntry = null;
		// break;
		// }
		// nextEntry = leafPage.getNext(currRID);
		// }

	}

	public void DestroyBTreeFileScan() throws ReplacerException,
			PageUnpinnedException, HashEntryNotFoundException,
			InvalidFrameNumberException, IOException {
		try {
			SystemDefs.JavabaseBM.unpinPage(leafPage.getCurPage(), false);
		} catch (Exception e) {
			System.out.println("-- Catch Destroy Scan!!!");
		}
		currKey = null;
		highKey = null;
		currRID = null;
	}

	@Override
	public int keysize() {
		return keySize;
	}

}