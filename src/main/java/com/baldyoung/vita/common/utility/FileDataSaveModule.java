
package com.baldyoung.vita.common.utility;


import java.io.*;
import java.util.LinkedList;
import java.util.List;

/**
 * 用于在web里处理文件上传需求，每一个FileDataSaveModule对象包含三个属性
 * dataSource：一个InputStream对象，代表将要被存储的文件数据
 * pathName：一个String对象，代表数据的存储地址和存储文件的类型
 * actionType：一个ActionType枚举对象，代表数据写入的方式是覆盖还是追加
 * 注意！：使用时只有保证存储位置正确即可，存储数据时会自动校正不同平台之间路径分割符不同的问题。
 * @author baldyoung
 */
public class FileDataSaveModule implements Runnable{
	/**
	 * 数据写入文件时的方式（{COVER:覆盖原文件, APPEND:追加到原文件的尾部}）
	 */
	public enum ActionType{
		COVER, APPEND
	}
	//用于UniqueCodeModule生成唯一标识的默认参数
	public static final String DEFAULT_HEADER = "FDOM", DEFAULT_TAIL = "";
	//用于生成唯一标识
	private static final UniqueCodeModule uniqueCodeModule = UniqueCodeModule.getInstance(DEFAULT_HEADER, DEFAULT_TAIL);
	private InputStream dataSource;
	private String pathName;
	private ActionType actionType;

	/**
	 * 公开的构造函数
	 *
	 * @param tDataSource 作为存储源的InputStream对象
	 * @param tPathName 存储地址（path+name）。
	 * @param tActioinType 存储方式
	 */
	public FileDataSaveModule(InputStream tDataSource, String tPathName, ActionType tActioinType){
		this.dataSource = tDataSource;
		this.pathName = adjustPathNameSeparator(tPathName);
		this.actionType = tActioinType;
	}
	public FileDataSaveModule(InputStream tDataSource, String tPathName){
		this(tDataSource, tPathName, ActionType.COVER);
	}
	public FileDataSaveModule(InputStream tDataSource){
		this(tDataSource, getUniqueIdentification());
	}
	public String getPathName(){
		return this.pathName;
	}
	public InputStream getDataSource(){
		return this.dataSource;
	}
	public ActionType getActionType(){
		return this.actionType;
	}

	/**
	 * 公开的成员方法
	 * 将当前InputStream对象的数据保存到指定的位置，并在保存完毕后关闭流。
	 * @return false：文件无法写入或存在null参数
	 * @throws IOException
	 */
	public boolean save() throws IOException{
		boolean result = false;
		if(null == this.dataSource || null == this.pathName)
			result = false;
		else{
			File file = new File(this.pathName);
			if(!file.exists()){
				file.createNewFile();
			}
			if(file.isDirectory() || !file.canWrite()){
				result = false;
			}else{
				FileOutputStream output = new FileOutputStream(this.pathName, (this.actionType != ActionType.APPEND ? false : true) );
				result = saveData(this.dataSource, output);
			}
		}
		return result;
	}

	@Override
	public void run(){
		try{
			save();
		}catch(Exception e){
		}
	}
		
//static method
	/**
	 * 公开的静态方法
	 * 将给定的InputStream集合全部打包成指定参数的FileDataSaveModule对象并返回一个存储它们的list集合。
	 * 注意！：文件的存储名称已经由模块自动生成，且确保它是唯一性的。每个数据对应的存储位置可由getPathName()调用获得。
	 * @param list inputstream对象集合
	 * @param path 存储路径
	 * @param fileType 存储文件的类型
	 * @return
	 */
	static public List<FileDataSaveModule> getListByInputStreams(List<InputStream> list, String path, String fileType){
		List<FileDataSaveModule> result = new LinkedList<>();
		if(null != list){
			for(InputStream input : list){
				result.add(new FileDataSaveModule(input, getUniqueIdentification(path, fileType)));
			}
		}
		return result;
	}

	static public FileDataSaveModule createTargetInstance(InputStream inputStream, String path, String fileType) {
		return new FileDataSaveModule(inputStream, getUniqueIdentification(path, fileType));
	}
	
	//**************** 获取唯一性标识

	/**
	 * 公开的静态方法
	 * 返回一个头部和尾部带指定字符串的唯一标识符
	 * @param strHeader 头部字符串
	 * @param strTail 尾部字符串
	 * @return
	 */
	static public String getUniqueIdentification(String strHeader, String strTail){
		StringBuilder builder = new StringBuilder();
		builder.append(strHeader);
		builder.append(uniqueCodeModule.getUniqueCode());
		builder.append(strTail);
		return builder.toString();
	}

	/**
	 * 公开的静态方法
	 * 返回要给唯一的字符串
	 * @return
	 */
	static public String getUniqueIdentification(){
		return uniqueCodeModule.getUniqueCode();
	}
	//**************** 数据拷贝调用
	/**
	 * 公开的静态方法
	 * 数据拷贝调用，从给定的数据源获取数据，并写入到指定的数据存储源。
	 * 注意！：数据拷贝完成后会尝试使用自动关闭语法关闭数据源
		@param input 数据来源
		@param output 数据存储源
		@return 数据拷贝成功：true；拷贝失败：false；
		@exception IOException 数据读取或写入异常
	*/
	static public boolean saveData(InputStream input, OutputStream output) throws IOException{
		boolean result = true;
		if(null == input || null == output)
			result = false;
		else{
			//使用自动关闭语法
			try(InputStream src = input; OutputStream dest = output){
				byte data[] = new byte[2048];
				int length; 
				while((length = src.read(data)) != -1){
					dest.write(data, 0, length);
				}
			}
		}
		return result;
	}
	//**************** 路径分隔符校正调用
	/**
	 * 公开的静态方法
	 * 因为不同的操作系统上，其所采用的路径分隔符不一样，该方法用于校正pathName参数中的路径分隔符。
	 *
	 * @param pathName 要被校正的路径字符串
	 * @return 返回校正后的路径字符串
	*/
	static public String adjustPathNameSeparator(String pathName){
		String separator = File.separator;
		String result = null;
		if("/".equals(separator))
			result = pathName.replace("\\", "/");
		else
			result = pathName.replace("/", "\\");
		return result;
	}
	
}
