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


/**
 * Base class for a index file scan
 */
public abstract class IndexFileScan 
{
  /**
   * Get the next record.
   * @return the KeyDataEntry, which contains the key and data
 * @throws ConstructPageException 
 * @throws InvalidFrameNumberException 
 * @throws HashEntryNotFoundException 
 * @throws PageUnpinnedException 
 * @throws ReplacerException 
 * @throws IOException 
 * @throws ConvertException 
 * @throws NodeNotMatchException 
 * @throws KeyNotMatchException 
   */
  abstract public KeyDataEntry get_next() throws KeyNotMatchException, NodeNotMatchException, ConvertException, IOException, ReplacerException, PageUnpinnedException, HashEntryNotFoundException, InvalidFrameNumberException, ConstructPageException;

  /** 
   * Delete the current record.
 * @throws BufMgrException 
 * @throws PagePinnedException 
 * @throws BufferPoolExceededException 
 * @throws PageNotReadException 
 * @throws HashOperationException 
 * @throws DeleteRecException 
 * @throws InvalidFrameNumberException 
 * @throws HashEntryNotFoundException 
 * @throws PageUnpinnedException 
 * @throws ReplacerException 
 * @throws ConstructPageException 
 * @throws IOException 
 * @throws ConvertException 
 * @throws NodeNotMatchException 
 * @throws KeyNotMatchException 
   */
   abstract public void delete_current() throws KeyNotMatchException, NodeNotMatchException, ConvertException, IOException, ConstructPageException, ReplacerException, PageUnpinnedException, HashEntryNotFoundException, InvalidFrameNumberException, DeleteRecException, HashOperationException, PageNotReadException, BufferPoolExceededException, PagePinnedException, BufMgrException;

  /**
   * Returns the size of the key
   * @return the keysize
   */
  abstract public int keysize();
}
