package com.vruiz.invertedindex.store.codec;

import com.vruiz.invertedindex.index.CorruptIndexException;

import java.util.Map;

/**
 *  Format used to store the norms is:
 * {documentId1}:{norm1}\n
 * {documentId2}:{norm2}\n
 * ...
 */
public class NormsCodec implements Codec {


	@Override
	public String writeEntry(Map.Entry entry) throws CorruptIndexException {
		Object key = entry.getKey();
		Object val = entry.getValue();

		if (key == null || val == null) {
			throw new CorruptIndexException("corrupted data in entry");
		}
		long documentId = ((Long) key).longValue();
		int count = ((Integer) val).intValue();
		return String.format("%d:%d", documentId, count);
	}

	@Override
	public Map.Entry readEntry(String data) throws CorruptIndexException {
		//split on ":" to get documentId and count
		String[] parts = data.split(":");
		if (parts.length != 2 || parts[0].length() == 0 || parts[1].length() == 0) {
			throw new CorruptIndexException("wrong data format: ".concat(data));
		}
		try {
			long documentId = Long.parseLong(parts[0]);
			int count = Integer.parseInt(parts[1]);
			return new Codec.Entry<Long,Integer >(documentId, count);
		} catch (NumberFormatException e) {
			throw new CorruptIndexException("wrong data format ".concat(e.getMessage()));
		}

	}
}
