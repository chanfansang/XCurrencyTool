package com.xmj.tool.util.xhdfs.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.FileNameMap;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FsStatus;
import org.apache.hadoop.fs.LocatedFileStatus;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.fs.RemoteIterator;
import org.apache.hadoop.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.xmj.tool.util.xhdfs.vo.FileInfo;

public class HDFSFileUtil {
	protected Logger log;

	public FileSystem hdfs;

	private String ip;
	private Integer port;

	private boolean isInitSuccess = false;

	public HDFSFileUtil(String host, Integer port, String user, Logger log) {
		this.log = log;
		this.ip = host;
		this.port = port;
		try {
			Configuration conf = new Configuration();
			conf.set("fs.defaultFS", "hdfs://" + host + ":" + port);
			conf.set("hadoop.job.ugi", user);
			System.setProperty("HADOOP_USER_NAME", user);
			hdfs = FileSystem.get(conf);
			isInitSuccess = true;
		} catch (Exception e) {
			log.error("初始化异常！", e);
			isInitSuccess = false;
		}
	}

	public HDFSFileUtil(String corePath, String hdfsPath, Logger log) {
		this.log = log;
		Configuration conf = new Configuration();
		conf.clear();
		File coreFile = new File(corePath);
		InputStream coreIn = null;
		InputStream hdfsIn = null;
		try {
			coreIn = new FileInputStream(coreFile);
			File hdfsfile = new File(hdfsPath);
			hdfsIn = new FileInputStream(hdfsfile);
			conf.addResource(coreIn);
			conf.addResource(hdfsIn);
			hdfs = FileSystem.get(conf);
			isInitSuccess = true;
		} catch (Exception e) {
			log.error("初始化异常！", e);
			isInitSuccess = false;
		} finally {
			if (coreIn != null) {
				try {
					coreIn.close();
					coreIn = null;
				} catch (IOException e) {
					log.error("关闭coreIn流异常", e);
				}
			}
			if (hdfsIn != null)
				try {
					hdfsIn.close();
					hdfsIn = null;
				} catch (IOException e) {
					log.error("关闭hdfsIn流异常", e);
				}
		}

	}

	public String getFileType(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		FileNameMap fileNameMap = URLConnection.getFileNameMap();
		String type = fileNameMap.getContentTypeFor(path);
		return type;
	}

	public List<String> getFileList(String path) {
		return getFileList(path, null, null);
	}

