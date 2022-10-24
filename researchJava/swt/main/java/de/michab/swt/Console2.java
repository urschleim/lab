package de.michab.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * https://www.eclipse.org/forums/index.php/t/640374/
 */
public class Console2 {
	Display display;
	Shell shell;
	Text text;

	private void process() {
		display = new Display();
		shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setLocation(new Point(80, 80));
		text = new Text(shell, SWT.MULTI | SWT.V_SCROLL | SWT.READ_ONLY);
		shell.open();
		Display.getDefault().asyncExec(new Runnable() {
			@Override
            public void run() {
				new Worker(text).work();
			}
		});
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}

	class Worker {
		Text text;
		public Worker(Text t) {
			text = t;
		};
		void work() {
			for (int i = 0; i < 10000; i++) {
				if (!text.isDisposed()) {
					text.append("hello " + i + "\n");
				}
			}
		}
	}
	public static void main(String[] args) {
		new Console2().process();
	}
}