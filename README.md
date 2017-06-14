# JythonScript v2.0

[![Build Status](https://travis-ci.org/adchilds/JythonScript.svg?branch=develop)](https://travis-ci.org/adchilds/JythonScript) [![Coverage Status](https://coveralls.io/repos/github/adchilds/JythonScript/badge.svg?branch=develop)](https://coveralls.io/github/adchilds/JythonScript?branch=develop) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.adchilds/jythonscript/badge.svg)](http://search.maven.org/#search%7Cga%7C1%7Ca%3A%22jythonscript%22)

## Description:
JythonScript is a simple Jython wrapper for easier execution and/or evaluation of Python expressions and scripts at 
runtime for JVM-based languages.

## Maven

    <dependency>
      <groupId>com.github.adchilds</groupId>
      <artifactId>jythonscript</artifactId>
      <version>2.0</version>
    </dependency>

## Dependencies:
None. JythonScript packages the Jython standalone JAR as part of it's distribution, so no further dependencies are
required.

## Examples:

  test.py:
	
	from java.lang import System
	
	def test():
		System.out.println('Hello from Jython')
	
	if __name__ == '__main__':
		test()

  Test.java:

	import com.github.adchilds.jython.JythonScript;
	
	public class Test {
	
		public static void main(String[] args) {
			String filePath = ClassLoader.getSystemResource("test.py").getPath();
			
			JythonScript.execute(filePath);
		}
	}
	
  Output: "Hello from Jython"
  
  
  test2.py:
  	
	import sys
	
	def multiply(a, b):
		return a * b

	if __name__ == '__main__':
		a = sys.argv[1]
		b = sys.argv[2]

		result = multiply(a, b)

  Test2.java

	import com.github.adchilds.jython.JythonScript;
	
	public class Test2 {
	
		public static void main(String[] args) {
			String filePath = ClassLoader.getSystemResource("test2.py").getPath();
			
			Integer result = JythonScript.evaluate(filePath, 5, 5);
			System.out.println("Result = [" + result + "]");
		}
	}
	
  Output: "Result = [25]"


  Test3.java
  
    import com.github.adchilds.jython.JythonScript;
    	
    public class Test2 {
    	
    	public static void main(String[] args) {
    		String script = "from java.lang import System\n" +
                            "\t\n" +
                            "\tdef test():\n" +
                            "\t\tSystem.out.println('Hello from Jython')\n" +
                            "\t\n" +
                            "\tif __name__ == '__main__':\n" +
                            "\t\ttest()";
    		
    		PyCode compiledScript = JythonScript.compileString(script);
    		JythonScript.execute(compiledScript);
    	}
    }

  Output: "Hello from Jython"


## Useful Links:
* JythonScript - https://github.com/adchilds/JythonScript
* Python - https://www.python.org
* Jython - http://www.jython.org