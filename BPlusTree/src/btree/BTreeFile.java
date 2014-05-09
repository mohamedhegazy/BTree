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
import diskmgr.DuplicateEntryException;
import diskmgr.FileIOException;
import diskmgr.FileNameTooLongException;
import diskmgr.InvalidPageNumberException;
import diskmgr.InvalidRunSizeException;
import diskmgr.OutOfSpaceException;
import diskmgr.Page;
import global.GlobalConst;
import global.PageId;
import global.RID;
import global.SystemDefs;

public class BTreeFile extends IndexFile {
	private BTreeHeaderPage headerPage;
	private PageId headId;
	private String file_name;

	public BTreeFile(String string, int keyType, int i, int j)
			throws IOException, ReplacerException, HashOperationException,
			PageUnpinnedException, InvalidFrameNumberException,
			PageNotReadException, BufferPoolExceededException,
			PagePinnedException, BufMgrException, ConstructPageException,
			HashEntryNotFoundException, DiskMgrException, FileIOException,
			InvalidPageNumberException, FileNameTooLongException,
			InvalidRunSizeException, DuplicateEntryException,
			OutOfSpaceException {// open the index if it exists or create new
									// one if it doesn't exist
		// TODO Auto-generated constructor stub
		headId = SystemDefs.JavabaseDB.get_file_entry(string);
		file_name = string;
		if (headId == null) {// index doesn't exist
			headerPage = new BTreeHeaderPage();
			headId = headerPage.getCurPage();
			SystemDefs.JavabaseDB.add_file_entry(string, headId);
			headerPage.setKeyType(keyType);
			headerPage.setMaxKeySize(i);
		} else {// index exists
			headId = SystemDefs.JavabaseDB.get_file_entry(string);
			Page pg = new Page();
			SystemDefs.JavabaseBM.pinPage(headId, pg, false);
			headerPage = new BTreeHeaderPage(pg);
			headerPage.setCurPage(headId);
		}

	}

	public BTreeFile(String string) throws FileIOException,
			InvalidPageNumberException, DiskMgrException, IOException,
			ConstructPageException, BufferPoolExceededException,
			HashOperationException, ReplacerException,
			HashEntryNotFoundException, InvalidFrameNumberException,
			PagePinnedException, PageUnpinnedException, PageNotReadException,
			BufMgrException { // open the index
		// TODO Auto-generated constructor stub
		headId = SystemDefs.JavabaseDB.get_file_entry(string);
		Page pg = new Page();
		SystemDefs.JavabaseBM.pinPage(headId, pg, false);
		headerPage = new BTreeHeaderPage(pg);
		headerPage.setCurPage(headId);
		file_name = string;
	}

	public BTreeHeaderPage getHeaderPage() throws ConstructPageException,
			BufferPoolExceededException, HashOperationException,
			ReplacerException, HashEntryNotFoundException,
			InvalidFrameNumberException, PagePinnedException,
			PageUnpinnedException, PageNotReadException, BufMgrException,
			DiskMgrException, IOException {
		// TODO Auto-generated method stub
		return headerPage;
	}

