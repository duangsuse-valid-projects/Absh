package bsh;

/**
 * UtilEvalError is an error corresponding to an EvalError but thrown by a utility or other class
 * that does not have the caller context (Node) available to it. A normal EvalError must supply the
 * caller Node in order for error messages to be pinned to the correct line and location in the
 * script. UtilEvalError is a checked exception that is *not* a subtype of EvalError, but instead
 * must be caught and rethrown as an EvalError by the a nearest location with context. The method
 * toEvalError( Node ) should be used to throw the EvalError, supplying the node.
 *
 * <p>To summarize: Utilities throw UtilEvalError. ASTs throw EvalError. ASTs catch UtilEvalError
 * and rethrow it as EvalError using toEvalError( Node ).
 *
 * <p>Philosophically, EvalError and UtilEvalError corrospond to RuntimeException. However they are
 * constrained in this way in order to add the context for error reporting.
 *
 * @see UtilTargetError
 */
public class UtilEvalError extends Exception {
    protected UtilEvalError() {}

    public UtilEvalError(String s) {
        super(s);
    }

    /**
     * Re-throw as an eval error, prefixing msg to the message and specifying the node. If a node
     * already exists the addNode is ignored.
     *
     * @see #setNode( bsh.SimpleNode )
     *     <p>
     * @param msg may be null for no additional message.
     */
    public EvalError toEvalError(String msg, SimpleNode node, CallStack callstack) {
        if (Interpreter.DEBUG) printStackTrace();

        if (msg == null) msg = "";
        else msg = msg + ": ";
        return new EvalError(msg + getMessage(), node, callstack);
    }

    public EvalError toEvalError(SimpleNode node, CallStack callstack) {
        return toEvalError(null, node, callstack);
    }
}
