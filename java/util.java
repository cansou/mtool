public static String getRandomString(int length) {  
	String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";  
	Random random = new Random();  
	StringBuilder sb = new StringBuilder();

	for (int i = 0; i < length; ++i) {  
		int number = random.nextInt(62);// [0,62)  
		sb.append(str.charAt(number));  
	}  
	return sb.toString(); 
}

//删除某目录下的所有文件
static void deleteAllFiles(File root) {  
   File files[] = root.listFiles();  
   if (files != null)  
	   for (File f : files) {  
		   if (f.isDirectory()) { // 判断是否为文件夹  
			   deleteAllFiles(f);  
			   try {  
				   f.delete();  
			   } catch (Exception e) {  
			   }  
		   } else {  
			   if (f.exists()) { // 判断是否存在  
				   deleteAllFiles(f);  
				   try {  
					   f.delete();  
				   } catch (Exception e) {  
				   }  
			   }  
		   }  
	   }  
} 