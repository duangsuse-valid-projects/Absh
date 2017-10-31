package bsh.org.objectweb.asm;

/**
 * A visitor to visit a Java class. The methods of this interface must be called
 * in the following order: <tt>visit</tt> (<tt>visitField</tt> |
 * <tt>visitMethod</tt> | <tt>visitInnerClass</tt>)* <tt>visitEnd</tt>.
 */

public interface ClassVisitor {

    /**
     * Visits the header of the class.
     *
     * @param access the class's access flags (see {@link Constants}). This
     *      parameter also indicates if the class is deprecated.
     * @param name the internal name of the class (see {@link Type#getInternalName
     *      getInternalName}).
     * @param superName the internal of name of the super class (see {@link
     *      Type#getInternalName getInternalName}). For interfaces, the super
     *      class is {@link Object}. May be <tt>null</tt>, but only for the {@link
     *      Object java.lang.Object} class.
     * @param interfaces the internal names of the class's interfaces (see {@link
     *      Type#getInternalName getInternalName}). May be <tt>null</tt>.
     * @param sourceFile the name of the source file from which this class was
     *      compiled. May be <tt>null</tt>.
     */

    void visit (
        int access,
        String name,
        String superName,
        String[] interfaces,
        String sourceFile);

    /**
     * Visits information about an inner class. This inner class is not
     * necessarily a member of the class being visited.
     *
     * @param name the internal name of an inner class (see {@link
     *      Type#getInternalName getInternalName}).
     * @param outerName the internal name of the class to which the inner class
     *      belongs (see {@link Type#getInternalName getInternalName}). May be
     *      <tt>null</tt>.
     * @param innerName the (simple) name of the inner class inside its enclosing
     *      class. May be <tt>null</tt> for anonymous inner classes.
     * @param access the access flags of the inner class as originally declared
     *      in the enclosing class.
     */

    void visitInnerClass (
        String name,
        String outerName,
        String innerName,
        int access);

    /**
     * Visits a field of the class.
     *
     * @param access the field's access flags (see {@link Constants}). This
     *      parameter also indicates if the field is synthetic and/or deprecated.
     * @param name the field's name.
     * @param desc the field's descriptor (see {@link Type Type}).
     * @param value the field's initial value. This parameter, which may be
     *      <tt>null</tt> if the field does not have an initial value, must be an
     *      {@link java.lang.Integer Integer}, a {@link java.lang.Float Float}, a
     *      {@link java.lang.Long Long}, a {@link java.lang.Double Double} or a
     *      {@link String String}. <em>This parameter is only used for static
     *      fields</em>. Its value is ignored for non static fields, which must be
     *      initialized through bytecode instructions in constructors or methods.
     */

    void visitField (int access, String name, String desc, Object value);

    /**
     * Visits a method of the class. This method <i>must</i> return a new
     * {@link CodeVisitor CodeVisitor} instance (or <tt>null</tt>) each time it
     * is called, i.e., it should not return a previously returned visitor.
     *
     * @param access the method's access flags (see {@link Constants}). This
     *      parameter also indicates if the method is synthetic and/or deprecated.
     * @param name the method's name.
     * @param desc the method's descriptor (see {@link Type Type}).
     * @param exceptions the internal names of the method's exception
     *      classes (see {@link Type#getInternalName getInternalName}). May be
     *      <tt>null</tt>.
     * @return an object to visit the byte code of the method, or <tt>null</tt> if
     *      this class visitor is not interested in visiting the code of this
     *      method.
     */

    CodeVisitor visitMethod (
        int access,
        String name,
        String desc,
        String[] exceptions);

    /**
     * Visits the end of the class. This method, which is the last one to be
     * called, is used to inform the visitor that all the fields and methods of
     * the class have been visited.
     */

    void visitEnd ();
}
