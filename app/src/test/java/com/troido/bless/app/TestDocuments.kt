package com.troido.bless.app

import org.junit.Assert
import org.junit.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.stream.Stream

private const val MAX_DEPTH = 1000

class TestDocuments {

    /**
     * Match any text that starts with "[" then comes text with length from 0 to more, then "]",
     * then goes open parenthesis, then may be "<" then comes the link (it's local, because it starts from .),
     * then optional ">" and we close the pathenthesis. Text inside brackets or <> if there are present
     * captured as a group.
     *
     * **Example of the text that passes the regex**
     * - `[Advertising](./core/advertising.md)`
     * - `[Advertising](./core/advertising.md "some text")`
     * - `![Advertising](<./core/advertisin g.md>)`
     *
     * **Example of the text that does not pass the regex**
     * - `![Advertising]<>`
     */
    private val findLocalReferencesRegex = """!?\[.*]\(<?(\..+?)>?\)""".toRegex()

    @Test
    fun checkThatAllLocalReferencesInMdFilesExists() {
        val docsFolder = getDocsFolder()

        var mdFilesStream: Stream<Path>? = null
        try {
            mdFilesStream = Files.find(docsFolder, MAX_DEPTH, { path, attr ->
                !attr.isDirectory && path.fileName.toString().endsWith(".md", true)
            })
            mdFilesStream.forEach { mdFilePath ->
                Files.lines(mdFilePath).forEach { line ->
                    findLocalReferencesRegex.findAll(line).forEach { result ->
                        result.groups[1]?.value?.let { capturedResult ->
                            val relativePathStr = removeTooltip(capturedResult).trim()
                            val basePath = mdFilePath.parent.toString()
                            val referencePath =
                                Paths.get(basePath, relativePathStr).normalize()
                            println("Check if $relativePathStr exist")
                            Assert.assertTrue(
                                "Can't reach path $relativePathStr in $mdFilePath",
                                Files.exists(referencePath),
                            )
                        } ?: throw RuntimeException("Group should be captured with the given regex")
                    }
                }
            }
        } finally {
            mdFilesStream?.close()
        }
    }

    /**
     * In markdown it is possible to write tooltips in the links, for example
     * [Advertising](./core/advertising.md "Tooltip"). This function extracts such tooltip from a
     * parsed result (./core/advertising.md "Tooltip")
     */
    private fun removeTooltip(parsedResult: String): String {
        return parsedResult.indexOf("\"").takeIf {
            it != -1
        }?.let { indexOfFirstQuote ->
            parsedResult.removeRange(indexOfFirstQuote, parsedResult.length)
        } ?: parsedResult
    }

    private fun getDocsFolder(): Path {
        // for example systempath/bless/app/fake - project path is systempath/bless
        return Paths.get("fake").toAbsolutePath().parent.parent
    }
}