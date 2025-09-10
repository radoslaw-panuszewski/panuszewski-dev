package dev.bnorm.storyboard.text.highlight

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import dev.bnorm.storyboard.text.highlight.antlr.xml.XMLLexer
import dev.bnorm.storyboard.text.highlight.antlr.xml.XMLParser
import dev.bnorm.storyboard.text.highlight.antlr.xml.XMLParserBaseListener
import org.antlr.v4.kotlinruntime.CharStreams
import org.antlr.v4.kotlinruntime.CommonTokenStream
import org.antlr.v4.kotlinruntime.ParserRuleContext
import org.antlr.v4.kotlinruntime.Token
import org.antlr.v4.kotlinruntime.TokenSource
import org.antlr.v4.kotlinruntime.tree.ParseTreeWalker
import org.antlr.v4.kotlinruntime.tree.TerminalNode

internal fun highlightXml(
    text: String,
    codeStyle: CodeStyle,
    scope: CodeScope = CodeScope.File,
    identifierStyle: (String) -> SpanStyle? = { null },
): AnnotatedString {
    return buildAnnotatedString {
        withStyle(codeStyle.simple) { append(text) }

        val formatListener = object : XMLParserBaseListener() {
            override fun enterElement(ctx: XMLParser.ElementContext) {
                // Style the opening tag
                ctx.OPEN(0)?.let { addStyle(codeStyle.keyword, it.symbol) }
                ctx.Name(0)?.let { addStyle(codeStyle.keyword, it.symbol) }

                // Style the closing tag if it exists
                ctx.SLASH()?.let {
                    addStyle(codeStyle.keyword, it.symbol)
                    ctx.Name(1)?.let { name -> addStyle(codeStyle.keyword, name.symbol) }
                }

                // Style the self-closing tag if it exists
                ctx.SLASH_CLOSE()?.let { addStyle(codeStyle.keyword, it.symbol) }

                // Style the closing brackets
                ctx.CLOSE()?.forEach { addStyle(codeStyle.keyword, it.symbol) }
            }

            override fun enterAttribute(ctx: XMLParser.AttributeContext) {
                // Style attribute name
                addStyle(codeStyle.property, ctx.Name().symbol)

                // Style equals sign
                addStyle(codeStyle.keyword, ctx.EQUALS().symbol)

                // Style attribute value
                addStyle(codeStyle.string, ctx.STRING().symbol)
            }

            override fun enterReference(ctx: XMLParser.ReferenceContext) {
                // Style entity and character references
                ctx.EntityRef()?.let { addStyle(codeStyle.keyword, it.symbol) }
                ctx.CharRef()?.let { addStyle(codeStyle.keyword, it.symbol) }
            }

            override fun enterProlog(ctx: XMLParser.PrologContext) {
                // Style XML declaration
                addStyle(codeStyle.keyword, ctx.XMLDeclOpen().symbol)
                addStyle(codeStyle.keyword, ctx.SPECIAL_CLOSE().symbol)
            }

            override fun visitTerminal(node: TerminalNode) {
                val symbol = node.symbol
                when (symbol.type) {
                    XMLLexer.Tokens.OPEN,
                    XMLLexer.Tokens.CLOSE,
                    XMLLexer.Tokens.SLASH,
                    XMLLexer.Tokens.SLASH_CLOSE,
                    XMLLexer.Tokens.EQUALS,
                    XMLLexer.Tokens.XMLDeclOpen,
                    XMLLexer.Tokens.SPECIAL_CLOSE -> {
                        addStyle(codeStyle.keyword, symbol)
                    }
                }
            }
        }

        // Make sure the text ends with a new-line.
        val stream = CharStreams.fromString(text + "\n")
        val lexer = XMLLexer(stream)

        // The parser ignores code comments,
        // so create a custom source which handles highlighting them.
        val source = object : TokenSource by lexer {
            override fun nextToken(): Token {
                val token = lexer.nextToken()
                when (token.type) {
                    XMLLexer.Tokens.COMMENT,
                    XMLLexer.Tokens.DTD -> {
                        addStyle(codeStyle.comment, token)
                    }
                    XMLLexer.Tokens.CDATA -> {
                        addStyle(codeStyle.string, token)
                    }
                }
                return token
            }
        }

        val parser = XMLParser(CommonTokenStream(source))
        val context = parser.document()

        val walker = ParseTreeWalker()
        walker.walk(formatListener, context)
    }
}
