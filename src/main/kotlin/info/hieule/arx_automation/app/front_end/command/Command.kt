package info.hieule.arx_automation.app.front_end.command

import org.apache.commons.cli.CommandLine

interface Command {
    fun getSubCommandName(): String
    fun execute(cmd: CommandLine)
}