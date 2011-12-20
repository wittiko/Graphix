package at.htlv.messner.graphix.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import at.htlv.messner.graphix.model.Matrix;

public class FileHelper {

	private FileHelper() {
	}

	public static Matrix load(File file) throws FileNotFoundException,
			IOException, ClassNotFoundException {
		ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

		Matrix loaded = (Matrix) in.readObject();
		in.close();

		return loaded;
	}

	public static void save(File file, Matrix m)
			throws FileNotFoundException, IOException {
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(
				file));
		out.writeObject(m);
		out.close();
	}
}
