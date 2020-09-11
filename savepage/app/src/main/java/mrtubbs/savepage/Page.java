package mrtubbs.savepage;

public class Page{
	//
	String title;
	String filepath;
	
	Page(String title, String path){
		this.title = title;
		this.filepath = path;
	}

	public String getTitle(){
		return this.title.replace(".mht","");
	}

	public String getPath(){
		return this.filepath;
	}

}


