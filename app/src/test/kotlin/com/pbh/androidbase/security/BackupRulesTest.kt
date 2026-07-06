package com.pbh.androidbase.security

import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.assertContains
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BackupRulesTest {
    @Test
    fun `manifest wires backup rule resources`() {
        val manifest = projectFile("src/main/AndroidManifest.xml").readText()

        assertContains(manifest, """android:fullBackupContent="@xml/backup_rules"""")
        assertContains(manifest, """android:dataExtractionRules="@xml/data_extraction_rules"""")
    }

    @Test
    fun `full backup excludes session secrets and local cache database`() {
        val excludes = projectFile("src/main/res/xml/backup_rules.xml").excludeRules()

        assertContains(excludes, BackupExclude(domain = "sharedpref", path = "secure_session.xml"))
        assertContains(excludes, BackupExclude(domain = "file", path = "datastore/session.preferences_pb"))
        assertContains(excludes, BackupExclude(domain = "database", path = "android_base.db"))
        assertContains(excludes, BackupExclude(domain = "database", path = "android_base.db-shm"))
        assertContains(excludes, BackupExclude(domain = "database", path = "android_base.db-wal"))
    }

    @Test
    fun `data extraction rules exclude sensitive data from cloud and device transfer`() {
        val document =
            DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
                projectFile("src/main/res/xml/data_extraction_rules.xml"),
            )
        val sections = listOf("cloud-backup", "device-transfer")

        sections.forEach { section ->
            val nodes = document.getElementsByTagName(section)
            assertEquals(1, nodes.length)

            val excludes =
                (nodes.item(0) as Element)
                    .getElementsByTagName("exclude")
                    .asElements()
                    .map { BackupExclude(domain = it.getAttribute("domain"), path = it.getAttribute("path")) }

            assertContains(excludes, BackupExclude(domain = "sharedpref", path = "secure_session.xml"))
            assertContains(excludes, BackupExclude(domain = "file", path = "datastore/session.preferences_pb"))
            assertContains(excludes, BackupExclude(domain = "database", path = "android_base.db"))
            assertContains(excludes, BackupExclude(domain = "database", path = "android_base.db-shm"))
            assertContains(excludes, BackupExclude(domain = "database", path = "android_base.db-wal"))
        }
    }
}

private data class BackupExclude(
    val domain: String,
    val path: String,
)

private fun File.excludeRules(): List<BackupExclude> {
    val document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(this)
    return document
        .getElementsByTagName("exclude")
        .asElements()
        .map { BackupExclude(domain = it.getAttribute("domain"), path = it.getAttribute("path")) }
}

private fun org.w3c.dom.NodeList.asElements(): List<Element> = List(length) { item(it) as Element }

private fun projectFile(path: String): File {
    val candidates =
        listOf(
            File(path),
            File("app", path),
        )
    return candidates.firstOrNull { it.exists() }
        ?: error("Missing project file '$path' from ${File(".").absolutePath}")
}
