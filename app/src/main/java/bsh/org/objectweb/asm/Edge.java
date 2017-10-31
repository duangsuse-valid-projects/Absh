package bsh.org.objectweb.asm;

/**
 * An edge in the control flow graph of a method body. See {@link Label Label}.
 */

class Edge {

    /**
     * The (relative) stack size in the basic block from which this edge
     * originates. This size is equal to the stack size at the "jump" instruction
     * to which this edge corresponds, relatively to the stack size at the
     * beginning of the originating basic block.
     */

    int stackSize;

    /**
     * The successor block of the basic block from which this edge originates.
     */

    Label successor;

    /**
     * The next edge in the list of successors of the originating basic block.
     * See {@link Label#successors successors}.
     */

    Edge next;

    /**
     * The next available edge in the pool. See {@link CodeWriter#pool pool}.
     */

    Edge poolNext;
}
