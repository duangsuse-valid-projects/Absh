package bsh;

class BSHUnaryExpression extends SimpleNode implements ParserConstants
{
    public int kind;
	public boolean postfix = false;

    BSHUnaryExpression(int id) { super(id); }

    public Object eval( CallStack callstack, Interpreter interpreter)  
		throws EvalError
    {
        SimpleNode node = (SimpleNode)jjtGetChild(0);

		// If this is a unary increment of decrement (either pre or postfix)
		// then we need an LHS to which to assign the result.  Otherwise
		// just do the unary operation for the value.
		try {
			if ( kind == INCR || kind == DECR ) {
				LHS lhs = ((BSHPrimaryExpression)node).toLHS( 
					callstack, interpreter );
				return lhsUnaryOperation( lhs, interpreter.getStrictJava() );
			} else
				return 
					unaryOperation( node.eval(callstack, interpreter), kind );
		} catch ( UtilEvalError e ) {
			throw e.toEvalError( this, callstack );
		}
    }

    private Object lhsUnaryOperation( LHS lhs, boolean strictJava ) 
		throws UtilEvalError
    {
        if ( Interpreter.DEBUG ) Interpreter.debug("lhsUnaryOperation");
        Object prevalue, postvalue;
        prevalue = lhs.getValue();
        postvalue = unaryOperation(prevalue, kind);

		Object retVal;
		if ( postfix )
			retVal = prevalue;
		else
			retVal = postvalue;

		lhs.assign( postvalue, strictJava );
		return retVal;
    }

    private Object unaryOperation( Object op, int kind ) throws UtilEvalError
    {
        if (op instanceof Boolean || op instanceof Character 
			|| op instanceof Number)
            return primitiveWrapperUnaryOperation( op, kind );

        if ( !(op instanceof Primitive) )
            throw new UtilEvalError( "Unary operation " + tokenImage[kind]
                + " inappropriate for object" );

		
        return Primitive.unaryOperation((Primitive)op, kind);
    }

    private Object primitiveWrapperUnaryOperation(Object val, int kind)
        throws UtilEvalError
    {
        Class operandType = val.getClass();
        Object operand = Primitive.promoteToInteger(val);

        if ( operand instanceof Boolean )
			return Primitive.booleanUnaryOperation((Boolean)operand, kind)
					? Boolean.TRUE : Boolean.FALSE;
        else 
		if ( operand instanceof Integer )
        {
            int result = Primitive.intUnaryOperation((Integer)operand, kind);

            // ++ and -- must be cast back the original type
            if(kind == INCR || kind == DECR)
            {
                if(operandType == Byte.TYPE)
                    return new Byte((byte)result);
                if(operandType == Short.TYPE)
                    return new Short((short)result);
                if(operandType == Character.TYPE)
                    return new Character((char)result);
            }

            return new Integer(result);
        }
        else if(operand instanceof Long)
            return new Long(Primitive.longUnaryOperation((Long)operand, kind));
        else if(operand instanceof Float)
            return new Float(Primitive.floatUnaryOperation((Float)operand, kind));
        else if(operand instanceof Double)
            return new Double(Primitive.doubleUnaryOperation((Double)operand, kind));
        else
            throw new InterpreterError("An error occurred.  Please call technical support.");
    }
}