	public void close() throws ReplacerException, PageUnpinnedException,
			HashEntryNotFoundException, InvalidFrameNumberException {
		// TODO Auto-generated method stub
		if (headerPage != null) {
			SystemDefs.JavabaseBM.unpinPage(headId, true);
			headerPage = null;
		}
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

	public void insert(KeyClass key, RID rid) throws IOException,
			ConstructPageException, ReplacerException, PageUnpinnedException,
			HashEntryNotFoundException, InvalidFrameNumberException,
			InsertRecException, HashOperationException, PageNotReadException,
			BufferPoolExceededException, PagePinnedException, BufMgrException,
			KeyNotMatchException, NodeNotMatchException, DeleteRecException,
			ConvertException, KeyNotValidException, LongKeyException {
		// TODO Auto-generated method stub
		if (!(key instanceof StringKey) && !(key instanceof IntegerKey)) {// signal
																			// error
																			// if
																			// the
																			// key
																			// type
																			// is
																			// not
																			// an
																			// integer
																			// or
																			// a
																			// string
																			// System.out.println("Key is not of valid type");
			throw new KeyNotValidException(null, "Not Valid Key Type");
		}
		if (BT.getKeyLength(key) > headerPage.getMaxKeySize()) {// signal error
																// if key is
																// bigger than
																// allowed value
			throw new LongKeyException(null, "Key is bigger than maximum size");
		}
		KeyDataEntry new_child_entry;
		if (headerPage.get_rootId().pid == -1) {// empty index so the index will
												// contain a leaf page
												// only(which is the root)
			BTLeafPage root = new BTLeafPage(headerPage.get_keyType());
			PageId temprootId = root.getCurPage();
			root.setNextPage(new PageId(GlobalConst.INVALID_PAGE));
			root.setPrevPage(new PageId(GlobalConst.INVALID_PAGE));
			root.insertRecord(key, rid);
			SystemDefs.JavabaseBM.unpinPage(temprootId, true);
			SystemDefs.JavabaseBM.pinPage(headId, headerPage, false);
			headerPage.setRootID(temprootId);
			SystemDefs.JavabaseBM.unpinPage(headId, true);
			return;
		} else {
			new_child_entry = insert(key, rid, headerPage.get_rootId()); // index
																			// is
																			// not
																			// empty
			if (new_child_entry != null) { // this means that splitting
											// propagated to the root so we need
											// new root
				BTIndexPage new_root = new BTIndexPage(headerPage.get_keyType());
				PageId id_new_root = new_root.getCurPage();
				new_root.insertKey(new_child_entry.key,
						((IndexData) new_child_entry.data).getData());
				new_root.setPrevPage(headerPage.get_rootId());
				SystemDefs.JavabaseBM.unpinPage(id_new_root, true);
				SystemDefs.JavabaseBM.pinPage(headId, headerPage, false);
				headerPage.setRootID(id_new_root);
				SystemDefs.JavabaseBM.unpinPage(headId, true);
			}
		}
	}

	private KeyDataEntry insert(KeyClass key, RID rid, PageId curID)
			throws ReplacerException, HashOperationException,
			PageUnpinnedException, InvalidFrameNumberException,
			PageNotReadException, BufferPoolExceededException,
			PagePinnedException, BufMgrException, IOException,
			HashEntryNotFoundException, KeyNotMatchException,
			NodeNotMatchException, ConstructPageException, DeleteRecException,
			InsertRecException, ConvertException {
		// this
		// is
		// where
		// the recursion
		// happens
		// TODO Auto-generated method stub
		BTSortedPage curPage;
		Page page = new Page(); // used as output
		KeyDataEntry entry;
		SystemDefs.JavabaseBM.pinPage(curID, page, false);
		curPage = new BTSortedPage(page, headerPage.get_keyType());
		if (curPage.getType() == NodeType.INDEX) {
			BTIndexPage curIndex = new BTIndexPage(page,
					headerPage.get_keyType());
			PageId curPageId = curID;
			PageId nextPageId = curIndex.getPageNoByKey(key);
			SystemDefs.JavabaseBM.unpinPage(curPageId, true);
			entry = insert(key, rid, nextPageId); // recurse to get next page
			if (entry == null) {
				return null; // no splitting happened
			} else { // splitting occured in lower level
				SystemDefs.JavabaseBM.pinPage(curPageId, page, false);
				curIndex = new BTIndexPage(page, headerPage.get_keyType());
				if (curIndex.available_space() >= BT.getKeyDataLength(key,
						NodeType.INDEX)) { // in this page no splitting occured
					curIndex.insertKey(entry.key,
							((IndexData) entry.data).getData());
					SystemDefs.JavabaseBM.unpinPage(curPageId, true);
					return null;
				} else {// in this page splitting will happen because there is
						// no enough space
					BTIndexPage newPage = new BTIndexPage(
							headerPage.get_keyType());
					PageId temPageId = newPage.getCurPage();
					System.out.println("Index page split and new page id is "
							+ temPageId.pid + " while the old is :"
							+ curPageId.pid);

					int half = curIndex.numberOfRecords() / 2;
					int space = newPage.available_space();
					RID rid2 = new RID();
					rid2.slotNo = half - 1;
					rid2.pageNo = curIndex.getCurPage();
					KeyDataEntry tempDataEntry = curIndex.getNext(rid2);
					while (tempDataEntry != null
							&& curIndex.available_space() != space / 2) {// move
																			// half
																			// of
																			// the
																			// entries
																			// to
																			// the
																			// new
																			// page
						newPage.insertKey(tempDataEntry.key,
								((IndexData) (tempDataEntry.data)).getData());
						curIndex.deleteSortedRecord(rid2);
						rid2.slotNo--;
						tempDataEntry = curIndex.getNext(rid2);
					}
					tempDataEntry = newPage.getFirst(new RID());
					if (BT.keyCompare(entry.key, tempDataEntry.key) >= 0) { // decide
																			// where
																			// the
																			// entry
																			// belongs
																			// (original
																			// page
																			// or
																			// the
																			// new
																			// one)
						newPage.insertKey(entry.key,
								((IndexData) entry.data).getData());
					} else {
						curIndex.insertKey(entry.key,
								((IndexData) entry.data).getData());
					}
					// SystemDefs.JavabaseBM.unpinPage(temPageId, true);
					entry = newPage.getFirst(new RID());
					newPage.setLeftLink(((IndexData) entry.data).getData());
					// set
					// left
					// link
					// of
					// the
					// new
					// page
					// to the value of next pointer because the first key will
					// be deleted
					newPage.deleteSortedRecord(new RID()); // first record in
															// new page is
															// pushed up to an
															// index page
					SystemDefs.JavabaseBM.unpinPage(temPageId, true);
					((IndexData) entry.data).setData(temPageId);
					return entry;
				}
			}
		} else if (curPage.getType() == NodeType.LEAF) { // we reached lowest
															// level in
															// recursion which
															// is the leaf page
			BTLeafPage curBtLeafPage = new BTLeafPage(page,
					headerPage.get_keyType());
			PageId curleaf = curID;
			if (curBtLeafPage.available_space() >= BT.getKeyDataLength(key,
					NodeType.LEAF)) { // enough space on leaf page so no
										// splitting occurs
				curBtLeafPage.insertRecord(key, rid);
				SystemDefs.JavabaseBM.unpinPage(curleaf, true);
				return null;
			} else {// no enough space on leaf so we split it
				BTLeafPage newLeaf = new BTLeafPage(headerPage.get_keyType());
				PageId newLeafId = newLeaf.getCurPage();
				newLeaf.setNextPage(curBtLeafPage.getNextPage());
				newLeaf.setPrevPage(curleaf);
				curBtLeafPage.setNextPage(newLeafId);// adjust pointers of the
														// doubly linked list
				PageId temp_id = newLeaf.getNextPage();
				if (temp_id.pid != -1) {// if the new page wasn't the last one
					// adjust prev page of the next page of the new page
					BTLeafPage next_to_new = new BTLeafPage(temp_id,
							headerPage.get_keyType());
					next_to_new.setPrevPage(newLeafId);
					SystemDefs.JavabaseBM.unpinPage(next_to_new.getCurPage(),
							true);
				}
				int half = curBtLeafPage.numberOfRecords() / 2;
				RID rid2 = new RID();
				rid2.slotNo = half - 1;
				rid2.pageNo = curBtLeafPage.getCurPage();
				KeyDataEntry tempDataEntry = curBtLeafPage.getNext(rid2);
				int space = newLeaf.available_space();
				while (tempDataEntry != null
						&& curBtLeafPage.available_space() != space / 2) {// move
																			// half
																			// of
																			// the
																			// entries
																			// to
																			// the
																			// new
																			// page
					newLeaf.insertRecord(tempDataEntry.key,
							((LeafData) (tempDataEntry.data)).getData());
					curBtLeafPage.deleteSortedRecord(rid2);
					rid2.slotNo--;
					tempDataEntry = curBtLeafPage.getNext(rid2);
				}
				tempDataEntry = newLeaf.getFirst(new RID());
				if (BT.keyCompare(key, tempDataEntry.key) >= 0) {// decide where
																	// the entry
																	// belongs
					newLeaf.insertRecord(key, rid);
				} else {
					curBtLeafPage.insertRecord(key, rid);
				}
				SystemDefs.JavabaseBM.unpinPage(curleaf, true);
				tempDataEntry = newLeaf.getFirst(new RID()); // notice that we
																// copy up the
																// value to the
																// upper
																// level(index
																// level)
				entry = new KeyDataEntry(tempDataEntry.key, newLeafId);
				SystemDefs.JavabaseBM.unpinPage(newLeafId, true);
				return entry;
			}

		}
		return null;
	}

	public boolean Delete(KeyClass key, RID rid) throws IOException,
			ReplacerException, HashOperationException, PageUnpinnedException,
			InvalidFrameNumberException, PageNotReadException,
			BufferPoolExceededException, PagePinnedException, BufMgrException,
			HashEntryNotFoundException, KeyNotMatchException,
			NodeNotMatchException, ConvertException, DeleteRecException, ConstructPageException {
		// TODO Auto-generated method stub
		if (headerPage.get_rootId().pid == -1)
			return false;
		return Delete(headerPage.get_rootId(), key, rid);
	}

	private boolean Delete(PageId curId, KeyClass key, RID rid)
			throws ReplacerException, HashOperationException,
			PageUnpinnedException, InvalidFrameNumberException,
			PageNotReadException, BufferPoolExceededException,
			PagePinnedException, BufMgrException, IOException,
			HashEntryNotFoundException, KeyNotMatchException,
			NodeNotMatchException, ConvertException, DeleteRecException, ConstructPageException {
		// TODO Auto-generated method stub
		BTSortedPage curPage = new BTSortedPage(curId, headerPage.get_keyType());
		if (curPage.getType() == NodeType.INDEX) {
			BTIndexPage curIndexPage = new BTIndexPage(curPage,
					headerPage.get_keyType());
			PageId nextId = curIndexPage.getPageNoByKey(key);
			SystemDefs.JavabaseBM.unpinPage(curId, true);
			boolean del= Delete(nextId, key, rid);
//			SystemDefs.JavabaseBM.unpinPage(curId, true);
			return del;
		} else {
			BTLeafPage curLeafPage = new BTLeafPage(curPage,
					headerPage.get_keyType());
			boolean del = curLeafPage.delEntry(new KeyDataEntry(key, rid));
			SystemDefs.JavabaseBM.unpinPage(curId, true);
//			SystemDefs.JavabaseBM.unpinPage(curId, true);
			return del;
		}
	}

	public String getFile_name() {
		return file_name;
	}

}
