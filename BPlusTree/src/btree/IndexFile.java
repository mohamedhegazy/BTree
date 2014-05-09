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
import global.*;

/**
 * Base class for a index file
 */
public abstract class IndexFile 
{
  /**
   * Insert entry into the index file.
   * @param data the key for the entry
   * @param rid the rid of the tuple with the key
 * @throws IOException 
 * @throws ConstructPageException 
 * @throws InvalidFrameNumberException 
 * @throws HashEntryNotFoundException 
 * @throws PageUnpinnedException 
 * @throws ReplacerException 
 * @throws InsertRecException 
 * @throws BufMgrException 
 * @throws PagePinnedException 
 * @throws BufferPoolExceededException 
 * @throws PageNotReadException 
 * @throws HashOperationException 
 * @throws NodeNotMatchException 
 * @throws KeyNotMatchException 
 * @throws DeleteRecException 
 * @throws ConvertException 
 * @throws KeyNotValidException 
 * @throws LongKeyException 
   */
  abstract public void insert(final KeyClass data, final RID rid) throws IOException, ConstructPageException, ReplacerException, PageUnpinnedException, HashEntryNotFoundException, InvalidFrameNumberException, InsertRecException, HashOperationException, PageNotReadException, BufferPoolExceededException, PagePinnedException, BufMgrException, KeyNotMatchException, NodeNotMatchException, DeleteRecException, ConvertException, KeyNotValidException, LongKeyException;
  
  /**
   * Delete entry from the index file.
   * @param data the key for the entry
   * @param rid the rid of the tuple with the key
 * @throws IOException 
 * @throws ConvertException 
 * @throws NodeNotMatchException 
 * @throws KeyNotMatchException 
 * @throws HashEntryNotFoundException 
 * @throws BufMgrException 
 * @throws PagePinnedException 
 * @throws BufferPoolExceededException 
 * @throws PageNotReadException 
 * @throws InvalidFrameNumberException 
 * @throws PageUnpinnedException 
 * @throws HashOperationException 
 * @throws ReplacerException 
 * @throws DeleteRecException 
 * @throws ConstructPageException 
 * @throws Exception 
   */
  abstract public boolean Delete(final KeyClass data, final RID rid) throws IOException, ReplacerException, HashOperationException, PageUnpinnedException, InvalidFrameNumberException, PageNotReadException, BufferPoolExceededException, PagePinnedException, BufMgrException, HashEntryNotFoundException, KeyNotMatchException, NodeNotMatchException, ConvertException, DeleteRecException, ConstructPageException, Exception;
}
