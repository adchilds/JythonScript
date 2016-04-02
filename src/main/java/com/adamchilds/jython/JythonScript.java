package com.adamchilds.jython;

import com.adamchilds.jython.exception.JythonResultNotFoundException;
import com.adamchilds.jython.exception.JythonScriptException;
import com.adamchilds.jython.exception.JythonScriptNotFoundException;
import com.adamchilds.util.FileUtil;
import com.adamchilds.util.StringUtil;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * {@link JythonScript} provides an easy hook for executing and/or evaluating Python expressions or scripts in the Java
 * Runtime Environment. JythonScript packages the Jython standalone JAR as part of it's distribution, so no further
 * dependencies should be required.
 *
 * JythonScript operates on a couple of select criteria:
 * <ol>
 *     <li>A local (to the JVM host operating system) Jython script</li>
 *     <li>Optional arguments passed to the given script</li>
 * </ol>
 *
 * To use this utility class, you must follow a few strict rules in creating Jython scripts:
 * <ul>
 *     <li>Each script passed to an {@code #evaluate(...)} or {@code #execute(...)} function, such as {@link
 *     JythonScript#evaluate(String, Object...)} or {@link JythonScript#execute(String, Object...)} MUST be setup as a
 *     standard Python main module. This means that the following code must exist:
 *     <pre>
 *     {@code if __name__ == '__main__':
 *          ...
 *          ...
 *     }
 *     </pre>
 *     </li>
 *     <li>If running an evaluation via any of the {@code #evaluate(...)} functions, and expecting a valid result to be
 *     returned, you MUST push the resulting value to a local Python variable named 'result', otherwise JythonScript
 *     will not be able to locate this value within the Python interpreter.</li>
 * </ul>
 *
 * When consuming arguments passed to a Jython script via the 'Object... args' parameter of an execute or evaluate
 * method, the arguments will be available via Python's sys.argv list. It's important to note that the first argument
 * (index 0 [sys.argv[0]]) is reserved. Therefore, Jython scripts should always begin accessing these arguments via the
 * second index of sys.argv (i.e. sys.argv[1]).
 *
 * <br />
 * <br />
 *
 * JythonScript provides {@code #compile(...)} functions that compile the given scripts into {@link PyCode} objects. For
 * speed increases at runtime, it's better to pre-compile Jython scripts with these functions, maintain the PyCode
 * objects via an in-memory cache or local variable, and execute or evaluate with the compiled scripts. JythonScript
 * provides the necessary {@link #evaluate(PyCode, Object...)} and {@link #execute(PyCode, Object...)} methods to
 * foster these speed increases.
 *
 * <br />
 * <br />
 *
 * Note: JythonScript does not dictate any other strict code structure. You are free to use any of the features provided
 * by Java, Python and Jython, including, but not limited to:
 * <ul>
 *     <li>OOP</li>
 *     <li>Python Standard Library</li>
 *     <li>Java Standard Library</li>
 *     <li>etc.</li>
 * </ul>
 *
 * For more information, follow the respective links to documentation:
 * <ul>
 *     <li>JythonScript - https://github.com/adchilds/jythonutil</li>
 *     <li>Python - https://www.python.org</li>
 *     <li>Jython - http://www.jython.org</li>
 * </ul>
 *
 * @author Adam Childs
 * @since 0.1
 */
public class JythonScript {

    private static PythonInterpreter INTERPRETER = new PythonInterpreter();

    private static final String EVALUATION_RESULT_LOCAL_VARIABLE = "result";

    /**
     * Compiles the Jython script at the given {@code filePath} into a {@link PyCode} object.
     *
     * @param filePath the absolute path of the Jython file to compile
     * @return a compiled Jython script
     * @throws JythonScriptException
     */
    public static PyCode compile(String filePath) throws JythonScriptException {
        // Make sure the file path is is not null or empty
        if (StringUtil.isBlank(filePath)) {
            throw new JythonScriptException("Given path is not a file. path=[" + filePath + "]");
        }

        // Compile the script
        return compile(new File(filePath));
    }

    /**
     * Compiles the given Jython script into a {@link PyCode} object.
     *
     * @param file the Jython file to compile
     * @return a compiled Jython script
     * @throws JythonScriptException
     */
    public static PyCode compile(File file) throws JythonScriptException {
        if (file == null) {
            throw new JythonScriptException("Given file is null; cannot be compiled into PyCode.");
        }

        // Make sure the file is actually a valid file
        if (!file.isFile()) {
            throw new JythonScriptException("Given file object is not a file. Is it a directory? file=[" +
                    file.getAbsolutePath() + "]");
        }

        // Compile the file, returning the associated PyCode object
        try {
            return INTERPRETER.compile(FileUtil.readFully(file));
        } catch (IOException e) {
            throw new JythonScriptException("Could not compile the given file. file=[" +
                    file.getAbsolutePath() + "]", e);
        }
    }

    /**
     * Evaluates the Jython script at the given {@code scriptPath}, returning the result as it's equivalent Java type.
     * Accepts optional arguments to be passed to the script at runtime.
     *
     * @param scriptPath the fully qualified path of the Jython script to execute
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException
     */
    public static Object evaluate(String scriptPath, Object... args) throws JythonScriptException {
        // Ensure that the scriptRelativePath is not null or empty
        if (StringUtil.isBlank(scriptPath)) {
            throw new JythonScriptNotFoundException("File not found at path=[" + scriptPath + "]");
        }

        // Open an InputStream for the file
        InputStream inputStream;
        try {
            inputStream = FileUtil.getFileInputStream(scriptPath);
        } catch (Exception e) {
            throw new JythonScriptException("Could not open Jython script from location=[" + scriptPath + "]", e);
        }

        return evaluate(inputStream, args);
    }

    /**
     * Evaluates the given Jython script, returning the result as it's equivalent Java type. Accepts optional arguments
     * to be passed to the script at runtime.
     *
     * @param scriptFile the Jython script to execute
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException
     */
    public static Object evaluate(File scriptFile, Object... args) throws JythonScriptException {
        // Ensure that the script is not null
        if (scriptFile == null) {
            throw new JythonScriptNotFoundException("Could not open Jython script, the file was null.");
        }

        // Open an InputStream for the file
        InputStream inputStream;
        try {
            inputStream = FileUtil.getFileInputStream(scriptFile);
        } catch (Exception e) {
            throw new JythonScriptException("Could not open Jython script from location=[" + scriptFile.getAbsolutePath() + "]", e);
        }

        // Execute the script
        return evaluate(inputStream, args);
    }

    /**
     * Evaluates the given Jython script, returning the result as it's equivalent Java type. Accepts optional arguments
     * to be passed to the script at runtime.
     *
     * @param inputStream the {@link InputStream} that represents the Jython script to be executed
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException
     */
    public static Object evaluate(InputStream inputStream, Object... args) throws JythonScriptException {
        // Execute the script
        execute(inputStream, args);

        // Obtain the value of a local variable named 'result' from the executed script
        PyObject result = INTERPRETER.get(EVALUATION_RESULT_LOCAL_VARIABLE);

        if (result == null) {
            throw new JythonResultNotFoundException("Local variable 'result' not found during script execution.");
        }

        return parseResult(result);
    }

    /**
     * Evaluates the given Jython script, returning the result as it's equivalent Java type. Accepts optional arguments
     * to be passed to the script at runtime.
     *
     * @param pyCode the compiled Jython script to evaluate
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException
     */
    public static Object evaluate(PyCode pyCode, Object... args) throws JythonScriptException {
        // Execute the script
        execute(pyCode, args);

        // Obtain the value of a local variable named 'result' from the executed script
        PyObject result = INTERPRETER.get(EVALUATION_RESULT_LOCAL_VARIABLE);

        if (result == null) {
            throw new JythonResultNotFoundException("Local variable 'result' not found during script execution.");
        }

        return parseResult(result);
    }

    /**
     * Executes the Jython script at the given {@code scriptPath} with optional arguments passed to the script at
     * runtime.
     *
     * @param scriptPath the fully qualified path of the Jython script to execute
     * @param args arguments to be passed to the script
     * @throws JythonScriptException
     */
    public static void execute(String scriptPath, Object... args) throws JythonScriptException {
        // Ensure that the scriptRelativePath is not null or empty
        if (StringUtil.isBlank(scriptPath)) {
            throw new JythonScriptNotFoundException("File not found at path=[" + scriptPath + "]");
        }

        // Open an InputStream for the file
        InputStream inputStream;
        try {
            inputStream = FileUtil.getFileInputStream(scriptPath);
        } catch (Exception e) {
            throw new JythonScriptException("Could not open Jython script from location=[" + scriptPath + "]", e);
        }

        // Execute the script
        execute(inputStream, args);
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime.
     *
     * @param scriptFile the Jython script to execute
     * @param args arguments to be passed to the script
     * @throws JythonScriptException
     */
    public static void execute(File scriptFile, Object... args) throws JythonScriptException {
        // Ensure that the script is not null
        if (scriptFile == null) {
            throw new JythonScriptNotFoundException("Could not open Jython script, the file was null.");
        }

        // Open an InputStream for the file
        InputStream inputStream;
        try {
            inputStream = FileUtil.getFileInputStream(scriptFile);
        } catch (Exception e) {
            throw new JythonScriptException("Could not open Jython script from location=[" + scriptFile.getAbsolutePath() + "]", e);
        }

        // Execute the script
        execute(inputStream, args);
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime.
     *
     * @param inputStream the {@link InputStream} that represents the Jython script to be executed
     * @param args arguments to be passed to the script
     * @throws JythonScriptException
     */
    public static void execute(InputStream inputStream, Object... args) throws JythonScriptException {
        if (inputStream == null) {
            throw new JythonScriptException("Cannot execute a Jython script that doesn't exist! InputStream is null.");
        }

        // Set the arguments on the Python System State
        updateInterpreterState(args);

        try {
            // Execute the script
            INTERPRETER.execfile(inputStream);
        } catch (Exception e) {
            throw new JythonScriptException("An error occurred during script execution. cause=[\n\t" + e.toString() + "]");
        }
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime.
     *
     * @param pyCode the compiled Jython script to evaluate
     * @param args arguments to be passed to the script
     * @throws JythonScriptException
     */
    public static void execute(PyCode pyCode, Object... args) throws JythonScriptException {
        if (pyCode == null) {
            throw new JythonScriptException("Cannot execute a Jython script that doesn't exist! InputStream is null.");
        }

        // Set the arguments on the Python System State
        updateInterpreterState(args);

        try {
            // Execute the script
            INTERPRETER.exec(pyCode);
        } catch (Exception e) {
            throw new JythonScriptException("An error occurred during script execution. cause=[\n\t" + e.toString() + "]");
        }
    }

    /**
     * Updates the {@link PythonInterpreter}s {@link PySystemState} by adding the given {@code args}. These arguments
     * may be accessed from within Jython scripts via the 'sys.argv' parameters, beginning at the second index (i.e.
     * sys.argv[1]). Note: the first index is reserved.
     *
     * @param args the arguments to set on the {@link PySystemState} for the current {@link PythonInterpreter}
     */
    private static void updateInterpreterState(Object... args) {
        // Setup the Python System State by appending the given arguments
        PySystemState state = parseArguments(args);

        // Add the arguments to the PythonInterpreter
        INTERPRETER = new PythonInterpreter(null, state);
    }

    /**
     * Given an arbitrary set of arguments, parses each individually into it's correct Jython type, before appending the
     * value to the set of arguments to be passed to the executing script.
     *
     * @param args the arguments to parse before being sent to a Python script
     */
    private static PySystemState parseArguments(Object... args) {
        PySystemState systemState = new PySystemState();

        for (Object arg : args) {
            systemState.argv.append(Py.java2py(arg));
        }

        return systemState;
    }

    /**
     * Given a {@link PyObject} attempts to convert the object to it's Java representation. If an equivalent java type
     * cannot be found, null is returned.
     *
     * Current supported type conversions:
     * <ul>
     *     <li>{@link PyBoolean} to boolean</li>
     *     <li>{@link PyInteger} to int</li>
     *     <li>{@link PyString} to {@link String}</li>
     *     <li>{@link PyFloat} to float</li>
     *     <li>{@link PyLong} to long</li>
     * </ul>
     *
     * @param object the object to convert to it's equivalent Java type
     * @return the Java type representation of the given {@link PyObject}
     */
    private static Object parseResult(PyObject object) {
        if (object == null) {
            // We should never get here since evaluate provides this check; but, just incase.
            return null;
        }

        if (object instanceof PyBoolean) {
            return Py.py2boolean(object);
        } else if (object instanceof PyInteger) {
            return Py.py2int(object);
        } else if (object instanceof PyString) {
            return ((PyString) object).getString();
        } else if (object instanceof PyFloat) {
            return Py.py2float(object);
        } else if (object instanceof PyLong) {
            return Py.py2long(object);
        }

        return null;
    }

    private JythonScript() { }

}