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
import diskmgr.DiskMgrException;
import diskmgr.Page;
import global.PageId;
import global.SystemDefs;
import heap.HFPage;

public class BTreeHeaderPage extends HFPage { // ////////////Header page
												// contains info about the index
												// file like root id,key
												// size(saved in slot 2),key
												// type(saved in slot 1)

	public BTreeHeaderPage() throws ConstructPageException,
			BufferPoolExceededException, HashOperationException,
			ReplacerException, HashEntryNotFoundException,
			InvalidFrameNumberException, PagePinnedException,
			PageUnpinnedException, PageNotReadException, BufMgrException,
			DiskMgrException, IOException {
		super();
		// TODO Auto-generated constructor stub
		Page pg = new Page();
		PageId id = SystemDefs.JavabaseBM.newPage(pg, 1);
		init(id, pg);
	}

	public BTreeHeaderPage(Page pg) {
		// TODO Auto-generated constructor stub
		super(pg);//initialize sorted page with pg
	}

	public void setRootID(PageId root) throws IOException {
		// TODO Auto-generated method stub
		setNextPage(root);

	}

	public PageId get_rootId() throws IOException {
		return getNextPage();
	}

	public void setKeyType(int type) throws IOException {
		setSlot(1, type, 0);
	}

	public short get_keyType() throws IOException {// returns short because
													// BT.printLeaf wants so
		return (short) getSlotLength(1);

	}

	public void setMaxKeySize(int size) throws IOException {
		setSlot(2, size, 0);
	}

	public int getMaxKeySize() throws IOException {
		return getSlotLength(2);
	}

}
