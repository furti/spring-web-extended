package at.porscheinformatik.common.springangular.resources;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;

import javax.servlet.ServletContext;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

public class ContextResourceScannerTest
{

	private static final Path TMPDIR = Paths.get(System
			.getProperty("java.io.tmpdir"));
	private static final Path ROOTDIR = TMPDIR
			.resolve("springangularcontextscannertest");

	@BeforeTest
	public void setup() throws IOException
	{
		deleteDirectory(ROOTDIR);
		Files.createDirectory(ROOTDIR);

		createFile(ROOTDIR, "test.txt", "Test");
		createFile(ROOTDIR, "test_en.txt", "Test");
		createFile(ROOTDIR, "test.min.txt", "Test");
		createFile(ROOTDIR, "other/test.txt", "Other");
	}

	@Test
	public void scanResources() throws IOException
	{
		ServletContext context = Mockito.mock(ServletContext.class);
		Mockito.when(context.getRealPath(Mockito.anyString())).then(
				new Answer<String>() {

					@Override
					public String answer(InvocationOnMock invocation)
							throws Throwable
					{
						String path = (String) invocation.getArguments()[0];

						if (path.equals("/"))
						{
							path = "";
						}

						return TMPDIR.resolve(path).toString();
					}
				});
		ContextResourceScanner scanner = new ContextResourceScanner();
		scanner.setServletContext(context);

		Map<String, Resource> actual = scanner
				.scanResources("springangularcontextscannertest");
		assertThat(actual, notNullValue());
		assertThat(actual.size(), equalTo(4));
		assertThat(actual.containsKey("test.txt"), equalTo(true));
		assertThat(actual.containsKey("test_en.txt"), equalTo(true));
		assertThat(actual.containsKey("test.min.txt"), equalTo(true));
		assertThat(actual.containsKey("other/test.txt"), equalTo(true));
	}

	@Test
	public void scanResourcesWithFileAndNoSubdirectories() throws IOException
	{
		ServletContext context = Mockito.mock(ServletContext.class);
		Mockito.when(context.getRealPath(Mockito.anyString())).then(
				new Answer<String>() {

					@Override
					public String answer(InvocationOnMock invocation)
							throws Throwable
					{
						String path = (String) invocation.getArguments()[0];

						if (path.equals("/"))
						{
							path = "";
						}

						return TMPDIR.resolve(path).toString();
					}
				});
		ContextResourceScanner scanner = new ContextResourceScanner();
		scanner.setServletContext(context);

		Map<String, Resource> actual = scanner
				.scanResources("springangularcontextscannertest", "test.txt",
						false);

		assertThat(actual, notNullValue());
		assertThat(actual.size(), equalTo(2));
		assertThat(actual.containsKey("test.txt"), equalTo(true));
		assertThat(actual.containsKey("test_en.txt"), equalTo(true));
	}

	@AfterTest
	public void tearDown() throws IOException
	{
		deleteDirectory(ROOTDIR);
	}

	private void deleteDirectory(Path rootdir) throws IOException
	{
		if (!Files.exists(rootdir))
		{
			return;
		}

		Assert.isTrue(Files.isDirectory(rootdir), "RootDir is not an directory");

		Files.walkFileTree(ROOTDIR, new DeleteFileVisitor());
	}

	private void createFile(Path dir, String file, String content)
			throws IOException
	{
		Path p = dir.resolve(file);

		Files.createDirectories(p.getParent());

		BufferedWriter writer = Files.newBufferedWriter(p,
				Charset.forName("UTF-8"),
				StandardOpenOption.CREATE);

		try
		{
			writer.write(content);
			writer.flush();
		} finally
		{
			writer.close();
		}
	}
}
