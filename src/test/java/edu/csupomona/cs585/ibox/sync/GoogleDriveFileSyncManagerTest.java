package edu.csupomona.cs585.ibox.sync;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.google.api.client.http.AbstractInputStreamContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.Drive.Files;
import com.google.api.services.drive.Drive.Files.Delete;
import com.google.api.services.drive.Drive.Files.Insert;
import com.google.api.services.drive.Drive.Files.Update;

public class GoogleDriveFileSyncManagerTest {
	Drive MockDService;
	GoogleDriveFileSyncManager gsync;

	@Before
	public void setUp() throws Exception {
		MockDService = Mockito.mock(Drive.class);
		gsync = new GoogleDriveFileSyncManager(MockDService);
	}

	@Test
	public void testAddFile() throws IOException {
		File localFile = Mockito.mock(File.class);
		Files testDir = Mockito.mock(Files.class);
		when(MockDService.files()).thenReturn(testDir);
		Insert insertTest = Mockito.mock(Insert.class);
		when(
				testDir.insert(
						isA(com.google.api.services.drive.model.File.class),
						isA(AbstractInputStreamContent.class))).thenReturn(
				insertTest);
		com.google.api.services.drive.model.File finalFile = new com.google.api.services.drive.model.File();
		finalFile.setId("Test ID");
		when(insertTest.execute()).thenReturn(finalFile);
		finalFile.getId();
		gsync.addFile(localFile);
		verify(MockDService).files();
	}

	@Test(expected = Exception.class)
	public void testExceptionUpdateFile() throws IOException {
		File localFile = Mockito.mock(File.class);
		when(localFile.getName()).thenReturn(null);
		gsync.updateFile(localFile);
	}

	@Test
	public void testUpdateFile() throws IOException {
		File localFile = Mockito.mock(File.class);
		when(localFile.getName()).thenReturn("Arun.txt");
		Files testDir = mock(Files.class);
		when(MockDService.files()).thenReturn(testDir);

		com.google.api.services.drive.Drive.Files.List mockList = mock(com.google.api.services.drive.Drive.Files.List.class);
		when(testDir.list()).thenReturn(mockList);

		com.google.api.services.drive.model.File finalFile = new com.google.api.services.drive.model.File();
		finalFile.setId("Test ID");
		finalFile.setTitle("Arun.txt");

		com.google.api.services.drive.model.FileList files = new com.google.api.services.drive.model.FileList();
		List<com.google.api.services.drive.model.File> items = new ArrayList<com.google.api.services.drive.model.File>();
		items.add(finalFile);
		files.setItems(items);

		when(mockList.execute()).thenReturn(files);

		String fileId = gsync.getFileId("Arun.txt");

		Update updateTest = Mockito.mock(Update.class);
		when(
				testDir.update(eq(fileId),
						isA(com.google.api.services.drive.model.File.class),
						isA(AbstractInputStreamContent.class))).thenReturn(
				updateTest);

		com.google.api.services.drive.model.File finalFile2 = new com.google.api.services.drive.model.File();
		when(updateTest.execute()).thenReturn(finalFile2);

		gsync.updateFile(localFile);
		verify(MockDService, times(3)).files();
	}

	@Test(expected = Exception.class)
	public void testExceptionDeleteFile() throws IOException {
		Drive MockDriveService = Mockito.mock(Drive.class);
		GoogleDriveFileSyncManager gsync = new GoogleDriveFileSyncManager(
				MockDriveService);

		File localFile = Mockito.mock(File.class);
		when(localFile.getName()).thenReturn(null);

		gsync.deleteFile(localFile);
	}

	@Test
	public void testGetFileId() throws IOException {
		Files testDir = mock(Files.class);
		when(MockDService.files()).thenReturn(testDir);

		com.google.api.services.drive.Drive.Files.List mockList = mock(com.google.api.services.drive.Drive.Files.List.class);
		when(testDir.list()).thenReturn(mockList);

		com.google.api.services.drive.model.File finalFile = new com.google.api.services.drive.model.File();
		finalFile.setId("Test ID");
		finalFile.setTitle("Arun.txt");

		com.google.api.services.drive.model.FileList files = new com.google.api.services.drive.model.FileList();
		List<com.google.api.services.drive.model.File> items = new ArrayList<com.google.api.services.drive.model.File>();
		items.add(finalFile);
		files.setItems(items);

		when(mockList.execute()).thenReturn(files);

		String getId = gsync.getFileId("Arun.txt");
		assertEquals("Test ID", getId);
	}

	@Test
	public void testDeleteFile() throws IOException {
		Files testDir = mock(Files.class);
		when(MockDService.files()).thenReturn(testDir);

		com.google.api.services.drive.Drive.Files.List mockList = mock(com.google.api.services.drive.Drive.Files.List.class);
		when(testDir.list()).thenReturn(mockList);

		com.google.api.services.drive.model.File finalFile = new com.google.api.services.drive.model.File();
		finalFile.setId("Test ID");
		finalFile.setTitle("Arun.txt");

		com.google.api.services.drive.model.FileList files = new com.google.api.services.drive.model.FileList();
		List<com.google.api.services.drive.model.File> items = new ArrayList<com.google.api.services.drive.model.File>();
		items.add(finalFile);
		files.setItems(items);

		when(mockList.execute()).thenReturn(files);

		String fileId = gsync.getFileId("Arun.txt");

		File localFile = Mockito.mock(File.class);
		when(localFile.getName()).thenReturn("Arun.txt");

		Delete deleteTest = Mockito.mock(Delete.class);
		when(testDir.delete(eq(fileId))).thenReturn(deleteTest);

		gsync.deleteFile(localFile);
		verify(MockDService, times(3)).files();
	}
}
