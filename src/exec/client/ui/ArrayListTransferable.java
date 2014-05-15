package exec.client.ui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.util.ArrayList;


public class ArrayListTransferable implements Transferable {

	public ArrayListTransferable(ArrayList alist) {
		localArrayListType = "application/x-java-jvm-local-objectref;class=java.util.ArrayList";
		data = alist;
		try {
			localArrayListFlavor = new DataFlavor(localArrayListType);
		} catch (ClassNotFoundException e) {
		}
		serialArrayListFlavor = new DataFlavor(java.util.ArrayList.class, "ArrayList");
	}

	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException {
		if (!isDataFlavorSupported(flavor))
			throw new UnsupportedFlavorException(flavor);
		else
			return data;
	}

	public DataFlavor[] getTransferDataFlavors() {
		return (new DataFlavor[] { localArrayListFlavor, serialArrayListFlavor });
	}

	public boolean isDataFlavorSupported(DataFlavor flavor) {
		if (localArrayListFlavor.equals(flavor))
			return true;
		else
			return serialArrayListFlavor.equals(flavor);
	}

	private DataFlavor localArrayListFlavor;

	private DataFlavor serialArrayListFlavor;

	private String localArrayListType;

	private ArrayList data;
}
