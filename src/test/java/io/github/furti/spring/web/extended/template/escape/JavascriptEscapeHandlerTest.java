package io.github.furti.spring.web.extended.template.escape;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.Test;

class JavascriptEscapeHandlerTest {

    private JavascriptEscapeHandler handler = new JavascriptEscapeHandler();

    @Test
    void testPlainText() {
        assertThat(handler.escapeContent("test"), equalTo("test"));
        assertThat(handler.escapeContent("hello world"), equalTo("hello world"));
        assertThat(handler.escapeContent("123456"), equalTo("123456"));
    }

    @Test
    void testNullInput() {
        assertThat(handler.escapeContent(null), equalTo(null));
    }

    @Test
    void testEmptyString() {
        assertThat(handler.escapeContent(""), equalTo(""));
    }

    @Test
    void testEscapesQuotesAndBackslash() {
        String input = "\"foo'\\bar\"";
        String escaped = handler.escapeContent(input);

        assertThat(escaped, containsString("\\x22")); // escaped double quote
        assertThat(escaped, containsString("\\x27")); // escaped single quote
    }

    @Test
    void testEscapesHtmlTags() {
        String input = "<script>alert('xss')</script>";
        String escaped = handler.escapeContent(input);

        // OWASP Encoder.forJavaScript doesn't escape < and > but escapes / in closing tags
        assertThat(escaped, containsString("<script>"));
        assertThat(escaped, not(containsString("</script>")));
        assertThat(escaped, containsString("\\/")); // escaped forward slash in closing tag
    }

    @Test
    void testUnicodeCharacters() {
        String input = "\u2028\u2029";
        String escaped = handler.escapeContent(input);
        assertThat(escaped, not(containsString("\u2028")));
        assertThat(escaped, not(containsString("\u2029")));
        // Should contain escaped Unicode
        assertThat(escaped, containsString("\\u"));
    }

    @Test
    void testEscapesWhitespace() {
        String input = "line1\nline2\tindented";
        String escaped = handler.escapeContent(input);

        assertThat(escaped, not(containsString("\n")));
        assertThat(escaped, not(containsString("\t")));
        assertThat(escaped, containsString("\\n"));
        assertThat(escaped, containsString("\\t"));
    }

    @Test
    void testEscapesCarriageReturn() {
        String input = "line1\rline2";
        String escaped = handler.escapeContent(input);

        assertThat(escaped, not(containsString("\r")));
        assertThat(escaped, containsString("\\r"));
    }

    @Test
    void testEscapesFormFeedAndVerticalTab() {
        String input = "text\f\u000Bmore";
        String escaped = handler.escapeContent(input);

        assertThat(escaped, not(containsString("\f")));
        assertThat(escaped, not(containsString("\u000B")));
    }

    @Test
    void testComplexMixedContent() {
        String input = "function test() {\n  alert(\"Hello 'World' & <script>\");\n}";
        String escaped = handler.escapeContent(input);

        // Should not contain any unescaped dangerous characters
        assertThat(escaped, not(containsString("\n")));
        assertThat(escaped, not(containsString("</script>")));
        assertThat(escaped, not(containsString("\"Hello")));

        // Should contain escaped versions
        assertThat(escaped, containsString("\\n"));
        assertThat(escaped, containsString("\\x22")); // escaped quotes
        // Note: < and > are not escaped by OWASP forJavaScript
        assertThat(escaped, containsString("<script>"));
    }

    @Test
    void testBackslashEscaping() {
        String input = "path\\to\\file";
        String escaped = handler.escapeContent(input);

        // Should escape backslashes
        assertThat(escaped, containsString("\\\\"));
    }

