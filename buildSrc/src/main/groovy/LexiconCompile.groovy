import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*

class LexiconCompile extends DefaultTask {
    @InputFile
    File allophonesFile = project.file("lib/modules/$project.locale/lexicon/allophones.${project.locale}.xml")

    @InputFile
    File lexiconFile = project.file("lib/modules/$project.locale/lexicon/${project.locale}.txt")

    @TaskAction
    void compile() {
        project.copy {
            from allophonesFile, lexiconFile
            into temporaryDir
        }
        project.javaexec {
            classpath project.configurations.lexiconCompile
            main 'marytts.tools.transcription.LTSLexiconPOSBuilder'
            args "$temporaryDir/$allophonesFile.name", "$temporaryDir/$lexiconFile.name"
        }
        project.copy {
            from temporaryDir
            into project.buildDir
            include '*.lts', '*.fst'
        }
    }
}
