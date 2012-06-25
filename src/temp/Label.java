package temp;

import symboltable.Symbol;

/**
 * A Label represents an address in assembly language.
 */

public class Label  {
   private String name;
   private static int count;

  /**
   * a printable representation of the label, for use in assembly
   * language output.
   */
   @Override
public String toString() {return name;}

  /**
   * Makes a new label that prints as "name".
   * Warning: avoid repeated calls to <tt>new Label(s)</tt> with
   * the same name <tt>s</tt>.
   */
   public Label(String n) {
        name=n;
   }

  /**
   * Makes a new label with an arbitrary name.
   */
   public Label() {
        this("L" + count++);
   }

  /**
   * Makes a new label whose name is the same as a symbol.
   */
   public Label(Symbol s) {
        this(s.toString());
   }


   @Override
public boolean equals(Object o){
           return ((Label)o).name.equals(this.name);
   }


   public int compare(Object o){
           if (((Label)o).name.equals(this.name)) return 0;
           else return 1;
   }

   public String print() {
           return name;
   }
}