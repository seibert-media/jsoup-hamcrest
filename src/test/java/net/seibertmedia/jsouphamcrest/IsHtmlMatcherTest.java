package net.seibertmedia.jsouphamcrest;

import static net.seibertmedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class IsHtmlMatcherTest {

	private static final String HTML = "<b>Hello World</b>";

	@Test
	public void acceptsValidHtmlString() {
		assertThat(HTML, isHtmlMatching(
				any(Element.class)
		));
	}

	@Test
	public void acceptyValidHtmlFile() throws IOException {
		File tempFile = File.createTempFile("html", "html");
		tempFile.deleteOnExit();
		BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));
		writer.write(HTML);
		writer.close();

		assertThat(tempFile, isHtmlMatching(
				any(Element.class)
		));
	}

	@Test
	public void acceptyValidHtmlStream() {
		InputStream stream = new ByteArrayInputStream(HTML.getBytes());

		assertThat(stream, isHtmlMatching(
				any(Element.class)
		));
	}

	@Test(expected = NullPointerException.class)
	public void rejectsMissingSubMatcher() {
		assertThat("<b>Hello World</b>", isHtmlMatching(null));
	}
}
