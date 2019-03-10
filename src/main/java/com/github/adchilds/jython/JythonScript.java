package com.github.adchilds.jython;

import com.github.adchilds.jython.exception.JythonResultNotFoundException;
import com.github.adchilds.jython.exception.JythonScriptException;
import com.github.adchilds.jython.exception.JythonScriptNotFoundException;
import com.github.adchilds.util.FileUtils;
import com.github.adchilds.util.StringUtils;
import org.python.core.*;
import org.python.util.PythonInterpreter;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * {@link JythonScript} provides an easy to use wrapper for executing and/or evaluating Python expressions or scripts
 * for JVM-based languages. JythonScript packages the Jython standalone JAR as part of it's distribution, so no further
 * dependencies should be required.
 *
 * JythonScript operates on a couple of select criteria:
 * <ol>
 *     <li>A Jython or Python script accessible by the JVM via a {@link String} file path, {@link File}, or {@link InputStream}</li>
 *     <li>Optional arguments to be passed to the given script</li>
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
 * (index 0 [sys.argv[0]]) is reserved. Therefore, Jython scripts should always access these arguments beginning with
 * the second index of sys.argv (i.e. sys.argv[1]).
 *
 * <br>
 * <br>
 *
 * JythonScript provides {@code #compile(...)} functions that compile the given scripts into {@link PyCode} objects. For
 * speed increases at runtime, it's better to pre-compile Jython scripts with these functions, maintain the PyCode
 * objects via an in-memory cache or local variable, and execute or evaluate against the compiled scripts. JythonScript
 * provides the necessary {@link #evaluate(PyCode, Object...)} and {@link #execute(PyCode, Object...)} methods to
 * foster these speed increases.
 *
 * <br>
 * <br>
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
 *     <li>JythonScript - https://github.com/adchilds/JythonScript</li>
 *     <li>Python - https://www.python.org</li>
 *     <li>Jython - http://www.jython.org</li>
 * </ul>
 *
 * @author Adam Childs
 * @since 1.0
 */
public class JythonScript {

    private static final String EVALUATION_RESULT_LOCAL_VARIABLE = "result";

    /**
     * Compiles the Jython script at the given {@code filePath} into a {@link PyCode} object.
     *
     * @param filePath the absolute path of the Jython file to compile
     * @return a compiled Jython script
     * @throws JythonScriptException when the given file path is empty or null
     * @since 1.0
     */
    public static PyCode compile(final String filePath) throws JythonScriptException {
        // Make sure the file path is is not null or empty
        if (StringUtils.isBlank(filePath)) {
            throw new JythonScriptException("Null or empty path is not a file.");
        }

        // Compile the script
        return compile(new File(filePath));
    }

    /**
     * Compiles the Jython script at the given {@code fileUrl} into a {@link PyCode} object.
     *
     * @param fileUrl the {@link URL} to a Jython file to compile
     * @return a compile Jython script
     * @throws JythonScriptException when the given URL is empty, null, or not a valid path
     * @since 2.0
     */
    public static PyCode compile(final URL fileUrl) throws JythonScriptException {
        if (fileUrl == null) {
            throw new JythonScriptException("Null path is not a URL.");
        }

        final File file;
        try {
            file = new File(fileUrl.toURI());
        } catch (Exception e) {
            throw new JythonScriptException("Could not convert URL to File.", e);
        }

        return compile(file);
    }

    /**
     * Compiles the given Jython script into a {@link PyCode} object.
     *
     * @param file the Jython file to compile
     * @return a compiled Jython script
     * @throws JythonScriptException when the given file is null or is not a file (i.e. a directory)
     * @since 1.0
     */
    public static PyCode compile(final File file) throws JythonScriptException {
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
            return compileString(FileUtils.readFully(file));
        } catch (IOException e) {
            throw new JythonScriptException("Could not compile the given file. file=[" +
                    file.getAbsolutePath() + "]", e);
        }
    }

    /**
     * Compiles the given Jython script into a {@link PyCode} object.
     *
     * @param script the Jython script to compile (this should be actual Jython code represented as a {@link String})
     * @return a compiled Jython script
     * @throws JythonScriptException when the given script is null or empty
     * @since 2.0
     */
    public static PyCode compileString(final String script) throws JythonScriptException {
        if (StringUtils.isBlank(script)) {
            throw new JythonScriptException("Given script was null or empty; cannot be compiled into PyCode.");
        }

        final PythonInterpreter interpreter = new PythonInterpreter();

        // Compile the script, returning the associated PyCode object
        return interpreter.compile(script);
    }

    /**
     * Evaluates the Jython script at the given {@code scriptPath}, returning the result as its equivalent Java type.
     * Accepts optional arguments to be passed to the script at runtime. {@code args} should be interpreted as 'sys.argv'
     * arguments in the given script. Note that the arguments passed in here will begin at the first index in a Jython
     * scripts sys.argv list.
     *
     * @param scriptPath the fully qualified path of the Jython script to execute
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException when the given file path is null, a directory, or cannot be found
     * @since 1.0
     */
    public static Object evaluate(final String scriptPath, final Object... args) throws JythonScriptException {
        // Ensure that the scriptRelativePath is not null or empty
        if (StringUtils.isBlank(scriptPath)) {
            throw new JythonScriptNotFoundException("File not found at path=[" + scriptPath + "]");
        }

        // Open an InputStream for the file
        final InputStream inputStream;
        try {
            inputStream = FileUtils.getFileInputStream(scriptPath);
        } catch (Exception e) {
            throw new JythonScriptNotFoundException("Could not open Jython script from location=[" + scriptPath + "]", e);
        }

        return evaluate(inputStream, args);
    }

    /**
     * Evaluates the Jython script at the given {@code scriptUrl}, returning the result as its equivalent Java type.
     * Accepts optional arguments to be passed to the script at runtime. {@code args} should be interpreted as 'sys.argv'
     * arguments in the given script. Note that the arguments passed in here will begin at the first index in a Jython
     * scripts sys.argv list.
     *
     * @param scriptUrl the {@link URL} to a Jython script to execute
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException when the given script is null, a directory, or cannot be found
     * @since 2.0
     */
    public static Object evaluate(final URL scriptUrl, final Object... args) throws JythonScriptException {
        if (scriptUrl == null) {
            throw new JythonScriptException("Null path is not a URL.");
        }

        // Convert the URL to a File
        final File file;
        try {
            file = new File(scriptUrl.toURI());
        } catch (Exception e) {
            throw new JythonScriptException("Could not convert URL to File.", e);
        }

        // Open an InputStream for the file
        final InputStream inputStream;
        try {
            inputStream = FileUtils.getFileInputStream(file);
        } catch (Exception e) {
            throw new JythonScriptNotFoundException("Could not open Jython script from location=[" + scriptUrl.getPath() + "]", e);
        }

        return evaluate(inputStream, args);
    }

    /**
     * Evaluates the given Jython script, returning the result as its equivalent Java type. Accepts optional arguments
     * to be passed to the script at runtime. {@code args} should be interpreted as 'sys.argv' arguments in the given
     * script. Note that the arguments passed in here will begin at the first index in a Jython scripts sys.argv list.
     *
     * @param scriptFile the Jython script to execute
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException when the given file is null, a directory, or cannot be found
     * @since 1.0
     */
    public static Object evaluate(final File scriptFile, final Object... args) throws JythonScriptException {
        // Ensure that the script is not null
        if (scriptFile == null) {
            throw new JythonScriptNotFoundException("Could not open Jython script, the file was null.");
        }

        // Open an InputStream for the file
        final InputStream inputStream;
        try {
            inputStream = FileUtils.getFileInputStream(scriptFile);
        } catch (Exception e) {
            throw new JythonScriptNotFoundException("Could not open Jython script from location=[" + scriptFile.getAbsolutePath() + "]", e);
        }

        // Execute the script
        return evaluate(inputStream, args);
    }

    /**
     * Evaluates the given Jython script, returning the result as its equivalent Java type. Accepts optional arguments
     * to be passed to the script at runtime. {@code args} should be interpreted as 'sys.argv' arguments in the given
     * script. Note that the arguments passed in here will begin at the first index in a Jython scripts sys.argv list.
     *
     * @param inputStream the {@link InputStream} that represents the Jython script to be executed
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException when a script execution error occurs or when a local Python variable named 'result' is not found
     * @since 1.0
     */
    public static Object evaluate(final InputStream inputStream, final Object... args) throws JythonScriptException {
        // Execute the script
        final PythonInterpreter interpreter = executeWithState(inputStream, args);

        // Obtain the value of a local variable named 'result' from the executed script
        final PyObject result = interpreter.get(EVALUATION_RESULT_LOCAL_VARIABLE);

        if (result == null) {
            throw new JythonResultNotFoundException("Local variable 'result' not found during script execution.");
        }

        return parseResult(result);
    }

    /**
     * Evaluates the given Jython script, returning the result as it's equivalent Java type. Accepts optional arguments
     * to be passed to the script at runtime. {@code args} should be interpreted as 'sys.argv' arguments in the given
     * script. Note that the arguments passed in here will begin at the first index in a Jython scripts sys.argv list.
     *
     * @param pyCode the compiled Jython script to evaluate
     * @param args arguments to be passed to the script
     * @return the result from executing the given script
     * @throws JythonScriptException when a script execution error occurs or when a local Python variable named 'result' is not found
     * @since 1.0
     */
    public static Object evaluate(final PyCode pyCode, final Object... args) throws JythonScriptException {
        // Execute the script
        final PythonInterpreter interpreter = executeWithState(pyCode, args);

        // Obtain the value of a local variable named 'result' from the executed script
        final PyObject result = interpreter.get(EVALUATION_RESULT_LOCAL_VARIABLE);

        if (result == null) {
            throw new JythonResultNotFoundException("Local variable 'result' not found during script execution.");
        }

        return parseResult(result);
    }

    /**
     * Executes the Jython script at the given {@code scriptPath} with optional arguments passed to the script at
     * runtime. {@code args} should be interpreted as 'sys.argv' arguments in the given script. Note that the arguments
     * passed in here will begin at the first index in a Jython scripts sys.argv list.
     *
     * @param scriptPath the fully qualified path of the Jython script to execute
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given file path is null, a directory, or cannot be found
     * @since 1.0
     */
    public static void execute(final String scriptPath, final Object... args) throws JythonScriptException {
        // Ensure that the scriptRelativePath is not null or empty
        if (StringUtils.isBlank(scriptPath)) {
            throw new JythonScriptNotFoundException("File not found at path=[" + scriptPath + "]");
        }

        // Open an InputStream for the file
        final InputStream inputStream;
        try {
            inputStream = FileUtils.getFileInputStream(scriptPath);
        } catch (Exception e) {
            throw new JythonScriptNotFoundException("Could not open Jython script from location=[" + scriptPath + "]", e);
        }

        // Execute the script
        execute(inputStream, args);
    }

    /**
     * Executes the Jython script at the given {@code scriptUrl} with optional arguments passed to the script at
     * runtime. {@code args} should be interpreted as 'sys.argv' arguments in the given script. Note that the arguments
     * passed in here will begin at the first index in a Jython scripts sys.argv list.
     *
     * @param scriptUrl the {@link URL} to a Jython script to execute
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given file is null, a directory, or cannot be found
     * @since 2.0
     */
    public static void execute(final URL scriptUrl, final Object... args) throws JythonScriptException {
        if (scriptUrl == null) {
            throw new JythonScriptException("Null path is not a URL.");
        }

        // Convert the URL to a File
        final File file;
        try {
            file = new File(scriptUrl.toURI());
        } catch (Exception e) {
            throw new JythonScriptException("Could not convert URL to File.", e);
        }

        // Open an InputStream for the file
        final InputStream inputStream;
        try {
            inputStream = FileUtils.getFileInputStream(file);
        } catch (Exception e) {
            throw new JythonScriptNotFoundException("Could not open Jython script from location=[" + scriptUrl.getPath() + "]", e);
        }

        execute(inputStream, args);
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime. {@code args} should be
     * interpreted as 'sys.argv' arguments in the given script. Note that the arguments passed in here will begin at
     * the first index in a Jython scripts sys.argv list.
     *
     * @param scriptFile the Jython script to execute
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given file is null, a directory, or cannot be found
     * @since 1.0
     */
    public static void execute(final File scriptFile, final Object... args) throws JythonScriptException {
        // Ensure that the script is not null
        if (scriptFile == null) {
            throw new JythonScriptNotFoundException("Could not open Jython script, the file was null.");
        }

        // Open an InputStream for the file
        final InputStream inputStream;
        try {
            inputStream = FileUtils.getFileInputStream(scriptFile);
        } catch (Exception e) {
            throw new JythonScriptNotFoundException("Could not open Jython script from location=[" + scriptFile.getAbsolutePath() + "]", e);
        }

        // Execute the script
        execute(inputStream, args);
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime. {@code args} should be
     * interpreted as 'sys.argv' arguments in the given script. Note that the arguments passed in here will begin at
     * the first index in a Jython scripts sys.argv list.
     *
     * @param inputStream the {@link InputStream} that represents the Jython script to be executed
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given inputstream is null or a script execution error occurs
     * @since 1.0
     */
    public static void execute(final InputStream inputStream, final Object... args) throws JythonScriptException {
        if (inputStream == null) {
            throw new JythonScriptException("Cannot execute a Jython script that doesn't exist! InputStream is null.");
        }

        // Set the arguments on the Python System State
        final PythonInterpreter interpreter = updateInterpreterState(args);

        try {
            // Execute the script
            interpreter.execfile(inputStream);
        } catch (Exception e) {
            throw new JythonScriptException("An error occurred during script execution. cause=[\n\t" + e.toString() + "]");
        }
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime. {@code args} should be
     * interpreted as 'sys.argv' arguments in the given script. Note that the arguments passed in here will begin at
     * the first index in a Jython scripts sys.argv list.
     *
     * @param pyCode the compiled Jython script to evaluate
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given PyCode is null or a script execution error occurs
     * @since 1.0
     */
    public static void execute(final PyCode pyCode, final Object... args) throws JythonScriptException {
        if (pyCode == null) {
            throw new JythonScriptException("Cannot execute a Jython script that doesn't exist! InputStream is null.");
        }

        // Set the arguments on the Python System State
        final PythonInterpreter interpreter = updateInterpreterState(args);

        try {
            // Execute the script
            interpreter.exec(pyCode);
        } catch (Exception e) {
            throw new JythonScriptException("An error occurred during script execution. cause=[\n\t" + e.toString() + "]");
        }
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime. {@code args} should be
     * interpreted as 'sys.argv' arguments in the given script. Note that the arguments passed in here will begin at
     * the first index in a Jython scripts sys.argv list.
     *
     * This function returns the {@link PythonInterpreter} state after executing the given Jython code.
     *
     * @param inputStream the {@link InputStream} that represents the Jython script to be executed
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given inputstream is null or a script execution error occurs
     * @since 2.0.1
     */
    private static PythonInterpreter executeWithState(final InputStream inputStream, final Object... args) throws JythonScriptException {
        if (inputStream == null) {
            throw new JythonScriptException("Cannot execute a Jython script that doesn't exist! InputStream is null.");
        }

        // Set the arguments on the Python System State
        final PythonInterpreter interpreter = updateInterpreterState(args);

        try {
            // Execute the script
            interpreter.execfile(inputStream);
        } catch (Exception e) {
            throw new JythonScriptException("An error occurred during script execution. cause=[\n\t" + e.toString() + "]");
        }

        return interpreter;
    }

    /**
     * Executes the given Jython script with optional arguments passed to the script at runtime. {@code args} should be
     * interpreted as 'sys.argv' arguments in the given script. Note that the arguments passed in here will begin at
     * the first index in a Jython scripts sys.argv list.
     *
     * This function returns the {@link PythonInterpreter} state after executing the given Jython code.
     *
     * @param pyCode the compiled Jython script to evaluate
     * @param args arguments to be passed to the script
     * @throws JythonScriptException when the given PyCode is null or a script execution error occurs
     * @since 2.0.1
     */
    private static PythonInterpreter executeWithState(final PyCode pyCode, final Object... args) throws JythonScriptException {
        if (pyCode == null) {
            throw new JythonScriptException("Cannot execute a Jython script that doesn't exist! InputStream is null.");
        }

        // Set the arguments on the Python System State
        final PythonInterpreter interpreter = updateInterpreterState(args);

        try {
            // Execute the script
            interpreter.exec(pyCode);
        } catch (Exception e) {
            throw new JythonScriptException("An error occurred during script execution. cause=[\n\t" + e.toString() + "]");
        }

        return interpreter;
    }

    /**
     * Updates the {@link PythonInterpreter}s {@link PySystemState} by adding the given {@code args}. These arguments
     * may be accessed from within Jython scripts via the 'sys.argv' parameters, beginning at the second index (i.e.
     * sys.argv[1]). Note: the first index is reserved.
     *
     * @param args the arguments to set on the {@link PySystemState} for the current {@link PythonInterpreter}
     * @since 1.0
     */
    private static PythonInterpreter updateInterpreterState(final Object... args) {
        // Setup the Python System State by appending the given arguments
        final PySystemState state = parseArguments(args);

        // Add the arguments to the PythonInterpreter
        return new PythonInterpreter(null, state);
    }

    /**
     * Given an arbitrary set of arguments, parses each individually into it's correct Jython type, before appending the
     * value to the set of arguments to be passed to the executing script.
     *
     * @param args the arguments to parse before being sent to a Python script
     * @since 1.0
     */
    private static PySystemState parseArguments(final Object... args) {
        final PySystemState systemState = new PySystemState();

        for (final Object arg : args) {
            systemState.argv.append(Py.java2py(arg));
        }

        return systemState;
    }

    /**
     * Given a {@link PyObject} attempts to convert the object to it's Java representation. If an equivalent java type
     * cannot be found, the original PyObject is returned.
     *
     * Current supported type conversions:
     * <ul>
     *     <li>{@link PyBoolean} to boolean</li>
     *     <li>{@link PyInteger} to int</li>
     *     <li>{@link PyString} to {@link String}</li>
     *     <li>{@link PyFloat} to float</li>
     *     <li>{@link PyLong} to long</li>
     *     <li>{@link PyList} to an array of {@link Object}s</li>
     *     <li>{@link PyDictionary} to a {@link Map} of {@link Object}s</li>
     *     <li>{@link PySet} to a {@link Set} of {@link Object}s</li>
     * </ul>
     *
     * @param object the object to convert to it's equivalent Java type, if supported; otherwise, returns the unconverted {@link PyObject}
     * @return the Java type representation of the given {@link PyObject}
     * @since 1.0
     */
    private static Object parseResult(final PyObject object) {
        if (object == null) {
            // We should never get here since evaluate provides this check; but, just in case.
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
        } else if (object instanceof PyList) {
            return parsePyObjectList(((PyList) object).getArray());
        } else if (object instanceof PyDictionary) {
            return parsePyObjectDict(((PyDictionary) object).getMap());
        } else if (object instanceof PySet) {
            return parsePyObjectSet(((PySet) object).getSet());
        }

        return object;
    }

    /**
     * Converts the given array of {@link PyObject}s to an array of the corresponding Java types for each value in the array.
     *
     * @param pyObjects an array of {@link PyObject}s to parse
     * @return a new array of {@link Object}s
     * @since 1.0
     */
    private static Object[] parsePyObjectList(final PyObject[] pyObjects) {
        final Object[] objects = new Object[pyObjects.length];

        int index = 0;
        for (final PyObject pyObject : pyObjects) {
            objects[index] = parseResult(pyObject);

            index++;
        }

        return objects;
    }

    /**
     * Converts the given {@link Map} of {@link PyObject}s to a Map of the corresponding Java types.
     *
     * @param pyDict the dictionary to parse
     * @return a new {@link Map} of {@link Object}s
     */
    private static Map<Object, Object> parsePyObjectDict(final Map<PyObject, PyObject> pyDict) {
        final Map<Object, Object> objects = new HashMap<>();

        for (final Map.Entry entry : pyDict.entrySet()) {
            final Object key = parseResult((PyObject) entry.getKey());
            final Object value = parseResult((PyObject) entry.getValue());

            objects.put(key, value);
        }

        return objects;
    }

    /**
     * Converts the given {@link Set} of {@link PyObject}s to a Set of the corresponding Java types.
     *
     * @param pySet the set to parse
     * @return a new {@link Set} of {@link Object}s
     * @since 1.0
     */
    private static Set<Object> parsePyObjectSet(final Set<PyObject> pySet) {
        final Set<Object> objects = new HashSet<>();

        for (final PyObject pyObject : pySet) {
            objects.add(parseResult(pyObject));
        }

        return objects;
    }

    // Don't allow this class to be instantiated
    private JythonScript() { }

}