	public List<String> getFileList(String path, Date stTime, Date edTime) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		List<String> filePaths = new ArrayList<String>();
		try {
			Path hdfsPath = new Path(path);
			FileStatus[] fileStatuss = hdfs.listStatus(hdfsPath);
			if (stTime == null && edTime == null) {
				for (FileStatus fileStatus : fileStatuss) {
					Path childPath = fileStatus.getPath();
					filePaths.add(path + "/" + childPath.getName());
				}
			} else if (stTime == null && edTime != null) {
				for (FileStatus fileStatus : fileStatuss) {
					Path childPath = fileStatus.getPath();

					long lastChangeTime = fileStatus.getModificationTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(lastChangeTime);
					Date date = calendar.getTime();
					if (edTime.after(date)) {
						filePaths.add(path + "/" + childPath.getName());
					}
				}
			} else if (stTime != null && edTime == null) {
				for (FileStatus fileStatus : fileStatuss) {
					Path childPath = fileStatus.getPath();

					long lastChangeTime = fileStatus.getModificationTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(lastChangeTime);
					Date date = calendar.getTime();
					if (stTime.before(date)) {
						filePaths.add(path + "/" + childPath.getName());
					}
				}
			} else {
				for (FileStatus fileStatus : fileStatuss) {
					Path childPath = fileStatus.getPath();

					long lastChangeTime = fileStatus.getModificationTime();
					Calendar calendar = Calendar.getInstance();
					calendar.setTimeInMillis(lastChangeTime);
					Date date = calendar.getTime();
					if (stTime.before(date) && edTime.after(date)) {
						filePaths.add(path + "/" + childPath.getName());
					}
				}
			}

		} catch (FileNotFoundException e) {
			log.error("找不到文件", e);
		} catch (IOException e) {
			log.error("列示文件异常", e);
		}
		return filePaths;
	}

	public FileInfo getFileDetail(String filePath) {
		Path hdfsPath = new Path(filePath);
		FileInfo fileInfo = new FileInfo();
		try {
			FileStatus fileStatus = hdfs.getFileStatus(hdfsPath);
			Path childPath = fileStatus.getPath();
			fileInfo.setFilePath(filePath);
			fileInfo.setLastChangeTime(fileStatus.getModificationTime());
			fileInfo.setFileName(childPath.getName());
			fileInfo.setDir(fileStatus.isDirectory());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			log.error("列示文件异常", e);
		}
		return fileInfo;
	}

	public boolean isFile(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		Path tmpPath = new Path(path);
		try {
			return hdfs.isFile(tmpPath);
		} catch (IOException e) {
			log.error("判断文件异常！", e);
			return false;
		}
	}

	// 下载文件
	public void downloadFile(String remoteFile, String localPath)
			throws IllegalArgumentException, IOException, URISyntaxException {

		hdfs.copyToLocalFile(new Path(remoteFile), new Path(localPath));

	}

	// 上传文件
	public void uploadFile(String localFile, String destPath) throws IOException, URISyntaxException {

		hdfs.copyFromLocalFile(new Path(localFile), new Path(destPath));

	}

	public InputStream getInputStream(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		Path tmpPath = new Path(path);
		try {
			return hdfs.open(tmpPath);
		} catch (IOException e) {
			log.error("获取输入流异常！", e);
			return null;
		}
	}

	public OutputStream getOutputStream(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		Path tmpPath = new Path(path);
		try {
			return hdfs.create(tmpPath);
		} catch (IOException e) {
			log.error("获取输出流异常！", e);
			return null;
		}
	}

	public void unLoadFile(InputStream inputStream, String path) {
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		OutputStream outputStream = null;
		try {
			outputStream = getOutputStream(path);
			bis = new BufferedInputStream(inputStream);
			bos = new BufferedOutputStream(outputStream);

			int hasRead = 0;
			byte b[] = new byte[1024];
			while ((hasRead = bis.read(b, 0, b.length)) != -1) {
				bos.write(b, 0, hasRead);
			}

		} catch (Exception e) {
			log.error("文件写入异常！", e);
		} finally {
			if (bos != null) {
				try {
					bos.flush();
					bos.close();
				} catch (IOException e) {
				}
			}
			if (bis != null) {
				try {
					bis.close();
				} catch (IOException e) {
				}
			}
			if (outputStream != null) {
				try {
					outputStream.close();
				} catch (IOException e) {
				}
				outputStream = null;
			}
		}

	}

	public String getIP() {
		return ip;
	}

	public Integer getPort() {
		return port;
	}

	public Date getLastChangeTime(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		Path tmpPath = new Path(path);
		try {
			FileStatus fileStatus = hdfs.getFileStatus(tmpPath);
			long lastChangeTime = fileStatus.getModificationTime();

			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(lastChangeTime);
			Date date = calendar.getTime();
			return date;
		} catch (IOException e) {
			log.error("获取最后修改时间异常！", e);
			return null;
		}
	}

	public void clean() {
		// HDFS连接对象中同一个进程内使用的是同一个对象，如果关了，则该进程内所有使用该连接的线程的连接对象都被关了
		try {
			hdfs.close();
		} catch (IOException e) {
			log.error("清理hdfs信息异常！", e);
			e.printStackTrace();
		}
	}

	public boolean isExit(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		log.info("++++文件存在否");
		Path tmpPath = new Path(path);
		try {
			return hdfs.exists(tmpPath);
		} catch (IOException e) {
			log.error("", e);
			return false;
		}
	}

	public boolean mkDir(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		log.info("++++建立文件夹：" + path);
		Path tmpPath = new Path(path);
		try {
			return hdfs.mkdirs(tmpPath);
		} catch (IOException e) {
			log.error("建立文件夹异常！", e);
			return false;
		}
	}

	public boolean mkDirs(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		log.info("++++建立文件夹：" + path);
		Path tmpPath = new Path(path);
		try {
			return hdfs.mkdirs(tmpPath);
		} catch (IOException e) {
			log.error("建立文件夹异常！", e);
			return false;
		}
	}

	public boolean rmFile(String path) {
		if (path.indexOf("//") != -1) {
			path = path.replaceAll("//", "/");
		}
		log.info("++++建立文件夹：" + path);
		Path tmpPath = new Path(path);
		try {
			return hdfs.delete(tmpPath, true);
		} catch (IOException e) {
			log.error("建立文件夹异常！", e);
			return false;
		}
	}

	public void deleteTimeoutFile(String fileDir, int day) throws Exception {
		Path path = new Path(fileDir);
		if (hdfs.exists(path) == false) {
			return;
		}
		RemoteIterator<LocatedFileStatus> listFiles = hdfs.listFiles(path, true);
		while (listFiles.hasNext()) {
			LocatedFileStatus next = listFiles.next();
			long mTime = next.getModificationTime();
			if (next.isFile() && System.currentTimeMillis() - mTime > day * 3600 * 24 * 1000) {
				hdfs.delete(next.getPath(), true);
			}
		}
	}

	public static void main(String[] args) {
		HDFSFileUtil fileUtil = new HDFSFileUtil("10.12.2.60", 8020, "boco", LoggerFactory.getLogger("test"));

		// System.out.println(fileUtil.listFile("//user"));

		System.out.println(fileUtil.getOutputStream("du/20140917101438852/test.csv"));
	}

	public List<String> unzipFile(String path, String destDir) {
		// TODO 该类型暂时未实现远程解压文件
		log.info("该类型暂时未实现远程解压文件");
		return null;
	}

	public boolean downLoadFile(String srcPath, OutputStream outputStream) throws Exception {
		log.warn("暂不支持此功能");
		return false;
	}

	public boolean isInitSuccess() {
		return isInitSuccess;
	}

}
