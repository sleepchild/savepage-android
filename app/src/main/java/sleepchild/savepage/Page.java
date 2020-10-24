package sleepchild.savepage;

public class Page{
	//
	String title;
	String filepath;
	
	Page(String title, String path){
		this.title = title;
		this.filepath = path;
	}

	public String getTitle(){
		return this.title;
	}

	public String getPath(){
		return this.filepath;
	}

}


