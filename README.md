# JSON API
Robust and efficient JSON parser and generator that includes line and column tracker for error handling.  
Can handle strings as well as streams for parsing and generating.  
Offers additional utilities such as an UTF8CharInputStream to read single UTF-8 characters from an InputStream.  
No other libraries required.  

## How to use:
``` java
String jsonStr = "{\"Hello World\":[{\"Test\":1.2,\"Bye\":null},345,]}";
		
// Automatically detects class needed to parse JSON
JSON<?> autoJson = JSON.parseAutoJSON(jsonStr);
System.out.println(autoJson.toString(true));

// Another example that shows how errors in the JSON data can be handled
try {
	JsonObject jsonObj = JsonObject.parse(jsonStr);
	System.out.println(jsonObj.toString());
} catch(JsonParseException ex) {
	LineColumnTracker lct = ex.getLineColumn();
	if(lct != null)
		System.out.println("Error occurred at line "+lct.getLine()+", column "+lct.getColumn());
	else
		ex.printStackTrace();
}
```

## References:
 - [LupCode.com](https://lupcode.com)
 - [Lup.services](https://lup.services)
