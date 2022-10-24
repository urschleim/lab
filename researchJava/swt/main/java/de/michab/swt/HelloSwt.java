package de.michab.swt;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

public class HelloSwt
{
   public static void main (String[] args)
   {
      Display display = new Display();
      Shell shell = new Shell(display);
      Label label = new Label(shell, SWT.NONE);
      label.setText("Hello World");
      label.pack();
      shell.pack();
      shell.open();
      while (!shell.isDisposed())
      {
         if (!display.readAndDispatch()) display.sleep();
      }
      display.dispose();
   }
}
