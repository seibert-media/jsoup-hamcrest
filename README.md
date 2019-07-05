# JSoup Matchers for Hamcrest and RestAssured
This is a library of Hamcrest-compatible matchers that allow matching HTML-Strings, -Files and -Streams with JSoup backed CSS Matchers. 
It can be used to assert certain structural aspects of HTML Strings (Template-Outputs, HTML-Responses).

## Installation
curtesy of [JitPack](https://jitpack.io/#seibert-media/jsoup-hamcrest/master-SNAPSHOT):
Add the following to your pom.xml:
```xml
<repositories>
	<repository>
		<id>jitpack.io</id>
		<url>https://jitpack.io</url>
	</repository>
</repositories>

…

<dependency>
	<groupId>com.github.seibert-media</groupId>
	<artifactId>jsoup-hamcrest</artifactId>
	<version>1.0.0</version>
	<scope>test</scope>
</dependency>
```

## General Usage: Hamcrest
```java
public class MyJUnitTest {
	@Test
	public void hasBoldHelloWorld() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("Hello World"))));
	}
}
```

## General Usage: RestAssured
```java
public class MyHamcrestRestIT {
	@Test
	public void hasExpectedHtmlResponse() {
		given()
			.log().all()
			.and().accept(ContentType.HTML)
		.when()
			.get("htttp://localhost:8080/…")
		.then()
			.log().headers()
			.contentType(ContentType.HTML)
			.statusCode(200)
			.body(isHtmlMatching(allOf(
					// Test HTML-Title
					hasElement("html head title", hasText(startsWith("MY Application"))),

					// Test Main-Menu is correct
					hasElement("#admin-nav-heading nav > ul li.selected a", allOf(
						hasText("Expected Menu"),
						hasAttribute("href", containsString("path/to/expected/page"))
					))
			)));
	}
}
```

## IsHtmlMatcher
This is the basic matcher that converts from a String, InputStream or a File to an JSoup Element that the other Matchers can work with.
The Matcher *requires* at a Sub-Matcher to assert the parsed HTML conforms to some kind of criteria, because JSoup will happily parse any
broken HTML, even empty Strings or binary Responses, just as your Browser will happily display anything, even if it is not really HTML.  
```java
public class IsHtmlMatcherTest {
	@Test
	public void hasBoldTag() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(any(Element.class)));
	}
}
```

## HasElementMatcher
Matches if there is *one* and *exatly one* Element in the DOM-Tree of the Element under Test conforming to the specified selector. Consult 
the JSoup-Documentation for [the list of allowed selectors](https://jsoup.org/cookbook/extracting-data/selector-syntax). The 
HasElementMatcher optionally takes a SubMatcher to further assert the matched Element.

```java
public class HasElementMatcherTest {
	@Test
	public void matchesTagSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b")));
	}

	@Test
	public void matchesDeepSelector() {
		String html = "<div><b><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div > b > i")));
	}

	@Test
	public void matchesTextSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b:contains(Hello World)")));
	}
	
	@Test
	public void acceptsSubMatcher() {
		String html = "<div><b>Hello</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("Hello"))));
	}
}
```

## HasElementsMatcher
Matches if there is *one or more* Elements in the DOM-Tree of the Element under Test conforming to the specified selector. Consult the 
JSoup-Documentation for [the list of allowed selectors](https://jsoup.org/cookbook/extracting-data/selector-syntax). The 
HasElementsMatcher optionally takes a SubMatcher to further assert the List of matched Elements.
```java
public class HasElementsMatcherTest {
	@Test
	public void hasBoldHelloWorld() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b")));

		assertThat(html, isHtmlMatching(hasElements("b", hasSize(2))));

		assertThat(html, isHtmlMatching(hasElements("b", containsInAnyOrder(
				hasText("World"),
				hasText("Hello")
		))));
	}
}
```

## HasTextMatcher
Matches if the Element under Test has Text-Content conforming to the specified Value or Matcher. There are two Versions of this Matcher:

 - `hasText` matches all Text-Nodes underneath the tested Element, including all Sub-Element's Text.
 - `hasOwnText` only matches the tested Elements direct Text-Content. Whitespace is normalized according to HTMLs 
 Whitespace-Compression-Rules.

```java
public class HasTextMatcherTest {
	@Test
	public void matchesString() {
		String html = "<b>Hello</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("Hello"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsSubMatcher() {
		String html = "<b>HELLO</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText(equalToIgnoringCase("world")))));
	}

	@Test
	public void matchesSubElementsText() {
		String html = "<b>Hello <i>beautiful</i> World</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("Hello beautiful World"))));
	}

	@Test
	public void matchesOwnText() {
		String html = "<b>Hello <i>beautiful</i> World</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasOwnText("Hello World"))));
		assertThat(html, isHtmlMatching(hasElement("i", hasOwnText("beautiful"))));
	}
}
```

## HasAttributeMatcher
Matches if the Element under Test has the specified Attribute. The HasAttributeMatcher optionally takes a desired Value or a Sub-Matcher 
to further assert the Attributes' Value.

```java
public class HasAttributeMatcherTest {
	@Test
	public void matchesHasAttribute() {
		String html = "<div><input required /></div>";
		assertThat(html, isHtmlMatching(hasElement("input", hasAttribute("required"))));
	}

	@Test
	public void matchesAttributeValue() {
		String html = "<div><input size='5' /></div>";
		assertThat(html, isHtmlMatching(hasElement("input", hasAttribute("size", "5"))));
	}

	@Test
	public void matchesAttributeValueMatcher() {
		String html = "<div><input title='Telephone' /></div>";
		assertThat(html, isHtmlMatching(hasElement("input",
				hasAttribute("title", equalToIgnoringCase("telephone")))));
	}
}
```

## HasClassMatcher
Matches if the Element under Test has the specified CSS-Class or the specified Set of CSS-Classes.

```java
public class HasClassMatcherTest {
	@Test
	public void matchesClass() {
		String html = "<div><br class='hello world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClass("hello"))));
	}

	@Test
	public void matchesClassWithSubMatcher() {
		String html = "<div><br class='fa far-world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClass(endsWith("-world")))));
	}
	@Test

	public void matchesClasses() {
		String html = "<div><br class='hello world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClasses("hello", "world"))));
	}

	@Test
	public void matchesClassesWithSubMatcher() {
		String html = "<div><br class='fa far-world'></div>";

		assertThat(html, isHtmlMatching(hasElement("br", hasClasses(containsInAnyOrder(
				equalTo("fa"),
				endsWith("-world")))
		)));
	}
}
```