    @Test
    void testMultipleSpecialCharacters() {
        String input = "\"\"''<><>\\\\";
        String escaped = handler.escapeContent(input);

        // Should not contain any unescaped quotes
        assertThat(escaped, not(containsString("\"\"")));
        assertThat(escaped, not(containsString("''")));
        // Note: < and > are not escaped by OWASP forJavaScript for JS contexts
        assertThat(escaped, containsString("<>"));
        // Should escape quotes properly
        assertThat(escaped, containsString("\\x22"));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testPreservesAlphanumericCharacters() {
        String input = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String escaped = handler.escapeContent(input);

        assertThat(escaped, equalTo(input));
    }

    @Test
    void testBacktickEscaping() {
        String input = "`template literal`";
        String escaped = handler.escapeContent(input);

        // Backticks should be escaped to prevent template literal execution
        assertThat(escaped, not(containsString("`")));
        assertThat(escaped, containsString("\\x60")); // escaped backtick
    }

    @Test
    void testNestedBackticks() {
        String input = "`outer `inner` template`";
        String escaped = handler.escapeContent(input);

        // All backticks should be escaped
        assertThat(escaped, not(containsString("`")));
        // Should contain multiple escaped backticks - count them correctly
        int escapedBacktickCount = escaped.split("\\\\x60").length - 1;
        assertThat(escapedBacktickCount, equalTo(3)); // 3 backticks in the input
    }

    @Test
    void testBacktickWithExpressions() {
        String input = "`Hello ${name}!`";
        String escaped = handler.escapeContent(input);

        // Backticks should be escaped
        assertThat(escaped, not(containsString("`")));
        assertThat(escaped, containsString("\\x60"));
        // Dollar sign gets escaped as well - this is the correct behavior for security
        assertThat(escaped, containsString("\\x36")); // escaped dollar sign
    }

    @Test
    void testStringLiteralBoundaries() {
        String input = "\"string1\" + 'string2'";
        String escaped = handler.escapeContent(input);

        // Quotes should be escaped
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x22")); // escaped double quote
        assertThat(escaped, containsString("\\x27")); // escaped single quote
    }

    @Test
    void testMixedQuoteEscaping() {
        String input = "\"He said 'Hello' to me\"";
        String escaped = handler.escapeContent(input);

        // Both quote types should be escaped
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x22")); // escaped double quotes
        assertThat(escaped, containsString("\\x27")); // escaped single quote
    }

    @Test
    void testStringBreakoutAttempts() {
        String input = "\"; alert('xss'); \"";
        String escaped = handler.escapeContent(input);

        // Should not contain any unescaped quotes that could break out
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x22"));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testAlternatingQuotes() {
        String input = "\"'\"'\"'\"";
        String escaped = handler.escapeContent(input);

        // No raw quotes should remain
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        // Should contain escaped versions
        assertThat(escaped, containsString("\\x22"));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testTemplateLiteralWithInjection() {
        String input = "`${alert('xss')}`";
        String escaped = handler.escapeContent(input);

        // Backticks should be escaped
        assertThat(escaped, not(containsString("`")));
        assertThat(escaped, containsString("\\x60"));
        // Single quotes in the expression should also be escaped
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testComplexTemplateLiteral() {
        String input = "`Hello ${user.name}, you have ${count} messages`";
        String escaped = handler.escapeContent(input);

        // Backticks should be escaped
        assertThat(escaped, not(containsString("`")));
        assertThat(escaped, containsString("\\x60"));
        // Dollar signs get escaped for security - this is correct behavior
        assertThat(escaped, containsString("\\x36")); // escaped dollar sign
        assertThat(escaped, containsString("{user.name}"));
        assertThat(escaped, containsString("{count}"));
    }

    @Test
    void testNestedTemplateLiterals() {
        String input = "`outer ${`inner ${variable}`} template`";
        String escaped = handler.escapeContent(input);

        // All backticks should be escaped
        assertThat(escaped, not(containsString("`")));
        // Should contain multiple escaped backticks
        assertThat(escaped, containsString("\\x60"));
        // Count escaped backticks - should be 3 (outer, inner opening, inner closing)
        int escapedBacktickCount = escaped.split("\\\\x60").length - 1;
        assertThat(escapedBacktickCount, equalTo(3));
    }

    @Test
    void testTemplateLiteralWithFunction() {
        String input = "`Result: ${eval('malicious code')}`";
        String escaped = handler.escapeContent(input);

        // Backticks should be escaped
        assertThat(escaped, not(containsString("`")));
        assertThat(escaped, containsString("\\x60"));
        // Single quotes should be escaped
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testQuoteEscapeSequences() {
        String input = "\\\"escaped quote\\\" and \\'escaped single\\'";
        String escaped = handler.escapeContent(input);

        // Should not contain any unescaped quotes
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        // Should escape backslashes and quotes properly
        assertThat(escaped, containsString("\\\\"));
        assertThat(escaped, containsString("\\x22"));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testMultilineStringWithQuotes() {
        String input = "\"Line 1 with 'quotes'\"\n+ \"Line 2 with 'more quotes'\"";
        String escaped = handler.escapeContent(input);

        // Should not contain any unescaped quotes
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        // Should escape newlines
        assertThat(escaped, not(containsString("\n")));
        assertThat(escaped, containsString("\\n"));
        // Should escape quotes
        assertThat(escaped, containsString("\\x22"));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testJavaScriptInjectionPatterns() {
        String input = "\";alert('XSS');//";
        String escaped = handler.escapeContent(input);

        // Should escape quotes to prevent breaking out of strings
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x22"));
        assertThat(escaped, containsString("\\x27"));
        // Forward slash should be escaped in some contexts
        assertThat(escaped, containsString("\\/"));
    }

    @Test
    void testRegexLiteralEscaping() {
        String input = "/pattern/flags and 'string'";
        String escaped = handler.escapeContent(input);

        // Forward slashes should be escaped
        assertThat(escaped, containsString("\\/"));
        // Quotes should be escaped
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, containsString("\\x27"));
    }

    @Test
    void testCombinedQuotesAndSpecialChars() {
        String input = "\"test\" + 'test' + `test` + /regex/ + </script>";
        String escaped = handler.escapeContent(input);

        // No unescaped quotes or backticks
        assertThat(escaped, not(containsString("\"")));
        assertThat(escaped, not(containsString("'")));
        assertThat(escaped, not(containsString("`")));

        // Should contain escaped versions
        assertThat(escaped, containsString("\\x22")); // double quote
        assertThat(escaped, containsString("\\x27")); // single quote
        assertThat(escaped, containsString("\\x60")); // backtick
        assertThat(escaped, containsString("\\/")); // forward slash

        // Script tag closing should be escaped
        assertThat(escaped, not(containsString("</script>")));
    }
}
