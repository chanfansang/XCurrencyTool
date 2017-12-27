package com.xmj.tool.util.xhdfs.vo;

import java.util.Map;

/**
 * 
 * 
 * <p>
 * 项目名称：GFrame_Element
 * <p>
 * 类名称：FileInfo
 * <p>
 * 类描述： 文件信息，包括文件路径等
 * <p>
 * 创建人：Killler
 * <p>
 * 创建时间：2016年7月3日 下午9:08:37
 * <p>
 */
public class FileInfo {

	// 文件路径
	private String filePath;

	// 文件更新时间
	private Long lastChangeTime;

	// 通过正则从路径中取得的正则匹配的结果,key为索引从1开始递增，value为结果
	private Map<Integer, String> patternVariable;

	// 文件名称
	private String fileName;

	// 是否为目录，true为文件目录，false为文件
	private boolean isDir;

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public Long getLastChangeTime() {
		return lastChangeTime;
	}

	public void setLastChangeTime(Long lastChangeTime) {
		this.lastChangeTime = lastChangeTime;
	}

	public Map<Integer, String> getPatternVariable() {
		return patternVariable;
	}

	public void setPatternVariable(Map<Integer, String> patternVariable) {
		this.patternVariable = patternVariable;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public boolean isDir() {
		return isDir;
	}

	public void setDir(boolean isDir) {
		this.isDir = isDir;
	}

	public String toString() {
		return "FileInfo [filePath=" + filePath + ", lastChangeTime="
				+ lastChangeTime + ", patternVariable=" + patternVariable
				+ ", fileName=" + fileName + ", isDir=" + isDir + "]";
	}

}
