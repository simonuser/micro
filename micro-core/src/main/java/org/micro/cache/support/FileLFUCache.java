package org.micro.cache.support;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 使用LFU缓存文件，以解决频繁读取文件引起的性能问题
 * @author lry
 */
public class FileLFUCache {

	/** LFU缓存 */
	protected final LFUCache<File, byte[]> cache;
	/** 容量 */
	protected final int capacity;
	/** 缓存的最大文件大小，文件大于此大小时将不被缓存 */
	protected final int maxFileSize;

	/** 已使用缓存空间 */
	protected int usedSize;

	/**
	 * 构造<br>
	 * 最大文件大小为缓存容量的一半<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 */
	public FileLFUCache(int capacity) {
		this(capacity, capacity / 2, 0);
	}

	/**
	 * 构造<br>
	 * 默认无超时
	 * @param capacity 缓存容量
	 * @param maxFileSize 最大文件大小
	 */
	public FileLFUCache(int capacity, int maxFileSize) {
		this(capacity, maxFileSize, 0);
	}

	/**
	 * 构造
	 * @param capacity 缓存容量
	 * @param maxFileSize 文件最大大小
	 * @param timeout 默认超时时间，0表示无默认超时
	 */
	public FileLFUCache(int capacity, int maxFileSize, long timeout) {
		this.cache = new LFUCache<File, byte[]>(0, timeout) {
			@Override
			public boolean isFull() {
				return usedSize > this.capacity;
			}
			@Override
			protected void onRemove(File key, byte[] cachedObject) {
				usedSize -= cachedObject.length;
			}
		};
		
		this.capacity = capacity;
		this.maxFileSize = maxFileSize;
	}

	/**
	 * @return 缓存容量（byte数）
	 */
	public int capacity() {
		return capacity;
	}

	/**
	 * @return 已使用空间大小（byte数）
	 */
	public int getUsedSize() {
		return usedSize;
	}

	/**
	 * @return 允许被缓存文件的最大byte数
	 */
	public int maxFileSize() {
		return maxFileSize;
	}

	/**
	 * @return 缓存的文件数
	 */
	public int getCachedFilesCount() {
		return cache.size();
	}

	/**
	 * @return 超时时间
	 */
	public long timeout() {
		return cache.timeout;
	}

	/**
	 * 清空缓存
	 */
	public void clear() {
		cache.clear();
		usedSize = 0;
	}

	// ---------------------------------------------------------------- get

	/**
	 * 获得缓存过的文件bytes
	 * @param path 文件路径
	 * @return 缓存过的文件bytes
	 * @throws IOException
	 */
	public byte[] getFileBytes(String path) throws IOException {
		return getFileBytes(new File(path));
	}

	/**
	 * 获得缓存过的文件bytes
	 * @param file 文件
	 * @return 缓存过的文件bytes
	 * @throws IOException
	 */
	public byte[] getFileBytes(File file) throws IOException {
		byte[] bytes = cache.get(file);
		if (bytes != null) {
			return bytes;
		}

		// add file
		bytes = readBytes(file);

		if ((maxFileSize != 0) && (file.length() > maxFileSize)) {
			//大于缓存空间，不缓存，直接返回
			return bytes;
		}

		usedSize += bytes.length;

		//文件放入缓存，如果usedSize > capacity，purge()方法将被调用
		cache.put(file, bytes);

		return bytes;
	}

	
	/**
	 * 读取文件所有数据<br>
	 * 文件的长度不能超过Integer.MAX_VALUE
	 * @param file 文件
	 * @return 字节码
	 * @throws IOException
	 */
	private static byte[] readBytes(File file) throws IOException {
		//check
		if (! file.exists()) {
			throw new FileNotFoundException("File not exist: " + file);
		}
		if (! file.isFile()) {
			throw new IOException("Not a file:" + file);
		}
		
		long len = file.length();
		if (len >= Integer.MAX_VALUE) {
			throw new IOException("File is larger then max array size");
		}

		byte[] bytes = new byte[(int) len];
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			in.read(bytes);
		}finally {
			if(in!=null){
				in.close();
			}
		}

		return bytes;
	}
}
