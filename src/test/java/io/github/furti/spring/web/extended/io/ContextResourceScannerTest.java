package io.github.furti.spring.web.extended.io;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jakarta.servlet.ServletContext;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class ContextResourceScannerTest
{

    private static final Path TMPDIR = Paths.get(System.getProperty("java.io.tmpdir"));
    private static final Path ROOTDIR = TMPDIR.resolve("springangularcontextscannertest");

    @BeforeEach
    public void setup() throws IOException
    {
        deleteDirectory(ROOTDIR);
        Files.createDirectory(ROOTDIR);

        createFile(ROOTDIR, "testfile.txt", "Test");
        createFile(ROOTDIR, "testfile_en.txt", "Test");
        createFile(ROOTDIR, "testfile.min.txt", "Test");
        createFile(ROOTDIR, "other/testfile.txt", "Other");
        createFile(ROOTDIR, "other/test.log", "Other");
    }

    @Test
    public void scanResources() throws IOException
    {
        ContextResourceScanner scanner = new ContextResourceScanner();
        scanner.setServletContext(buildServletContext());

        Map<String, Resource> actual = scanner.scanResources("/springangularcontextscannertest/**/*.txt", null);
        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(4));
        assertThat(actual.containsKey("testfile.txt"), equalTo(true));
        assertThat(actual.containsKey("testfile_en.txt"), equalTo(true));
        assertThat(actual.containsKey("testfile.min.txt"), equalTo(true));
        assertThat(actual.containsKey("other/testfile.txt"), equalTo(true));
    }

    @Test
    public void subDirectoriesShouldNotBeInTheList() throws IOException
    {
        ContextResourceScanner scanner = new ContextResourceScanner();
        scanner.setServletContext(buildServletContext());

        Map<String, Resource> actual = scanner.scanResources("springangularcontextscannertest/**/*", null);
        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(5));
        assertThat(actual.containsKey("testfile.txt"), equalTo(true));
        assertThat(actual.containsKey("testfile_en.txt"), equalTo(true));
        assertThat(actual.containsKey("testfile.min.txt"), equalTo(true));
        assertThat(actual.containsKey("other/testfile.txt"), equalTo(true));
        assertThat(actual.containsKey("other/test.log"), equalTo(true));
    }

    @Test
    public void scanResourcesWithFileAndNoSubdirectories() throws IOException
    {
        ContextResourceScanner scanner = new ContextResourceScanner();
        scanner.setServletContext(buildServletContext());

        Map<String, Resource> actual = scanner.scanResources("springangularcontextscannertest", "testfile.txt", false);

        assertThat(actual, notNullValue());
        assertThat(actual.size(), equalTo(2));
        assertThat(actual.containsKey("testfile.txt"), equalTo(true));
        assertThat(actual.containsKey("testfile_en.txt"), equalTo(true));
    }

    @AfterEach
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

    private void createFile(Path dir, String file, String content) throws IOException
    {
        Path p = dir.resolve(file);

        Files.createDirectories(p.getParent());

        BufferedWriter writer = Files.newBufferedWriter(p, Charset.forName("UTF-8"), StandardOpenOption.CREATE);

        try
        {
            writer.write(content);
            writer.flush();
        }
        finally
        {
            writer.close();
        }
    }

    /**
     * @return
     * @throws MalformedURLException
     */
    private ServletContext buildServletContext() throws MalformedURLException
    {
        ServletContext context = Mockito.mock(ServletContext.class);
        Mockito.when(context.getResource(Mockito.anyString())).then(invocation -> {
            String path = (String) invocation.getArguments()[0];

            if (!path.startsWith("/"))
            {
                throw new IllegalArgumentException("Path " + path + " must start with a / ");
            }

            //Remove the leading slash
            path = path.substring(1);

            Path fullPath = TMPDIR.resolve(path);

            if (!Files.exists(fullPath))
            {
                return null;
            }

            return fullPath.toUri().toURL();
        });

        Mockito.when(context.getResourcePaths(Mockito.anyString())).then(invocation -> {
            String path = (String) invocation.getArguments()[0];

            if (!path.startsWith("/"))
            {
                throw new IllegalArgumentException("Path " + path + " must start with a / ");
            }

            //Remove the leading slash
            path = path.substring(1);

            Set<String> paths = new HashSet<>();

            try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(TMPDIR.resolve(path));)
            {
                for (Path dirPath : dirStream)
                {
                    Path relative = TMPDIR.relativize(dirPath);

                    if (Files.isDirectory(dirPath))
                    {
                        paths.add("/" + relative.toString().replace("\\", "/") + "/");
                    }
                    else
                    {
                        paths.add("/" + relative.toString().replace("\\", "/"));
                    }
                }
            }

            return paths.isEmpty() ? null : paths;
        });

        return context;
    }
}
