package edu.csupomona.cs585.ibox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;

import edu.csupomona.cs585.ibox.sync.FileSyncManager;

public class WatchDirTest {
	/* File change notification allow to listen for changes in the file system */
	int count = 0;
	public static final String DIR_PATH = "C:\\Testsync\\";

	@Test
	public void testWatchDir() throws InterruptedException {
		try {
			FileSyncManager fileSyncManager = new FileSyncManager() {
				@Override
				public void updateFile(File localFile) throws IOException {
					System.out.println("update");
					count++;
				}

				@Override
				public void deleteFile(File localFile) throws IOException {
					System.out.println("delete");
					count++;
				}

				@Override
				public void addFile(File localFile) throws IOException {
					System.out.println("add");
					count++;
				}
			};
			Path dir = Paths.get(DIR_PATH);
			new TestADU().start(); // Call parallel thread to Add/update/delete
									// files
			new WatchDir(dir, fileSyncManager).processEvents();
			System.out.println(count);
			assertEquals(6, count); // count should be 6 (2 Add,2 update ,2
									// delete)

		} catch (IOException e) {
			fail();
		}
	}

}

// Class to add /delete /Update
class TestADU extends Thread {
	private AtomicBoolean done = new AtomicBoolean(false);

	@Override
	public void run() {
		try {
			File file = new File(WatchDirTest.DIR_PATH);
			if (!file.exists()) {
				if (file.mkdir()) {
					System.out.println("Directory is created!");
				} else {
					System.out.println("Failed to create directory!");
				}
			}
			Thread.sleep(2000);
			Path file1 = Paths.get(WatchDirTest.DIR_PATH + "test10.txt");
			Path file2 = Paths.get(WatchDirTest.DIR_PATH + "test20.txt");
			Files.createFile(file1); // add file1
			Thread.sleep(1000);
			FileWriter fw = new FileWriter(file1.toString());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write("Some text here to update file");
			bw.flush();
			bw.close();
			Files.createFile(file2); // add file2
			Thread.sleep(1000);
			Files.delete(file1); // delete file1
			Thread.sleep(1000);
			Files.delete(file2); // delete file2
			Thread.sleep(2000);
			done.compareAndSet(false, true);
			file.delete();
		} catch (IOException e) {
			e.printStackTrace();
			fail();
		} catch (InterruptedException e) {
			fail();
		}
	}

}
