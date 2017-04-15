# JythonScript v2.0

[![Build Status](https://travis-ci.org/adchilds/JythonScript.svg?branch=develop)](https://travis-ci.org/adchilds/JythonScript) [![Coverage Status](https://coveralls.io/repos/github/adchilds/JythonScript/badge.svg?branch=develop)](https://coveralls.io/github/adchilds/JythonScript?branch=develop) [![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.adchilds/jythonscript/badge.svg)](http://search.maven.org/#artifactdetails%7Ccom.github.adchilds%7Cjythonscript%7C1.0%7C)

## Description:
JythonScript is a simple Jython wrapper for easier execution and/or evaluation of Python expressions and scripts at 
runtime for JVM-based languages.

## Maven

    <dependency>
      <groupId>com.github.adchilds</groupId>
      <artifactId>jythonscript</artifactId>
      <version>1.0</version>
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

## Useful Links:
* JythonScript - https://github.com/adchilds/JythonScript
* Python - https://www.python.org
* Jython - http://www.jython.org