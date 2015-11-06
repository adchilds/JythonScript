# JythonScript v0.1

## Description:
JythonScript provides an easy to use wrapper for executing and/or evaluating Python expressions and scripts in the Java 
Runtime Environment.

## Dependencies:
None. JythonScript packages the Jython standalone JAR as part of it's distribution, so no further dependencies are
required.

## Examples:

  test.py:
	
	import sys
	
	def test():
		print 'Hello from Jython'
	
	if __name__ == '__main__':
		test()

  Test.java:

	import com.adamchilds.jython.JythonScript;
	
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

	import com.adamchilds.jython.JythonScript;
	
	public class Test2 {
	
		public static void main(String[] args) {
			String filePath = ClassLoader.getSystemResource("test2.py").getPath();
			
			Integer result = JythonScript.evaluate(filePath, 5, 5);
			System.out.println("Result = [" + result + "]");
		}
	}
	
  Output: "Result = [25]"

## Useful Links:
* JythonScript - https://github.com/adchilds/jythonutil
* Python - https://www.python.org
* Jython - http://www.jython.org