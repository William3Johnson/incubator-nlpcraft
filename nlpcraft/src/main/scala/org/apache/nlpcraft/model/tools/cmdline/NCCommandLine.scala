/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.nlpcraft.model.tools.cmdline

import org.apache.nlpcraft.common.ascii.NCAsciiTable
import org.apache.nlpcraft.common.util.NCUtils
import org.apache.nlpcraft.common.version.NCVersion

/**
 * 'nlpcraft' script entry point.
 */
object NCCommandLine extends App {
    private final val NAME = "Apache NLPCraft CLI"

    private final lazy val VER = NCVersion.getCurrent
    private final lazy val SCRIPT = NCUtils.sysEnv("NLPCRAFT_CLI_SCRIPT")
    private final lazy val SCRIPT_NAME = SCRIPT.getOrElse("NLPCraft CLI")

    // Single CLI command.
    case class Command(
        names: Seq[String],
        synopsis: String,
        desc: String = "",
        params: Seq[Parameter] = Seq.empty,
        examples: Seq[Example] = Seq.empty,
        body: Seq[String] => Unit
    ) {
        final val extNames = names.flatMap(name => // Safeguard against "common" errors.
            Seq(
                name,
                s"-$name",
                s"--$name",
                s"/$name",
                s"\\$name"
            )
        )
    }
    // Single command's example.
    case class Example(
        code: String,
        desc: String
    )
    // Single command's parameter.
    case class Parameter(
        names: Seq[String],
        valueDesc: Option[String] = None,
        arity: (Int, Int) = (1, 1), // Mandatory by default.
        desc: String
    )

    // All supported commands.
    private final val CMDS = Seq(
        Command(
            names = Seq("help", "?"),
            synopsis = s"Displays manual page for $SCRIPT_NAME.",
            desc =
                s"""
                   |By default, without '-all' or '-cmd' parameters, displays the abbreviated form of manual
                   |only listing the commands without parameters or examples.
                   |""".stripMargin,
            body = cmdHelp,
            params = Seq(
                Parameter(
                    names = Seq("--cmd", "-c"),
                    valueDesc = Some("{cmd}"),
                    arity = (0, 3),
                    desc = "Set of commands to show the manual for."
                ),
                Parameter(
                    names = Seq("--all", "-a"),
                    arity = (0, 1),
                    desc = "Flag to show full manual for all commands."
                )
            )
        ),
        Command(
            names = Seq("version", "ver"),
            synopsis = s"Displays version of $SCRIPT_NAME runtime.",
            body = cmdVersion
        ),
        Command(
            names = Seq("repl"),
            synopsis = s"Starts '$SCRIPT_NAME' in interactive REPL mode.",
            body = cmdRepl
        )
    )

    private final val HELP_CMD = CMDS.find(_.names.contains("help")).get
    private final val DFLT_CMD = CMDS.find(_.names.contains("repl")).get

    /**
     *
     * @param params Parameters, if any, for this command.
     */
    private def cmdHelp(params: Seq[String]): Unit = {
        log(
            s"""    |NAME
                    |    $SCRIPT_NAME - command line interface to control NLPCraft.
                    |
                    |USAGE
                    |    $SCRIPT_NAME COMMAND [PARAMETERS]
                    |
                    |COMMANDS""".stripMargin
        )

        if (params.isEmpty) {
            val tbl = NCAsciiTable().margin(left = 4)

            CMDS.foreach(cmd => tbl += (cmd.names.mkString(", "), cmd.synopsis))

            log(tbl.toString)
        }












        //            |              |
//              |    help
//
//        , ?
//              |         Displays the manual.
//              |
//              |         PARAMETERS
//              |             --cmd, -c={cmd}
//              |                 Optional (zero or more).
//              |                 Set of commands to show the manual for.
//              |             --all, -a
//              |                 Optional.
//              |                 Flag to show manual for all commands.
//              |         EXAMPLES
//              |             $$ $SCRIPT_NAME help repl
//              |                 Displays help for 'repl' command.
//              |             $$ $SCRIPT_NAME help -all
//              |                 Displays help for all commands.
//              |
//              |    version, ver
//              |         Displays the version of $SCRIPT_NAME.
//              |
//              |    model-stub
//              |         Generates JSON or YAML model stubs.
//              |
//              |         PARAMETERS
//              |             --type, -t={json|yaml}
//              |                 Mandatory.
//              |             --output, -o={file-path}?
//              |                 Optional.
//              |         EXAMPLES
//              |             $$ $SCRIPT_NAME model-stub --type=json --output=/home/user/model.js
//              |                 Generates JSON model stub into '/home/user/model.js' file.
//              |             $$ $SCRIPT_NAME model-stub -t=yaml
//              |                 Generates YAML model stub into 'model.js' file.
//              |""".stripMargin
//        )
    }

    /**
     *
     * @param params Parameters, if any, for this command.
     */
    private def cmdRepl(params: Seq[String]): Unit = {

    }

    /**
     *
     * @param params Parameters, if any, for this command.
     */
    private def cmdVersion(params: Seq[String]): Unit = {
        // Nothing - common header with version will be printed before anyways.
    }

    private def error(msg: String = ""): Unit = System.err.println(msg)
    private def log(msg: String = ""): Unit = System.out.println(msg)

    private def errorHelp(): Unit = SCRIPT match {
        // Running from *.{s|cmd} script.
        case Some(script) => error(s"Run '$script ${HELP_CMD.names}' to read the manual.")
        // Running from IDE.
        case None => error(s"Run the process with '${HELP_CMD.names}' parameter to read the manual.")
    }

    /**
     *
     * @param args
     */
    private def boot(args: Array[String]): Unit = {
        var status = 0

        // Print common version & copyright header.
        log(s"$NAME ver. ${VER.version}, rel. ${VER.date}")
        log(NCVersion.copyright)
        log()

        if (args.isEmpty)
            NCCommandLine.DFLT_CMD.body(Seq.empty)
        else {
            val cmdName = args.head

            CMDS.find(_.extNames.contains(cmdName)) match {
                case Some(cmd) => cmd.body(args.tail)
                case None =>
                    error(s"Unknown command: $cmdName")
                    errorHelp()

                    status = 1
            }
        }

        sys.exit(status)
    }

    // Boot up.
    boot(args)
}
