package net.corda.testing.node

import net.corda.core.internal.div
import net.corda.testing.common.internal.ProjectStructure.projectRootDir
import net.corda.testing.node.internal.JavaEntry
import net.corda.testing.node.internal.ProcessUtilities.startJavaProcess
import org.junit.Test
import kotlin.test.assertEquals

class MockNetworkIntegrationTests {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            MockNetwork(emptyList()).run {
                repeat(2) { createNode() }
                runNetwork()
                stopNodes()
            }
        }
    }

    @Test
    fun `does not leak non-daemon threads`() {
        val quasar = projectRootDir / "lib" / "quasar.jar"
        val exitCode = startJavaProcess(
                entry = JavaEntry.mainClass<MockNetworkIntegrationTests>(),
                appArguments = emptyList(),
                extraJvmArguments = listOf("-javaagent:$quasar")
        ).waitFor()
        assertEquals(0, exitCode)
    }
}
