package net.seibermedia.jsouphamcrest;

import static net.seibermedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.any;

import org.jsoup.nodes.Element;
import org.junit.Test;

public class IsHtmlMatcherTest {

	@Test
	public void acceptsValidHtml() {
		assertThat("<b>Hello World</b>", isHtmlMatching(
				any(Element.class)
		));
	}

	@Test(expected = IllegalArgumentException.class)
	public void rejectsMissingSubMatcher() {
		assertThat("<b>Hello World</b>", isHtmlMatching());
	}
}
